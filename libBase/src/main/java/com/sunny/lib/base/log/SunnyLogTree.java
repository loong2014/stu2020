/*
 * ************************************************************
 * File：LogTree.java  Module：app  Project：PaxLauncher
 * Current Modifier：2018-12-04 09:26:07
 * Last Modifier：2018-12-04 09:26:07
 * Author: WangYao
 * http://pax.leautolink.com
 * Copyright (c) 2018
 * ************************************************************
 */

package com.sunny.lib.base.log;

import timber.log.Timber;

public class SunnyLogTree extends Timber.DebugTree {
    public final String TAG;

    public SunnyLogTree(String tag) {
        TAG = tag;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        super.log(priority, TAG + tag, message, t);
    }
}
