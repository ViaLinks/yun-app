package com.yunapp.libx;

import android.app.Application;

import com.yunapp.libx.utils.StorageUtil;

import java.io.File;

public class AppConfig {
    public static final String NAME = "YunApp";
    public static final String VERSION = "1.0";
    public static final boolean DEBUG = true;

    private Application context;
    public String appId;

    public AppConfig(Application context, String id) {
        appId = id;
    }

    /**
     * ../yun-app/$appId/zip/
     *
     * @return app未解压zip包的位置
     */
    public File getZipDir() {
        return StorageUtil.getSubDir(context, "zip");
    }

    /**
     * ../yun-app/$appId/zip/$appId.zip
     *
     * @return app压缩包
     */
    public File getZipFile() {
        return new File(getZipDir(), String.format("%s.zip", appId));
    }

    /**
     * ../yun-app/$appId/source/
     *
     * @return app代码位置
     */
    public File getAppSourceDir() {
        return StorageUtil.getSubDir(context, "source");
    }

    public String getHomePage() {
        return null;
    }
}
