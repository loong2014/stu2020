package com.sunny.module.view.reverse

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.sunny.module.view.R
import com.sunny.module.view.databinding.ViewActCardReverseBinding


class ReverseCardView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mViewBinding: ViewActCardReverseBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_act_card_reverse,
            this,
            false
        )
    }

    init {
        addView(mViewBinding.root)
        doInit()
    }

    var mRightOutSet: AnimatorSet? = null
    var mLeftInSet: AnimatorSet? = null

    var mIsShowBack = false
    var isRunning = false

    private fun doInit() {
        mRightOutSet =
            AnimatorInflater.loadAnimator(context, R.animator.reverse_anim_out) as AnimatorSet
        mLeftInSet =
            AnimatorInflater.loadAnimator(context, R.animator.reverse_anim_in) as AnimatorSet
        // 设置点击事件
        mRightOutSet?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                isRunning = true
                mViewBinding.root.isClickable = false
            }
        })
        mRightOutSet?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                isRunning = false
                mViewBinding.root.isClickable = true
            }
        })

        // 改变视角距离, 贴近屏幕
        val distance = 16000
        val scale = resources.displayMetrics.density * distance
        mViewBinding.layoutCardFront.cameraDistance = scale
        mViewBinding.layoutCardBack.cameraDistance = scale

        setOnClickListener {
            doReverse()
        }
    }


    fun doReverse() {
        if (isRunning) return

        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet?.setTarget(mViewBinding.layoutCardFront)
            mLeftInSet?.setTarget(mViewBinding.layoutCardBack)
            mRightOutSet?.start();
            mLeftInSet?.start();
            mIsShowBack = true;
        } else { // 背面朝上
            mRightOutSet?.setTarget(mViewBinding.layoutCardBack)
            mLeftInSet?.setTarget(mViewBinding.layoutCardFront)
            mRightOutSet?.start();
            mLeftInSet?.start();
            mIsShowBack = false;
        }
    }

}