package com.yunapp.libx.utils;

import android.content.Context;

import java.io.File;

public class StorageUtil {

    private static final String BASE = "yunApp";

    /**
     * ../yunApp/
     *
     * @param context
     * @return
     */
    private static File getBaseDir(Context context) {
        File dir = new File(context.getFilesDir(), BASE);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * ../yunApp/$appId/$subPath/
     *
     * @param context
     * @param appId
     * @param subPath
     * @return
     */
    public static File getSubDir(Context context, String appId, String subPath) {
        File dir = new File(StorageUtil.getBaseDir(context), appId + File.separatorChar + subPath);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

}
