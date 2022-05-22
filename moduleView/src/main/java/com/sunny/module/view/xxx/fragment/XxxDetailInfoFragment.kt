package com.sunny.module.view.xxx.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter
import com.sunny.lib.base.log.SunLog.i
import com.sunny.lib.common.base.BaseFragment
import com.sunny.lib.common.city.CityAdapter
import com.sunny.lib.common.city.buildCityTipData
import com.sunny.lib.common.utils.ResUtils.getColor
import com.sunny.lib.ui.SunnySwipeToLoadLayout
import com.sunny.module.view.R
import com.sunny.module.view.xxx.fragment.XxxDetailDataListFragment

/**
 * Created by zhangxin17 on 2020/11/19
 */
class XxxDetailInfoFragment : BaseFragment() {
    private var mPageId = -1
    private var mPageName: String? = ""

    //
    private var hasInit = false
    private var mRootLayout: View? = null

    //
    private var mSwipeToLoadLayout: SunnySwipeToLoadLayout? = null

    //
    private var mSwipeRecyclerView: RecyclerView? = null
    private var mSwipeAdapter: CityAdapter? = null

    //
    private var mDataTabLayout: SmartTabLayout? = null

    //
    private var mDataViewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //
        if (savedInstanceState != null) {
            mPageId = savedInstanceState.getInt("detail_info_id")
            mPageName = savedInstanceState.getString("detail_info_name")
        }

        //
        val bundle = arguments
        if (bundle != null) {
            mPageId = bundle.getInt("detail_info_id")
            mPageName = bundle.getString("detail_info_name")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootLayout = inflater.inflate(R.layout.view_fragment_xxx_detail_info, container, false)
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
        mSwipeToLoadLayout = mRootLayout!!.findViewById(R.id.swipe_to_load_layout)
        mSwipeToLoadLayout?.setTopView(mRootLayout!!.findViewById(R.id.swipe_recyclerView))
        initSwipeToLoad()

        //
        mSwipeRecyclerView = mRootLayout!!.findViewById(R.id.swipe_recyclerView)
        initSwipeRecyclerView()

        //
        mDataTabLayout = mRootLayout!!.findViewById(R.id.data_tab_layout)
        mDataViewPager = mRootLayout!!.findViewById(R.id.data_list_view_pager)
        initTabAndViewPager()
    }

    private fun initSwipeToLoad() {
        mSwipeToLoadLayout!!.setOnRefreshListener {
            i(TAG, "SwipeToLoadLayout  onRefresh")
            mSwipeToLoadLayout!!.isRefreshing = false
        }
    }

    private fun initSwipeRecyclerView() {
        mSwipeRecyclerView!!.layoutManager = GridLayoutManager(mActivity, 1)
        mSwipeRecyclerView!!.setHasFixedSize(true)
        mSwipeAdapter = CityAdapter(buildCityTipData())
        mSwipeRecyclerView!!.adapter = mSwipeAdapter
    }

    private fun initTabAndViewPager() {

        //
        val tabNames = arrayOf("全部", "省", "其它")
        val creator = FragmentPagerItems.with(activity)
        for (i in tabNames.indices) {
            val args = Bundle()
            args.putInt("detail_data_id", i)
            creator.add(tabNames[i], XxxDetailDataListFragment::class.java, args)
        }

        //
        val tabAdapter = FragmentStatePagerItemAdapter(childFragmentManager, creator.create())
        mDataViewPager!!.adapter = tabAdapter
        mDataViewPager!!.offscreenPageLimit = 1

        //
        mDataTabLayout!!.setViewPager(mDataViewPager)
        mDataTabLayout!!.setOnPageChangeListener(object : OnPageChangeListener {
            private var mLastSelectedPod = -1
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val view = mDataTabLayout!!.getTabAt(position)
                if (view is TextView) {
                    val textView = view
                    textView.setTextColor(getColor(R.color.colorRed))
                    textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                if (mLastSelectedPod != -1) {
                    val preView = mDataTabLayout!!.getTabAt(mLastSelectedPod)
                    if (preView is TextView) {
                        val textView = preView
                        textView.setTextColor(getColor(R.color.black_20))
                        textView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    }
                }
                mLastSelectedPod = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mDataViewPager!!.currentItem = 0
    }

    companion object {
        private const val TAG = "Zhang-Detail-Info"
    }
}