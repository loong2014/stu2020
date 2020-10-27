package com.sunny.family.motionlayout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.sunny.family.R;
import com.sunny.lib.base.BaseActivity;
import com.sunny.lib.utils.SunLog;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/10/22
 * MotionLayout
 * 系列博客 https://blog.csdn.net/xgxmwang/article/details/100696851
 */
public class MotionLayoutActivity extends BaseActivity {

    private static final String TAG = "Sun-MotionLayout";

    //
    private View frontDataLayout;

    //
    private MotionLayout motionLayout;
    private View motionToView;
    private ImageView motionFromView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_motion_layout);
        //
        frontDataLayout = findViewById(R.id.front_data_layout);

        //开发辅助
        motionLayout = findViewById(R.id.motionLayout);
        motionFromView = findViewById(R.id.motion_from_view);
        motionToView = findViewById(R.id.motion_to_view);

        //
        findViewById(R.id.btn_to_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartToEnd();
            }
        });
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motionLayout.transitionToStart();
            }
        });
        findViewById(R.id.btn_set_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float cur = motionLayout.getProgress();
                float next = cur + 0.1F;

                if (next > 1) {
                    next = 0f;
                }
                motionLayout.setProgress(next);
            }
        });

        motionLayout.setTransitionListener(transitionListener);
    }

    private void doStartToEnd() {
        //
        //使控件可以进行缓存
        frontDataLayout.setDrawingCacheEnabled(true);
        //获取缓存的 Bitmap
        Bitmap drawingCache = frontDataLayout.getDrawingCache();
        //复制获取的 Bitmap
        drawingCache = Bitmap.createBitmap(drawingCache);
        //关闭视图的缓存
        frontDataLayout.setDrawingCacheEnabled(false);

        //
        if (drawingCache == null) {
            SunLog.i(TAG, "doStartToEnd error drawingCache is null");
            return;
        }

        //
        motionFromView.setImageBitmap(drawingCache);
        frontDataLayout.setVisibility(View.GONE);

        motionLayout.transitionToEnd();
    }

    private MotionLayout.TransitionListener transitionListener = new MotionLayout.TransitionListener() {
        @Override
        public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
            SunLog.i(TAG, "onTransitionStarted " + i + " , " + i1);
        }

        @Override
        public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
            SunLog.i(TAG, "onTransitionChange " + i + " , " + i1 + " , " + v);
        }

        @Override
        public void onTransitionCompleted(MotionLayout motionLayout, int i) {
            SunLog.i(TAG, "onTransitionCompleted " + i);
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
            SunLog.i(TAG, "onTransitionTrigger " + i + " , " + b + " , " + v);
        }
    };


}
