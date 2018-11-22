package com.yunapp.libx.page;

import android.content.Context;
import android.widget.LinearLayout;

import com.yunapp.libx.AppConfig;
import com.yunapp.libx.AppListener;
import com.yunapp.libx.web.BaseWebView;

public class Page extends LinearLayout implements BaseWebView.JsHandler {

    private AppConfig mAppConfig;
    private String mPagePath;
    private AppListener mAppListener;

    public Page(Context context, String pagePath, AppConfig appConfig) {
        super(context);
        this.mAppConfig = appConfig;
        init(context, pagePath);
    }

    private void init(Context context, String url) {

    }

    @Override
    public void onJsEvent(String event, String params, String handle) {

    }

    public void setAppListener(AppListener listener) {
        mAppListener = listener;
    }
}
