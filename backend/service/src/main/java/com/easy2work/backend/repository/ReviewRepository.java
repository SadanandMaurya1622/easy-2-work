package com.easy2work.backend.repository;

import com.easy2work.core.model.Review;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class ReviewRepository {

    private final DataSource dataSource;

    public ReviewRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Insert a new review.
     */
    public long insert(long bookingId, String customerName, String serviceType,
                       int rating, String comment) throws Exception {
        final String sql = """
            INSERT INTO service_review
              (booking_id, customer_name, service_type, rating, comment, created_at)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            """;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, bookingId);
            ps.setString(2, customerName);
            ps.setString(3, serviceType);
            ps.setInt(4, rating);
            ps.setString(5, comment);
            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        throw new IllegalStateException("No review id returned");
    }

    /**
     * Find all reviews for a specific service type.
     */
    public List<Review> findByServiceType(String serviceType, int limit) throws Exception {
        int cap = Math.min(Math.max(limit, 1), 500);
        final String sql = """
            SELECT id, booking_id, customer_name, service_type, rating, comment, created_at
            FROM service_review
            WHERE service_type = ?
            ORDER BY created_at DESC
            LIMIT ?
            """;
        List<Review> out = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, serviceType);
            ps.setInt(2, cap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(readRow(rs));
                }
            }
        }
        return out;
    }

    /**
     * Find all reviews (newest first).
     */
    public List<Review> findAllOrderByCreatedDesc(int limit) throws Exception {
        int cap = Math.min(Math.max(limit, 1), 500);
        final String sql = """
            SELECT id, booking_id, customer_name, service_type, rating, comment, created_at
            FROM service_review
            ORDER BY created_at DESC
            LIMIT ?
            """;
        List<Review> out = new ArrayList<>();
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

    /**
     * Get average rating for a service type.
     */
    public double getAverageRating(String serviceType) throws Exception {
        final String sql = """
            SELECT AVG(rating) as avg_rating
            FROM service_review
            WHERE service_type = ?
            """;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, serviceType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating");
                }
            }
        }
        return 0.0;
    }

    private static Review readRow(ResultSet rs) throws Exception {
        Timestamp cr = rs.getTimestamp("created_at");
        Instant created = cr == null ? Instant.now() : cr.toInstant();
        return new Review(
                rs.getLong("id"),
                rs.getLong("booking_id"),
                rs.getString("customer_name"),
                rs.getString("service_type"),
                rs.getInt("rating"),
                rs.getString("comment"),
                created
        );
    }
}
