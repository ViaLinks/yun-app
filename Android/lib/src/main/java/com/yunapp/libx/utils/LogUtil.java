package com.yunapp.libx.utils;

import android.util.Log;

import com.yunapp.libx.AppContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class LogUtil {

    private static final String TAG = "LogUtil";

    public static void d(String debugInfo) {
        d(TAG, debugInfo);
    }

    public static void d(String tag, String debugInfo) {
        if (AppContext.Config.DEBUG) {
            Log.d(tag, debugInfo);
        }
    }

    public static void w(String warning) {
        w(TAG, warning);
    }

    public static void w(String tag, String warning) {
        if (AppContext.Config.DEBUG) {
            Log.w(tag, warning);
        }
    }

    public static void e(String error) {
        e(TAG, error);
    }

    public static void e(String tag, String error) {
        if (AppContext.Config.DEBUG) {
            Log.e(tag, error);
        }
    }

    public static void e(Exception exception) {
        e(TAG, exception);
    }

    public static void e(String tag, Exception exception) {
        String stackInfo = getAllStackInformation(exception);
        e(tag, stackInfo);
    }

    /**
     * 获取所有堆栈信息
     *
     * @param ex
     * @return
     */
    private static String getAllStackInformation(Throwable ex) {
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();

            return writer.toString();
        } catch (Throwable e) {
            e("getAllStackInformation(Throwable) catch error " + e);
        }
        return "unknown: get stack information error";
    }
}
