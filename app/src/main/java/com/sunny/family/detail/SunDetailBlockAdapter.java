package com.sunny.family.detail;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.detail.view.common.BlockHolder;
import com.sunny.family.detail.view.common.BlockUtils;
import com.sunny.family.detail.view.common.IBlockFactory;
import com.sunny.family.detail.view.common.RebuildDataModel;

import java.util.List;

public class SunDetailBlockAdapter extends RecyclerView.Adapter<BlockHolder> {

    private final IBlockFactory mFactory;

    private List<RebuildDataModel> mData;

    public SunDetailBlockAdapter(IBlockFactory factory) {
        mFactory = factory;
    }

    public final void bindData(List<RebuildDataModel> data) {
        mData = data;
    }

    @NonNull
    @Override
    public BlockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mFactory.createBlockHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockHolder holder, int position) {
        holder.bindData(mData.get(position), position);
        BlockUtils.setAdapterPosition(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
