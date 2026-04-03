package com.easy2work.web.servlet;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.BookingRepository;
import com.easy2work.core.booking.MemoryBookingStore;
import com.easy2work.core.model.Booking;
import com.easy2work.core.util.PhoneKey;
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

public class MyBookingsServlet extends HttpServlet {

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
            req.setAttribute("lookupError", "Enter a valid 10-digit mobile number (with or without +91).");
            req.setAttribute("phoneInput", phoneRaw);
            req.getRequestDispatcher("/my-bookings.jsp").forward(req, resp);
            return;
        }

        try {
            List<Booking> all = fetchBookings(req, key);
            List<Booking> active = new ArrayList<>();
            List<Booking> done = new ArrayList<>();
            List<Booking> cancelled = new ArrayList<>();
            for (Booking b : all) {
                String s = b.getStatus() == null ? "PENDING" : b.getStatus().trim().toUpperCase(Locale.ROOT);
                switch (s) {
                    case "COMPLETED" -> done.add(b);
                    case "CANCELLED" -> cancelled.add(b);
                    default -> active.add(b);
                }
            }
            List<Booking> bookingsAll = new ArrayList<>(all);
            bookingsAll.sort(Comparator.comparing(Booking::getCreatedAt).reversed());

            req.setAttribute("phoneInput", phoneRaw);
            req.setAttribute("phoneKey", key);
            req.setAttribute("bookingsAll", bookingsAll);
            req.setAttribute("bookingCount", bookingsAll.size());
            req.setAttribute("bookingsActive", active);
            req.setAttribute("bookingsDone", done);
            req.setAttribute("bookingsCancelled", cancelled);
        } catch (Exception e) {
            req.setAttribute("lookupError", "We couldn’t load your list. Please try again in a moment.");
            req.setAttribute("phoneInput", phoneRaw);
        }
        req.getRequestDispatcher("/my-bookings.jsp").forward(req, resp);
    }

    private static List<Booking> fetchBookings(HttpServletRequest req, String key10) throws Exception {
        DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
        if (ds != null) {
            return new BookingRepository(ds).findByPhoneKey(key10);
        }
        MemoryBookingStore mem = (MemoryBookingStore) req.getServletContext().getAttribute(MemoryBookingStore.SERVLET_CONTEXT_KEY);
        if (mem == null) {
            return List.of();
        }
        return mem.findByPhoneKey(key10);
    }
}
