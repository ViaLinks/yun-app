package com.yunapp.libx.modules.core;

import android.app.Activity;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.AppListener;
import com.yunapp.libx.modules.AbsModule;

public class CoreModule extends AbsModule implements CoreWebView.JsHandler {
    private CoreWebView mCoreWebView;
    private AppListener mAppListener;

    public CoreModule(Activity activity, AppContext appContext, AppListener appListener) {
        super(appContext);
        mCoreWebView = new CoreWebView(activity, appContext);
        mCoreWebView.setJsHandler("NativeApi", this);
        mAppListener = appListener;
    }

    public CoreWebView getCoreWebView() {
        return mCoreWebView;
    }

    @Override
    public void invoke(String event, String params, EventCallback callback) {
        switch (event) {
            case "": {
                break;
            }
        }
    }

    @Override
    public void invokeNative(String api, String params, String callbackId) {
        if (mAppListener != null) {
            mAppListener.invokeNative(api, params, null);
        }
    }

    @Override
    public void invokeView(String api, String params, String callbackId, String viewIds) {

    }
}
