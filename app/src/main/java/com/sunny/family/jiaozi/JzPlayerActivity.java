package com.sunny.family.jiaozi;

import android.os.Bundle;
import android.view.View;

import com.sunny.family.R;
import com.sunny.lib.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by zhangxin17 on 2020/4/17
 */
public class JzPlayerActivity extends BaseActivity {

    private JzvdStd myJzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_player_jz);
        initView();
    }

    private void initView() {
        myJzvdStd = findViewById(R.id.jz_videoplayer);


        findViewById(R.id.btn_play_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JZDataSource dataSource = new JZDataSource("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                        , "饺子快长大");

                myJzvdStd.setUp(dataSource, Jzvd.SCREEN_NORMAL);

//                Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(myJzvdStd.thumbImageView);

            }
        });

        findViewById(R.id.btn_play_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myJzvdStd.startButton.performClick();
                myJzvdStd.startVideo();

            }
        });

        findViewById(R.id.btn_play_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jzvd.goOnPlayOnPause();
            }
        });

        findViewById(R.id.btn_play_full).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mJzvdStd.startWindowTiny();

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JzvdStd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
