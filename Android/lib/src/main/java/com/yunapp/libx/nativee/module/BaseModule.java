package com.yunapp.libx.nativee.module;

import com.yunapp.libx.AppContext.Config;
import com.yunapp.libx.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseModule {
    protected static final int RESULT_OK = 0;
    protected static final int RESULT_FAIL = 1;
    protected static final int RESULT_CANCEL = 2;

    protected Config mConfig;

    public BaseModule(Config config) {
        this.mConfig = config;
    }

    public static abstract class EventCallback<T> {
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

        protected String mixResult(int status, JSONObject data) {
            if (data == null) {
                data = new JSONObject();
            }
            String errMsg;
            switch (status) {
                case RESULT_OK:
                    errMsg = String.format("%s:ok", getEvent());
                    break;
                case RESULT_FAIL:
                    errMsg = String.format("%s:fail", getEvent());
                    break;
                case RESULT_CANCEL:
                    errMsg = String.format("%s:cancel", getEvent());
                    break;
                default:
                    errMsg = String.format("%s:ok", getEvent());
                    break;
            }

            try {
                data.put("errMsg", errMsg);
            } catch (JSONException e) {
                LogUtil.e(e);
            }
            return data.toString();
        }

        public abstract void onResult(T result);
    }

}
