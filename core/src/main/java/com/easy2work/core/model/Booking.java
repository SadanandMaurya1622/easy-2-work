package com.easy2work.core.model;

import com.easy2work.catalog.ServiceCatalog;
import com.easy2work.core.util.DateUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public final class Booking {

    private final long id;
    private final String customerName;
    private final String phone;
    private final String email;
    private final String serviceType;
    private final String description;
    private final String address;
    private final LocalDateTime preferredAt;
    private final String status;
    private final Instant createdAt;

    public Booking(long id, String customerName, String phone, String email, String serviceType,
                   String description, String address, LocalDateTime preferredAt, String status, Instant createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.serviceType = serviceType;
        this.description = description;
        this.address = address;
        this.preferredAt = preferredAt;
        this.status = status;
        this.createdAt = createdAt;
    }

    public long id() {
        return id;
    }

    public String customerName() {
        return customerName;
    }

    public String phone() {
        return phone;
    }

    public String email() {
        return email;
    }

    public String serviceType() {
        return serviceType;
    }

    public String description() {
        return description;
    }

    public String address() {
        return address;
    }

    public LocalDateTime preferredAt() {
        return preferredAt;
    }

    public String status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getServiceType() {
        return serviceType;
    }

    /** Title for UI, e.g. "AC servicing" instead of "AC". */
    public String getServiceTitle() {
        return ServiceCatalog.displayTitleForCode(serviceType);
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getPreferredAt() {
        return preferredAt;
    }

    public String getStatus() {
        return status;
    }

    /** For JSP badges: {@code done}, {@code cancelled}, or {@code active}. */
    public String getStatusKind() {
        if (status == null || status.isBlank()) {
            return "active";
        }
        return switch (status.trim().toUpperCase(Locale.ROOT)) {
            case "COMPLETED" -> "done";
            case "CANCELLED" -> "cancelled";
            default -> "active";
        };
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /** For JSTL {@code fmt:formatDate}. */
    public Date getCreatedAtDate() {
        return Date.from(createdAt);
    }

    public String getBookedAtDisplay() {
        return DateUtil.formatBookedAt(createdAt);
    }

    public static LocalDateTime parsePreferred(String isoLocal) {
        if (isoLocal == null || isoLocal.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(isoLocal);
        } catch (Exception e) {
            return null;
        }
    }
}
