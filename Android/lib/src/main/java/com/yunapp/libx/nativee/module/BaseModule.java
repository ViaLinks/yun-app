package com.yunapp.libx.nativee.module;

import com.yunapp.libx.AppConfig;

public class BaseModule {
    protected AppConfig mAppConfig;

    public BaseModule(AppConfig appConfig) {
        this.mAppConfig = appConfig;
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

        public abstract void onResult(T result);
    }
}
