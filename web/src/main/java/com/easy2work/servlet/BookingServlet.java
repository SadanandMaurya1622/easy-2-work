package com.easy2work.servlet;

import com.easy2work.booking.BookingRules;
import com.easy2work.booking.MemoryBookingStore;
import com.easy2work.config.DataSourceListener;
import com.easy2work.db.BookingRepository;
import com.easy2work.model.ServiceBooking;
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

    public static final String CTX_MEMORY_STORE = "com.easy2work.MemoryBookingStore";

    @Override
    public void init() throws ServletException {
        var ctx = getServletContext();
        synchronized (ctx) {
            if (ctx.getAttribute(CTX_MEMORY_STORE) == null) {
                ctx.setAttribute(CTX_MEMORY_STORE, new MemoryBookingStore());
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
        var preferred = ServiceBooking.parsePreferred(form.get("preferredAt"));

        try {
            DataSource ds = (DataSource) request.getServletContext().getAttribute(DataSourceListener.CTX_DATASOURCE);
            ServiceBooking saved;
            if (ds != null) {
                var repo = new BookingRepository(ds);
                saved = repo.insertAndLoad(name, phone, email, type, desc, addr, preferred);
            } else {
                var mem = (MemoryBookingStore) request.getServletContext().getAttribute(CTX_MEMORY_STORE);
                saved = mem.save(name, phone, email, type, desc, addr, preferred);
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
