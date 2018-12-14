package com.yunapp.libx;

import com.yunapp.libx.modules.AbsModule;

public interface AppListener {
    void invokeNative(String api, String params, AbsModule.EventCallback callback);
}
