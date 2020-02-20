package com.sunny.family.detail.view.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.lib.report.CtrManager;
import com.sunny.lib.report.ReportEvent;

/**
 * Created by ZhangBoshi
 * 一 概述
 * 将桌面业务进行抽象:
 * 以行为单位分为{@link BlockHolder}(BlockHolder),
 * 每一行是由{@link BasePosterController}(Controller)组成.
 * 为了尽可能复用样式, 将与样式无关的逻辑通过回调交给业务层处理.
 * <br/>
 * 二 定义
 * Controller只有ui逻辑, 控制View在绑定时如何展示, 以及对应的焦点逻辑.
 * BlockHolder负责一行内所有的逻辑, 包括如何绑定Controller, 点击, 上报等.
 * <br/>
 * 三 使用方法
 * 如何添加业务逻辑?
 * 1. 通过继承, 子类可以通过一系列hook方法, 实现业务逻辑:
 * -- {@link BlockHolder#onBindBlockData(BlockInfo)} ==> 用于绑定数据
 * -- {@link BlockHolder#onAttach()} ==> block被attach时的回调
 * -- {@link BlockHolder#onDetach()} ==> block被detach时的回调
 * -- {@link BlockHolder#handleFocus(RecyclerView, View, int)} ==> 处理block内的焦点逻辑
 * -- {@link BlockHolder#requestFocusFromBottom()} ==> 焦点从底部进入block时的焦点逻辑
 * -- {@link BlockHolder#requestFocusFromTop()} ==> 焦点从顶部进入block时的焦点逻辑
 * -- {@link BlockHolder#onControllerClick(PosterInfo)} ==> 处理controller被点击
 * -- {@link BlockHolder#doControllerClickReport(PosterInfo)} ==> 处理点击上报
 * -- {@link BlockHolder#processPosterInfo(PosterInfo)} ==> 处理Controller的poster info
 * 2. 对于复用度高的BlockHolder, 或者各block通用的逻辑, 可以通过回调实现业务逻辑:
 * -- {@link IBlockAttachListener} ==> block被attach, detach的回调
 * -- {@link IBlockClickListener} ==> block中controller被点击的回调
 * -- {@link IBlockBindListener} ==> block被bind时的回调
 * -- {@link IPosterInfoProcessor} ==> controller被绑定到框架时, 处理poster info数据
 * <br/>
 * 四 补充
 * 1. 回调过程中, 通过{@link PosterInfo}传递当前位置信息.
 * 2. 为了方便扩展回调, 使用抽象工厂{@link IBlockFactory}负责Block回调的创建.
 * 3. 为了将Controller注册到Block框架中,子类应使用
 * {@link BlockHolder#bindControllerData(BasePosterController, Poster, int)}绑定Controller数据
 */
public abstract class BlockHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnFocusChangeListener {

    @Nullable
    private IBlockAttachListener mAttachListener;
    @Nullable
    private IBlockClickListener mBlockClickListener;
    @Nullable
    private IBlockBindListener mBindListener;
    @Nullable
    private IPosterInfoProcessor mProcessor;
    @NonNull
    private final BlockInfo mBlockInfo = new BlockInfo(this);

    public BlockHolder(@NonNull View view) {
        super(view);
    }

    /**
     * 绑定 {@link RebuildDataModel} 数据
     */
    protected abstract void onBindBlockData(@NonNull BlockInfo info);

    @Override
    public final void onClick(View v) {
        PosterInfo posterInfo = BlockUtils.getPosterInfo(v);
        if (posterInfo == null)
            return;

        if (!onControllerClick(posterInfo) && mBlockClickListener != null)
            mBlockClickListener.onControllerClick(posterInfo);
        if (!doControllerClickReport(posterInfo) && mBlockClickListener != null)
            mBlockClickListener.doControllerClickReport(posterInfo);
    }

    @Override
    public final void onFocusChange(View v, boolean hasFocus) {
        PosterInfo posterInfo = BlockUtils.getPosterInfo(v);
        if (posterInfo != null) {
            posterInfo.getPosterController().onFocusChanged(hasFocus, v);
            onFocusChangePoster(v, hasFocus, posterInfo.getPosterController());
        }
    }

    public final void bindData(@Nullable RebuildDataModel model, int row) {
        mBlockInfo.setRow(row);
        mBlockInfo.setBlockModel(model);

        if (mBindListener != null)
            mBindListener.onBindBlockHolder(mBlockInfo);
        onBindBlockData(mBlockInfo);
    }

    final void setAttachListener(@Nullable IBlockAttachListener attachListener) {
        mAttachListener = attachListener;
    }

    final void setClickListener(@Nullable IBlockClickListener clickListener) {
        mBlockClickListener = clickListener;
    }

    final void setBindListener(@Nullable IBlockBindListener bindListener) {
        mBindListener = bindListener;
    }

    final void setPosterInfoProcessor(@Nullable IPosterInfoProcessor processor) {
        mProcessor = processor;
    }

    final void callAttach() {
        onAttach();
        if (mAttachListener != null)
            mAttachListener.onAttach(mBlockInfo);
    }

    final void callDetach() {
        onDetach();
        if (mAttachListener != null)
            mAttachListener.onDetach(mBlockInfo);
    }

    @NonNull
    public final BlockInfo getBlockInfo() {
        return mBlockInfo;
    }

    /**
     * 子类通过此方法, 绑定Controller数据并装配到Block框架
     */
    protected final void bindControllerData(@NonNull BasePosterController posterController,
                                            @Nullable Poster poster, int idx) {
        bindControllerData(posterController, poster, idx, true);
    }

    private void bindControllerData(@NonNull BasePosterController posterController,
                                    @Nullable Poster poster, int idx, boolean enableCtr) {
        View view = posterController.getView();
        if (view == null)
            return;

        // 1. bind data
        posterController.bindData(poster);

        // 2. create a empty poster info
        PosterInfo posterInfo = new PosterInfo(idx, mBlockInfo, poster, posterController);
        BlockUtils.setPosterInfo(view, posterInfo);

        // 3. process poster info
        processPosterInfo(posterInfo);
        if (mProcessor != null) {
            mProcessor.processPosterInfo(posterInfo);
        }

        // 4. set listener
        view.setOnClickListener(this);
        view.setOnFocusChangeListener(this);

        // 5. ctr
        bindReportEvent(view, posterInfo.getmReportEvent(), enableCtr);

    }

    private void bindReportEvent(View view, ReportEvent reportEvent, boolean enableCtr) {
        if (enableCtr) {
            CtrManager.setDataAndSkipTraversal(view, reportEvent);
        } else {
            CtrManager.skipTraversal(view);
        }
    }

    /**
     * attach回调
     */
    protected void onAttach() {
        // do nothing
    }

    /**
     * detach回调
     */
    protected void onDetach() {
        // do nothing
    }

    /**
     * BlockHolder被回收时回调
     */
    protected void onRecycled() {
        // do nothing
    }

    /**
     * {@link BlockRecyclerView}通过此方法在BlockHolder内寻找焦点, 子类可以通过覆盖此方法实现特殊焦点逻辑
     *
     * @return 找到的焦点
     */
    @Nullable
    public View handleFocus(@NonNull RecyclerView parent, @NonNull View focus, int direction) {
        return null;
    }

    /**
     * {@link BlockRecyclerView}通过此方法, 在从顶部进入BlockHolder时寻找焦点, 子类可以通过覆盖此方法实现特殊焦点逻辑
     */
    @Nullable
    public View requestFocusFromTop() {
        return null;
    }

    /**
     * {@link BlockRecyclerView}通过此方法, 在从底部进入BlockHolder时寻找焦点, 子类可以通过覆盖此方法实现特殊焦点逻辑
     */
    @Nullable
    public View requestFocusFromBottom() {
        return null;
    }

    protected void onFocusChangePoster(View v, boolean hasFocus, BasePosterController poster) {

    }

    /**
     * Controller级点击事件处理
     *
     * @param info
     * @return 若返回true, 不会进行下一级传递
     */
    protected boolean onControllerClick(@Nullable PosterInfo info) {
        return false;
    }

    /**
     * Controller级点击上报处理
     *
     * @param info
     * @return 若返回true, 不会进行下一级传递
     */
    protected boolean doControllerClickReport(@Nullable PosterInfo info) {
        return false;
    }

    /**
     * Controller级处理poster info
     */
    protected void processPosterInfo(@NonNull PosterInfo info) {
    }
}