package com.sunny.lib.common.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sunny.lib.common.R

class LoadingAndRetryManager(
    activityOrFragmentOrView: Any,
    listener: OnLoadingAndRetryListener
) {

    var mLoadingAndRetryLayout: LoadingAndRetryLayout

    val DEFAULT_LISTENER: OnLoadingAndRetryListener = object : OnLoadingAndRetryListener() {
        override fun setRetryEvent(retryView: View?) {
            //默认的不做任何操作
        }
    }

    companion object {
        val NO_LAYOUT_ID = 0
        var BASE_LOADING_LAYOUT_ID = R.layout.ball_base_loading
        var BASE_RETRY_LAYOUT_ID = R.layout.ball_base_retry
        var BASE_EMPTY_LAYOUT_ID = R.layout.ball_base_empty

        fun generate(
            activityOrFragment: Any,
            listener: OnLoadingAndRetryListener
        ): LoadingAndRetryManager {
            return LoadingAndRetryManager(activityOrFragment, listener)
        }

        /**
         * 初始化参数
         */
        fun init(loadingLayoutId: Int, emptyLayoutId: Int, retryLayoutId: Int) {
            BASE_LOADING_LAYOUT_ID = loadingLayoutId
            BASE_RETRY_LAYOUT_ID = retryLayoutId
            BASE_EMPTY_LAYOUT_ID = emptyLayoutId
        }
    }

    fun showLoading() {
        mLoadingAndRetryLayout.showLoading()
    }

    fun showRetry(): View? {
        return mLoadingAndRetryLayout.showRetry()
    }

    fun showContent() {
        mLoadingAndRetryLayout.showContent()
    }

    fun showEmpty() {
        mLoadingAndRetryLayout.showEmpty()
    }

    init {
        var listener = listener
        var contentParent: ViewGroup
        val context: Context
        when (activityOrFragmentOrView) {
            is Activity -> {
                val activity = activityOrFragmentOrView as Activity
                context = activity
                contentParent = activityOrFragmentOrView.findViewById(android.R.id.content)
            }
            is Fragment -> {
                val fragment = activityOrFragmentOrView as Fragment
                context = fragment.requireActivity()
                contentParent = fragment.view?.parent as ViewGroup
            }
            is View -> {
                val view = activityOrFragmentOrView as View
                contentParent = view.parent as ViewGroup
                context = view.context
            }
            else -> throw IllegalArgumentException("the argument's type must be Activity or Fragment、view")
        }
        val childCount = contentParent.childCount

        var index = 0
        var oldContent: View
        if (activityOrFragmentOrView is View) {
            oldContent = activityOrFragmentOrView as View
            for (i in 0 until childCount) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i
                    break
                }
            }
        } else {
            oldContent = contentParent.getChildAt(0)
        }
        contentParent.removeView(oldContent)

        val loadingAndRetryLayout = LoadingAndRetryLayout(context)

        val lp = oldContent.layoutParams
        contentParent.addView(loadingAndRetryLayout, index, lp)
        loadingAndRetryLayout.setContentView(oldContent)
        setupLoadingLayout(listener, loadingAndRetryLayout)
        setupRetryLayout(listener, loadingAndRetryLayout)
        setupEmptyLayout(listener, loadingAndRetryLayout)
        //回调
        if (loadingAndRetryLayout.mRetryView != null) {
            listener.setRetryEvent(loadingAndRetryLayout.mRetryView)
        }
        if (loadingAndRetryLayout.mRetryView != null) {
            listener.setLoadingEvent(loadingAndRetryLayout.mLoadingView)
        }
        if (loadingAndRetryLayout.mRetryView != null) {
            listener.setEmptyEvent(loadingAndRetryLayout.mEmptyView)
        }
        mLoadingAndRetryLayout = loadingAndRetryLayout
        showLoading()
    }

    private fun setupLoadingLayout(
        listener: OnLoadingAndRetryListener,
        loadingAndRetryLayout: LoadingAndRetryLayout
    ) {
        if (listener.isSetLoadingLayout()) {
            val layoutId = listener.generateLoadingLayoutId()
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setLoadingView(layoutId)
            } else {
                loadingAndRetryLayout.setLoadingView(listener.generateRetryLayout()!!)
            }
        } else {
            if (BASE_RETRY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setLoadingView(BASE_LOADING_LAYOUT_ID)
        }
    }

    private fun setupRetryLayout(
        listener: OnLoadingAndRetryListener,
        loadingAndRetryLayout: LoadingAndRetryLayout
    ) {
        if (listener.isSetRetryLayout()) {
            val layoutId = listener.generateRetryLayoutId()
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setRetryView(layoutId)
            } else {
                loadingAndRetryLayout.setRetryView(listener.generateRetryLayout()!!)
            }
        } else {
            if (BASE_RETRY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setRetryView(BASE_RETRY_LAYOUT_ID)
        }
    }


    private fun setupEmptyLayout(
        listener: OnLoadingAndRetryListener,
        loadingAndRetryLayout: LoadingAndRetryLayout
    ) {
        if (listener.isSetEmptyLayout()) {
            val layoutId = listener.generateEmptyLayoutId()
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setEmptyView(layoutId)
            } else {
                loadingAndRetryLayout.setEmptyView(listener.generateEmptyLayoutId()!!)
            }
        } else {
            if (BASE_RETRY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setEmptyView(BASE_EMPTY_LAYOUT_ID)
        }
    }


}
