package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.booking.ManagedBookingStore;
import com.easy2work.backend.repository.BookingRepository;
import com.easy2work.core.booking.BookingRules;
import com.easy2work.core.booking.MemoryBookingStore;
import com.easy2work.core.model.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class BookingServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        var ctx = getServletContext();
        synchronized (ctx) {
            if (ctx.getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY) == null) {
                ctx.setAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY, new MemoryBookingStore());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();
        HttpSession session = request.getSession();

        Map<String, String> form = readForm(request);
        String err = BookingRules.validate(form);
        if (err != null) {
            session.setAttribute("bookingError", err);
            session.setAttribute("bookingForm", form);
            response.sendRedirect(ctx + "/book.jsp");
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
            DataSource ds = (DataSource) request.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
            Booking saved;
            if (ds != null) {
                var repo = new BookingRepository(ds);
                saved = repo.insertAndLoad(name, phone, email, type, desc, addr, preferred);
            } else {
                // Shared file-backed fallback so admin portal can see non-DB bookings too.
                saved = ManagedBookingStore.save(name, phone, email, type, desc, addr, preferred);
            }
            session.removeAttribute("bookingForm");
            session.removeAttribute("bookingError");
            response.sendRedirect(ctx + "/?booked=1&ref=" + saved.id());
        } catch (Exception e) {
            session.setAttribute("bookingError", "Could not save your booking. Please try again or call support.");
            session.setAttribute("bookingForm", form);
            response.sendRedirect(ctx + "/book.jsp");
        }
    }

    private static Map<String, String> readForm(HttpServletRequest request) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("customerName", request.getParameter("customerName"));
        m.put("phone", request.getParameter("phone"));
        m.put("email", request.getParameter("email"));
        m.put("serviceType", request.getParameter("serviceType"));
        m.put("description", request.getParameter("description"));
        m.put("address", request.getParameter("address"));
        m.put("preferredAt", request.getParameter("preferredAt"));
        return m;
    }
}
