package com.sunny.module.stu.introduction;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.sunny.lib.common.router.RouterConstant;
import com.sunny.module.stu.R;
import com.sunny.module.stu.base.StuBaseActivity;

@Route(path = RouterConstant.Stu.PageIntroduction)
public class StuIntroductionActivity extends StuBaseActivity {

    @Override
    protected int buildLayoutResID() {
        return R.layout.act_stu_introduction;
    }

    @Override
    protected void initView() {
        initTopBarView(mJumpModel.getJumpType());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, StuFragmentSelector.getFragment(mJumpModel.getJumpType()))
                .commit();
    }
}
