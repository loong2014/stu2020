package com.sunny.module.stu.thread;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.sunny.lib.common.router.RouterConstant;
import com.sunny.module.stu.R;
import com.sunny.module.stu.base.StuBaseActivity;

@Route(path = RouterConstant.Stu.PageThread)
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
