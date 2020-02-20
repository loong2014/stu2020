package com.sunny.view.layout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SunBlockLayoutManager extends LinearLayoutManager {
    private static final float SCROLLING_SPEED_INIT = 0.3f;
    private float mScrollSpeed;

    public SunBlockLayoutManager(Context context) {
        super(context);
        init();
    }

    public SunBlockLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    public SunBlockLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScrollSpeed = SCROLLING_SPEED_INIT;
    }
}
