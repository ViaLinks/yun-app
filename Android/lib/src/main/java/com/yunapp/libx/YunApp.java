package com.yunapp.libx;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.yunapp.libx.page.PageManager;
import com.yunapp.libx.utils.LogUtil;
import com.yunapp.libx.utils.StorageUtil;
import com.yunapp.libx.utils.ZipUtil;
import com.yunapp.libx.webcore.WebCore;

import java.io.IOException;
import java.io.InputStream;

/**
 * 每一个YunApp实例代表一个小应用
 */
public class YunApp implements AppListener {

    private static final String TAG = "YunApp";

    private AppConfig mAppConfig;

    private Context mContext;

    private FrameLayout mYunAppRoot = null;
    private WebCore mWebCore = null;
    private PageManager mPageManager = null;

    private YunApp(Context context, FrameLayout rootView, AppConfig appConfig) {
        this.mContext = context;
        this.mYunAppRoot = rootView;
        this.mAppConfig = appConfig;
    }

    /**
     * @param context   当前activity
     * @param rootView  小应用的容器view，每个容器只能容纳一个小应用
     * @param appConfig 小应用的配置
     * @return
     */
    public static YunApp load(Context context, final FrameLayout rootView, AppConfig appConfig) {
        checkMainThread();
        rootView.removeAllViews();
        final YunApp yunApp = new YunApp(context, rootView, appConfig);
        new LoadTask(context.getApplicationContext(), new YunApp.LoadCallback() {
            @Override
            public void onResult(boolean result) {
                if (result) {
                    yunApp.loadWebCore();
                    yunApp.loadPageModule();
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, appConfig.appId, appConfig.getAppPath());
        return yunApp;
    }

    private void loadWebCore() {
        LogUtil.d("加载WebCore");
        mWebCore = new WebCore(mContext, mAppConfig, this);
        mYunAppRoot.addView(mWebCore, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void loadPageModule() {
        LogUtil.d("加载PageManager");
        mPageManager = new PageManager(mContext, mAppConfig);
        mYunAppRoot.addView(mPageManager.getContainer(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onAppReady() {
        LogUtil.d("WebCore加载成功，即将启动第一个页面");
        mPageManager.launchEntryPage(this);
    }

    private static void checkMainThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            LogUtil.e("当前为非UI线程");
            throw new IllegalThreadStateException("当前非UI线程");
        }
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
                    LogUtil.e(e);
                }
            }
            LogUtil.d("小应用zip包解压结果:" + unzipResult);
            return unzipResult;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mCallback.onResult(aBoolean);
        }
    }
}
