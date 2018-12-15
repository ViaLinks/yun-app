package com.yunapp.libx.modules.file;

import android.text.TextUtils;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.modules.AbsModule;
import com.yunapp.libx.modules.NativeMethod;
import com.yunapp.libx.utils.LogUtil;

import org.json.JSONObject;

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
                readFileString(params, callback);
                break;
            }
            case WRITE_FILE: {
                writeFileString(params, callback);
                break;
            }
        }
    }


    public void readFileString(String params, EventCallback callback) {
        try {
            String path = new JSONObject(params).optString("path");
            if (TextUtils.isEmpty(path)) {
                callback.onResult(packageResult(callback.getEvent(), RESULT_FAIL, null));
                return;
            }
            File file = new File(mAppContext.config.getStorageDir(), path);
            if (file.exists() && file.isFile() && callback != null) {
                long len = file.length();
                InputStream inputStream = new FileInputStream(file);
                byte[] content = new byte[(int) len];
                inputStream.read(content);
                inputStream.close();
                JSONObject data = new JSONObject().put("content", new String(content, "utf-8"));
                callback.onResult(packageResult(callback.getEvent(), RESULT_OK, data));
            }
        } catch (Exception e) {
            LogUtil.e(e);
            callback.onResult(packageResult(callback.getEvent(), RESULT_FAIL, null));
        }
    }

    public void writeFileString(String params, EventCallback callback) {
        try {
            JSONObject jParams = new JSONObject(params);
            String path = jParams.optString("path");
            String data = jParams.optString("data");
            boolean append = jParams.optBoolean("append", false);
            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(data)) {
                callback.onResult(packageResult(callback.getEvent(), RESULT_FAIL, null));
                return;
            }
            File file = new File(mAppContext.config.getStorageDir(), path);
            if (file.exists() && file.isFile()) {
                OutputStream inputStream = new FileOutputStream(file, append);
                inputStream.write(data.getBytes("utf-8"));
                inputStream.close();
                if (callback != null) {
                    callback.onResult(packageResult(callback.getEvent(), RESULT_OK, null));
                }
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onResult(packageResult(callback.getEvent(), RESULT_FAIL, null));
            }
            LogUtil.e(e);
        }
    }

    //    public void readFileBytes(String subPath, EventCallback callback) {
//        File file = new File(mAppContext.config.getStorageDir(), subPath);
//        if (file.exists() && file.isFile() && callback != null) {
//            long len = file.length();
//            try {
//                InputStream inputStream = new FileInputStream(file);
//                byte[] rst = new byte[(int) len];
//                inputStream.read(rst);
//                inputStream.close();
//                callback.onResult(rst);
//            } catch (Exception e) {
//                LogUtil.e(e);
//            }
//        }
//    }

//    public void writeFileBytes(String subPath, byte[] data, boolean append, EventCallback<Boolean> callback) {
//        File file = new File(mAppContext.config.getStorageDir(), subPath);
//        if (file.exists() && file.isFile() && callback != null) {
//            try {
//                OutputStream inputStream = new FileOutputStream(file, append);
//                inputStream.write(data);
//                inputStream.close();
//                callback.onResult(true);
//            } catch (Exception e) {
//                callback.onResult(false);
//                LogUtil.e(e);
//            }
//        }
//    }
}
