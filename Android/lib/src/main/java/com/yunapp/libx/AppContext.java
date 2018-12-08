package com.yunapp.libx;

import android.app.Application;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.yunapp.libx.utils.FileUtil;
import com.yunapp.libx.utils.LogUtil;
import com.yunapp.libx.utils.StorageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AppContext {

    private Config config;
    private AppInfo appInfo;
    private Map<String, PageInfo> pagesMap = new HashMap();

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
        return new File(getPageDir(), pageId + File.separatorChar + pagesMap.get(pageId).doc);
    }

    public File getHomePage() {
        return getPage(appInfo.main);
    }


    public static AppContext buildAppContext(@NonNull Config config) {
        return new AppContext(config);
    }

    private AppContext(Config config) {
        this.config = config;
        try {
            JSONObject appConfig = new JSONObject(FileUtil.readContent(getConfigJsonFile()));
            this.appInfo = new AppInfo(
                    appConfig.optString("id"),
                    appConfig.optString("main", "main"),
                    Color.parseColor(appConfig.optString("backgroundColor", "#FFFFFF")),
                    Color.parseColor(appConfig.optString("statusBarColor", "#FFFFFF"))
            );
            JSONArray jsonPages = appConfig.getJSONArray("pages");
            for (int i = 0; i < jsonPages.length(); i++) {
                JSONObject page = jsonPages.getJSONObject(i);
                this.pagesMap.put(page.optString("id"), new PageInfo(
                        appConfig.optString("id"),
                        appConfig.optString("doc", "view.html"),
                        Color.parseColor(appConfig.optString("backgroundColor", "#FFFFFF")),
                        Color.parseColor(appConfig.optString("statusBarColor", "#FFFFFF"))
                ));
            }
        } catch (JSONException e) {
            LogUtil.e(e);
        }
    }


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

    private static class AppInfo {
        public String id;
        public String main;
        public @ColorInt
        int backgroundColor;
        public @ColorInt
        int statusBarColor;

        public AppInfo(String id, String main, int backgroundColor, int statusBarColor) {
            this.id = id;
            this.main = main;
            this.backgroundColor = backgroundColor;
            this.statusBarColor = statusBarColor;
        }
    }

    private static class PageInfo {
        public String id;
        public String doc;
        public @ColorInt
        int backgroundColor;
        public @ColorInt
        int statusBarColor;

        public PageInfo(String id, String doc, int backgroundColor, int statusBarColor) {
            this.id = id;
            this.doc = doc;
            this.backgroundColor = backgroundColor;
            this.statusBarColor = statusBarColor;
        }
    }
}
