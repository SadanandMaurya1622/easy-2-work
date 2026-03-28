package com.easy2work.booking;

import com.easy2work.model.ServiceBooking;
import com.easy2work.util.PhoneKey;

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

    private static final AtomicLong IDS = new AtomicLong(System.currentTimeMillis());

    private final CopyOnWriteArrayList<ServiceBooking> bookings = new CopyOnWriteArrayList<>();

    public ServiceBooking save(String customerName, String phone, String email, String serviceType,
                               String description, String address, java.time.LocalDateTime preferredAt) {
        long id = IDS.incrementAndGet();
        ServiceBooking b = new ServiceBooking(id, customerName, phone, email == null ? "" : email, serviceType,
                description, address, preferredAt, "PENDING", Instant.now());
        bookings.add(0, b);
        return b;
    }

    public List<ServiceBooking> findByPhoneKey(String key10) {
        List<ServiceBooking> out = new ArrayList<>();
        for (ServiceBooking b : bookings) {
            if (PhoneKey.matchesStored(b.phone(), key10)) {
                out.add(b);
            }
        }
        out.sort(Comparator.comparing(ServiceBooking::createdAt).reversed());
        return out;
    }
}
