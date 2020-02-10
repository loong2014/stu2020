package com.sunny.family.detail.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.sunny.family.R;
import com.sunny.family.detail.view.cardview.CardView;
import com.sunny.lib.utils.ResUtils;


/**
 * @Author guoyanming@letv.com
 * FocusProcessLayout scales the focused view when focus changes.
 * @attr enlarge_x the size in horizontal that can be scaled to,
 * uses default scale if it's not specified.
 * @attr enlarge_y the size in horizontal that can be scaled to,
 * uses default scale if it's not specified.
 */
public class FocusProcessLayout extends CardView {
    private static final float DEFAULT_SCALE = 1.2f;
    private static final float UN_FOCUS_SCALE_X = 1.0f;
    private static final float UN_FOCUS_SCALE_Y = 1.0f;

    private float mFocusScaleX;
    private float mFocusScaleY;

    private ObjectAnimator xScaleAnim;
    private ObjectAnimator yScaleAnim;

    private int mEnlargeX = -1;
    private int mEnlargeY = -1;
    private int mStrokePix = -1;
    private float rate;
    private boolean mShowStroke;

    // anti alias when scale view
    private static DrawFilter sDrawFilternew = new PaintFlagsDrawFilter(0,
            Paint.FILTER_BITMAP_FLAG);

    // Paint for drawing focus Rect
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mFocusRect = new RectF();

    public FocusProcessLayout(Context context) {
        this(context, null);
    }

    public FocusProcessLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusProcessLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final AnimatorSetListener mAnimatorSetListener = new AnimatorSetListener() {
            @Override
            View getView() {
                return getScaleTarget();
            }
        };

        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        setCardElevation(0.0f);
        setMaxCardElevation(0.0f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(0);
            setTranslationZ(0);
        }

        // init x and y animator
        xScaleAnim = new ObjectAnimator();
        xScaleAnim.setTarget(this);
        xScaleAnim.setPropertyName("scaleX");
        xScaleAnim.setDuration(80);
        xScaleAnim.addListener(mAnimatorSetListener);

        yScaleAnim = new ObjectAnimator();
        yScaleAnim.setTarget(this);
        yScaleAnim.setPropertyName("scaleY");
        yScaleAnim.setDuration(80);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FocusProcessLayout);
        mEnlargeX = a.getDimensionPixelSize(R.styleable.FocusProcessLayout_enlarge_x, -1);
        mEnlargeY = a.getDimensionPixelSize(R.styleable.FocusProcessLayout_enlarge_y, -1);
        // 焦点框颜色
        mShowStroke = a.getBoolean(R.styleable.FocusProcessLayout_show_stroke, true);

        rate = a.getFloat(R.styleable.FocusProcessLayout_focus_scale_rate, DEFAULT_SCALE);
        final int strokeColor = a.getColor(R.styleable.FocusProcessLayout_stroke_color,
                Color.WHITE);

        mFocusScaleX = rate;
        mFocusScaleY = rate;
        int defaultStrokePix = context.getResources()
                .getDimensionPixelSize(R.dimen.focus_stroke_width);
        mStrokePix = ResUtils.INSTANCE.scaleWidth(a.getDimensionPixelSize(
                R.styleable.FocusProcessLayout_focus_stroke_width, defaultStrokePix));
        a.recycle();

        mPaint.setStrokeWidth(mStrokePix * 2);
        mPaint.setColor(strokeColor);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mEnlargeX >= 0) {
            mFocusScaleX = 1 + ((float) mEnlargeX) / getScaleTarget().getMeasuredWidth();
        }
        if (mEnlargeY >= 0) {
            mFocusScaleY = 1 + ((float) mEnlargeY) / getScaleTarget().getMeasuredHeight();
        }
    }

    protected View getScaleTarget() {
        return this;
    }

    /**
     * Api21以上使用Z轴实现，各桌面可根据具体布局情况进行override
     *
     * @param focused
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void bringToFrontApi21(boolean focused) {
        setTranslationZ(focused ? 0.1f : 0);
        ((View) getScaleTarget().getParent()).setTranslationZ(focused ? 0.1f : 0f);
    }

    /**
     * Api < 21没有Z轴，使用bringToFront()实现，各桌面可根据具体布局情况进行override
     *
     * @param focused
     */
    protected void bringToFrontBelowApi21(boolean focused) {
        if (focused) {
            // invalidate to cause re-draw in a ordered that the view has focus
            // is the last to be drawn, this order is organized by
            if (getScaleTarget().getParent() instanceof View) {
                ((View) getScaleTarget().getParent()).bringToFront();
            }
            bringToFront();
        }
    }

    protected void doScaleView(boolean focused) {
        if (!((UN_FOCUS_SCALE_X > mFocusScaleX) || (UN_FOCUS_SCALE_X < mFocusScaleX))
                || !((UN_FOCUS_SCALE_Y > mFocusScaleY) || (UN_FOCUS_SCALE_Y < mFocusScaleY))) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bringToFrontApi21(focused);
        } else {
            bringToFrontBelowApi21(focused);
        }

        // start re-scale animation
        xScaleAnim.setFloatValues(UN_FOCUS_SCALE_X, mFocusScaleX);
        yScaleAnim.setFloatValues(UN_FOCUS_SCALE_Y, mFocusScaleY);
        if (focused) {
            xScaleAnim.start();
            yScaleAnim.start();
        } else {
            xScaleAnim.reverse();
            yScaleAnim.reverse();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        doScaleView(gainFocus);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas.getDrawFilter() != sDrawFilternew) {
            canvas.setDrawFilter(sDrawFilternew);
        }
        super.draw(canvas);
        // draw focus
        if (hasFocus() && mShowStroke) {
            mFocusRect.left = getScrollX();
            mFocusRect.top = getScrollY();
            mFocusRect.right = mFocusRect.left + getWidth();
            mFocusRect.bottom = mFocusRect.top + getHeight();
            canvas.drawRoundRect(mFocusRect, getRadius(), getRadius(), mPaint);
        }
    }

    private abstract static class AnimatorSetListener implements Animator.AnimatorListener {
        View mView;
        int mLayerType;

        @Override
        public void onAnimationStart(Animator animation) {
            mView = getView();
            mLayerType = mView.getLayerType();
            mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mView != null) {
                mView.setLayerType(mLayerType, null);
                mView = null;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            onAnimationEnd(animation);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        abstract View getView();
    }

    public void setFocusScale(float focusScale) {
        this.mFocusScaleX = focusScale;
        this.mFocusScaleY = focusScale;
    }

    /**
     * 设置在获得焦点状态时, 是否开启描边
     * {@param showStroke} 如果是true, 则开启描边, 否则关闭.
     */
    public void setShowStroke(final boolean showStroke) {
        mShowStroke = showStroke;
    }

    public void setStrokeColor(final String strokeColor) {
        try {
            mPaint.setColor(Color.parseColor(strokeColor));
        } catch (Exception e) {
            e.printStackTrace();
            mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        }

    }
}