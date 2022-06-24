package com.sunny.lib.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.sunny.lib.base.utils.FontUtils;

/**
 * Created by zhangxin17 on 2020/7/30
 */
public class SunnyButton extends AppCompatButton {

    public SunnyButton(Context context) {
        super(context);
        initView(context);
    }

    public SunnyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SunnyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        FontUtils.changeDefaultFont(this);
        setBackgroundResource(R.drawable.sunny_btn_selecter);
    }
}
