package com.sunny.module.view.xxx.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunny.lib.common.base.BaseFragment
import com.sunny.lib.common.city.CityAdapter
import com.sunny.lib.common.city.buildCityData
import com.sunny.module.view.R

/**
 * Created by zhangxin17 on 2020/11/19
 */
class XxxDetailDataListFragment : BaseFragment() {
    private var mPageId = -1

    //
    private var hasInit = false
    private var mRootLayout: View? = null

    //
    private var mDataRecyclerView: RecyclerView? = null
    private var mDataAdapter: CityAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //
        if (savedInstanceState != null) {
            mPageId = savedInstanceState.getInt("detail_data_id")
        }

        //
        val bundle = arguments
        if (bundle != null) {
            mPageId = bundle.getInt("detail_data_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootLayout =
            inflater.inflate(R.layout.view_fragment_xxx_detail_data_list, container, false)
        return mRootLayout
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView() {
        if (hasInit) {
            return
        }
        hasInit = true

        //

        mDataRecyclerView = mRootLayout!!.findViewById(R.id.recyclerView)
        mDataRecyclerView?.apply {
            layoutManager = GridLayoutManager(mActivity, 1)
            setHasFixedSize(true)
            mDataAdapter = CityAdapter(mActivity, buildCityData(mPageId + 1))
            adapter = mDataAdapter
        }
    }

    companion object {
        private const val TAG = "Zhang-Detail-Info"
    }
}