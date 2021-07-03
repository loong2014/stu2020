package com.sunny.lib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 自定义圆点
 */
public class SunnyCirclePoint extends View {

    private int mColor = Color.RED;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SunnyCirclePoint(Context context) {
        this(context, null);
    }

    public SunnyCirclePoint(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunnyCirclePoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SunnyCirclePoint);
            mColor = a.getColor(R.styleable.SunnyCirclePoint_circle_color, mColor);
            a.recycle();
        }

        //
        mPaint.setColor(mColor);
    }

    public void updateColor(int color) {
        mColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        int radius = Math.min(w, h) / 2;
        canvas.drawCircle(w / 2, h / 2, radius, mPaint);
    }
}
