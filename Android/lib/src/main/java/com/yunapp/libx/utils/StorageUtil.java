package com.yunapp.libx.utils;

import android.content.Context;

import java.io.File;

public class StorageUtil {

    private static final String BASE = "yun-app";

    private static File getBaseDir(Context context) {
        File dir = new File(context.getFilesDir(), BASE);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getSubDir(Context context, String subPath) {
        File dir = new File(StorageUtil.getBaseDir(context), subPath);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

}
