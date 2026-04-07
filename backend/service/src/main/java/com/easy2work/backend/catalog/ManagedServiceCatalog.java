package com.easy2work.backend.catalog;

import com.easy2work.backend.api.ApiJson;
import com.easy2work.catalog.ServiceDetail;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Small file-backed catalog for admin-added services.
 * Shared across user/admin apps by storing in user home.
 */
public final class ManagedServiceCatalog {

    private static final Logger LOG = Logger.getLogger(ManagedServiceCatalog.class.getName());
    private static final Object LOCK = new Object();
    private static final Type LIST_TYPE = new TypeToken<List<ManagedService>>() { }.getType();
    private static final Path ROOT = resolveRootDir();
    private static final Path STORE_FILE = ROOT.resolve("managed-services.json");

    private ManagedServiceCatalog() {
    }

    public static List<ManagedService> list() {
        synchronized (LOCK) {
            return new ArrayList<>(readAllInternal());
        }
    }

    public static Optional<ManagedService> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        String normalized = code.trim().toUpperCase(Locale.ROOT);
        return list().stream().filter(s -> normalized.equals(s.code())).findFirst();
    }

    public static ManagedService add(
            String code,
            String title,
            String summary,
            String priceLabel,
            String imageDataUrl,
            String priceDetail,
            List<String> weProvide,
            List<String> fromYou,
            List<String> notIncluded,
            List<String> visitSteps
    ) {
        synchronized (LOCK) {
            List<ManagedService> all = readAllInternal();
            String normalizedCode = normalizeCode(code, title, all);
            ManagedService row = new ManagedService(
                    UUID.randomUUID().toString(),
                    normalizedCode,
                    safe(title),
                    safe(summary),
                    safe(priceLabel).isBlank() ? "On request" : safe(priceLabel),
                    safe(imageDataUrl),
                    safe(priceDetail),
                    safeList(weProvide),
                    safeList(fromYou),
                    safeList(notIncluded),
                    safeList(visitSteps)
            );
            all.removeIf(s -> s.code().equals(row.code()));
            all.add(row);
            all.sort(Comparator.comparing(ManagedService::title, String.CASE_INSENSITIVE_ORDER));
            writeAllInternal(all);
            return row;
        }
    }

    public static void deleteById(String id) {
        if (id == null || id.isBlank()) {
            return;
        }
        synchronized (LOCK) {
            List<ManagedService> all = readAllInternal();
            all.removeIf(s -> id.equals(s.id()));
            writeAllInternal(all);
        }
    }

    public static ServiceDetail toServiceDetail(ManagedService s) {
        List<String> defaultBullets = List.of(
                "Inspection and scope confirmation before work starts",
                "Basic service delivery by trained professional",
                "Final handover after completion"
        );
        List<String> defaultFromYou = List.of("Share exact requirement and preferred timing.");
        List<String> defaultNotIncluded = List.of("Material costs and heavy rework are charged separately.");
        List<String> defaultVisitSteps = List.of("Book the service", "Admin/pro confirms availability", "Service is delivered");
        return new ServiceDetail(
                s.code(),
                s.title(),
                s.imageDataUrl().isBlank() ? "https://images.unsplash.com/photo-1497366754035-f200968a6e72?w=1200&q=85" : s.imageDataUrl(),
                s.summary(),
                s.priceLabel(),
                s.priceDetail().isBlank() ? "Final price depends on job scope and location." : s.priceDetail(),
                safeOrDefault(s.weProvide(), defaultBullets),
                safeOrDefault(s.fromYou(), defaultFromYou),
                safeOrDefault(s.notIncluded(), defaultNotIncluded),
                safeOrDefault(s.visitSteps(), defaultVisitSteps)
        );
    }

    private static List<ManagedService> readAllInternal() {
        try {
            ensureStore();
            try (Reader reader = Files.newBufferedReader(STORE_FILE, StandardCharsets.UTF_8)) {
                List<ManagedService> rows = ApiJson.GSON.fromJson(reader, LIST_TYPE);
                return rows == null ? new ArrayList<>() : new ArrayList<>(rows);
            }
        } catch (Exception e) {
            LOG.warning("Failed to read managed services from " + STORE_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void writeAllInternal(List<ManagedService> rows) {
        try {
            ensureStore();
            try (Writer writer = Files.newBufferedWriter(STORE_FILE, StandardCharsets.UTF_8)) {
                ApiJson.GSON.toJson(rows, LIST_TYPE, writer);
            }
        } catch (IOException e) {
            LOG.warning("Failed to write managed services to " + STORE_FILE + ": " + e.getMessage());
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

    private static String normalizeCode(String requestedCode, String title, List<ManagedService> all) {
        String base = safe(requestedCode).replaceAll("[^A-Za-z0-9_]", "_").toUpperCase(Locale.ROOT);
        if (base.isBlank()) {
            base = safe(title).replaceAll("[^A-Za-z0-9_]", "_").toUpperCase(Locale.ROOT);
        }
        if (base.isBlank()) {
            base = "SERVICE_" + (all.size() + 1);
        }
        return base;
    }

    private static String safe(String v) {
        return v == null ? "" : v.trim();
    }

    private static List<String> safeList(List<String> values) {
        if (values == null) {
            return List.of();
        }
        List<String> out = new ArrayList<>();
        for (String value : values) {
            String cleaned = safe(value);
            if (!cleaned.isBlank()) {
                out.add(cleaned);
            }
        }
        return out;
    }

    private static List<String> safeOrDefault(List<String> values, List<String> fallback) {
        List<String> cleaned = safeList(values);
        return cleaned.isEmpty() ? fallback : cleaned;
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

    public record ManagedService(
            String id,
            String code,
            String title,
            String summary,
            String priceLabel,
            String imageDataUrl,
            String priceDetail,
            List<String> weProvide,
            List<String> fromYou,
            List<String> notIncluded,
            List<String> visitSteps
    ) {
        public String getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getTitle() {
            return title;
        }

        public String getSummary() {
            return summary;
        }

        public String getPriceLabel() {
            return priceLabel;
        }

        public String getImageDataUrl() {
            return imageDataUrl;
        }

        public String getPriceDetail() {
            return priceDetail;
        }

        public List<String> getWeProvide() {
            return weProvide;
        }

        public List<String> getFromYou() {
            return fromYou;
        }

        public List<String> getNotIncluded() {
            return notIncluded;
        }

        public List<String> getVisitSteps() {
            return visitSteps;
        }
    }
}
