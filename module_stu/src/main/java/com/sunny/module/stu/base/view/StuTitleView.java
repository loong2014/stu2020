package com.sunny.module.stu.base.view;

import android.content.Context;
import android.util.AttributeSet;

import com.sunny.lib.ui.SunnyTextView;

public class StuTitleView extends SunnyTextView {

    public StuTitleView(Context context) {
        this(context, null);
    }

    public StuTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StuTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
