package de.sindzinski.lpictrainer;

import android.util.Log;
/**
 * Created by steffen on 13.03.14.
 *
 * set logging on on adb shell using
 * adb shell setprop log.tag.MyAppTag WARN
 *
 */
public class Logger {
    //static String TAG = "MyApp";

    private static final Boolean LOGGING = true;

    public static void v(String tag, String str) {
        if ((Log.isLoggable(tag, Log.VERBOSE)) || LOGGING) {
            Log.v(tag, str);
        }
    }
    public static void v(String tag, String str, Throwable t) {
        if ((Log.isLoggable(tag, Log.VERBOSE)) || LOGGING) {
            Log.v(tag, str, t);
        }
    }

    public static void i(String tag, String str) {
        if ((Log.isLoggable(tag, Log.INFO)) || LOGGING) {
            Log.i(tag, str);
        }
    }
    public static void i(String tag, String str, Throwable t) {
        if ((Log.isLoggable(tag, Log.INFO)) || LOGGING) {
            Log.i(tag, str, t);
        }
    }
    public static void w(String tag, String str) {
        if ((Log.isLoggable(tag, Log.WARN)) || LOGGING) {
            Log.w(tag, str);
        }
    }
    public static void w(String tag, String str, Throwable t) {
        if ((Log.isLoggable(tag, Log.WARN)) || LOGGING) {
            Log.w(tag, str, t);
        }
    }
    public static void d(String tag, String str) {
        if ((Log.isLoggable(tag, Log.DEBUG)) || LOGGING) {
            Log.d(tag, str);
        }
    }
    public static void d(String tag, String str, Throwable t) {
        if ((Log.isLoggable(tag, Log.DEBUG)) || LOGGING) {
            Log.d(tag, str, t);
        }
    }
    public static void e(String tag, String str) {
        if ((Log.isLoggable(tag, Log.ERROR)) || LOGGING) {
            Log.e(tag, str);
        }
    }
    public static void e(String tag, String str, Throwable t) {
        if ((Log.isLoggable(tag, Log.ERROR)) || LOGGING) {
            Log.e(tag, str, t);
        }
    }
}
