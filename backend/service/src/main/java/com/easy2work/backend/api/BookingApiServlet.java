package com.easy2work.backend.api;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.BookingRepository;
import com.easy2work.core.booking.BookingRules;
import com.easy2work.core.booking.MemoryBookingStore;
import com.easy2work.core.model.Booking;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * POST /api/booking — JSON body (same fields as HTML form). Content-Type: application/json
 */
public class BookingApiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (req.getContentType() == null || !req.getContentType().toLowerCase(Locale.ROOT).contains("json")) {
            ApiJson.writeError(resp, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content-Type must be application/json");
            return;
        }

        Map<String, Object> raw;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> parsed = ApiJson.GSON.fromJson(req.getReader(), Map.class);
            raw = parsed == null ? Map.of() : parsed;
        } catch (Exception e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON body");
            return;
        }

        Map<String, String> form = new LinkedHashMap<>();
        form.put("customerName", str(raw.get("customerName")));
        form.put("phone", str(raw.get("phone")));
        form.put("email", str(raw.get("email")));
        form.put("serviceType", str(raw.get("serviceType")));
        form.put("description", str(raw.get("description")));
        form.put("address", str(raw.get("address")));
        form.put("preferredAt", str(raw.get("preferredAt")));

        String err = BookingRules.validate(form);
        if (err != null) {
            ApiJson.write(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("ok", false, "error", err));
            return;
        }

        String name = BookingRules.trim(form.get("customerName"));
        String phone = BookingRules.normalizePhone(form.get("phone"));
        String email = BookingRules.trimToNull(form.get("email"));
        String type = form.get("serviceType").trim().toUpperCase(Locale.ROOT);
        String desc = form.get("description").trim();
        String addr = form.get("address").trim();
        var preferred = Booking.parsePreferred(form.get("preferredAt"));

        try {
            DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
            Booking saved;
            if (ds != null) {
                saved = new BookingRepository(ds).insertAndLoad(name, phone, email, type, desc, addr, preferred);
            } else {
                MemoryBookingStore mem = (MemoryBookingStore) req.getServletContext().getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY);
                if (mem == null) {
                    synchronized (req.getServletContext()) {
                        if (req.getServletContext().getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY) == null) {
                            req.getServletContext().setAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY, new MemoryBookingStore());
                        }
                        mem = (MemoryBookingStore) req.getServletContext().getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY);
                    }
                }
                saved = mem.save(name, phone, email, type, desc, addr, preferred);
            }
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("ok", true);
            body.put("id", saved.getId());
            body.put("status", saved.getStatus());
            body.put("message", "Booking received");
            ApiJson.write(resp, HttpServletResponse.SC_CREATED, body);
        } catch (Exception e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not save booking");
        }
    }

    private static String str(Object o) {
        return o == null ? "" : String.valueOf(o);
    }
}
