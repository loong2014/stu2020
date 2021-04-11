package com.sunny.app.stu.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sunny.app.R;
import com.sunny.app.stu.StuConstant;
import com.sunny.app.stu.jump.StuJumpModel;
import com.sunny.lib.base.log.SunLog;
import com.sunny.lib.common.router.RouterConstant;
import com.sunny.lib.common.utils.JsonUtils;

public abstract class StuBaseActivity extends Activity {

    protected String TAG = "";

    protected StuJumpModel mJumpModel;

    protected abstract int buildLayoutResID();

    protected abstract void initView();

    protected void showLog(String log) {
        SunLog.i(TAG, log);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName() + "[" + getClass().getName() + "]";

        int layoutResID = buildLayoutResID();
        if (layoutResID <= 0) {
            throw new RuntimeException("layoutResID(" + layoutResID + ") is invalid");
        }

        //
        initJumpValue();

        //
        setContentView(layoutResID);

        //
        initView();
    }

    private void initJumpValue() {
        String jumpValue = getIntent().getStringExtra(RouterConstant.JumpValue);
        showLog("initJumpValue  jumpValue :" + jumpValue);
        if (jumpValue != null) {
            mJumpModel = JsonUtils.getBean(jumpValue);
        }

        if (mJumpModel != null) {
            StuConstant.reBuildJumpModel(mJumpModel);
        }
    }

    protected void initTopBarView(int titleResId) {
        initTopBarView(getString(titleResId));
    }

    protected void initTopBarView(String title) {
        View layoutTopBar = findViewById(R.id.layout_top_bar);
        if (layoutTopBar == null) {
            return;
        }

        //
        findViewById(R.id.top_bar_left_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //
        TextView titleView = findViewById(R.id.top_bar_title);
        titleView.setText(title);
    }

}
