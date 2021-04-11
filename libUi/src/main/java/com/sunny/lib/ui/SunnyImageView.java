package com.sunny.lib.ui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by zhangxin17 on 2020/7/30
 */
public class SunnyImageView extends AppCompatImageView {

    public SunnyImageView(Context context) {
        this(context, null);
    }

    public SunnyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunnyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // >= 6.0 (23)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//setForeground(getResources().getDrawable(R.attr.selectableItemBackground));
        }
    }
}
