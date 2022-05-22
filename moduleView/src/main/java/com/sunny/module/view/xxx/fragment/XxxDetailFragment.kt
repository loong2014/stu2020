package com.sunny.module.view.xxx.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter
import com.sunny.lib.common.base.BaseFragment
import com.sunny.lib.common.utils.ResUtils.getColor
import com.sunny.lib.common.view.SunnyTopBar
import com.sunny.module.view.R

/**
 * Created by zhangxin17 on 2020/11/19
 */
class XxxDetailFragment : BaseFragment() {
    private var rootLayout: View? = null

    //
    private var mTopBar: SunnyTopBar? = null

    //
    private var mTabLayout: SmartTabLayout? = null

    //
    private var mViewPager: ViewPager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootLayout = inflater.inflate(R.layout.view_fragment_xxx_detail, container, false)
        initView(rootLayout)
        return rootLayout
    }

    private fun initView(root: View?) {
        //
        mTopBar = root!!.findViewById(R.id.top_bar)
        initTopBar()

        //
        mTabLayout = root.findViewById(R.id.tab_layout)
        mViewPager = root.findViewById(R.id.view_page)
        initTabAndViewPager()
    }

    private fun initTopBar() {
        mTopBar!!.setMiddleName("详情页")
        mTopBar!!.setOnBackBtnClickListener { activity!!.finish() }
    }

    private fun initTabAndViewPager() {

        //
        val tabNames = arrayOf("世界", "亚洲", "中国", "北京")
        val creator = FragmentPagerItems.with(activity)
        for (i in tabNames.indices) {
            val args = Bundle()
            args.putInt("detail_info_id", i)
            args.putString("detail_info_name", tabNames[i])
            creator.add(
                tabNames[i],
                XxxDetailInfoFragment::class.java,
                args
            )
        }

        //
        val tabAdapter = FragmentStatePagerItemAdapter(childFragmentManager, creator.create())
        mViewPager!!.adapter = tabAdapter
        mViewPager!!.offscreenPageLimit = 1

        //
        mTabLayout!!.setViewPager(mViewPager)
        mTabLayout!!.setOnPageChangeListener(object : OnPageChangeListener {
            private var mLastSelectedPod = -1
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val view = mTabLayout!!.getTabAt(position)
                if (view is TextView) {
                    val textView = view
                    textView.setTextColor(getColor(R.color.colorRed))
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                }
                if (mLastSelectedPod != -1) {
                    val preView = mTabLayout!!.getTabAt(mLastSelectedPod)
                    if (preView is TextView) {
                        val textView = preView
                        textView.setTextColor(getColor(R.color.black_20))
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
                    }
                }
                mLastSelectedPod = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        //
        mViewPager!!.currentItem = 2
    }
}