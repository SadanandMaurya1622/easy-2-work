package com.easy2work.backend.repository;

import com.easy2work.core.model.Booking;
import com.easy2work.core.util.PhoneKey;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class BookingRepository {

    private final DataSource dataSource;

    public BookingRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long insert(String customerName, String phone, String email, String serviceType,
                       String description, String address, LocalDateTime preferredAt) throws Exception {
        final String sql = """
            INSERT INTO service_booking
              (customer_name, phone, email, service_type, description, address, preferred_at, status, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING', CURRENT_TIMESTAMP)
            """;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customerName);
            ps.setString(2, phone);
            if (email == null || email.isEmpty()) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, email);
            }
            ps.setString(4, serviceType);
            ps.setString(5, description);
            ps.setString(6, address);
            if (preferredAt == null) {
                ps.setNull(7, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(7, Timestamp.valueOf(preferredAt));
            }
            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        throw new IllegalStateException("No booking id returned");
    }

    public Booking insertAndLoad(String customerName, String phone, String email, String serviceType,
                                 String description, String address, LocalDateTime preferredAt) throws Exception {
        long id = insert(customerName, phone, email, serviceType, description, address, preferredAt);
        Instant created = Instant.now();
        return new Booking(id, customerName, phone, email == null ? "" : email, serviceType,
                description, address, preferredAt, "PENDING", created);
    }

    public List<Booking> findByPhoneKey(String key10) throws Exception {
        final String sql = """
            SELECT id, customer_name, phone, email, service_type, description, address, preferred_at, status, created_at
            FROM service_booking
            ORDER BY created_at DESC
            LIMIT 500
            """;
        List<Booking> out = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String phone = rs.getString("phone");
                if (!PhoneKey.matchesStored(phone, key10)) {
                    continue;
                }
                out.add(readRow(rs));
            }
        }
        return out;
    }

    /** Admin: newest bookings first (cap to protect DB). */
    public List<Booking> findAllOrderByCreatedDesc(int limit) throws Exception {
        int cap = Math.min(Math.max(limit, 1), 2_000);
        final String sql = """
            SELECT id, customer_name, phone, email, service_type, description, address, preferred_at, status, created_at
            FROM service_booking
            ORDER BY created_at DESC
            LIMIT ?
            """;
        List<Booking> out = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(readRow(rs));
                }
            }
        }
        return out;
    }

    /** Admin: update booking status. */
    public boolean updateStatus(long bookingId, String newStatus) throws Exception {
        final String sql = """
            UPDATE service_booking
            SET status = ?
            WHERE id = ?
            """;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setLong(2, bookingId);
            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }

    private static Booking readRow(ResultSet rs) throws Exception {
        Timestamp pref = rs.getTimestamp("preferred_at");
        LocalDateTime prefAt = pref == null ? null : pref.toLocalDateTime();
        Timestamp cr = rs.getTimestamp("created_at");
        Instant created = cr == null ? Instant.now() : cr.toInstant();
        String em = rs.getString("email");
        return new Booking(
                rs.getLong("id"),
                rs.getString("customer_name"),
                rs.getString("phone"),
                em == null ? "" : em,
                rs.getString("service_type"),
                rs.getString("description"),
                rs.getString("address"),
                prefAt,
                rs.getString("status"),
                created
        );
    }
}
