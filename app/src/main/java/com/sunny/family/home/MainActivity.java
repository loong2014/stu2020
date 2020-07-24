package com.sunny.family.home;

import android.os.Bundle;

import com.sunny.family.R;
import com.sunny.family.livedata.LiveDataFragment;
import com.sunny.lib.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/7/13
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.act_main, new LiveDataFragment())
                .commit();

    }
}
