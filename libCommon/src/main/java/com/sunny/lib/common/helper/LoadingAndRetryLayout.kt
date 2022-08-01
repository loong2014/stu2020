package com.sunny.lib.common.helper

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

class LoadingAndRetryLayout(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context) {
    var mLoadingView: View? = null
    var mRetryView: View? = null
    var mContentView: View? = null
    var mEmptyView: View? = null
    var mInflater: LayoutInflater = LayoutInflater.from(context)
    private val isMainThread: Boolean get() = Looper.myLooper() === Looper.getMainLooper()

    companion object {
        private val TAG = LoadingAndRetryLayout::class.java.simpleName
    }


    init {

    }


    fun showLoading(): View? {
        if (isMainThread) {
            showView(mLoadingView)
        } else {
            post {
                showView(mLoadingView)
            }
        }
        return mLoadingView
    }

    fun showRetry(): View? {
        if (isMainThread) {
            showView(mRetryView)
        } else {
            post {
                showView(mRetryView)
            }
        }
        return mRetryView
    }


    fun showContent(): View? {
        if (isMainThread) {
            showView(mContentView)
        } else {
            post {
                showView(mContentView)
            }
        }
        return mContentView
    }

    fun showEmpty(): View? {
        if (isMainThread) {
            showView(mEmptyView)
        } else {
            post {
                showView(mEmptyView)
            }
        }
        return mEmptyView
    }

    fun showView(view: View?) {
        if (view == null) return
        when (view) {
            mLoadingView -> {
                mLoadingView?.visibility = View.VISIBLE
                mRetryView?.visibility = View.GONE
                mContentView?.visibility = View.GONE
                mEmptyView?.visibility = View.GONE
            }
            mRetryView -> {
                mRetryView?.visibility = View.VISIBLE
                mLoadingView?.visibility = View.GONE
                mContentView?.visibility = View.GONE
                mEmptyView?.visibility = View.GONE
            }
            mContentView -> {
                mContentView?.visibility = View.VISIBLE
                mRetryView?.visibility = View.GONE
                mLoadingView?.visibility = View.GONE
                mEmptyView?.visibility = View.GONE
            }
            mEmptyView -> {
                mEmptyView?.visibility = View.VISIBLE
                mRetryView?.visibility = View.GONE
                mContentView?.visibility = View.GONE
                mLoadingView?.visibility = View.GONE
            }
        }
    }

    fun setContentView(layoutId: Int): View? {
        return setContentView(mInflater.inflate(layoutId, this, false))
    }

    fun setContentView(view: View): View? {
        val contentView = mContentView
        if (contentView != null) {
            Log.w(TAG, "you have already set a content view and would be instead of this new one")
        }
        removeView(contentView)
        addView(view)
        mContentView = view
        return mContentView
    }

    fun setLoadingView(layoutId: Int): View? {
        return setLoadingView(mInflater.inflate(layoutId, this, false))
    }

    fun setLoadingView(view: View): View? {
        val loadingView = mLoadingView
        if (loadingView != null) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one")
        }
        removeView(loadingView)
        addView(view)
        mLoadingView = view
        return mLoadingView
    }

    fun setEmptyView(layoutId: Int): View? {
        return setEmptyView(mInflater.inflate(layoutId, this, false))
    }

    fun setEmptyView(view: View): View? {
        val emptyView = mEmptyView;
        if (emptyView != null) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one")
        }
        removeView(emptyView)
        addView(view)
        mEmptyView = view
        return mEmptyView
    }

    fun setRetryView(layoutId: Int): View? {
        return setRetryView(mInflater.inflate(layoutId, this, false))
    }

    fun setRetryView(view: View): View? {
        val retryView = mRetryView;
        if (retryView != null) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one")
        }
        removeView(retryView)
        addView(view)
        mRetryView = view
        return retryView
    }


}