package com.easy2work.backend.user;

import com.easy2work.backend.api.ApiJson;
import com.easy2work.core.model.User;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * File-backed users store used when JDBC is not configured.
 */
public final class ManagedUserStore {

    private static final Logger LOG = Logger.getLogger(ManagedUserStore.class.getName());
    private static final Object LOCK = new Object();
    private static final Type LIST_TYPE = new TypeToken<List<UserRow>>() { }.getType();
    private static final Path ROOT = resolveRootDir();
    private static final Path STORE_FILE = ROOT.resolve("managed-users.json");

    private ManagedUserStore() {
    }

    public static Optional<User> findByEmail(String email) {
        String normalized = normalizeEmail(email);
        if (normalized.isBlank()) {
            return Optional.empty();
        }
        synchronized (LOCK) {
            return readAllInternal().stream()
                    .filter(row -> normalized.equals(normalizeEmail(row.email())))
                    .findFirst()
                    .map(ManagedUserStore::toUser);
        }
    }

    public static Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        synchronized (LOCK) {
            return readAllInternal().stream()
                    .filter(row -> row.id() == id)
                    .findFirst()
                    .map(ManagedUserStore::toUser);
        }
    }

    public static Optional<User> create(User user) {
        if (user == null || normalizeEmail(user.getEmail()).isBlank()) {
            return Optional.empty();
        }
        synchronized (LOCK) {
            List<UserRow> rows = readAllInternal();
            String email = normalizeEmail(user.getEmail());
            if (rows.stream().anyMatch(row -> email.equals(normalizeEmail(row.email())))) {
                return Optional.empty();
            }
            long id = nextId(rows);
            Instant createdAt = user.getCreatedAt() == null ? Instant.now() : user.getCreatedAt();
            UserRow row = new UserRow(
                    id,
                    email,
                    safe(user.getPasswordHash()),
                    safe(user.getFirstName()),
                    safe(user.getLastName()),
                    safe(user.getPhone()),
                    user.getRole() == null ? User.UserRole.CUSTOMER.name() : user.getRole().name(),
                    createdAt.toEpochMilli(),
                    0L
            );
            rows.add(row);
            rows.sort(Comparator.comparingLong(UserRow::createdAtEpochMs).reversed());
            writeAllInternal(rows);
            return Optional.of(toUser(row));
        }
    }

    public static void updateLastLogin(Long userId) {
        if (userId == null) {
            return;
        }
        synchronized (LOCK) {
            List<UserRow> rows = readAllInternal();
            boolean updated = false;
            long now = Instant.now().toEpochMilli();
            for (int i = 0; i < rows.size(); i++) {
                UserRow row = rows.get(i);
                if (row.id() == userId) {
                    rows.set(i, new UserRow(
                            row.id(),
                            row.email(),
                            row.passwordHash(),
                            row.firstName(),
                            row.lastName(),
                            row.phone(),
                            row.role(),
                            row.createdAtEpochMs(),
                            now
                    ));
                    updated = true;
                    break;
                }
            }
            if (updated) {
                writeAllInternal(rows);
            }
        }
    }

    public static List<User> listOrderByCreatedDesc(int limit) {
        int cap = Math.min(Math.max(limit, 1), 1000);
        synchronized (LOCK) {
            return readAllInternal().stream()
                    .sorted(Comparator.comparingLong(UserRow::createdAtEpochMs).reversed())
                    .limit(cap)
                    .map(ManagedUserStore::toUser)
                    .toList();
        }
    }

    private static User toUser(UserRow row) {
        User user = new User();
        user.setId(row.id());
        user.setEmail(row.email());
        user.setPasswordHash(row.passwordHash());
        user.setFirstName(row.firstName());
        user.setLastName(row.lastName());
        user.setPhone(row.phone());
        try {
            user.setRole(User.UserRole.valueOf(safe(row.role()).toUpperCase(Locale.ROOT)));
        } catch (Exception ignored) {
            user.setRole(User.UserRole.CUSTOMER);
        }
        if (row.createdAtEpochMs() > 0) {
            user.setCreatedAt(Instant.ofEpochMilli(row.createdAtEpochMs()));
        }
        if (row.lastLoginAtEpochMs() > 0) {
            user.setLastLoginAt(Instant.ofEpochMilli(row.lastLoginAtEpochMs()));
        }
        return user;
    }

    private static long nextId(List<UserRow> rows) {
        long max = 0L;
        for (UserRow row : rows) {
            if (row.id() > max) {
                max = row.id();
            }
        }
        return Math.max(max + 1, System.currentTimeMillis());
    }

    private static List<UserRow> readAllInternal() {
        try {
            ensureStore();
            try (Reader reader = Files.newBufferedReader(STORE_FILE, StandardCharsets.UTF_8)) {
                List<UserRow> rows = ApiJson.GSON.fromJson(reader, LIST_TYPE);
                return rows == null ? new ArrayList<>() : new ArrayList<>(rows);
            }
        } catch (Exception e) {
            LOG.warning("Failed to read managed users from " + STORE_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void writeAllInternal(List<UserRow> rows) {
        try {
            ensureStore();
            try (Writer writer = Files.newBufferedWriter(STORE_FILE, StandardCharsets.UTF_8)) {
                ApiJson.GSON.toJson(rows, LIST_TYPE, writer);
            }
        } catch (IOException e) {
            LOG.warning("Failed to write managed users to " + STORE_FILE + ": " + e.getMessage());
        }
    }

    private static void ensureStore() throws IOException {
        if (!Files.exists(ROOT)) {
            Files.createDirectories(ROOT);
        }
        if (!Files.exists(STORE_FILE)) {
            Files.writeString(STORE_FILE, "[]", StandardCharsets.UTF_8);
        }
    }

    private static Path resolveRootDir() {
        String override = System.getProperty("easy2work.data.dir");
        if (override != null && !override.isBlank()) {
            return Paths.get(override.trim());
        }
        return Paths.get(System.getProperty("user.home"), ".easy2work");
    }

    private static String normalizeEmail(String email) {
        return safe(email).toLowerCase(Locale.ROOT);
    }

    private static String safe(String value) {
        return value == null ? "" : value.trim();
    }

    public record UserRow(
            long id,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            String phone,
            String role,
            long createdAtEpochMs,
            long lastLoginAtEpochMs
    ) {
    }
}

