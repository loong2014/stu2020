package com.sunny.family.city.block;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sunny.family.R;
import com.sunny.view.data.SunBlockData;
import com.sunny.view.holder.SunBlockHolder;

public class CityBlockHolder extends SunBlockHolder {

    protected TextView mBlockNameView;

    public CityBlockHolder(@NonNull View itemView) {
        super(itemView);
        mBlockNameView = itemView.findViewById(R.id.tv_city_name);
    }

    @Override
    public void onBindBlockData(SunBlockData blockData) {

        mBlockNameView.setText(blockData.getName());
    }
}
