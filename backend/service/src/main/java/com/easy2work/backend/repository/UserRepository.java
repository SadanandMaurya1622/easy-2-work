package com.easy2work.backend.repository;

import com.easy2work.core.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Repository for managing user data in the database.
 */
public class UserRepository {
    private static final Logger LOG = Logger.getLogger(UserRepository.class.getName());
    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Find a user by email address.
     */
    public Optional<User> findByEmail(String email) {
        if (dataSource == null) {
            LOG.warning("Database not configured - cannot find user");
            return Optional.empty();
        }

        String sql = "SELECT id, email, password_hash, first_name, last_name, phone, role, created_at, last_login_at " +
                     "FROM users WHERE email = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error finding user by email: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Find a user by ID.
     */
    public Optional<User> findById(Long id) {
        if (dataSource == null) {
            return Optional.empty();
        }

        String sql = "SELECT id, email, password_hash, first_name, last_name, phone, role, created_at, last_login_at " +
                     "FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error finding user by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Create a new user in the database.
     */
    public Optional<User> create(User user) {
        if (dataSource == null) {
            LOG.warning("Database not configured - cannot create user");
            return Optional.empty();
        }

        String sql = "INSERT INTO users (email, password_hash, first_name, last_name, phone, role, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getRole().name());
            stmt.setTimestamp(7, Timestamp.from(user.getCreatedAt()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getLong(1));
                        return Optional.of(user);
                    }
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error creating user: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Update the last login timestamp for a user.
     */
    public void updateLastLogin(Long userId) {
        if (dataSource == null) {
            return;
        }

        String sql = "UPDATE users SET last_login_at = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.from(Instant.now()));
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.severe("Error updating last login: " + e.getMessage());
        }
    }

    /**
     * Check if an email is already registered.
     */
    public boolean emailExists(String email) {
        return findByEmail(email).isPresent();
    }

    public List<User> findAllOrderByCreatedDesc(int limit) {
        if (dataSource == null) {
            return List.of();
        }
        int cap = Math.min(Math.max(limit, 1), 1000);
        String sql = "SELECT id, email, password_hash, first_name, last_name, phone, role, created_at, last_login_at " +
                     "FROM users ORDER BY created_at DESC LIMIT ?";
        List<User> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cap);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    out.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error listing users: " + e.getMessage());
        }
        return out;
    }

    /**
     * Map a ResultSet row to a User object.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhone(rs.getString("phone"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toInstant());
        }

        Timestamp lastLoginAt = rs.getTimestamp("last_login_at");
        if (lastLoginAt != null) {
            user.setLastLoginAt(lastLoginAt.toInstant());
        }

        return user;
    }
}
