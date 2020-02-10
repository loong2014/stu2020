package com.sunny.family.detail.view.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ZhangBoshi
 */
public final class BlockInfo {

    BlockInfo(@NonNull BlockHolder blockHolder) {
        mBlockHolder = blockHolder;
    }

    @Nullable
    private RebuildDataModel mBlockModel;
    @NonNull
    private final BlockHolder mBlockHolder;

    private int mRow;

    @Nullable
    public RebuildDataModel getBlockModel() {
        return mBlockModel;
    }

    void setBlockModel(@Nullable RebuildDataModel blockModel) {
        mBlockModel = blockModel;
    }

    @NonNull
    public BlockHolder getBlockHolder() {
        return mBlockHolder;
    }

    public int getRow() {
        return mRow;
    }

    void setRow(int row) {
        mRow = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BlockInfo))
            return false;

        BlockInfo info = (BlockInfo) o;

        if (mRow != info.mRow)
            return false;
        if (mBlockModel != null ? !mBlockModel.equals(info.mBlockModel) : info.mBlockModel != null)
            return false;
        return mBlockHolder.equals(info.mBlockHolder);
    }

    @Override
    public int hashCode() {
        int result = mBlockModel != null ? mBlockModel.hashCode() : 0;
        result = 31 * result + mBlockHolder.hashCode();
        result = 31 * result + mRow;
        return result;
    }

    @Override
    public String toString() {
        return "BlockInfo{" + "mBlockModel=" + mBlockModel + ", mBlockHolder=" + mBlockHolder
                + ", mRow=" + mRow + '}';
    }
}
