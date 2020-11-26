package com.sunny.family.list

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter
import com.sunny.family.R
import com.sunny.family.list.fragment.ListViewFragment
import com.sunny.family.list.fragment.RecyclerViewFragment
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.ResUtils
import kotlinx.android.synthetic.main.act_list.*

/**
 * Created by zhangxin17 on 2020/11/26
 */
@Route(path = RouterConstant.PageList)
class ListActivity : BaseActivity() {

    private val tabNames = listOf<String>("ListView",
            "RecyclerView")

    private val tabFragments: List<Class<out Fragment?>> = listOf(ListViewFragment::class.java,
            RecyclerViewFragment::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_list)

        initView()
    }

    private fun initView() {
        //
        top_bar.setMiddleName("列表页")

        //
        val creator = FragmentPagerItems.with(this).apply {
            for (i in tabNames.indices) {
                add(tabNames[i], tabFragments[i])
            }
        }
        val tabAdapter = FragmentStatePagerItemAdapter(supportFragmentManager, creator.create())

        //
        view_page.adapter = tabAdapter
        view_page.offscreenPageLimit = 1

        //
        tab_layout.setViewPager(view_page)
        tab_layout.setOnPageChangeListener(mPageChangeListener)

        //
        view_page.currentItem = 0
        mPageChangeListener.onPageSelected(view_page.currentItem)
    }

    private val mPageChangeListener = object : ViewPager.OnPageChangeListener {

        var mLastSelectedPod = -1

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            //
            tab_layout.getTabAt(position)?.run {
                if (this is TextView) {
                    setTextColor(ResUtils.getColor(R.color.colorRed))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
            }

            //
            tab_layout.getTabAt(mLastSelectedPod)?.run {
                if (this is TextView) {
                    setTextColor(ResUtils.getColor(R.color.black_20))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }
            }

            mLastSelectedPod = position
            top_bar.setMiddleNameSuffix(tabNames[position])
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

}