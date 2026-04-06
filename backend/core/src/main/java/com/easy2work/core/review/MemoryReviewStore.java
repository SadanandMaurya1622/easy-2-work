package com.easy2work.core.review;

import com.easy2work.core.model.Review;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory review storage (for demo without database).
 */
public final class MemoryReviewStore {

    public static final String SERVLET_CONTEXT_KEY = "easy2work.MemoryReviewStore";

    private final AtomicLong idCounter = new AtomicLong(1000);
    private final ConcurrentHashMap<Long, Review> reviews = new ConcurrentHashMap<>();

    public long insert(long bookingId, String customerName, String serviceType,
                      int rating, String comment) {
        long id = idCounter.incrementAndGet();
        Review review = new Review(id, bookingId, customerName, serviceType,
            rating, comment, Instant.now());
        reviews.put(id, review);
        return id;
    }

    public List<Review> findByServiceType(String serviceType, int limit) {
        return reviews.values().stream()
            .filter(r -> r.serviceType().equalsIgnoreCase(serviceType))
            .sorted(Comparator.comparing(Review::createdAt).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<Review> findAllOrderByCreatedDesc(int limit) {
        return reviews.values().stream()
            .sorted(Comparator.comparing(Review::createdAt).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    public double getAverageRating(String serviceType) {
        List<Review> filtered = reviews.values().stream()
            .filter(r -> r.serviceType().equalsIgnoreCase(serviceType))
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return 0.0;
        }

        double sum = filtered.stream().mapToInt(Review::rating).sum();
        return sum / filtered.size();
    }
}
