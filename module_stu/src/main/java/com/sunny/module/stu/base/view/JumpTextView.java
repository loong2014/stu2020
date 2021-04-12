package com.sunny.module.stu.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sunny.lib.base.log.SunLog;
import com.sunny.lib.common.router.RouterJump;
import com.sunny.lib.ui.SunnyTextView;
import com.sunny.module.stu.R;
import com.sunny.module.stu.StuConstant;

public class JumpTextView extends SunnyTextView {

    private OnClickListener mClickListener;
    private String mJumpPath;
    private String mJumpType;

    public JumpTextView(Context context) {
        this(context, null);
    }

    public JumpTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JumpTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StuJumpText);
            mJumpPath = ta.getString(R.styleable.StuJumpText_jump_path);
            mJumpType = ta.getString(R.styleable.StuJumpText_jump_type);
            ta.recycle();
        }

        SunLog.i("JumpTextView", "mJumpPath :" + mJumpPath + " , mJumpType :" + mJumpType);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }

                if (mJumpPath != null) {
                    String jumpValue = StuConstant.buildJumpValue(mJumpType);
                    RouterJump.navigation(mJumpPath, jumpValue);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.mClickListener = l;
    }
}
