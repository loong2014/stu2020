package com.sunny.module.mob;

import androidx.annotation.NonNull;

import com.sunny.lib.common.base.BaseApplication;

public class MobApplication extends BaseApplication {
    @NonNull
    @Override
    public String getLogTag() {
        return "SunnyMob-";
    }
}
