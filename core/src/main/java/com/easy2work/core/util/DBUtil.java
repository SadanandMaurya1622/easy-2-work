package com.easy2work.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class DBUtil {

    private static final Logger LOG = Logger.getLogger(DBUtil.class.getName());

    private DBUtil() {
    }

    public static void closeQuietly(AutoCloseable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Exception e) {
            LOG.log(Level.FINE, "close failed", e);
        }
    }
}
