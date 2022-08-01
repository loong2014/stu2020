package com.sunny.lib.common.base

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunny.lib.common.R
import com.sunny.lib.common.dialog.AppLoadingDialogFragment
import com.sunny.lib.common.helper.LoadingAndRetryManager
import com.sunny.lib.common.helper.OnLoadingAndRetryListener
import com.sunny.lib.common.mvvm.CommonActionEvent
import com.sunny.lib.common.mvvm.isMainThread
import com.sunny.lib.common.utils.SunToast
import com.sunny.lib.common.vm.BaseViewModel

abstract class BaseLogicFragment<T : ViewDataBinding> : Fragment() {
    companion object {
        private const val REQUEST_ID_LOADING = 1123
    }

    private var mRootView: View? = null
        private set
    private var _binding: T? = null
    protected val mBinding get() = _binding!!


    protected val mHandler by lazy {
        FragmentHandler(this)
    }
    private var mAppLoadingDialogFragment: AppLoadingDialogFragment? = null
    protected var mLoadViewManager: LoadingAndRetryManager? = null

    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null
    protected var mActivity: FragmentActivity? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    /**
     * 必须在onCreateView之后调用
     */
    protected fun <T : ViewModel> getFragmentScopeViewModel(modelClass: Class<T>): T {
        if (mFragmentProvider == null) {
            mFragmentProvider = ViewModelProvider(this)
        }
        return mFragmentProvider!!.get(modelClass)
    }

    /**
     * 必须在onAttach之后调用
     */
    protected fun <T : ViewModel> getActivityScopeViewModel(modelClass: Class<T>): T {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(mActivity!!)
        }
        return mActivityProvider!!.get(modelClass)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView != null) {
            return mRootView
        }
        val layoutId: Int? = getContentView(savedInstanceState)
        if (layoutId != null && layoutId > 0) {
            if (useDataBinding()) {
                _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
                mBinding.lifecycleOwner = viewLifecycleOwner
                mRootView = mBinding.root
            } else {
                mRootView = inflater.inflate(layoutId, container, false)
            }
        }
        isViewCreated = true
        // 弥补第一次加载 loading 中...
        if (getUserVisibleHint()) {
            setUserVisibleHint(true)
        }
        return mRootView

    }

    abstract fun getViewModelList(): List<BaseViewModel>

    /**
     * 绑定控件监听器，以及viewModel常驻liveData事件，保证粘性事件返回修改UI不会空指针
     */
    open fun bindListener(savedInstanceState: Bundle?) {
        getViewModelList().forEach {
            it.actionLiveData.observe(this) { data ->
                when (data.action) {
                    CommonActionEvent.SHOW_LOADING -> {
                        showLoading(data.cancelled ?: true, data.isTrans ?: false)
                    }
                    CommonActionEvent.CANCEL_LOADING -> {
                        hideLoading()
                    }
                    CommonActionEvent.SHOW_LOADING_VIEW -> {
                        showLoadingView()
                    }
                    CommonActionEvent.SHOW_CONTENT -> {
                        showLoadSuccess()
                    }
                    CommonActionEvent.SHOW_EMPTY -> {
                        showEmptyView()
                    }
                    CommonActionEvent.SHOW_TOAST -> {
                        SunToast.show(data.message)
                    }
                }
            }
        }
    }

    open fun showLoading(cancelable: Boolean, isTrans: Boolean = true) {
        hideLoading()
        mAppLoadingDialogFragment = AppLoadingDialogFragment
            .newInstance(REQUEST_ID_LOADING, cancelable, isTrans)
        mAppLoadingDialogFragment?.setOnDismissListener {
            onLoadingDialogDismiss()
        }
        mAppLoadingDialogFragment?.showAllowingStateLoss(childFragmentManager)
    }


    open fun onLoadingDialogDismiss() {

    }

    open fun hideLoading() {
        mAppLoadingDialogFragment?.dismissAllowingStateLoss()
        mAppLoadingDialogFragment = null
    }

    open fun showLoadingView() {
        executeInMainThread {
            mLoadViewManager?.showLoading()
        }
    }

    open fun showLoadSuccess() {
        executeInMainThread { mLoadViewManager?.showContent() }
    }

    private var loadFailedView: ViewGroup? = null
    open fun showLoadFailedView() {
        executeInMainThread {
            loadFailedView = mLoadViewManager?.showRetry() as ViewGroup
        }
    }


    open fun showEmptyView() {
        executeInMainThread { mLoadViewManager?.showEmpty() }
    }


    protected fun executeInMainThread(block: () -> Unit) {
        if (isMainThread()) {
            block()
        } else {
            mHandler.post { block() }
        }
    }

    private var mViewCreated = false
    private var isViewCreated: Boolean = false
    private var currentVisibleState = false //当前可见状态

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(savedInstanceState)
        initView(view, savedInstanceState)
        bindListener(savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated) {
            if (!currentVisibleState && isVisibleToUser) {

                //加载数据
                dispatchUserVisibleHint(true)
            } else if (currentVisibleState && !isVisibleToUser) {

                // 页面从可见-切换到->不可见
                //
                dispatchUserVisibleHint(false)
            }

        }
    }

    private fun dispatchUserVisibleHint(isVisibleToUser: Boolean) {
        currentVisibleState = isVisibleToUser
        if (isVisibleToUser) {
            //可见,加载数据
            onFragmentLoad()
        } else {
            //停止加载数据
            onFragmentLoadStop()
        }
    }

    protected open fun onFragmentLoad() {}

    protected open fun onFragmentLoadStop() {}


    private var enterAnimation: Animation? = null
    private var exitAnimation: Animation? = null
    private var popEnterAnimation: Animation? = null
    private var popExitAnimation: Animation? = null
    protected open fun useFragmentAnimation(): Boolean {
        return false
    }


    override fun onResume() {
        super.onResume()
        // 从 activity 回来
        if (!currentVisibleState && userVisibleHint) {
//             页面从不可见-切换到->可见
            dispatchUserVisibleHint(true)
        }
    }


    override fun onPause() {
        super.onPause()
        //跳转到一个新的 activity
        if (currentVisibleState && userVisibleHint) {
            // 页面从可见-切换到->不可见
            dispatchUserVisibleHint(false)
        }
    }

    override fun onStop() {
        if (!isAdded || isDetached || mActivity == null || mActivity!!.isFinishing) {
            readyToResource()
        }
        super.onStop()
    }

    /**
     * 防内存泄漏的Handler类
     */
    class FragmentHandler(data: BaseLogicFragment<*>) : BaseHandler<BaseLogicFragment<*>?>(data) {
        override fun disposeMessage(msg: Message?) {}
    }

    open fun useDataBinding(): Boolean {
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        readyToResource()
        mViewCreated = false
//        hasStartDo = false
        mHandler.removeCallbacksAndMessages(null)
        mRootView = null
        _binding = null
    }

    //    private var hasStartDo = false
    private var isEnterAnimEnd = false
//    open fun startDo() {
//
//    }

    /**
     * 数据相关初始化
     */
    abstract fun initData(savedInstanceState: Bundle?)


    /**
     * view控件相关初始化
     */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    private fun initLoadingManager() {
        if (!isNeedLoadingView()) {
            return
        }
        mLoadViewManager =
            LoadingAndRetryManager.generate(getWrapperContainer(), getLoadingViewListener())
    }


    /**
     * 是否需要loading View
     */
    open fun isNeedLoadingView(): Boolean {
        return false
    }

    /**
     * 返回layout
     */
    abstract fun getContentView(savedInstanceState: Bundle?): Int?
    open fun getLoadingViewListener(): OnLoadingAndRetryListener {
        return object : OnLoadingAndRetryListener() {
            override fun generateEmptyLayoutId(): Int {
                return R.layout.ball_real_empty
            }

            override fun generateRetryLayoutId(): Int {
                return R.layout.ball_real_error
            }

            override fun generateLoadingLayoutId(): Int {
                return R.layout.ball_real_loading
            }

            override fun setEmptyEvent(emptyView: View?) {
                super.setEmptyEvent(emptyView)
                val tvEmpty = emptyView?.findViewById<TextView>(R.id.tv_empty_desc)
                emptyViewText()?.let {
                    tvEmpty?.text = it
                }
                val btnEmpty = emptyView?.findViewById<TextView>(R.id.tv_empty_button)
                btnEmpty?.setOnClickListener {
                    onClickRetry(btnEmpty)
                }
            }

            override fun setRetryEvent(retryView: View?) {
                val tvRetry = retryView?.findViewById<TextView>(R.id.tv_error_button)
                tvRetry?.setOnClickListener {
                    onClickRetry(retryView)
                }
            }
        }
    }

    open fun emptyViewText(): CharSequence? {
        return null
    }

    open fun onClickRetry(retryView: View) {
        // 重试
    }

    /**
     * 默认容器是contentView
     */
    open fun getWrapperContainer(): Any {
        return mRootView?.findViewById(getLoadingAreaResId()) ?: this
    }

    open fun getLoadingAreaResId(): Int {
        return 0
    }


    private fun readyToResource() {

        releaseResource()

    }

    open fun releaseResource() {}
}