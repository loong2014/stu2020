package com.sunny.family.eventbus;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sunny.family.R;
import com.sunny.lib.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/9/27
 */
public class EventBusActivity extends BaseActivity {

    private TextView tvMsgView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_event_bus);

        tvMsgView = findViewById(R.id.tv_msg);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMsg();
            }
        });

        EventBus.getDefault().register(this);
    }

    private void onSendMsg() {

        EventBus.getDefault().post(MessageWrap.getInstance("msg convey by eventBus"));

//        EventBus.getDefault().postSticky(MessageWrap.getInstance("sticky msg convey by eventBus"));
    }

    private void onGetMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "empty";
        }
        tvMsgView.setText(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {
        onGetMsg(message.message);
    }

    /**
     * threadMode 线程模型，表示处理函数所在的线程。
     * POSTING：默认，与发布函数同一个线程
     * MAIN：主线程处理
     * BACKGROUND：后台线程处理
     * ASYNC：新建自线程处理
     * sticky：是否接收黏性事件
     * priority 优先级，默认=0，值越大优先级越高，越早接收到事件
     * 只有两个订阅方法使用相同的threadMode时，priority才有效
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1)
    public void onGetStickyMessage(MessageWrap message) {
        onGetMsg(message.message);
    }

    /**
     * 只有threadMode=POSITION时，才能停止事件继续分发
     */
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onGetCancelMessage(MessageWrap message) {
        onGetMsg(message.message);
        EventBus.getDefault().cancelEventDelivery(message);
    }
}
