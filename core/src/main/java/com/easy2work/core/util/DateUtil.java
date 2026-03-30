package com.easy2work.core.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateUtil {

    /** English month names regardless of JVM default locale (UI is English-only). */
    private static final DateTimeFormatter BOOKED_AT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                    .withLocale(Locale.ENGLISH)
                    .withZone(ZoneId.systemDefault());

    private DateUtil() {
    }

    /** Plain text for JSP without JSTL fmt (Jetty-safe). */
    public static String formatBookedAt(Instant createdAt) {
        if (createdAt == null) {
            return "";
        }
        return BOOKED_AT.format(createdAt);
    }
}
