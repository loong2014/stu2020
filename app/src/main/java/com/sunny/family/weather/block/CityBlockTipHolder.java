package com.sunny.family.weather.block;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sunny.family.R;
import com.sunny.view.block.SunBlockType;
import com.sunny.view.data.SunBlockData;
import com.sunny.view.holder.SunBlockHolder;

public class CityBlockTipHolder extends SunBlockHolder {

    public static final int BlockType = SunBlockType.BLOCK_TYPE_CITY_TIP;

    protected TextView mBlockTipView;

    public CityBlockTipHolder(@NonNull View itemView) {
        super(itemView);
        mBlockTipView = itemView.findViewById(R.id.tv_city_tip);
    }

    public static CityBlockTipHolder create(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_city_tip, parent, false);
        return new CityBlockTipHolder(itemView);
    }

    @Override
    public void onBindBlockData(SunBlockData blockData) {

        mBlockTipView.setText(blockData.getName());
    }

}
