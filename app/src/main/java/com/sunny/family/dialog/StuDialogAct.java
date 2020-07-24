package com.sunny.family.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sunny.family.R;
import com.sunny.lib.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/7/7
 */
public class StuDialogAct extends BaseActivity implements DialogClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_alert_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShowAlertDialog();
            }
        });

        findViewById(R.id.btn_custom_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShowCustomDialog();
            }
        });
    }

    private void doShowAlertDialog() {
        MyAlertDialogFragment dialogFragment = MyAlertDialogFragment.buildDialog("一生一世");
        dialogFragment.show(getSupportFragmentManager(), "myAlert");

    }


    private void doShowCustomDialog() {
        MyCustomDialogFragment dialogFragment = MyCustomDialogFragment.buildDialog("一生一世");
        dialogFragment.show(getSupportFragmentManager(), "myCustom");

    }

    @Override
    public void onConfirmClick() {
        doShowCustomDialog();
    }

    @Override
    public void onSubmitClick(String pwd) {

        TextView tipView = findViewById(R.id.tv_tip);
        if (TextUtils.isEmpty(pwd)) {
            pwd = "null";
        } else {
            pwd = "pwd :" + pwd;
        }
        tipView.setText(pwd);
    }
}
