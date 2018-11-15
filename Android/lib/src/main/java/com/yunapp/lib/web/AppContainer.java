package com.yunapp.lib.web;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunapp.lib.utils.FileUtil;
import com.yunapp.lib.utils.StorageUtil;

import java.io.File;

public class AppContainer extends LinearLayout {

    private String appId;
    private YunWebView mYunWebView;

    public AppContainer(Context context, String appId) {
        super(context);
        this.appId = appId;
        mYunWebView = new YunWebView(context);
        mYunWebView.addJavascriptInterface(new JsHandler(), "NativeAPI");
        addView(mYunWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        File serviceFile = new File(StorageUtil.getMiniAppSourceDir(getContext(), appId), "index.html");
        String servicePath = FileUtil.toUriString(serviceFile);
        mYunWebView.loadUrl(servicePath);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
        mYunWebView.destroy();
    }

    class JsHandler {
        @JavascriptInterface
        public void showToast(final String text) {
            post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
