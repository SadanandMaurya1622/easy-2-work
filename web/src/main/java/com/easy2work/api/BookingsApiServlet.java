package com.easy2work.api;

import com.easy2work.booking.MemoryBookingStore;
import com.easy2work.catalog.ServiceCatalog;
import com.easy2work.config.DataSourceListener;
import com.easy2work.db.BookingRepository;
import com.easy2work.model.ServiceBooking;
import com.easy2work.servlet.BookingServlet;
import com.easy2work.util.PhoneKey;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * GET /api/bookings?phone=... — JSON lists by status (same rules as My Bookings page).
 */
public class BookingsApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String phoneRaw = req.getParameter("phone");
        if (phoneRaw == null || phoneRaw.isBlank()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Query parameter phone is required");
            return;
        }
        String key = PhoneKey.fromInput(phoneRaw);
        if (key.length() != 10) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Enter a valid 10-digit mobile number");
            return;
        }

        try {
            List<ServiceBooking> all = fetch(req, key);
            List<Map<String, Object>> pending = new ArrayList<>();
            List<Map<String, Object>> completed = new ArrayList<>();
            List<Map<String, Object>> cancelled = new ArrayList<>();
            for (ServiceBooking b : all) {
                String s = b.getStatus() == null ? "PENDING" : b.getStatus().trim().toUpperCase(Locale.ROOT);
                Map<String, Object> row = toRow(b);
                switch (s) {
                    case "COMPLETED" -> completed.add(row);
                    case "CANCELLED" -> cancelled.add(row);
                    default -> pending.add(row);
                }
            }
            List<Map<String, Object>> allSorted = new ArrayList<>();
            for (ServiceBooking b : all) {
                allSorted.add(toRow(b));
            }
            allSorted.sort(Comparator.comparing(m -> -((Long) m.get("createdAtMillis"))));

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("ok", true);
            body.put("phoneKey", key);
            body.put("count", allSorted.size());
            body.put("all", allSorted);
            body.put("pending", pending);
            body.put("completed", completed);
            body.put("cancelled", cancelled);
            ApiJson.write(resp, HttpServletResponse.SC_OK, body);
        } catch (Exception e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not load bookings");
        }
    }

    private static Map<String, Object> toRow(ServiceBooking b) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", b.getId());
        row.put("customerName", b.getCustomerName());
        row.put("phone", b.getPhone());
        row.put("serviceType", b.getServiceType());
        row.put("serviceTitle", ServiceCatalog.displayTitleForCode(b.getServiceType()));
        row.put("description", b.getDescription());
        row.put("address", b.getAddress());
        row.put("status", b.getStatus());
        row.put("statusKind", b.getStatusKind());
        row.put("bookedAt", b.getBookedAtDisplay());
        row.put("createdAtMillis", b.getCreatedAt().toEpochMilli());
        if (b.getPreferredAt() != null) {
            row.put("preferredAt", b.getPreferredAt().toString());
        }
        return row;
    }

    private static List<ServiceBooking> fetch(HttpServletRequest req, String key10) throws Exception {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DataSourceListener.CTX_DATASOURCE);
        if (ds != null) {
            return new BookingRepository(ds).findByPhoneKey(key10);
        }
        MemoryBookingStore mem = (MemoryBookingStore) req.getServletContext().getAttribute(BookingServlet.CTX_MEMORY_STORE);
        if (mem == null) {
            return List.of();
        }
        return mem.findByPhoneKey(key10);
    }
}
