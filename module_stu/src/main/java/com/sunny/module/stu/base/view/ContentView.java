package com.sunny.module.stu.base.view;

import android.content.Context;
import android.util.AttributeSet;

import com.sunny.lib.common.utils.ResUtils;
import com.sunny.lib.ui.SunnyTextView;
import com.sunny.module.stu.R;

public class ContentView extends SunnyTextView {

    public ContentView(Context context) {
        super(context);
    }

    public ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextSize(ResUtils.getDimension(R.dimen.stu_content_size));
    }
}
