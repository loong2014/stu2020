package com.sunny.family.layout

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.family.R
import com.sunny.family.city.CityActivity
import com.sunny.family.city.CityItemModel
import com.sunny.family.city.adapter.CityAdapter
import com.sunny.family.city.buildCityData
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_coordinator.*

/**
 * Created by zhangxin17 on 2020/11/15
 */
@Route(path = RouterConstant.PageCoordinator)
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
        setContentView(R.layout.act_coordinator)
        ARouter.getInstance().inject(this)

        initView()
    }

    private fun initView() {
        initToolBar()

        initRecyclerView()
    }

    private fun initToolBar() {
        mToolBar.title = "协调布局"
//        mToolBar.subtitle = "对title的补充"

        mToolBar.setLogo(R.drawable.icon_gift)
//        mToolBar.logoDescription = "对logo的注释"

//        mToolBar.setCollapseIcon(R.drawable.icon_series_history_play)

//        mToolBar.overflowIcon = ResUtils.getDrawable(R.drawable.ic_action_menu)

        mToolBar.setNavigationIcon(R.drawable.ic_back_btn)
        mToolBar.setNavigationOnClickListener {
            doExitActivity()
        }

//        setSupportActionBar(mToolBar)
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

                SunLog.i(CityActivity.TAG, "onScrolled $stickView")
            }
        })

    }

}