package com.kk.binding.util;

import android.util.Log;

/**
 * Created by hk on 13-12-8.
 */
public class BindDesignLog {
    private static boolean inDesignMode = false;
    private static StringBuilder fullLog;
    private static boolean logOpen = true;

    public static void d(String tag, String log) {
        if (inDesignMode) {
            designLog(tag, log);
        } else if (logOpen) {
            Log.d(tag, log);
        }
    }

    public static void i(String tag, String log) {
        if (inDesignMode) {
            designLog(tag, log);
        } else if (logOpen) {
            Log.i(tag, log);
        }
    }

    public static void v(String tag, String log) {
        if (inDesignMode) {
            designLog(tag, log);
        } else if (logOpen) {
            Log.v(tag, log);
        }
    }

    public static void e(String tag, String log) {
        if (inDesignMode) {
            designLog(tag, log);
        } else if (logOpen) {
            Log.e(tag, log);
        }
    }

    public static void w(String tag, String log) {
        if (inDesignMode) {
            designLog(tag, log);
        } else if (logOpen) {
            Log.w(tag, log);
        }
    }

    private static void designLog(String tag, String log) {
        if (fullLog != null) {
            fullLog.append("\nTAG: ").append(tag).append(" ");
            fullLog.append(log);
            fullLog.append("\n");
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

    public static void setBindLogOpen(boolean open) {
        logOpen = open;
    }
}
