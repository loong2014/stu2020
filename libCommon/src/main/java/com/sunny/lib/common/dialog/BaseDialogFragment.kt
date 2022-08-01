package com.sunny.lib.common.dialog

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentActivity
import com.sunny.lib.common.R
import java.util.*

/**
 *
 */
open class BaseDialogFragment : ExternalDialogFragment() {
    protected var mActivity: FragmentActivity? = null

    @FloatRange(from = 0.0, to = 1.0)
    private var mDimAmount = 0.5f
    private var mHeight = 0
    private var mWidth = 0
    private var mGravity = Gravity.CENTER
    private var mKeyboardEnable = true
    private var mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN

    @StyleRes
    private var mAnimationStyle = 0

    //点击外部是否可取消
    private var mCanceledOnTouchOutside = true
    private var mPaddingLeft = -1
    private var mPaddingTop = -1
    private var mPaddingRight = -1
    private var mPaddingBottom = -1

    //全屏并且适配导航栏的情况需要设置
    //<item name="android:windowIsFloating">false</item>
    //否则导航栏会被遮挡，如果没有导航栏则不影响
    //PS:windowIsFloating=true 适配导航栏太复杂
    private var mFullscreen = false

    //全屏情况，是否隐藏状态栏 >4.4
    private var mHideStatusBar = false

    //全屏情况，是否是浅色模式，即状态栏字体为深色>6.0
    private var mStatusBarLightMode = false

    //DialogFragment id
    private var mRequestId = -1

    //runnable list execute on animation end
    private var mDismissActions: ArrayList<Runnable>? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = if (context is FragmentActivity) {
            context
        } else {
            throw RuntimeException("Fragment onAttach method context is not FragmentActivity.")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mDimAmount = savedInstanceState.getFloat(SAVED_DIM_AMOUNT, 0.5f)
            mGravity = savedInstanceState.getInt(SAVED_GRAVITY, Gravity.CENTER)
            mWidth = savedInstanceState.getInt(SAVED_WIDTH, 0)
            mHeight = savedInstanceState.getInt(SAVED_HEIGHT, 0)
            mKeyboardEnable = savedInstanceState.getBoolean(SAVED_KEYBOARD_ENABLE, true)
            mSoftInputMode = savedInstanceState.getInt(
                SAVED_SOFT_INPUT_MODE,
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
            )
            mCanceledOnTouchOutside = savedInstanceState.getBoolean(
                SAVED_CANCEL_ON_TOUCH_OUTSIDE,
                true
            )
            mAnimationStyle = savedInstanceState.getInt(SAVED_ANIMATION_STYLE, 0)
            mRequestId = savedInstanceState.getInt(SAVED_REQUEST_ID, -1)
            mPaddingLeft = savedInstanceState.getInt(SAVED_PADDING_LEFT, -1)
            mPaddingTop = savedInstanceState.getInt(SAVED_PADDING_TOP, -1)
            mPaddingRight = savedInstanceState.getInt(SAVED_PADDING_RIGHT, -1)
            mPaddingBottom = savedInstanceState.getInt(SAVED_PADDING_BOTTOM, -1)
            mFullscreen = savedInstanceState.getBoolean(SAVED_FULLSCREEN, false)
            mHideStatusBar = savedInstanceState.getBoolean(SAVED_HIDE_STATUS_BAR, true)
            mStatusBarLightMode = savedInstanceState.getBoolean(SAVED_STATUS_BAR_LIGHT_MODE, false)
        }
        //设置Style 透明背景，No Title
        setStyle(
            STYLE_NORMAL,
            if (mFullscreen) R.style.NoFloatingDialogFragment else R.style.FloatingDialogFragment
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat(SAVED_DIM_AMOUNT, mDimAmount)
        outState.putInt(SAVED_GRAVITY, mGravity)
        outState.putInt(SAVED_WIDTH, mWidth)
        outState.putInt(SAVED_HEIGHT, mHeight)
        outState.putBoolean(SAVED_KEYBOARD_ENABLE, mKeyboardEnable)
        outState.putInt(SAVED_SOFT_INPUT_MODE, mSoftInputMode)
        outState.putBoolean(SAVED_CANCEL_ON_TOUCH_OUTSIDE, mCanceledOnTouchOutside)
        outState.putInt(SAVED_ANIMATION_STYLE, mAnimationStyle)
        outState.putInt(SAVED_REQUEST_ID, mRequestId)
        outState.putInt(SAVED_PADDING_LEFT, mPaddingLeft)
        outState.putInt(SAVED_PADDING_TOP, mPaddingTop)
        outState.putInt(SAVED_PADDING_RIGHT, mPaddingRight)
        outState.putInt(SAVED_PADDING_BOTTOM, mPaddingBottom)
        outState.putBoolean(SAVED_FULLSCREEN, mFullscreen)
        outState.putBoolean(SAVED_HIDE_STATUS_BAR, mHideStatusBar)
        outState.putBoolean(SAVED_STATUS_BAR_LIGHT_MODE, mStatusBarLightMode)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (dialog == null || !showsDialog
            || dialog!!.window == null
        ) {
            return
        }
        val window = dialog!!.window ?: return
        //set WindowManager LayoutParams
        val layoutParams = window.attributes
        layoutParams.dimAmount = mDimAmount
        layoutParams.gravity = mGravity
        if (mWidth > 0 || mWidth == ViewGroup.LayoutParams.MATCH_PARENT || mWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            layoutParams.width = mWidth
        }
        if (mHeight > 0 || mHeight == ViewGroup.LayoutParams.MATCH_PARENT || mHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            layoutParams.height = mHeight
        }
        //if fullscreen set width,height match_parent
        if (mFullscreen) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        window.attributes = layoutParams
        val decor = window.decorView
        decor.setPadding(
            if (mPaddingLeft >= 0) mPaddingLeft else decor.paddingLeft,
            if (mPaddingTop >= 0) mPaddingTop else decor.paddingTop,
            if (mPaddingRight >= 0) mPaddingRight else decor.paddingRight,
            if (mPaddingBottom >= 0) mPaddingBottom else decor.paddingBottom
        )
        if (mFullscreen) {
            if (mHideStatusBar) {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                DialogBarHelper.setTransparentStatusBar(window)
                if (mStatusBarLightMode) {
                    //设置状态栏字体颜色只对全屏有效，如果不是Match_parent即使设置了也不起作用
                    DialogBarHelper.setStatusBarLightMode(window, true)
                }
            }
            //全屏时适配刘海屏
            DialogBarHelper.fitNotch(window)
        }
        if (mAnimationStyle != 0) {
            window.setWindowAnimations(mAnimationStyle)
        }
        if (mKeyboardEnable) {
            window.setSoftInputMode(mSoftInputMode)
        }
        if (isCancelable) {
            dialog!!.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        executeActionsOnDismiss()
        for (dialogDismissListener in getDialogDismissListeners()) {
            dialogDismissListener.onDismiss(this, getRequestId())
        }
    }

    /**
     * execute runnable when dialog dismiss
     */
    private fun executeActionsOnDismiss() {
        //execute dismiss actions if not empty
        if (mDismissActions != null) {
            for (i in mDismissActions!!.indices) {
                val runnable = mDismissActions!![i]
                if (runnable != null) {
                    runnable.run()
                }
            }
            mDismissActions!!.clear()
        }
    }

    /**
     * add action, they will execute when dialog dismiss.
     *
     * @param runnable runnable
     */
    fun runOnDismiss(runnable: Runnable) {
        if (mDismissActions == null) {
            mDismissActions = ArrayList(1)
        }
        mDismissActions!!.add(runnable)
    }

    /**
     * @return requestId of you set
     */
    fun getRequestId(): Int {
        return mRequestId
    }

    /**
     * Sets requestId for DialogFragment, Can use by [BaseDialogFragment.getRequestId]
     *
     * @param requestId requestId
     */
    fun setRequestId(requestId: Int) {
        mRequestId = requestId
    }

    /**
     * @return Whether the dialog should be canceled when
     * touched outside the window.
     */
    open fun isCanceledOnTouchOutside(): Boolean {
        return mCanceledOnTouchOutside
    }

    /**
     * Sets whether this dialog is canceled when touched outside the window's
     * bounds.
     *
     * @param canceledOnTouchOutside Whether the dialog should be canceled when
     * touched outside the window.
     */
    fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean) {
        mCanceledOnTouchOutside = canceledOnTouchOutside
    }

    /**
     * @return window dim amount
     */
    fun getDimAmount(): Float {
        return mDimAmount
    }

    /**
     * Sets window dim amount when dialog show.
     *
     * @param dimAmount dim amount
     */
    fun setDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float) {
        mDimAmount = dimAmount
    }

    /**
     * @return DialogFragment content already set width
     */
    fun getWidth(): Int {
        return mWidth
    }

    /**
     * Sets DialogFragment content width
     *
     * @param width DialogFragment content height you want set
     */
    fun setWidth(width: Int) {
        mWidth = width
    }

    /**
     * Sets DialogFragment content width with dip unit.
     *
     * @param widthDp width dip
     */
    fun setWidth(widthDp: Float) {
        mWidth = dp2px(widthDp)
    }

    /**
     * Sets DialogFragment content width MATCH_PARENT.
     */
    fun setMatchWidth() {
        mWidth = ViewGroup.LayoutParams.MATCH_PARENT
    }

    /**
     * Sets DialogFragment content width WRAP_CONTENT
     */
    fun setWrapWidth() {
        mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * Sets DialogFragment content width with screen width percent.
     *
     * @param widthPercent percent of screen width
     */
    fun setWidthPercent(@FloatRange(from = 0.0, to = 1.0) widthPercent: Float) {
        mWidth = (Resources.getSystem().displayMetrics.widthPixels * widthPercent).toInt()
    }

    /**
     * @return DialogFragment content already set height
     */
    fun getHeight(): Int {
        return mHeight
    }

    /**
     * Sets DialogFragment content height.
     *
     * @param height DialogFragment content height you want set
     */
    fun setHeight(height: Int) {
        mHeight = height
    }

    /**
     * Sets DialogFragment content height with dip unit.
     *
     * @param heightDp height dip
     */
    fun setHeight(heightDp: Float) {
        mHeight = dp2px(heightDp)
    }

    /**
     * Sets DialogFragment content height MATCH_PARENT.
     */
    fun setMatchHeight() {
        mHeight = ViewGroup.LayoutParams.MATCH_PARENT
    }

    /**
     * Sets DialogFragment content height WRAP_CONTENT.
     */
    fun setWrapHeight() {
        mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * Sets DialogFragment content height with screen height percent.
     *
     * @param heightPercent percent of screen height
     */
    fun setHeightPercent(@FloatRange(from = 0.0, to = 1.0) heightPercent: Float) {
        mHeight = (Resources.getSystem().displayMetrics.heightPixels * heightPercent).toInt()
    }

    /**
     * @return Window content view padding left
     */
    fun getPaddingLeft(): Int {
        return mPaddingLeft
    }

    /**
     * Sets Window content view padding left.
     *
     * @param paddingLeft Window content view padding left
     */
    fun setPaddingLeft(paddingLeft: Int) {
        mPaddingLeft = paddingLeft
    }

    /**
     * Sets Window content view padding left with dip unit.
     *
     * @param paddingLeftDp padding left dip
     */
    fun setPaddingLeft(paddingLeftDp: Float) {
        mPaddingLeft = dp2px(paddingLeftDp)
    }

    /**
     * @return Window content view padding top
     */
    fun getPaddingTop(): Int {
        return mPaddingTop
    }

    /**
     * Sets Window content view padding top.
     *
     * @param paddingTop Window content view padding top
     */
    fun setPaddingTop(paddingTop: Int) {
        mPaddingTop = paddingTop
    }

    /**
     * Sets Window content view padding top with dip unit.
     *
     * @param paddingTopDp padding top dip
     */
    fun setPaddingTop(paddingTopDp: Float) {
        mPaddingTop = dp2px(paddingTopDp)
    }

    /**
     * @return Window content view padding top
     */
    fun getPaddingRight(): Int {
        return mPaddingRight
    }

    /**
     * Sets Window content view padding right.
     *
     * @param paddingRight Window content view padding right
     */
    fun setPaddingRight(paddingRight: Int) {
        mPaddingRight = paddingRight
    }

    /**
     * Sets Window content view padding right with dip unit.
     *
     * @param paddingRightDp padding right dip
     */
    fun setPaddingRight(paddingRightDp: Float) {
        mPaddingRight = dp2px(paddingRightDp)
    }

    /**
     * @return Window content view padding bottom
     */
    fun getPaddingBottom(): Int {
        return mPaddingBottom
    }

    /**
     * Sets Window content view padding bottom.
     *
     * @param paddingBottom Window content view padding bottom
     */
    fun setPaddingBottom(paddingBottom: Int) {
        mPaddingBottom = paddingBottom
    }

    /**
     * Sets Window content view padding bottom with dip unit.
     *
     * @param paddingBottomDp padding bottom dip
     */
    fun setPaddingBottom(paddingBottomDp: Float) {
        mPaddingBottom = dp2px(paddingBottomDp)
    }

    /**
     * Sets Window content view left and right padding at the same time.
     *
     * @param paddingLeft  padding left
     * @param paddingRight padding right
     */
    fun setPaddingHorizontal(paddingLeft: Int, paddingRight: Int) {
        mPaddingLeft = paddingLeft
        mPaddingRight = paddingRight
    }

    /**
     * Sets Window content view left and right padding with dip unit at the same time.
     *
     * @param paddingLeftDp  padding left dip
     * @param paddingRightDp padding right dip
     */
    fun setPaddingHorizontal(paddingLeftDp: Float, paddingRightDp: Float) {
        mPaddingLeft = dp2px(paddingLeftDp)
        mPaddingRight = dp2px(paddingRightDp)
    }

    /**
     * Sets Window content view top and bottom padding at the same time.
     *
     * @param paddingTop    padding top
     * @param paddingBottom padding bottom
     */
    fun setPaddingVertical(paddingTop: Int, paddingBottom: Int) {
        mPaddingTop = paddingTop
        mPaddingBottom = paddingBottom
    }

    /**
     * Sets Window content view top and bottom padding with dip unit at the same time.
     *
     * @param paddingTopDp    padding top dip
     * @param paddingBottomDp padding bottom dip
     */
    fun setPaddingVertical(paddingTopDp: Float, paddingBottomDp: Float) {
        mPaddingTop = dp2px(paddingTopDp)
        mPaddingBottom = dp2px(paddingBottomDp)
    }

    /**
     * @return Whether keyboard enabled.
     */
    fun isKeyboardEnable(): Boolean {
        return mKeyboardEnable
    }

    /**
     * Sets keyboard enabled
     *
     * @param keyboardEnable Whether keyboard enabled.
     */
    fun setKeyboardEnable(keyboardEnable: Boolean) {
        mKeyboardEnable = keyboardEnable
    }

    /**
     * @return window soft input mode.
     */
    fun getSoftInputMode(): Int {
        return mSoftInputMode
    }

    /**
     * Sets soft input mode for Window.
     *
     * @param softInputMode soft input mode
     */
    fun setSoftInputMode(softInputMode: Int) {
        mSoftInputMode = softInputMode
    }

    /**
     * @return Window content gravity
     */
    fun getGravity(): Int {
        return mGravity
    }

    /**
     * Sets Window content gravity.
     *
     * @param gravity Window content gravity
     */
    fun setGravity(gravity: Int) {
        mGravity = gravity
    }

    /**
     * Sets Window content gravity bottom.
     */
    fun setBottomGravity() {
        mGravity = Gravity.BOTTOM
    }

    /**
     * @return whether Window fullscreen
     */
    fun isFullscreen(): Boolean {
        return mFullscreen
    }

    /**
     * @return whether hide status bar
     */
    fun isHideStatusBar(): Boolean {
        return mHideStatusBar
    }

    /**
     * Sets whether Window fullscreen
     *
     * @param fullscreen
     */
    fun setFullscreen(fullscreen: Boolean) {
        setFullscreen(fullscreen, true, false)
    }

    /**
     * Sets whether Window fullscreen
     *
     * @param fullscreen    whether Window fullscreen
     * @param hideStatusBar whether hide status bar
     */
    fun setFullscreen(fullscreen: Boolean, hideStatusBar: Boolean) {
        setFullscreen(fullscreen, hideStatusBar, false)
    }

    /**
     * Sets whether Window fullscreen
     *
     * @param fullscreen    whether Window fullscreen
     * @param hideStatusBar whether hide status bar
     * @param isLightMode   whether status bar light mode
     */
    fun setFullscreen(fullscreen: Boolean, hideStatusBar: Boolean, isLightMode: Boolean) {
        mFullscreen = fullscreen
        mHideStatusBar = hideStatusBar
        mStatusBarLightMode = isLightMode
    }

    /**
     * @return Window animation style
     */
    fun getAnimationStyle(): Int {
        return mAnimationStyle
    }

    /**
     * Sets Window animation style
     *
     * @param animationStyle Window animation style
     */
    fun setAnimationStyle(@StyleRes animationStyle: Int) {
        mAnimationStyle = animationStyle
    }

    /**
     * @return OnDialogDismissListener list from targetFragment,parentFragment,getActivity
     */
    protected fun getDialogDismissListeners(): List<OnDialogDismissListener> {
        return getDialogListeners(OnDialogDismissListener::class.java)
    }

    /**
     * @return OnDialogClickListener list from targetFragment,parentFragment,getActivity
     */
    protected fun getDialogClickListeners(): List<OnDialogClickListener> {
        return getDialogListeners(OnDialogClickListener::class.java)
    }

    /**
     * @return OnDialogClickListener from one of targetFragment,parentFragment,getActivity
     */
    protected fun getDialogClickListener(): OnDialogClickListener {
        return getDialogListener(OnDialogClickListener::class.java)!!
    }

    /**
     * @return OnDialogMultiChoiceClickListener list from targetFragment,parentFragment,getActivity
     */
    protected fun getDialogMultiChoiceClickListeners(): List<OnDialogMultiChoiceClickListener> {
        return getDialogListeners(OnDialogMultiChoiceClickListener::class.java)
    }

    /**
     * @return OnDialogMultiChoiceClickListener from one of targetFragment,parentFragment,getActivity
     */
    protected fun getDialogMultiChoiceClickListener(): OnDialogMultiChoiceClickListener {
        return getDialogListener(OnDialogMultiChoiceClickListener::class.java)!!
    }

    /**
     * 获取所有符合条件的 DialogFragment 监听器
     *
     * @param listenerInterface listener class
     * @param <T>               listener
     * @return List of listeners
    </T> */
    protected fun <T> getDialogListeners(listenerInterface: Class<T>): List<T> {
        val listeners = ArrayList<T>(3)
        if (listenerInterface.isInstance(targetFragment)) {
            listeners.add(targetFragment as T)
        }
        if (listenerInterface.isInstance(parentFragment)) {
            listeners.add(parentFragment as T)
        }
        if (listenerInterface.isInstance(activity)) {
            listeners.add(activity as T)
        }
        return Collections.unmodifiableList(listeners)
    }

    /**
     * 获取一个符合条件的 DialogFragment 监听器
     *
     * @param listenerInterface listener class
     * @param <T>               listener
     * @return one of Listeners
    </T> */
    protected fun <T> getDialogListener(listenerInterface: Class<T>): T? {
        if (listenerInterface.isInstance(targetFragment)) {
            return targetFragment as T?
        }
        if (listenerInterface.isInstance(parentFragment)) {
            return parentFragment as T?
        }
        return if (listenerInterface.isInstance(activity)) {
            activity as T?
        } else null
    }

    companion object {
        private const val TAG = "BaseDialogFragment"
        private const val SAVED_DIM_AMOUNT = "SAVED_DIM_AMOUNT"
        private const val SAVED_GRAVITY = "SAVED_GRAVITY"
        private const val SAVED_HEIGHT = "SAVED_HEIGHT"
        private const val SAVED_WIDTH = "SAVED_WIDTH"
        private const val SAVED_KEYBOARD_ENABLE = "SAVED_KEYBOARD_ENABLE"
        private const val SAVED_SOFT_INPUT_MODE = "SAVED_SOFT_INPUT_MODE"
        private const val SAVED_CANCEL_ON_TOUCH_OUTSIDE = "SAVED_CANCEL_ON_TOUCH_OUTSIDE"
        private const val SAVED_ANIMATION_STYLE = "SAVED_ANIMATION_STYLE"
        private const val SAVED_REQUEST_ID = "SAVED_REQUEST_ID"
        private const val SAVED_PADDING_LEFT = "SAVED_PADDING_LEFT"
        private const val SAVED_PADDING_TOP = "SAVED_PADDING_TOP"
        private const val SAVED_PADDING_RIGHT = "SAVED_PADDING_RIGHT"
        private const val SAVED_PADDING_BOTTOM = "SAVED_PADDING_BOTTOM"
        private const val SAVED_FULLSCREEN = "SAVED_FULLSCREEN"
        private const val SAVED_HIDE_STATUS_BAR = "SAVED_HIDE_STATUS_BAR"
        private const val SAVED_STATUS_BAR_LIGHT_MODE = "SAVED_STATUS_BAR_LIGHT_MODE"
        private fun dp2px(dp: Float): Int {
            //向上取整
            return Math.ceil(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp, Resources.getSystem().displayMetrics
                ).toDouble()
            ).toInt()
        }
    }
}