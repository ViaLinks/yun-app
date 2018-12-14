package com.yunapp.libx.webcore;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import com.yunapp.libx.base.BaseWebView;

public class CoreWebView extends BaseWebView {
    public CoreWebView(Context context) {
        super(context);
    }

    public CoreWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoreWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setJsHandler(String name, JsHandler handler) {
        addJavascriptInterface(new JSInterface(handler), name);
    }

    public static interface JsHandler {
        /**
         * @param api        事件名称
         * @param params     参数
         * @param callbackId 回调Id
         */
        void invokeNative(String api, String params, String callbackId);

        /**
         * @param api        事件名称
         * @param params     参数
         * @param callbackId 回调Id
         * @param viewIds
         */
        void invokeView(String api, String params, String callbackId, String viewIds);
    }

    private static class JSInterface {
        private JsHandler mJsHandler;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        public JSInterface(JsHandler handler) {
            mJsHandler = handler;
        }

        @JavascriptInterface
        public void invokeView(final String api, final String params, final String callbackId, final String viewIds) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mJsHandler != null) {
                        mJsHandler.invokeView(api, params, callbackId, viewIds);
                    }
                }
            });
        }

        @JavascriptInterface
        public void invokeNative(final String api, final String params, final String callbackId) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mJsHandler != null) {
                        mJsHandler.invokeNative(api, params, callbackId);
                    }
                }
            });
        }
    }
}
