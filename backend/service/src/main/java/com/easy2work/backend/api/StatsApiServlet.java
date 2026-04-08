package com.easy2work.backend.api;

import com.easy2work.backend.booking.ManagedBookingStore;
import com.easy2work.backend.catalog.ManagedServiceCatalog;
import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.StatsRepository;
import com.easy2work.backend.review.ManagedReviewCatalog;
import com.easy2work.backend.user.ManagedUserStore;
import com.easy2work.core.model.Booking;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatsApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Cache-Control", "public, max-age=60");

        Map<String, Object> body = new LinkedHashMap<>();
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        if (ds == null) {
            applyDynamicFallback(body);
            ApiJson.write(resp, HttpServletResponse.SC_OK, body);
            return;
        }

        try {
            var repo = new StatsRepository(ds);
            var opt = repo.findSingleton();
            if (opt.isEmpty()) {
                applyDynamicFallback(body);
            } else {
                var s = opt.get();
                body.put("source", "mysql");
                body.put("homesServiced", s.homesServiced());
                body.put("hoursSaved", s.hoursSaved());
                body.put("verifiedProfessionals", s.verifiedProfessionals());
            }
            body.put("totalUsers", countLoggedInUsersFromDb(ds));
            body.put("completedWorks", countCompletedWorksFromDb(ds));
        } catch (Exception e) {
            body.put("error", "db_unavailable");
            applyDynamicFallback(body);
        }
        ApiJson.write(resp, HttpServletResponse.SC_OK, body);
    }

    private static void applyDynamicFallback(Map<String, Object> body) {
        int bookingCount = ManagedBookingStore.listAllByCreatedDesc().size();
        int reviewCount = ManagedReviewCatalog.findAllOrderByCreatedDesc(10_000).size();
        int serviceCount = ManagedServiceCatalog.list().size();

        int dynamicHomes = bookingCount * 12 + reviewCount * 6 + serviceCount * 45;
        int dynamicHours = bookingCount * 3 + reviewCount * 4 + serviceCount * 20;
        int dynamicPros = serviceCount * 3 + Math.max(0, reviewCount / 5);
        int completedWorks = (int) ManagedBookingStore.listAllByCreatedDesc().stream()
                .filter(b -> "COMPLETED".equalsIgnoreCase(safeStatus(b)))
                .count();

        body.put("source", "dynamic-fallback");
        body.put("homesServiced", Math.max(0, dynamicHomes));
        body.put("hoursSaved", Math.max(0, dynamicHours));
        body.put("verifiedProfessionals", Math.max(0, dynamicPros));
        body.put("totalUsers", countLoggedInUsersFromManagedStore());
        body.put("completedWorks", Math.max(0, completedWorks));
    }

    private static int countLoggedInUsersFromDb(DataSource ds) {
        final String sql = "SELECT COUNT(*) FROM users WHERE last_login_at IS NOT NULL";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception ignored) {
            return countLoggedInUsersFromManagedStore();
        }
    }

    private static int countCompletedWorksFromDb(DataSource ds) {
        final String sql = "SELECT COUNT(*) FROM service_booking WHERE UPPER(status) = 'COMPLETED'";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception ignored) {
            List<Booking> bookings = ManagedBookingStore.listAllByCreatedDesc();
            return (int) bookings.stream()
                    .filter(b -> "COMPLETED".equalsIgnoreCase(safeStatus(b)))
                    .count();
        }
    }

    private static String safeStatus(Booking booking) {
        return booking == null || booking.status() == null ? "" : booking.status().trim().toUpperCase(Locale.ROOT);
    }

    private static int countLoggedInUsersFromManagedStore() {
        return (int) ManagedUserStore.listOrderByCreatedDesc(1000).stream()
                .filter(u -> u.getLastLoginAt() != null)
                .count();
    }
}
