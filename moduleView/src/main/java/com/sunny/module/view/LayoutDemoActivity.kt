package com.sunny.module.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.utils.SunNumberUtils
import com.sunny.lib.common.utils.showSnckbar
import com.sunny.lib.common.utils.showToast
import com.sunny.module.view.bar.SystemBarTintManager
import com.sunny.module.view.databinding.ViewActivityDemoBinding
import kotlinx.android.synthetic.main.view_activity_demo.*

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.View.PageDemo)
class LayoutDemoActivity : BaseActivity() {

    lateinit var mActBinding: ViewActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemBarTintManager.translucentStatus(this)

        mActBinding = DataBindingUtil.setContentView(this, R.layout.view_activity_demo)

        SystemBarTintManager.setViewPaddingTopStatusBar(this, toolBar)

        setSupportActionBar(toolBar)

        initView()
    }

    private fun initView() {
        initToolBar()

        initRecyclerView()
    }

    private fun initToolBar() {
        //
        toolBar.title = "模块：控件"
        toolBar.subtitle = "各种控件的使用实战"

        // 更改ToolBar左侧按钮的两种方式
        // 1针对toolBar
//        toolBar.setNavigationIcon(R.drawable.ic_arrow_left_black)
//        // 默认点击触发finish()，可通过下面方法更改click处理
//        toolBar.setNavigationOnClickListener {
//            showToast("重写click事件")
//        }

        // 2针对actionBar
        supportActionBar?.let {
            // 打开toolbar最左侧的默认按钮(Home按钮)，默认图标是一个返回箭头，点击后返回上一个activity
            it.setDisplayHomeAsUpEnabled(true)
            // 更改Home按钮的图片
//            it.setHomeAsUpIndicator(R.drawable.ic_gift)
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter = LayoutDemoAdapter(this, buildDataList())
        recyclerView.adapter = adapter

        adapter.setItemClickListener(object : DemoItemClickListener {
            override fun onItemClick(model: LayoutDemoModel) {
                ARouter.getInstance().build(model.jumpPath).navigation()
            }
        })
    }

    private fun buildDataList(): List<LayoutDemoModel> {
        val list = mutableListOf<LayoutDemoModel>()
        list.add(LayoutDemoModel(name = "XXXLayout", jumpPath = RouterConstant.View.PageXxx))
        list.add(LayoutDemoModel(name = "DrawerLayout", jumpPath = RouterConstant.View.PageDrawer))
        list.add(
            LayoutDemoModel(
                name = "CoordinatorLayout",
                jumpPath = RouterConstant.View.PageCoordinator
            )
        )
        list.add(LayoutDemoModel(name = "状态栏", jumpPath = RouterConstant.View.PageStatusBar))
        list.add(LayoutDemoModel(name = "BlackBg", jumpPath = RouterConstant.View.PageBarBlack))
        list.add(LayoutDemoModel(name = "CardView", jumpPath = RouterConstant.View.PageCardView))
        list.add(
            LayoutDemoModel(
                name = "TitleUpdate",
                jumpPath = RouterConstant.View.PageTitleUpdate
            )
        )
        list.add(
            LayoutDemoModel(
                name = "RemoteViewsService",
                jumpPath = RouterConstant.View.PageRemoteViewsService
            )
        )
        list.add(
            LayoutDemoModel(
                name = "RemoteViewsClient",
                jumpPath = RouterConstant.View.PageRemoteViewsClient
            )
        )
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                showToast("点击了ToolBar的默认Home按钮")

//                showSnackBar("你确定要点！！！")

                startActivity(Intent(getActivity(), LayoutCommonActivity::class.java))
            }
            else -> {
                showToast(item.title as String)
            }
        }
        return true
    }

    private fun showSnackBar(tip: String) {
        /**
         * 第一个参数只要是当前布局的一个view就行
         */
//        Snackbar.make(toolBar, tip, Snackbar.LENGTH_SHORT)
//                .setAction("Yes") {
//                    showToast("点击了ToolBar的默认Home按钮")
//                }
//                .show()

        toolBar.showSnckbar(tip, "Yes") {

            val a = 10
            val b = 20
            val c = 30

            val max = SunNumberUtils.max(a, b, c)
            "ddd".showToast()
            finish()
        }
    }

}