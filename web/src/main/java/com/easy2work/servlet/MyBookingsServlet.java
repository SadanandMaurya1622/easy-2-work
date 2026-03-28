package com.easy2work.servlet;

import com.easy2work.booking.MemoryBookingStore;
import com.easy2work.config.DataSourceListener;
import com.easy2work.db.BookingRepository;
import com.easy2work.model.ServiceBooking;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.easy2work.util.PhoneKey;

public class MyBookingsServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        var ctx = getServletContext();
        synchronized (ctx) {
            if (ctx.getAttribute(BookingServlet.CTX_MEMORY_STORE) == null) {
                ctx.setAttribute(BookingServlet.CTX_MEMORY_STORE, new MemoryBookingStore());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phoneRaw = req.getParameter("phone");
        if (phoneRaw == null) {
            phoneRaw = "";
        }

        boolean lookup = !phoneRaw.isBlank() || "POST".equalsIgnoreCase(req.getMethod());

        if (!lookup && "GET".equalsIgnoreCase(req.getMethod())) {
            req.getRequestDispatcher("/my-bookings.jsp").forward(req, resp);
            return;
        }

        String key = PhoneKey.fromInput(phoneRaw);
        if (key.length() != 10) {
            req.setAttribute("lookupError", "Apna 10-digit mobile number enter karein (ya +91 ke saath).");
            req.setAttribute("phoneInput", phoneRaw);
            req.getRequestDispatcher("/my-bookings.jsp").forward(req, resp);
            return;
        }

        try {
            List<ServiceBooking> all = fetchBookings(req, key);
            List<ServiceBooking> active = new ArrayList<>();
            List<ServiceBooking> done = new ArrayList<>();
            List<ServiceBooking> cancelled = new ArrayList<>();
            for (ServiceBooking b : all) {
                String s = b.getStatus() == null ? "PENDING" : b.getStatus().trim().toUpperCase(Locale.ROOT);
                switch (s) {
                    case "COMPLETED" -> done.add(b);
                    case "CANCELLED" -> cancelled.add(b);
                    default -> active.add(b);
                }
            }
            List<ServiceBooking> bookingsAll = new ArrayList<>(all);
            bookingsAll.sort(Comparator.comparing(ServiceBooking::getCreatedAt).reversed());

            req.setAttribute("phoneInput", phoneRaw);
            req.setAttribute("phoneKey", key);
            req.setAttribute("bookingsAll", bookingsAll);
            req.setAttribute("bookingCount", bookingsAll.size());
            req.setAttribute("bookingsActive", active);
            req.setAttribute("bookingsDone", done);
            req.setAttribute("bookingsCancelled", cancelled);
        } catch (Exception e) {
            req.setAttribute("lookupError", "Abhi list load nahi ho payi. Thodi der baad try karein.");
            req.setAttribute("phoneInput", phoneRaw);
        }
        req.getRequestDispatcher("/my-bookings.jsp").forward(req, resp);
    }

    private static List<ServiceBooking> fetchBookings(HttpServletRequest req, String key10) throws Exception {
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
