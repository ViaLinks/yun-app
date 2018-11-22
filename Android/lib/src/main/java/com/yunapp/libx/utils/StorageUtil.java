package com.yunapp.libx.utils;

import android.content.Context;

import java.io.File;

public class StorageUtil {

    private static final String BASE = "yunApp";

    public static File getBaseDir(Context context) {
        File heraDir = new File(context.getFilesDir(), BASE);
        if (!heraDir.exists() || !heraDir.isDirectory()) {
            heraDir.mkdirs();
        }
        return heraDir;
    }

    public static File getAppDir(Context context) {
        File heraAppDir = new File(getBaseDir(context), "app");
        if (!heraAppDir.exists() || !heraAppDir.isDirectory()) {
            heraAppDir.mkdirs();
        }
        return heraAppDir;
    }

    public static File getMiniAppDir(Context context, String appId) {
        File miniAppDir = new File(getAppDir(context), appId);
        if (!miniAppDir.exists() || !miniAppDir.isDirectory()) {
            miniAppDir.mkdirs();
        }
        return miniAppDir;
    }

    public static File getMiniAppSourceDir(Context context, String appId) {
        File miniAppSourceDir = new File(getMiniAppDir(context, appId), "source");
        if (!miniAppSourceDir.exists() || !miniAppSourceDir.isDirectory()) {
            miniAppSourceDir.mkdirs();
        }
        return miniAppSourceDir;
    }


}
