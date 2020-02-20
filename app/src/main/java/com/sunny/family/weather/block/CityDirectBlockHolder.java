package com.sunny.family.weather.block;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sunny.family.R;
import com.sunny.lib.city.model.CityType;
import com.sunny.view.block.SunBlockType;

public class CityDirectBlockHolder extends CityBlockHolder {

    public static final int BlockType = SunBlockType.BLOCK_TYPE_CITY_DIRECT;

    public CityDirectBlockHolder(@NonNull View itemView) {
        super(itemView);
        mBlockNameView.setTextColor(Color.YELLOW);
    }

    public static CityDirectBlockHolder create(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_city, parent, false);
        return new CityDirectBlockHolder(itemView);
    }
}
