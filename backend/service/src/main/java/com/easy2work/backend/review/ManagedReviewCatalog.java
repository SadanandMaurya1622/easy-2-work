package com.easy2work.backend.review;

import com.easy2work.backend.api.ApiJson;
import com.easy2work.core.model.Review;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * File-backed review store shared across admin and user apps.
 */
public final class ManagedReviewCatalog {

    private static final Logger LOG = Logger.getLogger(ManagedReviewCatalog.class.getName());
    private static final Object LOCK = new Object();
    private static final Type LIST_TYPE = new TypeToken<List<ReviewRow>>() { }.getType();
    private static final Path ROOT = resolveRootDir();
    private static final Path STORE_FILE = ROOT.resolve("reviews.json");

    private ManagedReviewCatalog() {
    }

    public static long insert(long bookingId, String customerName, String serviceType, int rating, String comment) {
        synchronized (LOCK) {
            List<ReviewRow> all = readAllInternal();
            long nextId = nextId(all);
            all.add(new ReviewRow(nextId, bookingId, safe(customerName), safe(serviceType), rating, safe(comment), Instant.now().toEpochMilli()));
            all.sort(Comparator.comparingLong(ReviewRow::createdAtMillis).reversed());
            writeAllInternal(all);
            return nextId;
        }
    }

    public static List<Review> findAllOrderByCreatedDesc(int limit) {
        synchronized (LOCK) {
            return toReviews(readAllInternal(), limit);
        }
    }

    public static List<Review> findByServiceType(String serviceType, int limit) {
        String normalized = safe(serviceType).toUpperCase(Locale.ROOT);
        synchronized (LOCK) {
            List<ReviewRow> filtered = readAllInternal().stream()
                    .filter(r -> r.serviceType() != null && r.serviceType().toUpperCase(Locale.ROOT).equals(normalized))
                    .toList();
            return toReviews(filtered, limit);
        }
    }

    public static double getAverageRating(String serviceType) {
        List<Review> list = findByServiceType(serviceType, 10_000);
        if (list.isEmpty()) {
            return 0.0;
        }
        double total = 0;
        for (Review r : list) {
            total += r.rating();
        }
        return total / list.size();
    }

    private static long nextId(List<ReviewRow> rows) {
        AtomicLong max = new AtomicLong(1000);
        for (ReviewRow row : rows) {
            max.set(Math.max(max.get(), row.id()));
        }
        return max.incrementAndGet();
    }

    private static List<Review> toReviews(List<ReviewRow> rows, int limit) {
        int cap = Math.min(Math.max(limit, 1), 10_000);
        List<Review> out = new ArrayList<>();
        for (int i = 0; i < rows.size() && i < cap; i++) {
            ReviewRow r = rows.get(i);
            out.add(new Review(r.id(), r.bookingId(), r.customerName(), r.serviceType(), r.rating(), r.comment(),
                    Instant.ofEpochMilli(r.createdAtMillis())));
        }
        return out;
    }

    private static List<ReviewRow> readAllInternal() {
        try {
            ensureStore();
            try (Reader reader = Files.newBufferedReader(STORE_FILE, StandardCharsets.UTF_8)) {
                List<ReviewRow> rows = ApiJson.GSON.fromJson(reader, LIST_TYPE);
                List<ReviewRow> safeRows = rows == null ? new ArrayList<>() : new ArrayList<>(rows);
                safeRows.sort(Comparator.comparingLong(ReviewRow::createdAtMillis).reversed());
                return safeRows;
            }
        } catch (Exception e) {
            LOG.warning("Failed to read managed reviews from " + STORE_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void writeAllInternal(List<ReviewRow> rows) {
        try {
            ensureStore();
            try (Writer writer = Files.newBufferedWriter(STORE_FILE, StandardCharsets.UTF_8)) {
                ApiJson.GSON.toJson(rows, LIST_TYPE, writer);
            }
        } catch (IOException e) {
            LOG.warning("Failed to write managed reviews to " + STORE_FILE + ": " + e.getMessage());
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

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static Path resolveRootDir() {
        String override = System.getProperty("easy2work.data.dir");
        if (override != null && !override.isBlank()) {
            return Paths.get(override.trim());
        }
        Path cwd = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        Path p = cwd;
        while (p != null) {
            if (Files.exists(p.resolve("admin-portal")) && Files.exists(p.resolve("user-portal")) && Files.exists(p.resolve("backend"))) {
                return p.resolve(".easy2work");
            }
            p = p.getParent();
        }
        return cwd.resolve(".easy2work");
    }

    private record ReviewRow(
            long id,
            long bookingId,
            String customerName,
            String serviceType,
            int rating,
            String comment,
            long createdAtMillis
    ) { }
}
