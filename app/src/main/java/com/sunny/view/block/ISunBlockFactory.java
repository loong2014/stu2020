package com.sunny.view.block;

import android.view.ViewGroup;

import com.sunny.view.holder.SunBlockHolder;

public interface ISunBlockFactory {

    SunBlockHolder createBlockHolder(ViewGroup parent, int uiType);
}
