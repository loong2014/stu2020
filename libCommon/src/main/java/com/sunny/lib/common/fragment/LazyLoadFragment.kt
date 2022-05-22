package com.sunny.lib.common.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sunny.lib.common.base.BaseFragment


// 扩展方法
fun ViewPager2.bindFragment(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    fragments: List<Fragment>
): ViewPager2 {
    offscreenPageLimit = fragments.size - 1

    adapter = object : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    return this
}

abstract class LazyLoadFragment : BaseFragment() {

    private var isViewCreated = false//布局是否被创建
    private var isLoadData = false//数据是否加载
    private var isFirstVisible = true//是否第一次可见

    abstract fun onLazyInitData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isFragmentVisible(this) && isAdded) {
            if (parentFragment == null || isFragmentVisible(parentFragment)) {
                onLazyInitData()
                isLoadData = true
                if (isFirstVisible) isFirstVisible = false
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isFragmentVisible(this) && !isLoadData && isViewCreated && isAdded) {
            onLazyInitData()
            isLoadData = true
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //onHiddenChanged调用在Resumed之前，所以此时可能fragment被add, 但还没resumed
        if (!hidden && !isResumed) return
        //使用hide和show时，fragment的所有生命周期方法都不会调用，除了onHiddenChanged（）
        if (!hidden && isFirstVisible && isAdded) {
            onLazyInitData()
            isFirstVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isViewCreated = false
        isLoadData = false
        isFirstVisible = true
    }

    /**
     * 当前Fragment是否对用户是否可见
     */
    private fun isFragmentVisible(fragment: Fragment?): Boolean {
        return fragment?.isHidden == false && fragment.userVisibleHint
    }
}