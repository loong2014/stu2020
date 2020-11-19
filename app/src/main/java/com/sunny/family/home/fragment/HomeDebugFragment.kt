package com.sunny.family.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.family.R
import com.sunny.family.home.HomeItemDecoration
import com.sunny.family.home.HomeItemModel
import com.sunny.family.home.adapter.HomeAdapter
import com.sunny.lib.base.BaseFragment
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.fragment_home_debug.*

class HomeDebugFragment : BaseFragment() {

    companion object {
        const val TAG = "HomeMainFragment"
    }

    var hasInit: Boolean = false
    lateinit var rootLayout: View
    lateinit var mAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SunLog.i(TAG, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        SunLog.i(TAG, "onCreateView")
        rootLayout = inflater.inflate(R.layout.fragment_home_debug, container, false)
        return rootLayout
    }

    override fun onStart() {
        SunLog.i(TAG, "onStart")
        super.onStart()
        initView()
    }

    private fun initView() {
        if (hasInit) {
            return
        }
        hasInit = true
        mFragmentName.text = "主页"
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerview.layoutManager = GridLayoutManager(activity, 3)
        mRecyclerview.setHasFixedSize(true)
        mRecyclerview.addItemDecoration(HomeItemDecoration())

        mAdapter = HomeAdapter(buildHomeData())
        mRecyclerview.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, _, position ->
            val itemModel: HomeItemModel = adapter.getItem(position) as HomeItemModel
            ARouter.getInstance().build(itemModel.routePath).navigation()
        }
    }
}

private fun buildHomeData(): MutableList<HomeItemModel> {
    val list = mutableListOf<HomeItemModel>()
    list.add(HomeItemModel(showName = "详情页", routePath = RouterConstant.PageDetail))
    list.add(HomeItemModel(showName = "主页", routePath = RouterConstant.PageHome))
    list.add(HomeItemModel(showName = "城市列表", routePath = RouterConstant.PageCity))
    list.add(HomeItemModel(showName = "折叠列表", routePath = RouterConstant.PageExpandable))
    list.add(HomeItemModel(showName = "协调布局", routePath = RouterConstant.PageCoordinator))
    list.add(HomeItemModel(showName = "运动布局", routePath = RouterConstant.PageMotion))
    list.add(HomeItemModel(showName = "侧滑布局", routePath = RouterConstant.PageDrawer))
    list.add(HomeItemModel(showName = "下拉刷新布局", routePath = RouterConstant.PageSwipeRefresh))
    list.add(HomeItemModel(showName = "xxx布局", routePath = RouterConstant.PageXxxLayout))
    list.add(HomeItemModel(showName = "loading", routePath = RouterConstant.PageLoading))
    return list
}