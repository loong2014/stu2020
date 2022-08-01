package com.sunny.lib.common.helper

import android.view.View

abstract class OnLoadingAndRetryListener {

    abstract fun setRetryEvent(retryView: View??)

    open fun setLoadingEvent(loadingView: View?) {}

    open fun setEmptyEvent(emptyView: View?) {}

    open fun generateLoadingLayoutId(): Int {
        return LoadingAndRetryManager.NO_LAYOUT_ID
    }

    open fun generateRetryLayoutId(): Int {
        return LoadingAndRetryManager.NO_LAYOUT_ID
    }

    open fun generateEmptyLayoutId(): Int {
        return LoadingAndRetryManager.NO_LAYOUT_ID
    }

    open fun generateLoadingLayout(): View? {
        return null
    }

    open fun generateRetryLayout(): View? {
        return null
    }

    open fun generateEmptyLayout(): View? {
        return null
    }

    fun isSetLoadingLayout(): Boolean {
        if (generateLoadingLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || generateLoadingLayout() != null)
            return true
        return false
    }

    fun isSetRetryLayout(): Boolean {
        if (generateRetryLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || generateRetryLayout() != null)
            return true
        return false
    }

    fun isSetEmptyLayout(): Boolean {
        if (generateEmptyLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || generateEmptyLayout() != null)
            return true
        return false
    }


}