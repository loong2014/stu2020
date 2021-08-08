package com.sunny.module.stu.BAndroid.AView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.sunny.module.stu.R;

public class ASunnyView extends View {
    public ASunnyView(Context context) {
        this(context, null);
    }

    public ASunnyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ASunnyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * @param context      上下文
     * @param attrs        xml中定义的属性
     * @param defStyleAttr them中定义的属性，当attrs中没有指定时，从defStyleAttr中获取
     * @param defStyleRes  当defStyleAttr为0时使用的默认样式，在sdk21后新增
     */
    public ASunnyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (attrs == null) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ASunnyView, defStyleAttr, defStyleRes);
        String name = ta.getString(R.styleable.ASunnyView_sunny_name);

        //记得进行回收，否则会存在内存泄露
        ta.recycle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
