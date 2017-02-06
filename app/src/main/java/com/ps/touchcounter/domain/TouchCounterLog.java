package com.ps.touchcounter.domain;

import android.util.Log;

public class TouchCounterLog {
    private static final String LOG_PREFIX = "TouchCounter";
    private static final boolean debug = false;
    private static final int MAX_TAG_LENGTH = 23;

    public static String setLogTag(Class className) {
        return buildLogTag(className.getSimpleName());
    }

    public static String buildLogTag(String str) {
        if (str.length() > MAX_TAG_LENGTH - LOG_PREFIX.length()) {
            return LOG_PREFIX + str.substring(0, MAX_TAG_LENGTH - LOG_PREFIX.length() - 1);
        }

        return LOG_PREFIX + str;
    }


    public static void Debug(final String logTag,String message) {
        if (debug) {
            Log.d(logTag, message);
        }
    }


    public static void Info(final String logTag,String message) {
        if (debug) {
            Log.i(logTag, message);
        }
    }

    public static void Warning(final String logTag,String message) {
        if (debug) {
            Log.w(logTag, message);
        }
    }

    public static void Error(final String logTag,String message) {
        if (debug) {
            Log.e(logTag, message);
        }
    }

}
