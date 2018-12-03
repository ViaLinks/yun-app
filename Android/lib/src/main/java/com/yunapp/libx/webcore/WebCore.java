package com.yunapp.libx.webcore;

import android.content.Context;
import android.widget.LinearLayout;

import com.yunapp.libx.AppConfig;
import com.yunapp.libx.AppListener;
import com.yunapp.libx.utils.FileUtil;

import java.io.File;

public class WebCore extends LinearLayout implements CoreWebView.JsHandler {

    private AppConfig mAppConfig;
    private CoreWebView mCoreWebView;
    private AppListener mAppListener;

    public WebCore(Context context, AppConfig appConfig, AppListener listener) {
        super(context);
        mAppConfig = appConfig;
        mCoreWebView = new CoreWebView(context);
        mCoreWebView.setJsHandler("NativeApi", this);
        mAppListener = listener;
        addView(mCoreWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        File serviceFile = new File(mAppConfig.getAppSourceDir(), "index.html");
        String servicePath = FileUtil.toUriString(serviceFile);
        mCoreWebView.loadUrl(servicePath);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
        mCoreWebView.destroy();
    }

    @Override
    public void invokeNative(String api, String params, String callbackId) {
        if ("onServiceReady".equals(api)) {
            if (mAppListener != null) {
                mAppListener.onAppReady();
            }
        }
    }

    @Override
    public void invokeView(String api, String params, String callbackId, String viewIds) {

    }
}
