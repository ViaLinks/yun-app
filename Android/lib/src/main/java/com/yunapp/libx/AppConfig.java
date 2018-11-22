package com.yunapp.libx;

public class AppConfig {
    public static final String NAME = "YunApp";
    public static final String VERSION = "1.0";
    public static final boolean DEBUG = true;

    public String appId = "";
    public String appPath = "";

    public AppConfig(String id, String path) {
        appId = id;
        appPath = path;
    }
}
