package com.yunapp.libx.base;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunapp.lib.BuildConfig;
import com.yunapp.libx.AppContext.Config;

import java.lang.reflect.Method;

public class BaseWebView extends WebView {
    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        removeJavaInterface();

        WebSettings webSetting = getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        String ua = webSetting.getUserAgentString();
        webSetting.setUserAgentString(String.format("%s %s(version/%s)", ua, Config.NAME, Config.VERSION));
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);

        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());

        setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
    }

    private void removeJavaInterface() {
        try {
            Method removeJavascriptInterface = this.getClass().getMethod("removeJavascriptInterface", String.class);
            if (removeJavascriptInterface != null) {
                removeJavascriptInterface.invoke(this, "searchBoxJavaBridge_");
                removeJavascriptInterface.invoke(this, "accessibility");
                removeJavascriptInterface.invoke(this, "accessibilityTraversal");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
