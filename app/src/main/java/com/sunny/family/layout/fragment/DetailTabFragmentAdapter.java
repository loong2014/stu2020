package com.sunny.family.layout.fragment;

import androidx.fragment.app.FragmentManager;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

/**
 * Created by zhangxin17 on 2020/11/19
 */
public class DetailTabFragmentAdapter extends FragmentStatePagerItemAdapter {
    public DetailTabFragmentAdapter(FragmentManager fm, FragmentPagerItems pages) {
        super(fm, pages);
    }
}
