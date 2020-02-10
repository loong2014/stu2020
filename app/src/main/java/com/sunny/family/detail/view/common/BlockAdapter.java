package com.sunny.family.detail.view.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.lib.utils.SunLog;

import java.util.List;
import java.util.Locale;

/**
 * Created by ZhangBoshi
 * 负责将{@link BlockHolder}装配到{@link RecyclerView}中, 并绑定业务回调.
 */
public final class BlockAdapter extends RecyclerView.Adapter<BlockHolder> {
    @NonNull
    private final IBlockFactory mFactory;
    @Nullable
    private final IBlockAttachListener mBlockAttachListener;
    @Nullable
    private final IBlockClickListener mBlockClickListener;
    @Nullable
    private final IBlockBindListener mBlockBindListener;
    @Nullable
    private final IPosterInfoProcessor mProcessor;

    @Nullable
    private List<RebuildDataModel> mData;

    private static final String TAG = "BlockAdapter";
    private static final String FORMAT_BIND = "[bind] type: %d, position: %d, cost time: %d";
    private static final String FORMAT_CREATE = "[create] type: %d, cost time: %d";

    public BlockAdapter(@NonNull IBlockFactory factory) {
        mFactory = factory;
        mBlockAttachListener = factory.createBlockAttachListener();
        mBlockClickListener = factory.createBlockClickListener();
        mBlockBindListener = factory.createBlockBindListener();
        mProcessor = factory.createPosterInfoProcessor();
    }

    public final void bindData(@Nullable List<RebuildDataModel> data) {
        if (data == null) {
            SunLog.INSTANCE.i(TAG, "bindData() - data is not valid");
            return;
        }
        mData = data;
    }

    @Override
    public void onViewRecycled(BlockHolder holder) {
        if (holder != null)
            holder.onRecycled();
    }

    /**
     * 将业务相关的回调注册到{@link BlockHolder}中
     */
    @Override
    public void onBindViewHolder(BlockHolder vh, int position) {
        if (mData == null || mData.size() <= position)
            throw new IndexOutOfBoundsException("invalid position!");
        long start = System.currentTimeMillis();
        vh.setAttachListener(mBlockAttachListener);
        vh.setClickListener(mBlockClickListener);
        vh.setBindListener(mBlockBindListener);
        vh.setPosterInfoProcessor(mProcessor);
        vh.bindData(mData.get(position), position);
        BlockUtils.setAdapterPosition(vh.itemView, position);
        SunLog.INSTANCE.i(TAG, String.format(Locale.US, FORMAT_BIND, vh.getItemViewType(), position,
                System.currentTimeMillis() - start));
    }

    @Override
    public final void onViewAttachedToWindow(BlockHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.callAttach();
    }

    @Override
    public final void onViewDetachedFromWindow(BlockHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.callDetach();
    }

    @Override
    public final int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public final BlockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        long start = System.currentTimeMillis();
        BlockHolder holder = mFactory.createBlockHolder(parent, viewType);
        SunLog.INSTANCE.i(TAG, String.format(Locale.US, FORMAT_CREATE, viewType,
                System.currentTimeMillis() - start));

        return holder;
    }

    @Override
    public final int getItemViewType(int position) {
        if (mData == null || mData.size() <= position)
            throw new IndexOutOfBoundsException("invalid position!");
        return mData.get(position).getUiType();
    }
}