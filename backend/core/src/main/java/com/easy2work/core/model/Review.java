package com.easy2work.core.model;

import java.time.Instant;
import java.util.Date;

/**
 * Represents a customer review/feedback for a service.
 */
public final class Review {

    private final long id;
    private final long bookingId;
    private final String customerName;
    private final String serviceType;
    private final int rating; // 1-5 stars
    private final String comment;
    private final Instant createdAt;

    public Review(long id, long bookingId, String customerName, String serviceType,
                  int rating, String comment, Instant createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.serviceType = serviceType;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public long id() {
        return id;
    }

    public long bookingId() {
        return bookingId;
    }

    public String customerName() {
        return customerName;
    }

    public String serviceType() {
        return serviceType;
    }

    public int rating() {
        return rating;
    }

    public String comment() {
        return comment;
    }

    public Instant createdAt() {
        return createdAt;
    }

    // JavaBean getters for JSP
    public long getId() {
        return id;
    }

    public long getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Date getCreatedAtDate() {
        return Date.from(createdAt);
    }

    /** Returns star icons for display (★★★★★) */
    public String getStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stars.append(i < rating ? "★" : "☆");
        }
        return stars.toString();
    }
}
