package com.sunny.family.detail.view.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.R;
import com.sunny.lib.image.FrescoUtils;
import com.sunny.lib.report.ICtrTrigger;
import com.sunny.lib.utils.SunLog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by ZhangBoshi
 * on 2019-12-9
 * 使用户可以方便地用Recycler view实现BlockHolder, 从而对Controller进行复用.
 * {@link HorizontalAdapter#setSize(int)} ==> 设置recycler view的item count, 将item数量固定<br/>
 */
public final class HorizontalAdapter
        extends RecyclerView.Adapter<HorizontalAdapter.ControllerHolder> {
    private static final String TAG = HorizontalAdapter.class.getSimpleName();
    private static final int DEFAULT_VIEW_TYPE = -100;

    private int mSize;
    private int itemWidth;

    @Nullable
    private List<Poster> mPosters;
    private final IControllerFactory mPosterFactory;
    @Nullable
    private AdapterCallback mCallback;

    private HorizontalAdapter(@NonNull Builder builder) {
        mPosterFactory = builder.mControllerFactory;
        mSize = builder.mSize;
        itemWidth = builder.mWidth;
        mCallback = builder.mCallback;
    }

    public void setSize(int size) {
        if (size < 0)
            throw new IllegalArgumentException("size can not be negative!");
        mSize = size;
    }

    public void setItemWidth(int width) {
        if (itemWidth != width) {
            itemWidth = width;
        }
    }

    public void setCallback(@Nullable AdapterCallback callback) {
        mCallback = callback;
    }

    public void bindData(@Nullable List<Poster> posters) {
        SunLog.INSTANCE.i(TAG, "bindData()-itemCount=" + getItemCount() + "-size=" + mSize + "-posters="
                + (posters == null ? 0 : posters.size()));
        bindDataNotNotify(posters);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void bindDataNotNotify(@Nullable List<Poster> posters) {
        mPosters = posters;
    }

    @Override
    public void onBindViewHolder(@NonNull ControllerHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        onBindViewHolder(holder, position);

    }

    @Override
    public void onBindViewHolder(@NotNull HorizontalAdapter.ControllerHolder controllerHolder,
                                 int position) {

        Poster poster = null;
        // int size = calculateSize();

        if (mPosters != null && mPosters.size() > position) {
            poster = mPosters.get(position);
        }

        View view = controllerHolder.itemView;
        if (mCallback != null)
            mCallback.onBindController(controllerHolder.mController, poster, position);

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
    public void onViewRecycled(@NotNull ControllerHolder holder) {
        if (mCallback != null)
            mCallback.onViewRecycled(holder.mController);
    }

    @Override
    public int getItemViewType(int position) {
        return mCallback == null ? DEFAULT_VIEW_TYPE : mCallback.getControllerType(position);
    }

    @Override
    public HorizontalAdapter.ControllerHolder onCreateViewHolder(@NotNull ViewGroup viewGroup,
                                                                 int viewType) {
        BasePosterController controller = mPosterFactory.createPosterController(viewGroup,
                viewType);
        View view = controller.getView();
        if (view != null && itemWidth > 0) {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
            params.width = itemWidth;
            view.setLayoutParams(params);
        }
        return new ControllerHolder(controller) {
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
        @Nullable
        private ICtrTrigger mCtrTrigger;

        private int mSize = 0;
        private int mMargin = -1;
        private int mWidth = -1;
        private float mScrollSpeed = 0.5f;

        private Builder() {
        }

        @NonNull
        public static Builder create() {
            return new Builder();
        }

        /**
         * {@link HorizontalAdapter#onCreateViewHolder(ViewGroup, int)}中会使用此factory生产view holder
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
        public Builder setWidth(int width) {
            mWidth = width;
            return this;
        }

        @NonNull
        public Builder setScrollSpeed(float scrollSpeed) {
            mScrollSpeed = scrollSpeed;
            return this;
        }

        public Builder setICtrTrigger(ICtrTrigger trigger) {
            mCtrTrigger = trigger;
            return this;
        }

        /**
         * {@link HorizontalAdapter}将复用此view holder pool
         */
        @NonNull
        public Builder setRecyclerViewPool(@Nullable RecyclerView.RecycledViewPool pool) {
            mPool = pool;
            return this;
        }

        @NonNull
        public Builder setSize(int size) {
            mSize = size;
            return this;
        }

        @NonNull
        public HorizontalAdapter build() {
            if (mRecyclerView == null)
                throw new IllegalArgumentException("recycler view can not be null!");
            if (mControllerFactory == null)
                throw new IllegalArgumentException("poster factory can not be null!");

            Context context = mRecyclerView.getContext();
            HorizontalLinearLayoutManager layoutManager = new HorizontalLinearLayoutManager(
                    context);
            // LinearLayoutManager layoutManager=new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            // layoutManager.setScrollingSpeed(mScrollSpeed);
            if (mMargin < 0) {
                mMargin = mRecyclerView.getContext().getResources()
                        .getDimensionPixelSize(R.dimen.recycler_controller_default_margin);
            }
            ControllerItemDecoration itemDecoration = new ControllerItemDecoration(0, 0, 0,
                    mMargin);

            // if (mPool != null) {
            // mRecyclerView.setRecycledViewPool(mPool);
            // layoutManager.setRecycleChildrenOnDetach(true);
            // }
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == SCROLL_STATE_IDLE) {
                        FrescoUtils.pause();
                        if (mCtrTrigger != null)
                            mCtrTrigger.triggerReport();
                    } else {
                        FrescoUtils.resume();
                        if (mCtrTrigger != null)
                            mCtrTrigger.cancelReport();
                    }
                }
            });
            return new HorizontalAdapter(this);
        }
    }

    public abstract static class AdapterCallback {
        /**
         * {@link HorizontalAdapter#onBindViewHolder}中调用，业务端设置展示类型并绑定数据
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