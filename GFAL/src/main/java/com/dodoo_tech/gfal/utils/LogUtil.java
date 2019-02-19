package com.dodoo_tech.gfal.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dodoo_tech.gfal.app.GFALApp;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * create by hui at 2017/6/11
 */
public class LogUtil {

    private static boolean debugMode;
    private static boolean logToFile;
    private static String packageName = "com.dodoo.gfal";
    private static long logCount = 0;
    private static long logDuration = System.currentTimeMillis();

    public static void setLogToFile(boolean logToFile) {
        LogUtil.logToFile = logToFile;
    }

    public static void logInfo(String className, String msg) {
        logMsg(className, LogType.I, null, msg, null);
    }

    public static void logInfo(String msg) {
        logMsg(null, LogType.I, null, msg, null);
    }

    public static void logInfo(String className, String method, String msg) {
        logMsg(className, LogType.I, method, msg, null);
    }

    private synchronized static void logMsg(@Nullable String className, @NonNull LogType logType, @Nullable String method, @Nullable String msg, @Nullable Throwable throwable) {

        if (StringUtil.isEmpty(className)) {
            className = "";
        } else {
            className = "[" + className + "]";
        }
        if (StringUtil.isEmpty(method)) {
            method = "";
        } else {
            method = "[" + method + "]";
        }
        try {
            if (isDebugMode()) {
                addLogCount();

                String message = "[" + getLogCount() + "][" + getLogDuration() + "]" + className + method + "->" + msg;
                if (throwable != null) {
                    message += "\n" + throwableToString(throwable);
                }
                switch (logType) {
                    case V:
                        Log.v(packageName, message);
                        break;
                    case D:
                        Log.d(packageName, message);
                        break;
                    case I:
                        Log.i(packageName, message);
                        break;
                    case W:
                        Log.w(packageName, message);
                        break;
                    case E:
                        Log.e(packageName, message);
                        break;
                    default:
                        Log.w(packageName, "show log failed: logType");
                        break;
                }

                if (logToFile) {
                    LogcatUtil.dumpSysLog(GFALApp.getContext(), packageName, message, logType.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
			/*String error = throwableToString(e);
			Log.e(packageName,error);*/
        }
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean isDebugMode) {
        debugMode = isDebugMode;
    }

    public synchronized static void addLogCount() {
        logCount++;
    }

    public static long getLogCount() {
        return logCount;
    }

    public synchronized static long getLogDuration() {
        long current = System.currentTimeMillis();
        long l = current - logDuration;
        logDuration = current;
        return l;
    }

    private static String throwableToString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public static void logDebug(String className, String msg) {
        logMsg(className, LogType.D, null, msg, null);
    }

    public static void logDebug(String className, String method, String msg) {
        logMsg(className, LogType.D, method, msg, null);
    }

    public static void logError(String className, String method, String msg) {
        logError(className, method, msg, null);
    }

    public static void logError(String error) {
        logError(null, null, error, null);
    }

    public static void logError(String className, String method, String msg, Throwable e) {
        logMsg(className, LogType.E, method, msg, e);
    }

    public static void logError(Throwable e) {
        logError(null, null, null, e);
    }

    public static void logError(String className, Throwable e) {
        logError(className, null, null, e);
    }

    public static void logError(String className, String method, Throwable e) {
        logError(className, method, null, e);
    }

    public static void logInfo(String className, Object object) {
        String msg = null;
        try {
            msg = JSON.toJSONString(object);
            logInfo(className, msg);
        } catch (Exception e) {
            logError(className, e);
        }
    }

    public static void logInfo(Object object) {
        String msg = null;
        try {
            msg = JSON.toJSONString(object);
            logInfo(null, msg);
        } catch (Exception e) {
            logError(e);
        }
    }

    public static void setPackageName(String packageName) {
        LogUtil.packageName = packageName;
    }

    public static void logWarn(String className, String method, String warn) {
        logMsg(className, LogType.W, method, warn, null);
    }

    public static void logWarn(String className, String method, Throwable e) {
        logMsg(className, LogType.W, method, null, e);
    }

    private enum LogType {
        V,
        D,
        I,
        W,
        E
    }
}
