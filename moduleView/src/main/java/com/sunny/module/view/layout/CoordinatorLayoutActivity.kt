package com.sunny.module.view.layout

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.snackbar.Snackbar
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.city.CityAdapter
import com.sunny.lib.common.city.CityItemModel
import com.sunny.lib.common.city.buildCityData
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.view.R
import com.sunny.module.view.bar.SystemBarTintManager
import kotlinx.android.synthetic.main.view_act_layout_coordinator.*

/**
 * Created by zhangxin17 on 2020/11/15
 */
@Route(path = RouterConstant.View.PageCoordinator)
class CoordinatorLayoutActivity : BaseActivity() {

    companion object {
        const val TAG = "City-Act"
    }

    @JvmField
    @Autowired(name = "titleName")
    var titleName: String? = null

    lateinit var mAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarTintManager.translucentStatus(this)

        setContentView(R.layout.view_act_layout_coordinator)

//        SystemBarTintManager.setViewPaddingTopStatusBar(this,appBarLayout)
        ARouter.getInstance().inject(this)

        initView()
    }

    private fun initView() {
        initToolBar()

        initRecyclerView()

        initFloatingActionButton()
    }

    private fun initToolBar() {
        mToolBar.title = "协调布局"
//        mToolBar.subtitle = "对title的补充"

        mToolBar.setLogo(R.drawable.ic_gift)
//        mToolBar.logoDescription = "对logo的注释"

//        mToolBar.setCollapseIcon(R.drawable.icon_series_history_play)

//        mToolBar.overflowIcon = ResUtils.getDrawable(R.drawable.ic_action_menu)

        mToolBar.setNavigationIcon(R.drawable.ic_arrow_left_black)
        mToolBar.setNavigationOnClickListener {
            showSnackBar("确定退出么？")
        }

        mToolBar.logo

//        setSupportActionBar(mToolBar)
    }

    private fun initFloatingActionButton() {
        faBtn.setOnClickListener {
            showToast("点击了悬浮窗")
        }
    }

    private fun initRecyclerView() {
        mRecyclerView.layoutManager = GridLayoutManager(this, 1)
        mRecyclerView.setHasFixedSize(true)

        mAdapter = CityAdapter(buildCityData())
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->

            val itemModel: CityItemModel = adapter.getItem(position) as CityItemModel
            if (itemModel.isCity()) {
                showToast(itemModel.showName)
            }
        }
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val stickView = recyclerView.findChildViewUnder(0f, 0f)

                SunLog.i(TAG, "onScrolled $stickView")
            }
        })

    }

    private fun showSnackBar(tip: String) {
        /**
         * 第一个参数只要是当前布局的一个view就行
         */
        Snackbar.make(faBtn, tip, Snackbar.LENGTH_SHORT)
                .setAction("Yes") {
                    doExitActivity()
                }
                .show()
    }
}