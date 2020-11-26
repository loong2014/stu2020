package com.sunny.family.layout.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;
import com.sunny.family.R;
import com.sunny.family.view.SunnyTopBar;
import com.sunny.lib.base.BaseFragment;
import com.sunny.lib.utils.ResUtils;

/**
 * Created by zhangxin17 on 2020/11/19
 */
public class XxxDetailFragment extends BaseFragment {

    private View rootLayout;

    //
    private SunnyTopBar mTopBar;

    //
    private SmartTabLayout mTabLayout;

    //
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = inflater.inflate(R.layout.fragment_xxx_detail, container, false);
        initView(rootLayout);
        return rootLayout;
    }

    private void initView(View root) {
        //
        mTopBar = root.findViewById(R.id.top_bar);
        initTopBar();

        //
        mTabLayout = root.findViewById(R.id.tab_layout);
        mViewPager = root.findViewById(R.id.view_page);
        initTabAndViewPager();

    }

    private void initTopBar() {
        mTopBar.setMiddleName("详情页");
        mTopBar.setOnBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initTabAndViewPager() {

        //
        String[] tabNames = new String[]{"世界", "亚洲", "中国", "北京"};
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(getActivity());
        for (int i = 0; i < tabNames.length; i++) {
            Bundle args = new Bundle();
            args.putInt("detail_info_id", i);
            args.putString("detail_info_name", tabNames[i]);
            creator.add(tabNames[i], XxxDetailInfoFragment.class, args);
        }

        //
        FragmentStatePagerItemAdapter tabAdapter = new FragmentStatePagerItemAdapter(getChildFragmentManager(), creator.create());
        mViewPager.setAdapter(tabAdapter);
        mViewPager.setOffscreenPageLimit(1);

        //
        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mLastSelectedPod = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View view = mTabLayout.getTabAt(position);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    textView.setTextColor(ResUtils.getColor(R.color.colorRed));
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }

                if (mLastSelectedPod != -1) {
                    View preView = mTabLayout.getTabAt(mLastSelectedPod);
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

        //
        mViewPager.setCurrentItem(2);
    }

}
