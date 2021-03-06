package com.yunapp.libx.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yunapp.libx.AppConfig;

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
        webSetting.setUserAgentString(String.format("%s %s(version/%s)", ua, AppConfig.NAME, AppConfig.VERSION));
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);

        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
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

    public void setJsHandler(JsHandler handler) {
        addJavascriptInterface(new JSInterface(handler), "YunAppJSCore");
    }

    public static interface JsHandler {
        /**
         * @param event  事件名称
         * @param params 参数
         * @param handle 回调结果处理句柄
         */
        void onJsEvent(String event, String params, String handle);
    }

    private static class JSInterface {
        private JsHandler mJsHandler;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        public JSInterface(JsHandler handler) {
            mJsHandler = handler;
        }

        @JavascriptInterface
        public void invoke(final String event, final String params, final String handle) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mJsHandler != null) {
                        mJsHandler.onJsEvent(event, params, handle);
                    }
                }
            });
        }
    }
}
