package com.sunny.module.view.xxx

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * Created by zhangxin17 on 2020/11/19
 */
class XxxFragmentAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm) {
//    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mCurrentFragment: Fragment? = null
    private var mFragmentLifecycleListener: FragmentLifecycleListener? = null


    override fun getCount(): Int {
        return XxxPageList.size
    }

    override fun getItem(position: Int): Fragment {
        val fragment = XxxPageList[position].fragment
        return fragment ?: Fragment()
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        super.setPrimaryItem(container, position, obj)
        mCurrentFragment = obj as Fragment
        if (mFragmentLifecycleListener == null && mCurrentFragment != null) {

            mFragmentLifecycleListener = FragmentLifecycleListener(fm)
        }
    }

    fun getXxxPage(pos: Int): XxxPage {
        return XxxPageList[pos]
    }

    fun getXxxPageList() = XxxPageList

    fun getDefPageIndex(): Int {
        return DefPageIndex
    }

    fun getCurrentFragment(): Fragment? {
        return mCurrentFragment
    }

    inner class FragmentLifecycleListener(fm: FragmentManager) :
        FragmentManager.FragmentLifecycleCallbacks() {

        init {
            fm.registerFragmentLifecycleCallbacks(this, false)
        }

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            if (f == mCurrentFragment) {
                mCurrentFragment = null
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }
}