package com.yunapp.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.yunapp.libx.AppContext;
import com.yunapp.libx.AppManager;

public class MainActivity extends Activity {
    AppManager mAppManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppContext.Config config = new AppContext.Config((Application) getApplicationContext(), "d41d8cd98f00b204e9800998ecf8427e");
        FrameLayout yunRoot = this.findViewById(R.id.yunAppRoot);
        AppManager.load(this, yunRoot, config);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {

                break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
