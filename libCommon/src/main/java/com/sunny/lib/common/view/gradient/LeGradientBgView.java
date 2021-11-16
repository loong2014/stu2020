package com.sunny.lib.common.view.gradient;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.sunny.lib.common.fresco.FrescoUtils;
import com.sunny.lib.common.fresco.LeFrescoImageView;

public class LeGradientBgView extends LeFrescoImageView {

    private BgHandler mBgHandler;

    public LeGradientBgView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public LeGradientBgView(Context context) {
        super(context);
        init();
    }

    public LeGradientBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeGradientBgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LeGradientBgView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBgHandler = new BgHandler(this);
        }
    }

    public void updateBg(String bgUrl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (mBgHandler != null) {
                mBgHandler.handle(new BgMsg(Type.Single, bgUrl));
            }
        } else {
            FrescoUtils.loadImageUrl(bgUrl, this);
        }
    }
}
