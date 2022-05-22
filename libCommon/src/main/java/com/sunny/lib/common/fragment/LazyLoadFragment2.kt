package com.sunny.lib.common.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sunny.lib.common.base.BaseFragment

// 扩展方法
fun ViewPager.bindFragment(
    fm: FragmentManager,
    fragments: List<Fragment>,
    pageTitles: List<String>? = null,
    behavior: Int = 0 // 当为1的时候就不会回调 setUserVisibleHint 方法了，我们直接监听 OnResume 即可。
): ViewPager {
    offscreenPageLimit = fragments.size - 1
    adapter = object : FragmentStatePagerAdapter(fm, behavior) {
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pageTitles?.get(position)
        }
    }

    return this
}

open class LazyLoadFragment2 : BaseFragment() {

    private var isLoadData = false//数据是否加载

    private fun initData() {
        isLoadData = true
    }

    override fun onResume() {
        super.onResume()
        if (!isLoadData) initData()
    }
}