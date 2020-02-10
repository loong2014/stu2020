package com.sunny.family.detail.view.common;


import androidx.annotation.NonNull;

public interface IBlockAttachListener {
    void onAttach(@NonNull BlockInfo blockInfo);

    void onDetach(@NonNull BlockInfo blockInfo);
}
