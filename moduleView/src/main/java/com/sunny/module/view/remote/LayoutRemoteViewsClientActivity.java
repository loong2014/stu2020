package com.sunny.module.view.remote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sunny.lib.common.base.BaseActivity;
import com.sunny.lib.common.router.RouterConstant;
import com.sunny.module.view.R;
import com.sunny.module.view.layout.TitleUpdateLayoutActivity;

import org.jetbrains.annotations.Nullable;

@RequiresApi(api = Build.VERSION_CODES.O)
@Route(path = RouterConstant.View.PageRemoteViewsClient)
public class LayoutRemoteViewsClientActivity extends BaseActivity {

    private static final String RemoteAction = "com.sunny.module.view.action.remoteviews";
    private static final String ExtraRemoteViews = "extra_remoteviews";

    private int viewCount = 0;

    private static final int SysNotifyId = 1;
    private static final int SelfNotifyId = 2;

    /**
     * 自定义通知样式
     */
    private void doAddOneNotifySelf() {

        //
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TitleUpdateLayoutActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "stu_notify");
        builder.setSmallIcon(R.drawable.ic_gift)
                .setContentTitle("重大通知 :" + Process.myPid())
                .setContentText("万里晴空")
                .setWhen(System.currentTimeMillis())
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.view_view_layout_remote);
        remoteViews.setTextViewText(R.id.tv_name, "msg from client :" + Process.myPid() + " , count :" + (viewCount++));
        remoteViews.setImageViewResource(R.id.iv_icon, R.drawable.ic_gift);
        remoteViews.setOnClickPendingIntent(R.id.btn_click, pendingIntent);

        builder.setCustomContentView(remoteViews);

        //
        NotificationChannel channel = new NotificationChannel("stu_notify", "sunny_name", NotificationManager.IMPORTANCE_HIGH);

        //
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        manager.notify(SelfNotifyId, builder.build());
    }


    /**
     * 系统通知样式
     */
    private void doAddOneNotifySys() {

        //
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TitleUpdateLayoutActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, "stu_notify")
                .setSmallIcon(R.drawable.ic_gift)
                .setContentTitle("重大通知 :" + Process.myPid())
                .setContentText("万里晴空")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        //
        NotificationChannel channel = new NotificationChannel("stu_notify", "sunny_name", NotificationManager.IMPORTANCE_HIGH);

        //
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        manager.notify(SysNotifyId, notification);
    }

    private void doAddOneRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.view_view_layout_remote);
        remoteViews.setTextViewText(R.id.tv_name, "msg from client :" + Process.myPid() + " , count :" + (viewCount++));

        //
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TitleUpdateLayoutActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_click, pendingIntent);

        //
        Intent intent = new Intent(RemoteAction);
        intent.putExtra(ExtraRemoteViews, remoteViews);
        sendBroadcast(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_act_remote_client);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_add_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddOneRemoteViews();
            }
        });
        findViewById(R.id.btn_add_notify_sys).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddOneNotifySys();
            }
        });
        findViewById(R.id.btn_add_notify_self).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddOneNotifySelf();
            }
        });
    }
}
