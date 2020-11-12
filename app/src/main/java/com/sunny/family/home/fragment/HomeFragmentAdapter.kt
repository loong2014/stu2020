package com.sunny.family.home.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sunny.family.home.HomeFragmentModel

/**
 * Created by zhangxin17 on 2020/11/16
 */
class HomeFragmentAdapter : FragmentStateAdapter {

    var mFragments: MutableList<HomeFragmentModel>

    constructor(fragmentActivity: FragmentActivity, fragments: MutableList<HomeFragmentModel>) : super(fragmentActivity) {
        mFragments = fragments
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position].fragment
    }

    fun getItem(position: Int): HomeFragmentModel {
        return mFragments[position]
    }

}