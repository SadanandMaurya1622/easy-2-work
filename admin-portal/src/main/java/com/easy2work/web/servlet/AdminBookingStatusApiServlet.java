package com.easy2work.web.servlet;

import com.easy2work.backend.api.ApiJson;
import com.easy2work.backend.booking.ManagedBookingStore;
import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.BookingRepository;
import com.easy2work.core.booking.MemoryBookingStore;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class AdminBookingStatusApiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String bookingIdRaw = req.getParameter("bookingId");
        String statusRaw = req.getParameter("status");
        if ((bookingIdRaw == null || bookingIdRaw.isBlank() || statusRaw == null || statusRaw.isBlank())
                && req.getContentType() != null
                && req.getContentType().toLowerCase(Locale.ROOT).contains("json")) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> json = ApiJson.GSON.fromJson(req.getReader(), Map.class);
                if (json != null) {
                    if (bookingIdRaw == null || bookingIdRaw.isBlank()) {
                        Object v = json.get("bookingId");
                        bookingIdRaw = v == null ? "" : String.valueOf(v);
                    }
                    if (statusRaw == null || statusRaw.isBlank()) {
                        Object v = json.get("status");
                        statusRaw = v == null ? "" : String.valueOf(v);
                    }
                }
            } catch (Exception ignore) {
            }
        }

        if (bookingIdRaw == null || bookingIdRaw.isBlank() || statusRaw == null || statusRaw.isBlank()) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "bookingId and status are required");
            return;
        }

        long bookingId;
        try {
            bookingId = Long.parseLong(bookingIdRaw.trim());
        } catch (NumberFormatException e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid bookingId");
            return;
        }

        String status = statusRaw.trim().toUpperCase(Locale.ROOT);
        if (!isValidStatus(status)) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid status");
            return;
        }

        try {
            boolean updated = updateStatus(req, bookingId, status);
            if (!updated) {
                ApiJson.writeError(resp, HttpServletResponse.SC_NOT_FOUND, "Booking not found");
                return;
            }
            ApiJson.write(resp, HttpServletResponse.SC_OK, Map.of("ok", true, "bookingId", bookingId, "status", status));
        } catch (Exception e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not update booking status");
        }
    }

    private static boolean updateStatus(HttpServletRequest req, long bookingId, String newStatus) throws Exception {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        if (ds != null) {
            return new BookingRepository(ds).updateStatus(bookingId, newStatus);
        }
        if (ManagedBookingStore.updateStatus(bookingId, newStatus)) {
            return true;
        }
        MemoryBookingStore mem = (MemoryBookingStore) req.getServletContext().getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY);
        return mem != null && mem.updateStatus(bookingId, newStatus);
    }

    private static boolean isValidStatus(String status) {
        return switch (status) {
            case "PENDING", "CONFIRMED", "IN_PROGRESS", "COMPLETED", "CANCELLED" -> true;
            default -> false;
        };
    }
}
