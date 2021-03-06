package com.yunapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.yunapp.libx.AppConfig;
import com.yunapp.libx.YunApp;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppConfig appConfig = new AppConfig("1", "");

        FrameLayout yunRoot = this.findViewById(R.id.yunAppRoot);
        YunApp.load(this, yunRoot, appConfig);
    }
}
