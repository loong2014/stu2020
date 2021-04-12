package com.sunny.module.stu.introduction;


import com.sunny.module.stu.base.StuBaseActivity;

public class StuIntroductionActivity extends StuBaseActivity {


    @Override
    protected int buildLayoutResID() {
        return 0;
    }

    @Override
    protected void initView() {

        initTopBarView("多线程");
    }
}
