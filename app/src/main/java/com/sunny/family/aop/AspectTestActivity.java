package com.sunny.family.aop;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.sunny.family.R;
import com.sunny.lib.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/8/25
 */
public class AspectTestActivity extends BaseActivity {
    private static final String TAG = "AOP-AspectTestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_aop);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_aop_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBtn1(v);
            }
        });
        findViewById(R.id.btn_aop_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBtn2(v);
            }
        });
        findViewById(R.id.btn_aop_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBtn3(v);
            }
        });
    }

    public void testAop(String name, View view) {
        long startTime = System.currentTimeMillis();
        //每个方法内部具体执行内容
        SystemClock.sleep(300);
        Log.i(TAG, "testAop :" + name);

        long endTime = System.currentTimeMillis();
        Log.i(TAG, name + " 总共耗时: " + (endTime - startTime));
    }

    private void testBtn1(View v) {
        testAop("测试1", v);
    }

    private void testBtn2(View v) {
        testAop("测试2", v);
    }

    private void testBtn3(View v) {
        testAop("测试3", v);
    }
}
