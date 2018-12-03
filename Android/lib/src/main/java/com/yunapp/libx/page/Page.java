package com.yunapp.libx.page;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yunapp.lib.R;
import com.yunapp.libx.AppConfig;
import com.yunapp.libx.AppListener;
import com.yunapp.libx.utils.FileUtil;

public class Page extends LinearLayout {

    private AppConfig mAppConfig;
    private AppListener mAppListener;
    private FrameLayout mWebContainer;
    private PageWebView mPageWebView;

    private Page(Context context, AppConfig appConfig) {
        super(context);
        this.mAppConfig = appConfig;
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.webpage, this);
        LinearLayout topLayout = findViewById(R.id.mTopContainer);
        LinearLayout bottomLayout = findViewById(R.id.mBottomContainer);
        mWebContainer = findViewById(R.id.mWebContainer);
        mPageWebView = new PageWebView(context);
        mWebContainer.addView(mPageWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public Page loadPath(String path) {
        mPageWebView.loadUrl(FileUtil.toUriString(path));
        return this;
    }

    public void setAppListener(AppListener listener) {
        mAppListener = listener;
    }

    public static Page newInstance(Context context, AppConfig appConfig) {
        return new Page(context, appConfig);
    }
}
