package com.sunny.lib.common.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by zhangxin17 on 2020/8/18
 */
public class SunnyConstraintLayout extends ConstraintLayout {
    public SunnyConstraintLayout(Context context) {
        super(context);
    }

    public SunnyConstraintLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SunnyConstraintLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
