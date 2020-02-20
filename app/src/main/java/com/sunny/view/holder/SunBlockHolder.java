package com.sunny.view.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.view.data.SunBlockData;

public abstract class SunBlockHolder extends RecyclerView.ViewHolder {

    public SunBlockHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void onBindBlockData(SunBlockData blockData);
}
