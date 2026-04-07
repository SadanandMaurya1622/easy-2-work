package com.easy2work.backend.api;

import com.easy2work.backend.config.DatabaseConfig;
import com.easy2work.backend.repository.ReviewRepository;
import com.easy2work.backend.review.ManagedReviewCatalog;
import com.easy2work.core.model.Review;
import com.easy2work.core.review.MemoryReviewStore;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GET /api/reviews?serviceType=... — Get reviews for a service
 * GET /api/reviews — Get all reviews (recent)
 * POST /api/reviews — Submit a new review
 */
public class ReviewsApiServlet extends HttpServlet {

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        synchronized (ctx) {
            if (ctx.getAttribute(MemoryReviewStore.SERVLET_CONTEXT_KEY) == null) {
                ctx.setAttribute(MemoryReviewStore.SERVLET_CONTEXT_KEY, new MemoryReviewStore());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String serviceType = req.getParameter("serviceType");
        int limit = parseLimit(req.getParameter("limit"), 50);

        try {
            List<Review> reviews;
            double avgRating = 0.0;

            DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
            if (ds != null) {
                // Use database
                ReviewRepository repo = new ReviewRepository(ds);
                if (serviceType != null && !serviceType.isBlank()) {
                    reviews = repo.findByServiceType(serviceType, limit);
                    if (!reviews.isEmpty()) {
                        avgRating = repo.getAverageRating(serviceType);
                    }
                } else {
                    reviews = repo.findAllOrderByCreatedDesc(limit);
                }
            } else {
                // Use shared file-backed store when DB is unavailable.
                if (serviceType != null && !serviceType.isBlank()) {
                    reviews = ManagedReviewCatalog.findByServiceType(serviceType, limit);
                    avgRating = ManagedReviewCatalog.getAverageRating(serviceType);
                } else {
                    reviews = ManagedReviewCatalog.findAllOrderByCreatedDesc(limit);
                }
            }

            List<Map<String, Object>> reviewList = new ArrayList<>();
            for (Review r : reviews) {
                reviewList.add(toRow(r));
            }

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("ok", true);
            body.put("reviews", reviewList);
            body.put("count", reviewList.size());
            body.put("averageRating", Math.round(avgRating * 10.0) / 10.0);
            if (serviceType != null && !serviceType.isBlank()) {
                body.put("serviceType", serviceType);
            }
            ApiJson.write(resp, HttpServletResponse.SC_OK, body);

        } catch (Exception e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Could not load reviews: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Read JSON body
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String body = sb.toString();

            // Simple JSON parsing (no external library needed)
            long bookingId = extractLong(body, "bookingId");
            String customerName = extractString(body, "customerName");
            String serviceType = extractString(body, "serviceType");
            int rating = extractInt(body, "rating");
            String comment = extractString(body, "comment");

            // Validate
            if (customerName == null || customerName.isBlank()) {
                ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer name is required");
                return;
            }
            if (serviceType == null || serviceType.isBlank()) {
                ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Service type is required");
                return;
            }
            if (rating < 1 || rating > 5) {
                ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Rating must be between 1 and 5");
                return;
            }
            if (comment == null || comment.isBlank()) {
                ApiJson.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, "Comment is required");
                return;
            }

            long reviewId;
            DataSource ds = (DataSource) req.getServletContext().getAttribute(DatabaseConfig.CTX_DATASOURCE);
            if (ds != null) {
                // Use database
                ReviewRepository repo = new ReviewRepository(ds);
                reviewId = repo.insert(bookingId, customerName, serviceType, rating, comment);
            } else {
                reviewId = ManagedReviewCatalog.insert(bookingId, customerName, serviceType, rating, comment);
            }

            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("ok", true);
            responseBody.put("message", "Review submitted successfully");
            responseBody.put("reviewId", reviewId);
            ApiJson.write(resp, HttpServletResponse.SC_CREATED, responseBody);

        } catch (Exception e) {
            ApiJson.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Could not submit review: " + e.getMessage());
        }
    }

    private static Map<String, Object> toRow(Review r) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", r.getId());
        row.put("bookingId", r.getBookingId());
        row.put("customerName", r.getCustomerName());
        row.put("serviceType", r.getServiceType());
        row.put("rating", r.getRating());
        row.put("comment", r.getComment());
        row.put("stars", r.getStars());
        row.put("createdAtMillis", r.getCreatedAt().toEpochMilli());
        return row;
    }

    private static int parseLimit(String s, int defaultVal) {
        if (s == null || s.isBlank()) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    // Simple JSON extraction helpers
    private static String extractString(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private static int extractInt(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    private static long extractLong(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return Long.parseLong(m.group(1));
        }
        return 0L;
    }
}
