package com.yunapp.libx;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.yunapp.libx.AppContext.Config;
import com.yunapp.libx.page.PageManager;
import com.yunapp.libx.utils.LogUtil;
import com.yunapp.libx.utils.ZipUtil;
import com.yunapp.libx.webcore.WebCoreManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * 每一个YunApp实例代表一个小应用
 */
public class AppManager implements AppListener {

    private static final String TAG = "AppManager";

    private AppContext mAppContext;

    private Context mContext;

    private FrameLayout mYunAppRoot;
    private WebCoreManager mWebCoreManager;
    private PageManager mPageManager;

    private AppManager(Context context, FrameLayout rootView, AppContext appContext) {
        this.mContext = context;
        this.mYunAppRoot = rootView;
        this.mAppContext = appContext;
    }

    /**
     * @param context  当前activity
     * @param rootView 小应用的容器view，每个容器只能容纳一个小应用
     * @param config   小应用的配置
     * @return
     */
    public static void load(final Context context, final FrameLayout rootView, final Config config) {
        checkMainThread();
        rootView.removeAllViews();
        new LoadTask(new AppManager.LoadCallback() {
            @Override
            public void onResult(boolean result) {
                if (result) {
                    AppManager yunApp = new AppManager(context, rootView, AppContext.buildAppContext(config));
                    yunApp.loadWebCore();
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context.getApplicationContext(), config);
    }

    private void loadWebCore() {
        LogUtil.d("加载WebCore");
        mWebCoreManager = new WebCoreManager(mContext, mAppContext, this);
        mYunAppRoot.addView(mWebCoreManager, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onAppReady() {
        LogUtil.d("WebCore加载成功");
        loadPageModule();
    }

    private void loadPageModule() {
        LogUtil.d("加载PageManager");
        mPageManager = new PageManager(mContext, mAppContext);
        mYunAppRoot.addView(mPageManager.getContainer(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        LogUtil.d("即将启动第一个页面");
        mPageManager.launchIndexPage(this);
    }




    private static void checkMainThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            LogUtil.e("当前为非UI线程");
            throw new IllegalThreadStateException("当前非UI线程");
        }
    }

    private static class LoadTask extends AsyncTask<Object, Void, Boolean> {

        private LoadCallback mCallback;

        LoadTask(LoadCallback callback) {
            mCallback = callback;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            if (params == null || params.length != 2) {
                return false;
            }
            Context context = (Context) params[0];
            Config appConfig = (Config) params[1];
            String zipFilePath = appConfig.getCodeZip().getAbsolutePath();
            String outputPath = appConfig.getAppSourceDir().getAbsolutePath();
            boolean unzipResult = false;
            if (!TextUtils.isEmpty(zipFilePath)) {
                unzipResult = ZipUtil.unzipFile(zipFilePath, outputPath);
            }
            if (!unzipResult) {
                try {
                    InputStream in = context.getAssets().open(appConfig.appId + ".zip");
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

    public interface LoadCallback {
        void onResult(boolean result);
    }
}
