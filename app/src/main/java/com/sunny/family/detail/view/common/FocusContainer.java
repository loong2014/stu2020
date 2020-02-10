package com.sunny.family.detail.view.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import com.sunny.family.detail.view.FocusProcessLayout;
import com.sunny.family.detail.view.ScaledRecyclerView;

/**
 * Created by ZhangBoshi on 2019/5/7.
 */

public class FocusContainer extends FocusProcessLayout {
    public FocusContainer(Context context) {
        super(context, null);
    }

    public FocusContainer(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public FocusContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * todo by zhoujiamu 2018.4.4
     * 发现在918平台,{@link android.widget.LinearLayout}会存在item飘动问题. 若不覆盖此方法, 也会偶现布局问题, 未查明原因.
     * 目前在乐范桌面中使用{@link android.widget.FrameLayout}替代.
     */
    @Override
    protected final void bringToFrontBelowApi21(boolean focused) {
        ViewParent parent = getParent();
        View child = this;

        while (parent instanceof View) {
            if (parent instanceof BlockRecyclerView) {
                break;
            }

            // 在api21以下, ScaledRecyclerView会根据焦点状态调整绘制顺序, 因此不需要bringToFront
            if (!(parent instanceof ScaledRecyclerView)) {
                parent.bringChildToFront(child);
            }

            child = (View) parent;
            parent = child.getParent();
        }
        requestLayout();
        invalidate();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void bringToFrontApi21(boolean focused) {
        View cur = this;

        while (true) {
            cur.setTranslationZ(focused ? 0.1f : 0);

            ViewParent parent = cur.getParent();
            if (!(parent instanceof View) || parent instanceof BlockRecyclerView)
                break;

            cur = (View) parent;
        }
    }
}
