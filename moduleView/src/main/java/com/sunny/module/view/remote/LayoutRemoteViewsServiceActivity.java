package com.sunny.module.view.remote;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sunny.lib.common.router.RouterConstant;
import com.sunny.module.view.R;

import org.jetbrains.annotations.Nullable;

@Route(path = RouterConstant.View.PageRemoteViewsService)
public class LayoutRemoteViewsServiceActivity extends Activity {

    private static final String RemoteAction = "com.sunny.module.view.action.remoteviews";
    private static final String ExtraRemoteViews = "extra_remoteviews";

    private ViewGroup mRemoteContent;

    private final BroadcastReceiver mRemoteViewsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews = intent.getParcelableExtra(ExtraRemoteViews);
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }
    };

    private void updateUI(RemoteViews remoteViews) {

        // load view and init view with actions, such as setText、 add click listener(by PendingIntent)
        View view = remoteViews.apply(this, mRemoteContent);
        mRemoteContent.addView(view);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_act_remote_service);

        initView();
    }

    private void initView() {
        mRemoteContent = findViewById(R.id.layout_content);

        IntentFilter filter = new IntentFilter(RemoteAction);
        registerReceiver(mRemoteViewsReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mRemoteViewsReceiver);
        super.onDestroy();
    }
}
