package com.yunapp.libx.modules.net;

import android.net.Uri;
import android.text.TextUtils;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.modules.AbsModule;
import com.yunapp.libx.modules.annotation.NativeMethod;
import com.yunapp.libx.utils.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@NativeMethod("get")
public class NetModule extends AbsModule {

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build();

    public NetModule(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void invoke(String event, String params, EventCallback callback) {

    }

    public void get(String url, String query, String headers, final EventCallback<String> eventCallback) {
        Request.Builder requestBuilder = new Request.Builder();
        if (TextUtils.isEmpty(url)) return;
        try {
            //params
            Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
            JSONObject jsonQuery = new JSONObject(query);
            Iterator<String> queryKeys = jsonQuery.keys();
            while (queryKeys.hasNext()) {
                String key = queryKeys.next();
                String value = jsonQuery.optString(key);
                if (!TextUtils.isEmpty(value)) {
                    uriBuilder.appendQueryParameter(key, value);
                }
            }
            requestBuilder.url(uriBuilder.toString());
            //headers
            JSONObject jsonHeaders = new JSONObject(headers);
            Iterator<String> headerKeys = jsonHeaders.keys();
            while (headerKeys.hasNext()) {
                String key = headerKeys.next();
                String value = jsonHeaders.optString(key);
                if (!TextUtils.isEmpty(value)) {
                    requestBuilder.addHeader(key, value);
                }
            }
            //request
            requestBuilder.get();
            CLIENT.newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.e(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    eventCallback.onResult(response.body().string());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e);
        }
    }
}
