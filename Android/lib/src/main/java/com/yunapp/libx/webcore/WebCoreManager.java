package com.yunapp.libx.webcore;

import android.content.Context;
import android.widget.LinearLayout;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.AppListener;
import com.yunapp.libx.utils.FileUtil;

public class WebCoreManager extends LinearLayout implements CoreWebView.JsHandler {

    private AppContext mAppContext;
    private CoreWebView mCoreWebView;
    private AppListener mAppListener;

    public WebCoreManager(Context context, AppContext appContext, AppListener listener) {
        super(context);
        mAppContext = appContext;
        mCoreWebView = new CoreWebView(context);
        mCoreWebView.setJsHandler("NativeApi", this);
        mAppListener = listener;
        addView(mCoreWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCoreWebView.loadUrl(FileUtil.toUriString(mAppContext.getWebCoreJsFile()));
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
