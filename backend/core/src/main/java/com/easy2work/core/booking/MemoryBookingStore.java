package com.easy2work.core.booking;

import com.easy2work.core.model.Booking;
import com.easy2work.core.util.PhoneKey;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Used when JDBC is not configured so local / demo runs can still accept bookings.
 */
public final class MemoryBookingStore {

    /** ServletContext attribute key shared by HTML servlets and JSON API when JDBC is off. */
    public static final String SERVLET_CONTEXT_KEY = "com.easy2work.MemoryBookingStore";

    private static final AtomicLong IDS = new AtomicLong(System.currentTimeMillis());

    private final CopyOnWriteArrayList<Booking> bookings = new CopyOnWriteArrayList<>();

    public Booking save(String customerName, String phone, String email, String serviceType,
                        String description, String address, java.time.LocalDateTime preferredAt) {
        long id = IDS.incrementAndGet();
        Booking b = new Booking(id, customerName, phone, email == null ? "" : email, serviceType,
                description, address, preferredAt, "PENDING", Instant.now());
        bookings.add(0, b);
        return b;
    }

    public List<Booking> findByPhoneKey(String key10) {
        List<Booking> out = new ArrayList<>();
        for (Booking b : bookings) {
            if (PhoneKey.matchesStored(b.phone(), key10)) {
                out.add(b);
            }
        }
        out.sort(Comparator.comparing(Booking::createdAt).reversed());
        return out;
    }

    /** Newest first (for admin). Copy of internal list. */
    public List<Booking> listAllByCreatedDesc() {
        List<Booking> out = new ArrayList<>(bookings);
        out.sort(Comparator.comparing(Booking::createdAt).reversed());
        return out;
    }
}
