package com.sunny.module.view.xxx.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.sunny.lib.base.utils.SunRandomTools
import com.sunny.lib.common.base.BaseFragment
import com.sunny.module.view.R
import com.sunny.module.view.xxx.adapter.StringAdapter
import com.sunny.module.view.xxx.adapter.StringModel

/**
 * Created by zhangxin17 on 2020/11/19
 */
class XxxFlexboxFragment : BaseFragment() {

    var mRecyclerView: RecyclerView? = null
    var mAdapter: StringAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootLayout = inflater.inflate(R.layout.view_fragment_xxx_flexbox, container, false)
        val nameView: AppCompatTextView = rootLayout.findViewById(R.id.fragment_name)
        nameView.text = "Flexbox"
        return rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }

    private fun initView(rootView: View) {
        rootView.findViewById<View>(R.id.fragment_name).setOnClickListener {

            mAdapter?.let {
                val model =
                    StringModel(SunRandomTools.buildRandomString(maxLen = 10), mDataList.size)
                mDataList.add(0, model)
                it.setList(mDataList)
            }
        }
        initRecyclerView(rootView)
    }

    private fun buildDataList(): MutableList<StringModel> {
        val list = mutableListOf<StringModel>()

        for (index in 1..10) {
            val name = SunRandomTools.buildRandomString(maxLen = 10)
            mDataList.add(StringModel(name, index))
        }
        return list
    }

    private var mDataList = mutableListOf<StringModel>()

    private fun initRecyclerView(rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.recycler_view)

        //
        mRecyclerView?.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            /*
            FlexWrap.WRAP:当内容超过一行，自动换行
            FlexWrap.NOWRAP:单行显示不换行，默认值
            FlexWrap.WRAP_REVERSE：反向换行，下一行内容在这一行上方
             */
            flexWrap = FlexWrap.WRAP
            /*
            FlexDirection.ROW   主轴方向按照水平方向排列
            FlexDirection.ROW_REVERSE   主轴方向按照水平方向反向排版(从右侧开始排列)
            FlexDirection.COLUMN    主轴方向按竖直方向排版
            FlexDirection.COLUMN_REVERSE    主轴方向按竖直方向反向排版(从下方开始排列)
             */
            flexDirection = FlexDirection.ROW
        }

        //
        mAdapter = StringAdapter(mDataList)
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val itemModel: StringModel = adapter.getItem(position) as StringModel
            showToast(itemModel.name)
        }
        mRecyclerView?.adapter = mAdapter
    }
}