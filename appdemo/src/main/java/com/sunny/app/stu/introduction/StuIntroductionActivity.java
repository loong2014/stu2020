package com.sunny.app.stu.introduction;

import com.sunny.app.stu.base.StuBaseActivity;

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
