package com.easy2work.web.servlet;

import com.easy2work.backend.booking.ManagedBookingStore;
import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.BookingRepository;
import com.easy2work.core.booking.MemoryBookingStore;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin servlet to update booking status.
 * Requires admin key authentication (same as AdminServlet).
 */
public class AdminUpdateStatusServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AdminUpdateStatusServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext ctx = req.getServletContext();
        String expected = AdminServlet.resolveExpectedSecret(ctx);

        if (expected.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Admin not configured");
            return;
        }

        // Check admin key
        String provided = req.getHeader("X-Admin-Key");
        if (provided == null || provided.isBlank()) {
            provided = req.getParameter("key");
        }
        if (provided == null || !expected.equals(provided)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Get parameters
        String bookingIdStr = req.getParameter("bookingId");
        String newStatus = req.getParameter("status");

        if (bookingIdStr == null || bookingIdStr.isBlank() || newStatus == null || newStatus.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing bookingId or status");
            return;
        }

        long bookingId;
        try {
            bookingId = Long.parseLong(bookingIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bookingId");
            return;
        }

        // Validate status
        if (!isValidStatus(newStatus)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status. Must be PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, or CANCELLED");
            return;
        }

        try {
            boolean updated = updateStatus(req, bookingId, newStatus);
            if (updated) {
                LOG.log(Level.INFO, "Admin updated booking {0} to status {1}", new Object[]{bookingId, newStatus});
                // Redirect back to admin bookings with the key
                String redirectUrl = req.getContextPath() + "/admin/bookings?key=" + provided + "&updated=" + bookingId;
                resp.sendRedirect(redirectUrl);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating booking status", e);
            throw new ServletException("Error updating booking status", e);
        }
    }

    private static boolean isValidStatus(String status) {
        return switch (status.toUpperCase()) {
            case "PENDING", "CONFIRMED", "IN_PROGRESS", "COMPLETED", "CANCELLED" -> true;
            default -> false;
        };
    }

    private static boolean updateStatus(HttpServletRequest req, long bookingId, String newStatus) throws Exception {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        if (ds != null) {
            return new BookingRepository(ds).updateStatus(bookingId, newStatus.toUpperCase());
        }
        if (ManagedBookingStore.updateStatus(bookingId, newStatus.toUpperCase())) {
            return true;
        }

        MemoryBookingStore mem = (MemoryBookingStore) req.getServletContext().getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY);
        if (mem != null) {
            return mem.updateStatus(bookingId, newStatus.toUpperCase());
        }

        return false;
    }
}
