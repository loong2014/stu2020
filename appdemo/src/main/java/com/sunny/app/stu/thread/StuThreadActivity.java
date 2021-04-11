package com.sunny.app.stu.thread;

import com.sunny.app.R;
import com.sunny.app.stu.base.StuBaseActivity;

public class StuThreadActivity extends StuBaseActivity {

    @Override
    protected int buildLayoutResID() {
        return R.layout.act_stu_thread_main;
    }

    @Override
    protected void initView() {
        initTopBarView("多线程");
    }


}
