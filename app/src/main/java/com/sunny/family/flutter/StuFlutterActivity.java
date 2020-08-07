package com.sunny.family.flutter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.sunny.family.R;
import com.sunny.lib.base.BaseActivity;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterView;

/**
 * Created by zhangxin17 on 2020/8/5
 */
public class StuFlutterActivity extends BaseActivity {

    private FlutterView flutterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_flutter);

        flutterView = Flutter.createView(
                this,
                getLifecycle(),
                "flutter_view" // 这个很关键，android和flutter就是通过这个进行绑定
        );

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(flutterView, layoutParams);
    }

}
