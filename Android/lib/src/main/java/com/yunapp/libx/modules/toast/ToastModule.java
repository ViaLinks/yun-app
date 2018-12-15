package com.yunapp.libx.modules.toast;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.modules.AbsModule;
import com.yunapp.libx.modules.NativeMethod;

import org.json.JSONObject;

@NativeMethod(ToastModule.API_TOAST)
public class ToastModule extends AbsModule {
    public static final String API_TOAST = "toast";

    private Context mContext;

    public ToastModule(AppContext appContext, Context context) {
        super(appContext);
        this.mContext = context;
    }

    @Override
    public void invoke(String event, String params, EventCallback callback) {
        switch (event) {
            case API_TOAST: {
                toast(params, callback);
                break;
            }
        }
    }

    private void toast(String params, final EventCallback callback) {
        try {
            final String text = new JSONObject(params).optString("text");
            final int duration = new JSONObject(params).optInt("duration") == 1 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        callback.onResult(packageResult(callback.getEvent(), RESULT_FAIL, null));
                        return;
                    }
                    Toast.makeText(mContext, text, duration).show();
                    if (callback != null) {
                        callback.onResult(packageResult(callback.getEvent(), RESULT_OK, null));
                    }
                }
            });
        } catch (Exception e) {
            callback.onResult(packageResult(callback.getEvent(), RESULT_FAIL, null));
        }
    }
}
