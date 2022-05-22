package com.sunny.module.view.xxx

import androidx.fragment.app.Fragment
import com.sunny.module.view.xxx.coordinator.XxxCoordinatorFragment
import com.sunny.module.view.xxx.fragment.*

val XxxPageList = arrayOf(
    XxxPage(
        "详情",
        XxxDetailFragment::class.java
    ),
    XxxPage(
        "侧滑",
        XxxDrawerFragment::class.java
    ),
    XxxPage(
        "协调",
        XxxCoordinatorFragment::class.java
    ),
    XxxPage(
        "运动",
        XxxMotionFragment::class.java
    ),
    XxxPage(
        "折叠",
        XxxExpandableFragment::class.java
    ),
    XxxPage(
        "Flexbox",
        XxxFlexboxFragment::class.java
    ),
)
const val DefPageIndex = 2

class XxxPage(val title: String, private val fragmentClass: Class<*>) {
    var mFragment: Fragment? = null
    val fragment: Fragment?
        get() {
            if (mFragment == null) {
                mFragment = newFragment()
            }
            return mFragment
        }

    fun updateFragment(fragment: Fragment?) {
        mFragment = fragment
    }

    private fun newFragment(): Fragment? {
        try {
            val get = fragmentClass.getMethod("newInstance")
            return get.invoke(fragmentClass) as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                return fragmentClass.getDeclaredConstructor().newInstance() as Fragment
            } catch (e1: Exception) {
                e1.printStackTrace()
            }
        }
        assert(false) { "Failed to create instance of " + fragmentClass.name }
        return null
    }
}