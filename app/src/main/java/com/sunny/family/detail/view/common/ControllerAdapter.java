package com.sunny.family.detail.view.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.R;
import com.sunny.lib.utils.SunLog;

import java.util.List;

/**
 * Created by ZhangBoshi
 * on 2019-12-9
 * {@link ControllerAdapter}是适配Block框架的工具类, 用于生成平分布局的网格状的recycler view,
 * 使用户可以方便地用Recycler view实现BlockHolder, 从而对Controller进行复用.
 * {@link ControllerAdapter#setSize(int)} ==> 设置recycler view的item count, 将item数量固定<br/>
 * {@link ControllerAdapter#setSpanCount(int)} ==> 设置recycler view的一行的展示个数<br/>
 */
public final class ControllerAdapter
        extends RecyclerView.Adapter<ControllerAdapter.ControllerHolder> {
    private static final String TAG = ControllerAdapter.class.getSimpleName();
    private static final int DEFAULT_VIEW_TYPE = -100;
    private static final int DEFAULT_SPAN_COUNT = 2;

    private int mSize;
    private int mSpanCount = DEFAULT_SPAN_COUNT;
    private int mMargin;
    private int mFactor;
    private int mRowHeight;

    @Nullable
    private List<Poster> mPosters;
    private final IControllerFactory mPosterFactory;
    private final ViewGroup mParent;
    @NonNull
    private final GridLayoutManager mLayoutManager;
    @Nullable
    private AdapterCallback mCallback;

    private ControllerAdapter(@NonNull GridLayoutManager layoutManager,
                              @NonNull IControllerFactory factory, @NonNull ViewGroup parent) {
        mLayoutManager = layoutManager;
        mPosterFactory = factory;
        mParent = parent;
    }

    public void setSize(int size) {
        if (size < 0)
            throw new IllegalArgumentException("size can not be negative!");
        mSize = size;
    }

    public void setMargin(int margin) {
        if (margin < 0)
            throw new IllegalArgumentException("size can not be negative!");
        mMargin = margin;
        syncFactor();
    }

    public void setSpanCount(int spanCount) {
        if (spanCount < 0)
            throw new IllegalArgumentException("span count can not be negative!");
        if (spanCount == mSpanCount)
            return;
        mLayoutManager.setSpanCount(spanCount);
        mSpanCount = spanCount;
        syncFactor();
    }

    public void setRowHeight(int rowHeight) {
        if (mRowHeight != rowHeight) {
            mRowHeight = rowHeight;
            resetLayoutParams();
        }
    }

    public void setCallback(@Nullable AdapterCallback callback) {
        mCallback = callback;
    }

    public void bindData(@Nullable List<Poster> posters) {
        SunLog.INSTANCE.i(TAG, "bindData()-itemCount=" + getItemCount() + "-size=" + mSize + "-posters="
                + (posters == null ? 0 : posters.size()));
        bindDataNotNotify(posters);
        resetLayoutParams();
        notifyItemRangeChanged(0, getItemCount());
    }

    public void bindDataNotNotify(@Nullable List<Poster> posters) {
        mPosters = posters;
    }

    private void syncFactor() {
        if (mSpanCount > 0)
            mFactor = mMargin / mSpanCount;
    }

    private void resetLayoutParams() {
        if (mRowHeight <= 0)
            return;

        int size = calculateSize();
        if (size <= 0)
            return;

        int rowNum = size / mSpanCount;
        int rvHeight = mRowHeight * rowNum + mMargin * (rowNum - 1);

        ViewGroup.LayoutParams params = mParent.getLayoutParams();
        if (params.height != rvHeight) {
            params.height = rvHeight;
            mParent.setLayoutParams(params);
        }
    }

    private int calculateSize() {
        if (mSize > 0)
            return mSize;

        return mPosters == null || mPosters.isEmpty() ? 0 : mPosters.size();
    }

    @Override
    public void onBindViewHolder(ControllerAdapter.ControllerHolder controllerHolder,
                                 int position) {
        Poster poster = null;
        int size = calculateSize();

        if (mPosters != null && mPosters.size() > position) {
            poster = mPosters.get(position);
        }

        View view = controllerHolder.itemView;
        if (mCallback != null)
            mCallback.onBindController(controllerHolder.mController, poster, position);

        // 计算measure数据
        int spanCount = mSpanCount;
        int factor = mFactor;

        int heightOffset = 0;
        int rowCount = 0;
        if (spanCount > 0) {
            rowCount = size / spanCount;
            if (size % spanCount > 0)
                rowCount++;
            if (rowCount > 0)
                heightOffset = (rowCount - 1) * mMargin;
        }

        int leftMargin = 0;
        int rightMargin = 0;
        if (spanCount > 0) {
            int idxInRow = position % spanCount;
            leftMargin = idxInRow * factor;
            if (idxInRow < spanCount - 1)
                rightMargin = mMargin - leftMargin - factor;
        }

        int topMargin = 0;
        // 非第一行
        if (spanCount > 0 && position / spanCount > 0) {
            topMargin = mMargin;
        }

        // 防止除零溢出
        if (rowCount == 0)
            rowCount = 1;
        int height;
        if (mRowHeight > 0) {
            height = mRowHeight + topMargin;
        } else {
            height = (mParent.getHeight() - heightOffset) / rowCount + topMargin;
        }

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.height = height;
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        params.topMargin = topMargin;

        view.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        if (mSize > 0)
            return mSize;
        if (mPosters == null)
            return 0;
        return mPosters.size();
    }

    @Override
    public void onViewRecycled(ControllerHolder holder) {
        if (mCallback != null && holder != null)
            mCallback.onViewRecycled(holder.mController);
    }

    @Override
    public int getItemViewType(int position) {
        return mCallback == null ? DEFAULT_VIEW_TYPE : mCallback.getControllerType(position);
    }

    @Override
    public ControllerAdapter.ControllerHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                 int viewType) {
        return new ControllerHolder(mPosterFactory.createPosterController(viewGroup, viewType)) {
        };
    }

    public static class Builder {
        @Nullable
        private IControllerFactory mControllerFactory;
        @Nullable
        private AdapterCallback mCallback;
        @Nullable
        private RecyclerView mRecyclerView;
        @Nullable
        private RecyclerView.RecycledViewPool mPool;

        private int mSpanCount = DEFAULT_SPAN_COUNT;
        private int mSize = 0;
        private int mMargin = -1;
        private int mRowHeight = 0;

        private Builder() {
        }

        @NonNull
        public static Builder create() {
            return new Builder();
        }

        /**
         * {@link ControllerAdapter#onCreateViewHolder(ViewGroup, int)}中会使用此factory生产view holder
         */
        @NonNull
        public Builder setControllerFactory(@Nullable IControllerFactory controllerFactory) {
            mControllerFactory = controllerFactory;
            return this;
        }

        @NonNull
        public Builder setMargin(int margin) {
            mMargin = margin;
            return this;
        }

        @NonNull
        public Builder setCallback(@Nullable AdapterCallback callback) {
            mCallback = callback;
            return this;
        }

        @NonNull
        public Builder setRecyclerView(@Nullable RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            return this;
        }

        @NonNull
        public Builder setRowHeight(int rowHeight) {
            mRowHeight = rowHeight;
            return this;
        }

        /**
         * {@link ControllerAdapter}将复用此view holder pool
         */
        @NonNull
        public Builder setRecyclerViewPool(@Nullable RecyclerView.RecycledViewPool pool) {
            mPool = pool;
            return this;
        }

        @NonNull
        public Builder setSpanCount(int spanCount) {
            mSpanCount = spanCount;
            return this;
        }

        @NonNull
        public Builder setSize(int size) {
            mSize = size;
            return this;
        }

        @NonNull
        public ControllerAdapter build() {
            if (mRecyclerView == null)
                throw new IllegalArgumentException("recycler view can not be null!");
            if (mControllerFactory == null)
                throw new IllegalArgumentException("poster factory can not be null!");

            Context context = mRecyclerView.getContext();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,
                    DEFAULT_SPAN_COUNT);
            ControllerAdapter adapter = new ControllerAdapter(gridLayoutManager, mControllerFactory,
                    mRecyclerView);
            adapter.setSize(mSize);
            adapter.setSpanCount(mSpanCount);
            adapter.setCallback(mCallback);
            if (mMargin < 0) {
                mMargin = mRecyclerView.getContext().getResources()
                        .getDimensionPixelSize(R.dimen.recycler_controller_default_margin);
            }
            adapter.setMargin(mMargin);
            adapter.setRowHeight(mRowHeight);

            if (mPool != null) {
                mRecyclerView.setRecycledViewPool(mPool);
                gridLayoutManager.setRecycleChildrenOnDetach(true);
            }
            mRecyclerView.setLayoutManager(gridLayoutManager);
            return adapter;
        }
    }

    public abstract static class AdapterCallback {
        /**
         * {@link ControllerAdapter#onBindViewHolder}中调用，业务端设置展示类型并绑定数据
         */
        public abstract void onBindController(@NonNull BasePosterController controller,
                                              @Nullable Poster poster, int position);

        /**
         * 根据位置信息，获取Controller的类型
         */
        public abstract int getControllerType(int position);

        protected void onViewRecycled(@NonNull BasePosterController holder) {
        }
    }

    static class ControllerHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final BasePosterController mController;

        private ControllerHolder(@NonNull BasePosterController controller) {
            super(controller.getView());
            mController = controller;
        }
    }

}