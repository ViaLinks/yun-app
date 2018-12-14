package com.yunapp.libx.modules.file;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.modules.AbsModule;
import com.yunapp.libx.modules.NativeMethod;
import com.yunapp.libx.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@NativeMethod({FileModule.READ_FILE, FileModule.WRITE_FILE})
public class FileModule extends AbsModule {
    public static final String READ_FILE = "readFileString";
    public static final String WRITE_FILE = "writeFileString";

    public FileModule(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void invoke(String event, String params, EventCallback callback) {
        switch (event) {
            case READ_FILE: {
                break;
            }
            case WRITE_FILE: {
                break;
            }
        }
    }


    public void readFileString(String subPath, EventCallback<String> callback) {
        File file = new File(mAppContext.config.getStorageDir(), subPath);
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

    public void readFileBytes(String subPath, EventCallback<byte[]> callback) {
        File file = new File(mAppContext.config.getStorageDir(), subPath);
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

    public void writeFileString(String subPath, String data, boolean append, EventCallback<Boolean> callback) {
        File file = new File(mAppContext.config.getStorageDir(), subPath);
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

    public void writeFileBytes(String subPath, byte[] data, boolean append, EventCallback<Boolean> callback) {
        File file = new File(mAppContext.config.getStorageDir(), subPath);
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
