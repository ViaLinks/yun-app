package com.yunapp.libx;

import android.app.Application;
import android.support.annotation.NonNull;

import com.yunapp.libx.utils.FileUtil;
import com.yunapp.libx.utils.LogUtil;
import com.yunapp.libx.utils.StorageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 该类的属性和方法在代码zip解压之后访问才是安全的
 */
public class AppContext {

    private Config config;
    private String main;

    /**
     * 获取webcore执行的代码路径
     * ../yun-app/$appId/source/service.html
     *
     * @return
     */
    public File getWebCoreJsFile() {
        return new File(config.getAppSourceDir(), "service.html");
    }

    /**
     * 获取配置文件路径
     *
     * @return
     */
    public File getConfigJsonFile() {
        return new File(config.getAppSourceDir(), "app.json");
    }

    public File getPageDir() {
        File dir = new File(config.getAppSourceDir(), "pages");
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    public File getPage(String pageId) {
        return new File(getPageDir(), pageId + ".html");
    }

    public File getHomePage() {
        return getPage(main);
    }


    public static AppContext buildAppContext(@NonNull Config config) {
        return new AppContext(config);
    }

    private AppContext(Config config) {
        this.config = config;
        try {
            JSONObject appConfig = new JSONObject(FileUtil.readContent(getConfigJsonFile()));
            this.main = appConfig.optString("main", "index");
        } catch (JSONException e) {
            LogUtil.e(e);
        }
    }


    /**
     * 该类的方法和属性在代码zip文件解压前也是安全的
     */
    public static class Config {
        public static final String NAME = "YunApp";
        public static final String VERSION = "1.0";
        public static final boolean DEBUG = true;

        private Application context;
        public String appId;

        public Config(Application context, String id) {
            appId = id;
            this.context = context;
        }

        /**
         * ../yun-app/$appId/zip/
         *
         * @return app未解压zip包的位置
         */
        public File getZipDir() {
            return StorageUtil.getSubDir(context, appId, "zip");
        }

        /**
         * ../yun-app/$appId/source/
         *
         * @return app代码位置
         */
        public File getAppSourceDir() {
            return StorageUtil.getSubDir(context, appId, "source");
        }

        public File getStorageDir() {
            return StorageUtil.getSubDir(context, appId, "storage");
        }

        /**
         * ../yun-app/$appId/zip/$appId.zip
         *
         * @return app压缩包
         */
        public File getCodeZip() {
            return new File(getZipDir(), String.format("%s.zip", appId));
        }
    }
}
