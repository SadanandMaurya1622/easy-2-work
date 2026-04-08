package com.easy2work.backend.booking;

import com.easy2work.backend.api.ApiJson;
import com.easy2work.core.model.Booking;
import com.easy2work.core.util.PhoneKey;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * File-backed booking fallback when JDBC is not configured.
 * Shared between user portal and admin portal via ~/.easy2work.
 */
public final class ManagedBookingStore {

    private static final Logger LOG = Logger.getLogger(ManagedBookingStore.class.getName());
    private static final Object LOCK = new Object();
    private static final Type LIST_TYPE = new TypeToken<List<BookingRow>>() { }.getType();
    private static final Path ROOT = resolveRootDir();
    private static final Path STORE_FILE = ROOT.resolve("managed-bookings.json");

    private ManagedBookingStore() {
    }

    public static Booking save(String customerName, String phone, String email, String serviceType,
                               String description, String address, LocalDateTime preferredAt) {
        synchronized (LOCK) {
            List<BookingRow> rows = readAllInternal();
            long id = nextId(rows);
            Instant now = Instant.now();
            BookingRow row = new BookingRow(
                    id,
                    safe(customerName),
                    safe(phone),
                    safe(email),
                    safe(serviceType).toUpperCase(Locale.ROOT),
                    safe(description),
                    safe(address),
                    preferredAt == null ? null : preferredAt.toString(),
                    "PENDING",
                    now.toEpochMilli()
            );
            rows.add(row);
            rows.sort(Comparator.comparingLong(BookingRow::createdAtEpochMs).reversed());
            writeAllInternal(rows);
            return toBooking(row);
        }
    }

    public static List<Booking> findByPhoneKey(String key10) {
        synchronized (LOCK) {
            List<Booking> out = new ArrayList<>();
            for (BookingRow row : readAllInternal()) {
                if (PhoneKey.matchesStored(row.phone(), key10)) {
                    out.add(toBooking(row));
                }
            }
            out.sort(Comparator.comparing(Booking::createdAt).reversed());
            return out;
        }
    }

    public static List<Booking> listAllByCreatedDesc() {
        synchronized (LOCK) {
            List<Booking> out = new ArrayList<>();
            for (BookingRow row : readAllInternal()) {
                out.add(toBooking(row));
            }
            out.sort(Comparator.comparing(Booking::createdAt).reversed());
            return out;
        }
    }

    public static boolean updateStatus(long bookingId, String status) {
        synchronized (LOCK) {
            List<BookingRow> rows = readAllInternal();
            boolean updated = false;
            String normalized = safe(status).toUpperCase(Locale.ROOT);
            for (int i = 0; i < rows.size(); i++) {
                BookingRow row = rows.get(i);
                if (row.id() == bookingId) {
                    rows.set(i, new BookingRow(
                            row.id(),
                            row.customerName(),
                            row.phone(),
                            row.email(),
                            row.serviceType(),
                            row.description(),
                            row.address(),
                            row.preferredAtIso(),
                            normalized,
                            row.createdAtEpochMs()
                    ));
                    updated = true;
                    break;
                }
            }
            if (updated) {
                writeAllInternal(rows);
            }
            return updated;
        }
    }

    private static long nextId(List<BookingRow> rows) {
        long max = 0L;
        for (BookingRow row : rows) {
            if (row.id() > max) {
                max = row.id();
            }
        }
        long byTime = System.currentTimeMillis();
        return Math.max(max + 1, byTime);
    }

    private static Booking toBooking(BookingRow row) {
        LocalDateTime preferredAt = null;
        if (row.preferredAtIso() != null && !row.preferredAtIso().isBlank()) {
            preferredAt = Booking.parsePreferred(row.preferredAtIso());
        }
        Instant createdAt = Instant.ofEpochMilli(Math.max(0L, row.createdAtEpochMs()));
        return new Booking(
                row.id(),
                row.customerName(),
                row.phone(),
                row.email(),
                row.serviceType(),
                row.description(),
                row.address(),
                preferredAt,
                row.status(),
                createdAt
        );
    }

    private static List<BookingRow> readAllInternal() {
        try {
            ensureStore();
            try (Reader reader = Files.newBufferedReader(STORE_FILE, StandardCharsets.UTF_8)) {
                List<BookingRow> rows = ApiJson.GSON.fromJson(reader, LIST_TYPE);
                return rows == null ? new ArrayList<>() : new ArrayList<>(rows);
            }
        } catch (Exception e) {
            LOG.warning("Failed to read managed bookings from " + STORE_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void writeAllInternal(List<BookingRow> rows) {
        try {
            ensureStore();
            try (Writer writer = Files.newBufferedWriter(STORE_FILE, StandardCharsets.UTF_8)) {
                ApiJson.GSON.toJson(rows, LIST_TYPE, writer);
            }
        } catch (IOException e) {
            LOG.warning("Failed to write managed bookings to " + STORE_FILE + ": " + e.getMessage());
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

    private static String safe(String value) {
        return value == null ? "" : value.trim();
    }

    public record BookingRow(
            long id,
            String customerName,
            String phone,
            String email,
            String serviceType,
            String description,
            String address,
            String preferredAtIso,
            String status,
            long createdAtEpochMs
    ) {
    }
}

