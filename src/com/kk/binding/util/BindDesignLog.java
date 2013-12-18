package com.kk.binding.util;

import android.util.Log;

/**
 * Created by hk on 13-12-8.
 */
public class BindDesignLog {
    private static boolean inDesignMode = false;
    private static StringBuilder fullLog;

    public static void d(String tag, String log) {
        if (inDesignMode) {
            if (fullLog != null) {
                fullLog.append("\nTAG: ").append(tag).append(" ");
                fullLog.append(log);
                fullLog.append("\n");
            }
        } else {
            Log.d(tag, log);
        }
    }

    public static void throwDesignLog() {
        if (inDesignMode) {
            throw new RuntimeException(fullLog != null ? fullLog.toString() : null);
        }
    }

    public static void setInDesignMode(boolean inDesignMode) {
        BindDesignLog.inDesignMode = inDesignMode;
        if (inDesignMode) {
            fullLog = new StringBuilder(1024 * 10);
        }
    }

    public static boolean isInDesignMode() {
        return inDesignMode;
    }
}
