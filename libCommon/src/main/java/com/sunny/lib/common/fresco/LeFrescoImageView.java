package com.sunny.lib.common.fresco;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

public class LeFrescoImageView extends SimpleDraweeView {

    public LeFrescoImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public LeFrescoImageView(Context context) {
        super(context);
    }

    public LeFrescoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeFrescoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LeFrescoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
