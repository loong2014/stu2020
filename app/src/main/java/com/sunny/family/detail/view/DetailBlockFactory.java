package com.sunny.family.detail.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.detail.view.block.BlockType;
import com.sunny.family.detail.view.block.FooterBlockHolder;
import com.sunny.family.detail.view.block.HeaderBlockHolder;
import com.sunny.family.detail.view.block.S1001BlockHolder;
import com.sunny.family.detail.view.block.S1002BlockHolder;
import com.sunny.family.detail.view.block.S1003BlockHolder;
import com.sunny.family.detail.view.block.S1004BlockHolder;
import com.sunny.family.detail.view.common.BaseBlockFactory;
import com.sunny.family.detail.view.common.BasePosterController;
import com.sunny.family.detail.view.common.BlockHolder;
import com.sunny.family.detail.view.common.BlockInfo;
import com.sunny.family.detail.view.common.ControllerType;
import com.sunny.family.detail.view.common.IBlockBindListener;
import com.sunny.family.detail.view.common.IBlockClickListener;
import com.sunny.family.detail.view.common.IControllerFactory;
import com.sunny.family.detail.view.common.PosterInfo;
import com.sunny.family.detail.view.common.T10000WidgetController;
import com.sunny.family.detail.view.widget.BaseWidget;
import com.sunny.family.detail.view.widget.W101_Image_S1001;
import com.sunny.family.detail.view.widget.W102_BottomTitle;
import com.sunny.family.detail.view.widget.W103_CenterText;
import com.sunny.family.detail.view.widget.W104_Image_S1004;
import com.sunny.family.detail.view.widget.WidgetType;
import com.sunny.lib.report.ICtrTrigger;
import com.sunny.lib.utils.SunLog;
import com.sunny.player.helper.IPlayerViewHelper;

/**
 * Created by ZhangBoshi
 */
public class DetailBlockFactory extends BaseBlockFactory {

    private static final String TAG = "DetailBlockFactory";
    private static final RecyclerView.RecycledViewPool sBlockPool;
    private static final RecyclerView.RecycledViewPool sControllerPool;
    private static final T10000WidgetController.WidgetPool sWidgetPool;

    static {
        sBlockPool = new RecyclerView.RecycledViewPool();
        sControllerPool = new RecyclerView.RecycledViewPool();
        sWidgetPool = new T10000WidgetController.WidgetPool();
        sControllerPool.setMaxRecycledViews(ControllerType.WIDGET_10000, 25);
    }

    private final HeaderBLockListener mHeaderBlockListener;
    private final OnHeaderWidgetClickListener headerWidgetClickListener;
    private final ICtrTrigger mCtrTrigger;
    private final IBlockClickListener mBlockListener;
    private final IBackTopListener mBackTopListener;
    private final IPlayerViewHelper mPlayerViewHelper;

    private DetailBlockFactory(Builder builder) {
        this.mHeaderBlockListener = builder.mHeaderBlockListener;
        this.headerWidgetClickListener = builder.clickListener;
        this.mBlockListener = builder.mBlockClickListener;
        this.mBackTopListener = builder.mBackToTopListener;
        this.mCtrTrigger = builder.mCtrTrigger;
        this.mPlayerViewHelper = builder.mPlayerViewHelper;
    }

    /**
     * 创建每个坑位的基础view的Controller
     */
    @NonNull
    private final IControllerFactory mControllerFactory = new IControllerFactory() {
        @NonNull
        @Override
        public BasePosterController createPosterController(@NonNull ViewGroup viewGroup,
                                                           int viewType) {
            SunLog.INSTANCE.i(TAG, "create controller: " + viewType);
            BasePosterController controller;

            T10000WidgetController widgetController = T10000WidgetController.create(viewGroup,
                    viewType);
            widgetController.setWidgetFactory(mWidgetFactory);
            widgetController.setWidgetPool(sWidgetPool);
            controller = widgetController;
            return controller;
        }
    };

    /**
     * 被BlockAdapter调用，创建每个blockholder的view
     */
    @NonNull
    @Override
    public BlockHolder createBlockHolder(@NonNull ViewGroup parent, int viewType) {
        SunLog.INSTANCE.i(TAG, "create block: " + viewType);

        BlockHolder blockHolder = null;
        switch (viewType) {

            case BlockType.BLOCK_S1000:
                blockHolder = HeaderBlockHolder.create(parent, mControllerFactory,
                        headerWidgetClickListener);
                break;
            case BlockType.BLOCK_S1001:
                blockHolder = S1001BlockHolder.create(parent, mControllerFactory, sControllerPool);
                break;
            case BlockType.BLOCK_S1002:
                blockHolder = S1002BlockHolder.create(parent, mControllerFactory);
                break;
            case BlockType.BLOCK_S1003:
                blockHolder = S1003BlockHolder.create(parent, mControllerFactory);
                break;

            case BlockType.BLOCK_S1004:
                blockHolder = S1004BlockHolder.create(parent, mControllerFactory, sControllerPool);
                break;
            case BlockType.BLOCK_FOOTER:
                blockHolder = FooterBlockHolder.create(parent);
                break;
            default:
                // do nothing
        }

        return blockHolder == null ? super.createBlockHolder(parent, viewType) : blockHolder;
    }

    // widget
    @NonNull
    private final T10000WidgetController.IWidgetFactory mWidgetFactory = new T10000WidgetController.IWidgetFactory() {
        @NonNull
        @Override
        public BaseWidget createWidget(@NonNull ViewGroup parent, int widgetType) {
            SunLog.INSTANCE.i(TAG, "create widget: " + widgetType);
            BaseWidget widget;
            switch (widgetType) {
                case WidgetType.WIDGET_101:
                    widget = W101_Image_S1001.create(parent);
                    break;
                case WidgetType.WIDGET_102:
                    widget = W102_BottomTitle.create(parent);
                    break;
                case WidgetType.WIDGET_103:
                    widget = W103_CenterText.create(parent);
                    break;
                case WidgetType.WIDGET_104:
                    widget = W104_Image_S1004.create(parent);
                    break;
                default:
                    throw new IllegalArgumentException("unsupported widget: " + widgetType);
            }
            return widget;
        }

        @Override
        public int getWidgetType(String tagType, String position) {
            return 0;

        }

    };

    /**
     * 这里处理每个坑位的点击事件
     */
    @Nullable
    @Override
    public IBlockClickListener createBlockClickListener() {
        return new IBlockClickListener() {
            @Override
            public void onControllerClick(@NonNull PosterInfo info) {

//                if (info.getPoster() != null && info.getPoster().getCategoryId() != null && info.getPoster().getCategoryId().equals(String.valueOf(TypeInfo.CategoryType.VARIETY))) {
//                    处理综艺剧集的点击事件
//                    mPlayerViewHelper.getPlayerListManager().onVideoListItemOutClick(info.getPoster().getVideoId());
//                } else if (mBlockListener != null) {
                //处理内容位跳转事件
                mBlockListener.onControllerClick(info);
//                }
            }

            @Override
            public void doControllerClickReport(@NonNull PosterInfo info) {
                // 点击事件的数据上报
//                ReportTool.reportContent(info.getmReportEvent(), ReportPropKeys.TYPE_CLICK);

            }
        };
    }

    /**
     * 在这里给blockholder的子类传一些必要的参数
     */
    @Nullable
    @Override
    public IBlockBindListener createBlockBindListener() {
        return new IBlockBindListener() {
            @Override
            public void onBindBlockHolder(@NonNull BlockInfo info) {
                BlockHolder holder = info.getBlockHolder();

                if (holder instanceof HeaderBlockHolder) {
                    ((HeaderBlockHolder) holder).setCtrTrigger(mCtrTrigger);
                    ((HeaderBlockHolder) holder).setPlayerViewHelper(mPlayerViewHelper);
                } else if (holder instanceof FooterBlockHolder) {
                    ((FooterBlockHolder) holder).setBackToTopListener(mBackTopListener);
                }

            }
        };
    }

    public static class Builder {

        @Nullable
        private ICtrTrigger mCtrTrigger;
        @Nullable
        private HeaderBLockListener mHeaderBlockListener;
        // @Nullable
        // private ReportTool mReportTool;
        private OnHeaderWidgetClickListener clickListener;
        private IBlockClickListener mBlockClickListener;
        private IBackTopListener mBackToTopListener;
        private IPlayerViewHelper mPlayerViewHelper;

        public Builder ctrTrigger(@Nullable ICtrTrigger ctrTrigger) {
            mCtrTrigger = ctrTrigger;
            return this;
        }

        public Builder setHeaderBlockListener(@Nullable HeaderBLockListener listener) {
            mHeaderBlockListener = listener;
            return this;
        }

        public Builder setBlockListener(@Nullable IBlockClickListener listener) {
            mBlockClickListener = listener;
            return this;
        }

        public Builder setPlayerViewHelper(IPlayerViewHelper mPlayerViewHelper) {
            this.mPlayerViewHelper = mPlayerViewHelper;
            return this;
        }
        // Builder reportTool(@Nullable ReportTool reportTool) {
        // mReportTool = reportTool;
        // return this;
        // }

        public Builder setBackToTopListener(@Nullable IBackTopListener backToTopListener) {
            mBackToTopListener = backToTopListener;
            return this;
        }

        public Builder setHeaderWidgetListener(OnHeaderWidgetClickListener headerWidgetListener) {
            clickListener = headerWidgetListener;
            return this;
        }

        public Builder setHeaderPlayerWindowView(View view) {
            // TODO: 2019-12-18 这里进行小窗在首屏block中的焦点移动处理
            return this;
        }

        public DetailBlockFactory build() {
            return new DetailBlockFactory(this);
        }
    }

    public View getPlayViewLeftView() {
        return null;
    }
}
