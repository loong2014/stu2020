package com.sunny.module.view.layout

import android.os.Bundle
import android.view.View
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
import com.sunny.lib.common.utils.HandlerUtils
import com.sunny.module.view.R
import com.sunny.module.view.bar.SystemBarTintManager
import kotlinx.android.synthetic.main.view_act_layout_title_update.*

/**
 * Created by zhangxin17 on 2020/11/15
 */
@Route(path = RouterConstant.View.PageTitleUpdate)
class TitleUpdateLayoutActivity : BaseActivity() {

    companion object {
        const val TAG = "TitleUpdate-Act"
    }

    @JvmField
    @Autowired(name = "titleName")
    var titleName: String? = null

    lateinit var mAdapter: CityAdapter

    private var scrollDy = 0 //内容滑动距离，< 0 =顶部内容超出屏幕距离

    private val TopBarH: Int = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarTintManager.translucentStatus(this)

        setContentView(R.layout.view_act_layout_title_update)

//        SystemBarTintManager.setViewPaddingTopStatusBar(this,appBarLayout)
        ARouter.getInstance().inject(this)

        initView()
    }

    private fun initView() {

        initRecyclerView()

        initFloatingActionButton()
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

            /**
             * dy > 0 上滑
             * dy < 0 下滑
             */
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                scrollDy -= dy

                HandlerUtils.uiHandler.post {
                    updateTopBarLayout()
                }

                val stickView = recyclerView.findChildViewUnder(0f, 0f)

                SunLog.i(TAG, "onScrolled $stickView  $dx , $dy")
            }
        })
    }

    private fun updateTopBarLayout() {

        val topY = -scrollDy;
        SunLog.i(TAG, "updateTopBarLayout topY :$topY")

        /**
         * 第一个item正在滑出屏幕顶部
         */
        if (topY < TopBarH) {
            val alpha = 255 * 255 / TopBarH

            // 改变标题栏透明度 全透->不透
            layout_top.background.alpha = alpha

            // 隐藏标题栏内容
            if (layout_opt.visibility != View.GONE) {
                layout_opt.visibility = View.GONE
            }
        }

        /**
         * 第一个item完全滑出屏幕顶部
         */
        else {
            layout_top.background.alpha = 255

            // 显示标题栏内容
            if (layout_opt.visibility != View.VISIBLE) {
                layout_opt.visibility = View.VISIBLE
            }
        }

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