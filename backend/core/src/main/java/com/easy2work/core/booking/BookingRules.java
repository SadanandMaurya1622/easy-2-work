package com.easy2work.core.booking;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/** Shared validation for HTML form and JSON booking APIs. */
public final class BookingRules {

    public static final int MAX_NAME = 120;
    public static final int MAX_PHONE = 32;
    public static final int MAX_EMAIL = 255;
    public static final int MAX_TYPE = 64;
    public static final int MAX_DESC = 4000;
    public static final int MAX_ADDR = 2000;

    public static final Set<String> SERVICE_TYPES = Set.of(
            "ELECTRICAL", "AC", "COOLER", "LAUNDRY", "WINDOW", "UTENSILS", "BALCONY", "BATHROOM", "OTHER"
    );

    private static final Pattern PHONE_DIGITS = Pattern.compile("^[0-9+\\s\\-]{10,20}$");

    private BookingRules() {
    }

    /** @return error message or null if valid */
    public static String validate(Map<String, String> form) {
        String name = trim(form.get("customerName"));
        if (name.isEmpty() || name.length() > MAX_NAME) {
            return "Please enter your name (max " + MAX_NAME + " characters).";
        }
        String phoneRaw = form.get("phone");
        if (phoneRaw == null || !PHONE_DIGITS.matcher(phoneRaw.trim()).matches()) {
            return "Please enter a valid phone number (10–20 digits, may include +, spaces, or hyphens).";
        }
        String email = trimToNull(form.get("email"));
        if (email != null && (email.length() > MAX_EMAIL || !email.contains("@"))) {
            return "Please enter a valid email or leave it blank.";
        }
        String type = form.get("serviceType");
        if (type == null || type.isBlank()) {
            return "Please choose a service type.";
        }
        String typeU = type.trim().toUpperCase(Locale.ROOT);
        if (!SERVICE_TYPES.contains(typeU)) {
            return "Invalid service type.";
        }
        String desc = trim(form.get("description"));
        if (desc.isEmpty() || desc.length() > MAX_DESC) {
            return "Please describe what you need (max " + MAX_DESC + " characters).";
        }
        String addr = trim(form.get("address"));
        if (addr.isEmpty() || addr.length() > MAX_ADDR) {
            return "Please enter your full service address.";
        }
        return null;
    }

    public static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    public static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    public static String normalizePhone(String phone) {
        return phone == null ? "" : phone.trim().replaceAll("\\s+", " ");
    }
}
