/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.logging;

import android.support.annotation.Nullable;
import android.util.Log;


/**
 * It is used to provide log history in order to show in the bee.
 */
public final class BeeLog {

    public static boolean DEBUG = false;
    private static String tag = "ToshNgure";

    private BeeLog() {
        // no instance
    }

    public static void init(boolean debug, @Nullable String logTag) {
        if (logTag != null) {
            tag = logTag;
        }
        DEBUG = debug;
    }

    public static void d(String subTag, Object message) {
        if (DEBUG) {
            Log.d(subTag, subTag + " : " + message);
            addToHistory(subTag, String.valueOf(message));
        }
    }

    public static void e(String subTag, Object message) {
        if (DEBUG) {
            Log.e(subTag, subTag + " : " + message);
            addToHistory(subTag, String.valueOf(message));
        }
    }

    public static void e(String subTag, Exception e) {
        if (DEBUG) {
            if (e != null) {
                Log.e(subTag, subTag + " : " + e.getLocalizedMessage());
                addToHistory(subTag, e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    public static void w(String subTag, Object message) {
        if (DEBUG) {
            Log.w(subTag, subTag + " : " + message);
            addToHistory(subTag, String.valueOf(message));
        }
    }


    public static void i(String subTag, Object message) {
        if (DEBUG) {
            Log.i(subTag, subTag + " : " + message);
            addToHistory(subTag, String.valueOf(message));
        }
    }

    private static void addToHistory(String subTag, String message) {
        LogHistoryManager.getInstance().add(new LogItem(subTag, message));
    }

    public static void e(String subTag, Throwable e) {
        if (DEBUG) {
            Log.e(subTag, subTag + " : " + e.getMessage());
            addToHistory(subTag, e.getMessage());
        }
    }
}
