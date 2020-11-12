package com.sunny.family.layout

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.family.R
import com.sunny.family.city.CityItemModel
import com.sunny.family.city.ItemViewTypeTip
import com.sunny.family.city.adapter.CityAdapter
import com.sunny.family.city.buildCityData
import com.sunny.family.city.buildCityTipData
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.ResUtils
import kotlinx.android.synthetic.main.act_drawer.*

/**
 * Created by zhangxin17 on 2020/11/16
 */
@Route(path = RouterConstant.PageDrawer)
class DrawerLayoutActivity : BaseActivity() {

    private lateinit var mAdapter: CityAdapter
    private lateinit var mDrawerAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_drawer)

        initView()
    }

    private fun initView() {
        initTopBar()

        initRecyclerView()

        initDrawerLayout()
    }

    private fun initTopBar() {
        mTopBar.setMiddleName("抽屉布局")

        mTopBar.setOnBackBtnClickListener(View.OnClickListener { doExitActivity() })

        mTopBar.setLogoIcon(R.drawable.icon_gift)
        mTopBar.setOnLogoIconClickListener(View.OnClickListener {
            showDrawer()
        })
    }

    private fun initRecyclerView() {
        mRecyclerView.layoutManager = GridLayoutManager(this, 1)
        mRecyclerView.setHasFixedSize(true)

        mAdapter = CityAdapter(buildCityData())
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, _, position ->

            val itemModel: CityItemModel = adapter.getItem(position) as CityItemModel
            if (itemModel.isCity()) {
                showToast(itemModel.showName)
            }
        }
    }

    private fun initDrawerLayout() {
        mDrawerRecyclerView.layoutManager = GridLayoutManager(this, 1)
        mDrawerRecyclerView.setHasFixedSize(true)
        mDrawerRecyclerView.addItemDecoration(DrawerItemDecoration())

        mDrawerAdapter = CityAdapter(buildCityTipData())
        mDrawerRecyclerView.adapter = mDrawerAdapter

        mDrawerAdapter.setOnItemClickListener { adapter, _, position ->
            val itemModel: CityItemModel = adapter.getItem(position) as CityItemModel
            if (itemModel.isTip()) {
                showToast(itemModel.showName)
                dealDrawerItemClick(itemModel)
            }
            hideDrawer()
        }
    }

    private fun showDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun hideDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun dealDrawerItemClick(cityItem: CityItemModel) {
        var pos = -1
        for (item in mAdapter.data) {
            if (ItemViewTypeTip == item.itemType) {
                val itemModel = item as CityItemModel
                if (itemModel.dataType == cityItem.dataType) {
                    pos = mAdapter.getItemPosition(item)
                    if (pos > 13) {
                        if (pos > mAdapter.itemCount - 13) {
                            pos = mAdapter.itemCount - 1
                        }
                    }
                    break
                }
            }
        }
        if (pos != -1) {
            mAdapter.recyclerView.scrollToPosition(pos)
        }
    }

}

class DrawerItemDecoration : RecyclerView.ItemDecoration {

    private val offset: Int = ResUtils.getDimensionPixelSize(R.dimen.dp_4)

    constructor()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = offset
        outRect.bottom = offset
    }
}