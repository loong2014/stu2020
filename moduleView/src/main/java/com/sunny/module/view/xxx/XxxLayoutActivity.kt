package com.sunny.module.view.xxx

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.ui.SunnyViewPage
import com.sunny.module.view.R
import timber.log.Timber

/**
 * Created by zhangxin17 on 2020/11/19
 */
@Route(path = RouterConstant.View.PageXxx)
class XxxLayoutActivity : BaseActivity() {

    private lateinit var mTabLayout: TabLayout

    private var mViewPager: SunnyViewPage? = null
    private var mPagerAdapter: XxxFragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        SystemBarTintManager.translucentStatus(this)
        setContentView(R.layout.view_act_xxxlayout)

        initView()
    }

    private fun initView() {
        mTabLayout = findViewById(R.id.tab_layout)
        mViewPager = findViewById(R.id.view_pager)

        initToolBar()
        initViewPage()

        mViewPager?.currentItem = DefPageIndex
    }

    private fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        toolbar.visibility = View.VISIBLE
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

        //
        XxxPageList.forEach {
            mTabLayout.addTab(mTabLayout.newTab().setText(it.title))
        }

        mTabLayout.addOnTabSelectedListener(object :
            TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            override fun onTabReselected(tab: TabLayout.Tab) {
                mPagerAdapter?.getXxxPage(tab.position)?.let {
                    Timber.i("onTabReselected :${it.title}")
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                super.onTabSelected(tab)
                mPagerAdapter?.getXxxPage(tab.position)?.let {
                    Timber.i("onTabSelected :${it.title}")
                }
            }
        })
    }

    private fun initViewPage() {
        mPagerAdapter = XxxFragmentAdapter(supportFragmentManager)

        val pageAdapter = mPagerAdapter ?: return

        mViewPager?.adapter = mPagerAdapter
        mViewPager?.offscreenPageLimit = pageAdapter.count
        mViewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
    }
}