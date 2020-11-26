package com.sunny.family.layout.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;
import com.sunny.family.R;
import com.sunny.family.city.CityModelKt;
import com.sunny.family.city.adapter.CityAdapter;
import com.sunny.family.layout.SunnySwipeToLoadLayout;
import com.sunny.lib.base.BaseFragment;
import com.sunny.lib.utils.ResUtils;
import com.sunny.lib.utils.SunLog;

/**
 * Created by zhangxin17 on 2020/11/19
 */
public class XxxDetailInfoFragment extends BaseFragment {
    private static final String TAG = "Zhang-Detail-Info";

    private int mPageId = -1;
    private String mPageName = "";

    //
    private boolean hasInit = false;
    private View mRootLayout;

    //
    private SunnySwipeToLoadLayout mSwipeToLoadLayout;

    //
    private RecyclerView mSwipeRecyclerView;
    private CityAdapter mSwipeAdapter;

    //
    private SmartTabLayout mDataTabLayout;

    //
    private ViewPager mDataViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        if (savedInstanceState != null) {
            mPageId = savedInstanceState.getInt("detail_info_id");
            mPageName = savedInstanceState.getString("detail_info_name");
        }

        //
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPageId = bundle.getInt("detail_info_id");
            mPageName = bundle.getString("detail_info_name");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootLayout = inflater.inflate(R.layout.fragment_xxx_detail_info, container, false);
        return mRootLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        if (hasInit) {
            return;
        }
        hasInit = true;

        //
        mSwipeToLoadLayout = mRootLayout.findViewById(R.id.swipe_to_load_layout);
        mSwipeToLoadLayout.setTopView(mRootLayout.findViewById(R.id.swipe_recyclerView));
        initSwipeToLoad();

        //
        mSwipeRecyclerView = mRootLayout.findViewById(R.id.swipe_recyclerView);
        initSwipeRecyclerView();

        //
        mDataTabLayout = mRootLayout.findViewById(R.id.data_tab_layout);
        mDataViewPager = mRootLayout.findViewById(R.id.data_list_view_pager);
        initTabAndViewPager();
    }

    private void initSwipeToLoad() {
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                SunLog.i(TAG, "SwipeToLoadLayout  onRefresh");
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });
    }

    private void initSwipeRecyclerView() {
        mSwipeRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 1));
        mSwipeRecyclerView.setHasFixedSize(true);

        mSwipeAdapter = new CityAdapter(CityModelKt.buildCityTipData());
        mSwipeRecyclerView.setAdapter(mSwipeAdapter);
    }

    private void initTabAndViewPager() {

        //
        String[] tabNames = new String[]{"全部", "省", "其它"};
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(getActivity());
        for (int i = 0; i < tabNames.length; i++) {
            Bundle args = new Bundle();
            args.putInt("detail_data_id", i);
            creator.add(tabNames[i], XxxDetailDataListFragment.class, args);
        }

        //
        FragmentStatePagerItemAdapter tabAdapter = new FragmentStatePagerItemAdapter(getChildFragmentManager(), creator.create());
        mDataViewPager.setAdapter(tabAdapter);
        mDataViewPager.setOffscreenPageLimit(1);

        //
        mDataTabLayout.setViewPager(mDataViewPager);
        mDataTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mLastSelectedPod = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View view = mDataTabLayout.getTabAt(position);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    textView.setTextColor(ResUtils.getColor(R.color.colorRed));
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }

                if (mLastSelectedPod != -1) {
                    View preView = mDataTabLayout.getTabAt(mLastSelectedPod);
                    if (preView instanceof TextView) {
                        TextView textView = (TextView) preView;
                        textView.setTextColor(ResUtils.getColor(R.color.black_20));
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }

                mLastSelectedPod = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mDataViewPager.setCurrentItem(0);
    }

}
