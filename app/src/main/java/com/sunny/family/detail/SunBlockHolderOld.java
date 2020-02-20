package com.sunny.family.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sunny.family.R;
import com.sunny.family.detail.view.common.BlockHolder;
import com.sunny.family.detail.view.common.BlockInfo;
import com.sunny.family.detail.view.common.RebuildDataModel;
import com.sunny.lib.city.model.CityInfo;

public class SunBlockHolderOld extends BlockHolder {

    private TextView mCityNameView;

    public SunBlockHolderOld(@NonNull View itemView) {
        super(itemView);
        mCityNameView = itemView.findViewById(R.id.tv_weather_name);
    }

    public static SunBlockHolderOld create(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_weather, parent, false);
        return new SunBlockHolderOld(itemView);
    }

    @Override
    protected void onBindBlockData(@NonNull BlockInfo info) {

        RebuildDataModel dataModel = info.getBlockModel();
        CityInfo cityInfo = (CityInfo) dataModel.getData();
        mCityNameView.setText(cityInfo.getName());
    }
}
