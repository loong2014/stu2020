package com.sunny.lib.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sunny.lib.manager.ActivityLifeManager;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLifeManager.INSTANCE.onCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityLifeManager.INSTANCE.onDestroy(this);

    }
}
