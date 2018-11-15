package com.yunapp.lib.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.yunapp.lib.utils.StorageUtil;
import com.yunapp.lib.utils.ZipUtil;

import java.io.IOException;
import java.io.InputStream;

public class YunAppLoader {

    private static final String TAG = "YunLoader";

    private YunAppLoader() {
    }

    public static void load(Context context, String appId, String appPath, LoadCallback loadCallback) {
        new LoadTask(context.getApplicationContext(), loadCallback)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, appId, appPath);
    }

    public interface LoadCallback {
        void onResult(boolean result);
    }

    private static class LoadTask extends AsyncTask<String, Void, Boolean> {

        private Context mContext;
        private LoadCallback mCallback;

        public LoadTask(Context context, LoadCallback callback) {
            mContext = context;
            mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (params == null || params.length < 2) {
                return false;
            }
            String appId = params[0];
            String appPath = params[1];
            String outputPath = StorageUtil.getMiniAppSourceDir(mContext, appId).getAbsolutePath();
            boolean unzipResult = false;
            if (!TextUtils.isEmpty(appPath)) {
                unzipResult = ZipUtil.unzipFile(appPath, outputPath);
            }
            if (!unzipResult) {
                try {
                    InputStream in = mContext.getAssets().open(appId + ".zip");
                    unzipResult = ZipUtil.unzipFile(in, outputPath);
                } catch (IOException e) {
                }
            }
            return unzipResult;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mCallback.onResult(aBoolean);
        }
    }
}
