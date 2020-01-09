package com.sunny.family.camera;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sunny.family.R;
import com.sunny.lib.base.BaseActivity;

public class CameraActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_camera);
    }
}
