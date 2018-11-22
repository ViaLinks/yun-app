package com.yunapp.libx.webcore;

import android.content.Context;
import android.widget.LinearLayout;

import com.yunapp.libx.AppConfig;
import com.yunapp.libx.AppListener;
import com.yunapp.libx.utils.FileUtil;
import com.yunapp.libx.utils.StorageUtil;
import com.yunapp.libx.web.BaseWebView;

import java.io.File;

public class WebCore extends LinearLayout implements BaseWebView.JsHandler {

    private AppConfig mAppConfig;
    private CoreWebView mCoreWebView;
    private AppListener mAppListener;

    public WebCore(Context context, AppConfig appConfig, AppListener listener) {
        super(context);
        mAppConfig = appConfig;
        mCoreWebView = new CoreWebView(context);
        mCoreWebView.setJsHandler(this);
        mAppListener = listener;
        addView(mCoreWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        File serviceFile = new File(StorageUtil.getMiniAppSourceDir(getContext(), mAppConfig.appId), "index.html");
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
    public void onJsEvent(String event, String params, String handle) {
        if ("custom_event_serviceReady".equals(event)) {
            if (mAppListener != null) {
                mAppListener.onAppReady();
            }
        }
    }
}
