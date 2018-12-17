package com.yunapp.libx.modules.net;

import android.net.Uri;
import android.text.TextUtils;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.modules.AbsModule;
import com.yunapp.libx.modules.NativeMethod;
import com.yunapp.libx.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@NativeMethod("get")
public class NetModule extends AbsModule {
    public static final String API_NET_GET = "get";
    public static final String API_NET_POST = "post";

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
        switch (event) {
            case API_NET_GET: {
                get(params, callback);
                break;
            }
            case API_NET_POST: {
                post(params, callback);
                break;
            }
        }
    }

    private void get(String params, final EventCallback eventCallback) {
        try {
            //params
            JSONObject jParams = new JSONObject(params);
            String url = jParams.optString("url");
            JSONObject jjQuery = jParams.optJSONObject("query");
            JSONObject jHeaders = jParams.optJSONObject("headers");
            //
            if (TextUtils.isEmpty(url)) {
                if (eventCallback != null) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_FAIL, null));
                        }
                    });
                }
                return;
            }
            Request.Builder requestBuilder = new Request.Builder();
            Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
            Iterator<String> queryKeys = jjQuery.keys();
            while (queryKeys.hasNext()) {
                String key = queryKeys.next();
                String value = jjQuery.optString(key);
                if (!TextUtils.isEmpty(value)) {
                    uriBuilder.appendQueryParameter(key, value);
                }
            }
            requestBuilder.url(uriBuilder.toString());
            //headers
            Iterator<String> headerKeys = jHeaders.keys();
            while (headerKeys.hasNext()) {
                String key = headerKeys.next();
                String value = jHeaders.optString(key);
                if (!TextUtils.isEmpty(value)) {
                    requestBuilder.addHeader(key, value);
                }
            }
            //request
            requestBuilder.get();
            CLIENT.newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (eventCallback != null) {
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_FAIL, null));
                            }
                        });
                    }
                    LogUtil.e(e);
                }

                @Override
                public void onResponse(Call call, final Response response) {
                    if (eventCallback != null) {
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    eventCallback.onResult(packageResult(eventCallback.getEvent(),
                                            RESULT_OK,
                                            new JSONObject()
                                                    .put("statusCode", response.code())
                                                    .put("data", response.body().string())));
                                } catch (Exception e) {
                                    eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_FAIL, null));
                                    LogUtil.e(e);
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            if (eventCallback != null) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_FAIL, null));
                    }
                });
            }
            e.printStackTrace();
            LogUtil.e(e);
        }
    }

    private void post(String params, final EventCallback eventCallback) {
        try {
            JSONObject jParams = new JSONObject(params);
            String url = jParams.optString("url");
            String filePath = jParams.optString("filePath");
            String name = jParams.optString("name");
            JSONObject jHeaders = jParams.optJSONObject("header");
            JSONObject jFormData = jParams.optJSONObject("formData");

            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(filePath) || TextUtils.isEmpty(name)) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        if (eventCallback != null)
                            eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_FAIL, null));
                    }
                });
                return;
            }

            Map<String, String> reqParam = parseJsonToMap(jFormData);
            Headers headers = Headers.of(parseJsonToMap(jHeaders));
            File file = new File(filePath);
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
            bodyBuilder.setType(MultipartBody.FORM);
            bodyBuilder.addFormDataPart(name, file.getName(),
                    RequestBody.create(MediaType.parse("image/jpeg"), file));
            for (Map.Entry<String, String> entry : reqParam.entrySet()) {
                bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }

            Request request = new Request.Builder()
                    .headers(headers)
                    .url(url)
                    .post(bodyBuilder.build())
                    .build();

            CLIENT.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject data = new JSONObject();
                    try {
                        data.put("statusCode", response.code());
                        data.put("data", response.body().string());
                    } catch (JSONException e) {
                        LogUtil.e(e);
                    }
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            if (eventCallback != null)
                                eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_OK, data));
                        }
                    });
                }

                @Override
                public void onFailure(Call call, final IOException e) {
                    final JSONObject data = new JSONObject();
                    try {
                        data.put("exception", e != null ? e.getMessage() : "upload onFailure");
                    } catch (Exception ex) {
                        LogUtil.e(e);
                    }
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            if (eventCallback != null)
                                eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_FAIL, data));
                        }
                    });
                }
            });

        } catch (Exception e) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    if (eventCallback != null)
                        eventCallback.onResult(packageResult(eventCallback.getEvent(), RESULT_FAIL, null));
                }
            });
        }
    }

    /**
     * 将JSONObject对象转为Map对象
     *
     * @param json json字符串
     * @return 解析转换后的Map对象
     */
    private static Map<String, String> parseJsonToMap(JSONObject json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) {
            return map;
        }

        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = json.optString(key);
            if (!TextUtils.isEmpty(value)) {
                map.put(key, value);
            }
        }

        return map;
    }
}
