package com.sunny.lib.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.sunny.lib.base.utils.FontUtils;

/**
 * Created by zhangxin17 on 2020/8/18
 */
public class SunnyTextView extends AppCompatTextView {
    public SunnyTextView(Context context) {
        super(context);
        initView(context, null);
    }

    public SunnyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SunnyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        FontUtils.changeDefaultFont(this);
    }
}
