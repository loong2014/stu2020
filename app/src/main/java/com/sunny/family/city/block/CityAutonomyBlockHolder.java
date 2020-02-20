package com.sunny.family.city.block;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sunny.family.R;
import com.sunny.view.block.SunBlockType;

public class CityAutonomyBlockHolder extends CityBlockHolder {

    public static final int BlockType = SunBlockType.BLOCK_TYPE_CITY_AUTONOMY;

    public CityAutonomyBlockHolder(@NonNull View itemView) {
        super(itemView);
        mBlockNameView.setTextColor(Color.GREEN);
    }

    public static CityAutonomyBlockHolder create(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_city, parent, false);
        return new CityAutonomyBlockHolder(itemView);
    }
}
