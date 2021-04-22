package com.sunny.module.stu.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.sunny.lib.base.log.SunLog;
import com.sunny.lib.ui.SunnyTextView;
import com.sunny.module.stu.R;

public class FoldTextView extends SunnyTextView {

    private OnClickListener mClickListener;
    private int mVisibility;
    private int mFoldLayoutResId;
    private View mFoldLayoutView;

    public FoldTextView(Context context) {
        this(context, null);
    }

    public FoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StuFoldText);
            mFoldLayoutResId = ta.getResourceId(R.styleable.StuFoldText_fold_layout_res_id, View.NO_ID);
            ta.recycle();
        }

        SunLog.i("JumpTextView", "mVisibility :" + mVisibility + " , mFoldLayoutResId :" + mFoldLayoutResId);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }

                dealFoldClick();
            }
        });
    }

    public void updateFoldContentVisibility(int visibility) {

        initFoldInfo();

        if (mVisibility == visibility) {
            return;
        }

        if (mFoldLayoutView != null) {
            mVisibility = visibility;
            mFoldLayoutView.setVisibility(mVisibility);
        }
    }

    private void dealFoldClick() {

        initFoldInfo();

        int nextVisibility;
        if (mVisibility == View.VISIBLE) {
            nextVisibility = View.GONE;
        } else {
            nextVisibility = View.VISIBLE;
        }
        updateFoldContentVisibility(nextVisibility);
    }

    private void initFoldInfo() {
        if (mFoldLayoutView != null) {
            return;
        }

        if (mFoldLayoutResId == View.NO_ID) {
            return;
        }

        if (getParent() == null || !(getParent() instanceof ViewGroup)) {
            return;
        }

        ViewGroup parent = (ViewGroup) getParent();
        mFoldLayoutView = parent.findViewById(mFoldLayoutResId);
        if (mFoldLayoutView != null) {
            mVisibility = mFoldLayoutView.getVisibility();
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.mClickListener = l;
    }
}
