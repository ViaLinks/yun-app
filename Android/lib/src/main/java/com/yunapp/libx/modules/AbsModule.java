package com.yunapp.libx.modules;

import android.os.Handler;
import android.os.Looper;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbsModule {
    protected static final int RESULT_OK = 0;
    protected static final int RESULT_FAIL = 1;
    protected static final int RESULT_CANCEL = 2;

    protected static final Handler HANDLER = new Handler(Looper.getMainLooper());

    protected AppContext mAppContext;

    public AbsModule(AppContext appContext) {
        mAppContext = appContext;
    }

    public abstract void invoke(String event, String params, EventCallback callback);

    public String packageResult(String event, int status, JSONObject data) {
        JSONObject resp = new JSONObject();
        try {
            String msg;
            switch (status) {
                case RESULT_OK:
                    msg = String.format("%s:ok", event);
                    break;
                case RESULT_FAIL:
                    msg = String.format("%s:fail", event);
                    break;
                case RESULT_CANCEL:
                    msg = String.format("%s:cancel", event);
                    break;
                default:
                    msg = String.format("%s:ok", event);
                    break;
            }
            resp.put("msg", msg);
            resp.put("code", status);
            if (data != null) {
                resp.put("data", data);
            }
        } catch (JSONException e) {
            LogUtil.e(e);
        }
        return resp.toString();
    }

    public static abstract class EventCallback {
        private String mEvent;
        private String mCallbackId;

        public EventCallback(String event, String callbackId) {
            mEvent = event;
            mCallbackId = callbackId;
        }

        public String getEvent() {
            return mEvent;
        }

        public String getCallbackId() {
            return mCallbackId;
        }

        public abstract void onResult(String result);
    }

}
