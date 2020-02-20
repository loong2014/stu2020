package com.sunny.family.weather.block;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sunny.family.R;
import com.sunny.view.block.SunBlockType;

public class CityNormalBlockHolder extends CityBlockHolder {

    public static final int BlockType = SunBlockType.BLOCK_TYPE_CITY_NORMAL;

    public CityNormalBlockHolder(@NonNull View itemView) {
        super(itemView);
        mBlockNameView.setTextColor(Color.WHITE);
    }

    public static CityNormalBlockHolder create(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_city, parent, false);
        return new CityNormalBlockHolder(itemView);
    }
}
