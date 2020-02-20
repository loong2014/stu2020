package com.sunny.family.city;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.view.block.ISunBlockFactory;
import com.sunny.view.data.SunBlockData;
import com.sunny.view.holder.SunBlockHolder;

import java.util.List;

public class CityBlockAdapter extends RecyclerView.Adapter<SunBlockHolder> {

    private final ISunBlockFactory mFactory;
    private List<SunBlockData> mData;


    public CityBlockAdapter(ISunBlockFactory factory) {
        mFactory = factory;
    }

    public final void bindData(List<SunBlockData> data) {
        mData = data;
    }

    @NonNull
    @Override
    public SunBlockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mFactory.createBlockHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull SunBlockHolder holder, int position) {
        holder.onBindBlockData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null || mData.size() <= position)
            throw new IndexOutOfBoundsException("invalid position!");
        return mData.get(position).getUiType();
    }
}
