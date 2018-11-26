package com.yunapp.libx.nativee.module;

import com.yunapp.libx.AppConfig;
import com.yunapp.libx.nativee.annotation.NativeMethod;
import com.yunapp.libx.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileModule extends BaseModule {

    public FileModule(AppConfig appConfig) {
        super(appConfig);
    }

    @NativeMethod(name = "readFileString")
    public void readFileString(String subPath, EventCallback<String> callback) {
        File file = new File(mAppConfig.getStorageDir(), subPath);
        if (file.exists() && file.isFile() && callback != null) {
            long len = file.length();
            try {
                InputStream inputStream = new FileInputStream(file);
                byte[] content = new byte[(int) len];
                inputStream.read(content);
                inputStream.close();
                String rst = new String(content, "utf-8");
                callback.onResult(rst);
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }

    @NativeMethod(name = "readFileBytes")
    public void readFileBytes(String subPath, EventCallback<byte[]> callback) {
        File file = new File(mAppConfig.getStorageDir(), subPath);
        if (file.exists() && file.isFile() && callback != null) {
            long len = file.length();
            try {
                InputStream inputStream = new FileInputStream(file);
                byte[] rst = new byte[(int) len];
                inputStream.read(rst);
                inputStream.close();
                callback.onResult(rst);
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }

    @NativeMethod(name = "writeFileString")
    public void writeFileString(String subPath, String data, boolean append, EventCallback<Boolean> callback) {
        File file = new File(mAppConfig.getStorageDir(), subPath);
        if (file.exists() && file.isFile() && callback != null) {
            try {
                OutputStream inputStream = new FileOutputStream(file, append);
                inputStream.write(data.getBytes("utf-8"));
                inputStream.close();
                callback.onResult(true);
            } catch (Exception e) {
                callback.onResult(false);
                LogUtil.e(e);
            }
        }
    }

    @NativeMethod(name = "writeFileBytes")
    public void writeFileBytes(String subPath, byte[] data, boolean append, EventCallback<Boolean> callback) {
        File file = new File(mAppConfig.getStorageDir(), subPath);
        if (file.exists() && file.isFile() && callback != null) {
            try {
                OutputStream inputStream = new FileOutputStream(file, append);
                inputStream.write(data);
                inputStream.close();
                callback.onResult(true);
            } catch (Exception e) {
                callback.onResult(false);
                LogUtil.e(e);
            }
        }
    }
}
