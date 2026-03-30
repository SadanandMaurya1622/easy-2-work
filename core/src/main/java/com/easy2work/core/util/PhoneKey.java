package com.easy2work.core.util;

public final class PhoneKey {

    private PhoneKey() {
    }

    /** Last 10 digits for Indian mobiles (+91 / spaces stripped). */
    public static String fromInput(String raw) {
        if (raw == null) {
            return "";
        }
        String d = raw.replaceAll("[^0-9]", "");
        if (d.length() >= 10) {
            return d.substring(d.length() - 10);
        }
        return d;
    }

    public static boolean matchesStored(String storedPhone, String key10) {
        return fromInput(storedPhone).equals(key10);
    }
}
