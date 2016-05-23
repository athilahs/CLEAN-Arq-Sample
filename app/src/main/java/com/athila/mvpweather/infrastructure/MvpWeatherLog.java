package com.athila.mvpweather.infrastructure;

import android.text.TextUtils;
import android.util.Log;

import com.athila.mvpweather.BuildConfig;


@SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
public class MvpWeatherLog {

    private static final String DEFAULT_TAG = "MVPWheather";

    // ATTENTION: these values must be in match with the values used in build.gradle for log level definitions
    private static final int LOG_LEVEL_DEBUG = 4;
    private static final int LOG_LEVEL_INFO = 3;
    private static final int LOG_LEVEL_WARN = 2;
    private static final int LOG_LEVEL_ERROR = 1;

    private MvpWeatherLog() {
        // just to disable default constructor
    }

    public static void debug(String tag, String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_DEBUG && !TextUtils.isEmpty(msg)) {
            Log.v(tag, msg);
        }
    }

    public static void debug(String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_DEBUG && !TextUtils.isEmpty(msg)) {
            Log.v(DEFAULT_TAG, msg);
        }
    }

    public static void info(String tag, String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_INFO && !TextUtils.isEmpty(msg)) {
            Log.i(tag, msg);
        }
    }

    public static void info(String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_INFO && !TextUtils.isEmpty(msg)) {
            Log.i(DEFAULT_TAG, msg);
        }
    }

    public static void warn(String tag, String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_WARN && !TextUtils.isEmpty(msg)) {
            Log.w(tag, msg);
        }
    }

    public static void warn(String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_WARN && !TextUtils.isEmpty(msg)) {
            Log.w(DEFAULT_TAG, msg);
        }
    }

    public static void error(String tag, String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_ERROR && !TextUtils.isEmpty(msg)) {
            Log.e(tag, msg);
        }
    }

    public static void error(String msg) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_ERROR && !TextUtils.isEmpty(msg)) {
            Log.e(DEFAULT_TAG, msg);
        }
    }

    public static void error(String msg, Throwable ex) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_ERROR && !TextUtils.isEmpty(msg)) {
            Log.e(DEFAULT_TAG, msg, ex);
        }
    }

    public static void error(String tag, String msg, Throwable ex) {
        if (BuildConfig.LOG_LEVEL >= LOG_LEVEL_ERROR && !TextUtils.isEmpty(msg)) {
            Log.e(tag, msg, ex);
        }
    }
}
