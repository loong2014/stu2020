package com.sunny.lib.common.dialog

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.lang.Exception
import kotlin.jvm.JvmOverloads

open class ExternalDialogFragment : DialogFragment() {
    /**
     * Sets how long prevent multiple operations in a short time
     *
     * @param antiShakeDuration anti shake duration
     */
    //Prevent multiple operations in a short time
    // fix Fragment already added.
    var antiShakeDuration = DEFAULT_ANTI_SHAKE_DURATION
    private var mLashShowTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            antiShakeDuration =
                savedInstanceState.getLong(SAVED_ANTI_SHAKE_DURATION, DEFAULT_ANTI_SHAKE_DURATION)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(SAVED_ANTI_SHAKE_DURATION, antiShakeDuration)
        super.onSaveInstanceState(outState)
    }

    /**
     * 是否正在显示
     *
     * @return is showing
     */
    val isShowing: Boolean
        get() = dialog != null && dialog!!.isShowing

    /**
     * 简单的显示 DialogFragment
     *
     * @param manager FragmentManager
     */
    fun show(manager: FragmentManager) {
        show(manager, simpleTag)
    }

    /**
     * 带 tag 的显示 DialogFragment
     *
     * @param manager FragmentManager
     * @param tag     tag
     */
    override fun show(manager: FragmentManager, tag: String?) {
        val current = SystemClock.elapsedRealtime()
        if (current - mLashShowTime <= antiShakeDuration) {
            return
        }
        if (manager.isDestroyed || manager.isStateSaved) {
            return
        }
        if (isAdded) {
            dismissAllowingStateLoss()
        }
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            //catch java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        }
        mLashShowTime = current
    }
    /**
     * 带 Tag 显示 DialogFragment 允许状态丢失
     *
     * @param manager FragmentManager
     * @param tag     Tag
     */
    /**
     * 简单的显示 DialogFragment 允许状态丢失
     *
     * @param manager FragmentManager
     */
    @JvmOverloads
    fun showAllowingStateLoss(manager: FragmentManager, tag: String? = simpleTag) {
        val current = SystemClock.elapsedRealtime()
        if (current - mLashShowTime <= antiShakeDuration) {
            return
        }
        if (manager.isDestroyed) {
            return
        }
        if (isAdded) {
            dismissAllowingStateLoss()
        }
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
        mLashShowTime = current
    }

    /**
     * 更加安全的 dismiss
     */
    override fun dismiss() {
        if (fragmentManager == null) {
            return
        }
        try {
            super.dismiss()
        } catch (e: Exception) {
            //catch java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        }
    }

    /**
     * 更加安全的 dismiss
     */
    override fun dismissAllowingStateLoss() {
        if (fragmentManager == null) {
            return
        }
        super.dismissAllowingStateLoss()
    }

    /**
     * 获取简单的 Tag
     *
     * @return simple name for tag
     */
    val simpleTag: String
        get() = this.javaClass.name

    companion object {
        private const val SAVED_ANTI_SHAKE_DURATION = "SAVED_ANTI_SHAKE_DURATION"
        private const val DEFAULT_ANTI_SHAKE_DURATION: Long = 100
    }
}