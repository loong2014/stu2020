package com.sunny.lib.report;

import androidx.annotation.Nullable;

import com.sunny.lib.utils.SunStrUtils;

import java.util.HashMap;

/**
 * Created by ZhangBoshi on 2019/12/11
 * Event事件
 */
public class ReportEvent extends HashMap<String, String> {

    public ReportEvent() {
    }

    @Nullable
    @Override
    public String put(String key, String value) {
        if (SunStrUtils.INSTANCE.isBlank(key))
            return null;

        String pKey = processString(key);
        String pValue = SunStrUtils.INSTANCE.isBlank(value) ? "" : processString(value);

        return super.put(pKey, pValue);
    }

    // sdk上报中, 逗号和冒号是非法字符
    @Nullable
    private String processString(@Nullable String str) {
        return str == null ? null : str.replace(",", "_D_").replace(":", "_M_");
    }


}