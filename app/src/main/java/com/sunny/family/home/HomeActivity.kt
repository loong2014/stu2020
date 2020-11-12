package com.sunny.family.home

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.sunny.family.R
import com.sunny.family.home.fragment.HomeDebugFragment
import com.sunny.family.home.fragment.HomeFragmentAdapter
import com.sunny.family.home.fragment.HomeMainFragment
import com.sunny.family.home.fragment.HomeUserFragment
import com.sunny.family.livedata.LiveDataFragment
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_home.*

class HomeActivity : BaseActivity() {

    companion object {
        const val TAG = "HomeActivity"
    }

    lateinit var mHomeAdapter: HomeFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_home)

        initView()
    }

    private fun initView() {
        initTopBar()

        initPageView()

        initClick()
    }

    private fun initTopBar() {
        mTopBar.setMiddleName("主页")
        mTopBar.setOnBackBtnClickListener(View.OnClickListener {
            doExitActivity()
        })
    }

    private fun initPageView() {
        mHomeAdapter = HomeFragmentAdapter(this, buildHomeFragmentList())
        mViewPager2.adapter = mHomeAdapter

        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback)
        mViewPager2.currentItem = 2
    }

    private val mPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val itemModel = mHomeAdapter.getItem(position)
            SunLog.i(TAG, "onPageSelected   position :$position , name :${itemModel.name}")

            mTopBar.setMiddleName(itemModel.name)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            SunLog.i(TAG, "onPageScrolled  position: $position , offset: $positionOffset , offsetPixels :$positionOffsetPixels")
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            SunLog.i(TAG, "onPageScrollStateChanged  state :$state")
        }
    }

    private fun initClick() {
        mBottomBtn1.text = "个人"
        mBottomBtn1.setOnClickListener {
            mViewPager2.setCurrentItem(0, true)
        }

        mBottomBtn2.text = "主页"
        mBottomBtn2.setOnClickListener {
            mViewPager2.setCurrentItem(1, true)
        }

        mBottomBtn3.text = "测试"
        mBottomBtn3.setOnClickListener {
            mViewPager2.setCurrentItem(2, true)
        }

        mBottomBtn4.text = "其它"
        mBottomBtn4.setOnClickListener {
            mViewPager2.setCurrentItem(3, true)
        }

        mBottomBtn5.text = "其它"
        mBottomBtn5.setOnClickListener {
            mViewPager2.setCurrentItem(4, true)
        }
    }

    private fun buildHomeFragmentList(): MutableList<HomeFragmentModel> {
        val list = mutableListOf<HomeFragmentModel>()
        list.add(HomeFragmentModel("个人", "1", HomeUserFragment()))
        list.add(HomeFragmentModel("主页", "2", HomeMainFragment()))
        list.add(HomeFragmentModel("测试", "3", HomeDebugFragment()))
        list.add(HomeFragmentModel("其它", "3", LiveDataFragment()))
        list.add(HomeFragmentModel("其它", "4", LiveDataFragment()))
        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewPager2?.unregisterOnPageChangeCallback(mPageChangeCallback)
    }
}
