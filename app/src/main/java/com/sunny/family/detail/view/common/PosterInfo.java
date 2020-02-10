package com.sunny.family.detail.view.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.lib.report.ReportEvent;

public final class PosterInfo {
    PosterInfo(int idx, @NonNull BlockInfo blockInfo, @Nullable Poster poster,
               @NonNull BasePosterController controller) {
        mIdx = idx;
        mBlockInfo = blockInfo;
        mPoster = poster;
        mPosterController = controller;
    }

    /**
     * BlockData里Poster的位置信息
     */
    private final int mIdx;
    @NonNull
    private final BlockInfo mBlockInfo;
    @Nullable
    private final Poster mPoster;
    @NonNull
    private final BasePosterController mPosterController;
    @NonNull
    private final ReportEvent mReportEvent = new ReportEvent();

    public int getIdx() {
        return mIdx;
    }

    @NonNull
    public BlockInfo getBlockInfo() {
        return mBlockInfo;
    }

    @Nullable
    public Poster getPoster() {
        return mPoster;
    }

    @NonNull
    public BasePosterController getPosterController() {
        return mPosterController;
    }

    @NonNull
    public ReportEvent getmReportEvent() {
        return mReportEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PosterInfo))
            return false;

        PosterInfo that = (PosterInfo) o;

        if (mIdx != that.mIdx)
            return false;
        if (!mBlockInfo.equals(that.mBlockInfo))
            return false;
        if (mPoster != null ? !mPoster.equals(that.mPoster) : that.mPoster != null)
            return false;
        if (!mPosterController.equals(that.mPosterController))
            return false;
        return mReportEvent.equals(that.mReportEvent);
    }

    @Override
    public int hashCode() {
        int result = mIdx;
        result = 31 * result + mBlockInfo.hashCode();
        result = 31 * result + (mPoster != null ? mPoster.hashCode() : 0);
        result = 31 * result + mPosterController.hashCode();
        result = 31 * result + mReportEvent.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PosterInfo{" + "mIdx=" + mIdx + ", mBlockInfo=" + mBlockInfo + ", mPoster="
                + mPoster + ", mPosterController=" + mPosterController + ", mReportEvent="
                + mReportEvent + '}';
    }
}
