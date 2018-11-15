package com.yunapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.yunapp.lib.loader.YunAppLoader;
import com.yunapp.lib.web.AppContainer;

public class MainActivity extends Activity {

    private FrameLayout mYunAppRoot = null;
    private AppContainer mAppContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYunAppRoot = this.findViewById(R.id.yunAppRoot);
        YunAppLoader.load(this, "1", "", new YunAppLoader.LoadCallback() {
            @Override
            public void onResult(boolean result) {
                if (result) {
                    loadPage(mYunAppRoot);
                }
            }
        });
    }

    private void loadPage(FrameLayout root) {
        mAppContainer = new AppContainer(this, "1");
        root.addView(mAppContainer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }
}
