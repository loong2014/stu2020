package com.sunny.family.layout.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sunny.family.layout.XxxLayoutConstants;

/**
 * Created by zhangxin17 on 2020/11/19
 */
public class XxxFragmentAdapter extends FragmentPagerAdapter {

    public XxxFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case XxxLayoutConstants.FragmentDetail:
                fragment = new XxxDetailFragment();
                break;

            case XxxLayoutConstants.FragmentDrawer:
                fragment = new XxxDrawerFragment();
                break;

            case XxxLayoutConstants.FragmentCoordinator:
                fragment = new XxxCoordinatorFragment();
                break;

            case XxxLayoutConstants.FragmentMotion:
                fragment = new XxxMotionFragment();
                break;

            case XxxLayoutConstants.FragmentExpandable:
                fragment = new XxxExpandableFragment();
                break;

            default:
                fragment = new XxxDetailFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return XxxLayoutConstants.FragmentCount;
    }
}
