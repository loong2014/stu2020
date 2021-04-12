package com.sunny.module.stu.fragment;

import com.sunny.module.stu.base.StuBaseFragment;
import com.sunny.module.stu.handler.StuHandlerFragment;

import java.util.HashMap;
import java.util.Map;

public class StuFragmentSelector {

    private static final Map<String, StuBaseFragment> mFragmentMap = new HashMap<>();

    public static StuBaseFragment getFragment(String type) {

        StuBaseFragment result = mFragmentMap.get(type);
        if (result != null) {
            return result;
        }

        switch (type) {
            case "stu_handler":
                result = new StuHandlerFragment();
                break;

            default:
                result = new NormalFragment();
                break;
        }

        mFragmentMap.put(type, result);
        return result;
    }
}
