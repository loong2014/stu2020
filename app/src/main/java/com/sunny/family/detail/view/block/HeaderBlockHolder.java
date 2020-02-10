package com.sunny.family.detail.view.block;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sunny.family.R;
import com.sunny.family.detail.model.DetailHeaderModel;
import com.sunny.family.detail.model.DetailModelMoreResult;
import com.sunny.family.detail.model.DetailModelResult;
import com.sunny.family.detail.view.OnHeaderWidgetClickListener;
import com.sunny.family.detail.view.common.BasePosterController;
import com.sunny.family.detail.view.common.BlockHolder;
import com.sunny.family.detail.view.common.BlockInfo;
import com.sunny.family.detail.view.common.BlockUtils;
import com.sunny.family.detail.view.common.ControllerType;
import com.sunny.family.detail.view.common.HorizontalAdapter;
import com.sunny.family.detail.view.common.IControllerFactory;
import com.sunny.family.detail.view.common.Poster;
import com.sunny.family.detail.view.common.PosterInfo;
import com.sunny.family.detail.view.common.RebuildDataModel;
import com.sunny.family.detail.view.common.T10000WidgetController;
import com.sunny.family.detail.view.widget.WidgetType;
import com.sunny.lib.image.FrescoUtils;
import com.sunny.lib.report.ICtrTrigger;
import com.sunny.lib.utils.ResUtils;
import com.sunny.lib.utils.SunLog;
import com.sunny.player.helper.IPlayerViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther:libenqi
 * @date:2019/12/11
 * @email: libenqi1@le.com
 * @description:
 */
public class HeaderBlockHolder extends BlockHolder {

    private static final int NUMBER_OF_PER_SCREEN_MOVIE_VARIETY = 5;
    private static final int NUMBER_OF_VARIETY_SHOW_TOTAL = 8;
    private static final String TAG = "HeaderBlockHolder";
    private final Context mContext;
    private ICtrTrigger mCtrTrigger;
    private DetailModelResult.DetailModel detailModel;
    // private JsonDynamicResult dynamicModel;
    private DetailModelMoreResult.DetailModelMore detailModelMore;
    // 封面占位图
    private SimpleDraweeView mBigCoverPlan;
    //    private StargazerWidgetPresenter widgetPresenter = new StargazerWidgetPresenter();
    // 导航控件
//    private TopNavigationView mNavigationView;
    // 视频信息控件
//    private final TopVideoInfoView mVideoInfoView;
    // 电视剧或者综艺的容器
    private final FrameLayout mFrameLayout;
    private IPlayerViewHelper mPlayerViewHelper;

    private final IControllerFactory mControllerFactory;

    private List<Pair<String, Integer>> bottomPairList;

    private HeaderBlockHolder(View itemView, IControllerFactory factory,
                              RecyclerView.RecycledViewPool pool, OnHeaderWidgetClickListener widgetClickListener) {
        super(itemView);
        mControllerFactory = factory;
        mContext = itemView.getContext();
        mBigCoverPlan = itemView.findViewById(R.id.iv_header_cover_plan_place);
//        mNavigationView = itemView.findViewById(R.id.ttv_top_navigation);
//        mVideoInfoView = itemView.findViewById(R.id.viv_header_video_info);
//        mVideoInfoView.setWidgetClick(widgetClickListener);
        mFrameLayout = itemView.findViewById(R.id.detail_header_bottom);
//        widgetPresenter.setTopNavigationView(mNavigationView);
//        widgetPresenter.setAdSpaceLayout(mVideoInfoView.getAdSpaceLayout());
//        widgetPresenter.getStargazerData();

    }

    private void settingMergeData() {
        //
        FrescoUtils.setImageURI(detailModel.getBigImg(), mBigCoverPlan);

        // mVideoInfoView.setData(detailModel, dynamicModel.tags);
//        TvSeriesLayout seriesLayout = new TvSeriesLayout(mContext);
//        seriesLayout.setCtrTrigger(mCtrTrigger);
//        seriesLayout.setData(detailModel);
        mFrameLayout.removeAllViews();
//        mFrameLayout.addView(seriesLayout);
    }

    @Override
    protected void onBindBlockData(@NonNull BlockInfo info) {

        Log.e(TAG, "onBindBlockData");
        RebuildDataModel rebuildDataModel = info.getBlockModel();
        if (null == rebuildDataModel) {
            return;
        }
        DetailHeaderModel detailHeaderModel = (DetailHeaderModel) rebuildDataModel.getData();
        detailModel = detailHeaderModel.getDetailModel();
//        mVideoInfoView.setData(detailModel, detailModel.getTags());
//        if (TextUtils.equals(detailModel.getCategoryId(), Category.TV_SERIES.getCategoryId())) {
//            settingMergeData();
//        } else if (TextUtils.equals(detailModel.getCategoryId(),
//                Category.VARIETY_SHOW.getCategoryId())) {
//            initVarietyShowSelection(detailModel.getPositiveSeries(), 3);
//        }
        // if (DetailActivity.type == 2) {
        // settingMergeData();
        // } else if (DetailActivity.type == 3) {
        // initVarietyShowSelection(detailModel.getPositiveSeries(), 3);
        // }

    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Log.e(TAG, "onAttach");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }

    @Nullable
    @Override
    public View requestFocusFromTop() {
        Log.e(TAG, "requestFocusFromTop");
        return super.requestFocusFromTop();
    }

    @Nullable
    @Override
    public View requestFocusFromBottom() {
        Log.e(TAG, "requestFocusFromBottom");

        if (currentBottomView != null) {
            return currentBottomView;
        }
        return super.requestFocusFromBottom();
    }

    @Override
    protected void onRecycled() {
        super.onRecycled();
        currentTopView = null;
        currentBottomView = null;
        currentBottomController = null;
//        if (mNavigationView != null) {
//            mNavigationView.deleteObserver();
//        }

        Log.e(TAG, "onRecycled");
    }

    public static HeaderBlockHolder create(@NonNull ViewGroup parent,
                                           @NonNull IControllerFactory factory,
                                           @NonNull OnHeaderWidgetClickListener widgetClickListener) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.block_header, parent, false);
        return new HeaderBlockHolder(itemView, factory, null, widgetClickListener);
    }

    private int currentTopPostion;
    private int currentBottomPostion;
    private View currentTopView;
    private View currentBottomView;
    private BasePosterController currentBottomController;
    private RecyclerView topRecler;
    private RecyclerView bottomRecler;

    @Override
    protected void processPosterInfo(@NonNull PosterInfo info) {
        super.processPosterInfo(info);
        int contentIdx = info.getIdx() + 1;
//        String spmId = ReportEventUtils.buildContentSpmId(5, 1, contentIdx);
//        info.getmReportEvent().put(ReportPropKeys.KEY_SPM_ID, spmId);

    }

    @Override
    protected void onFocusChangePoster(View v, boolean hasFocus,
                                       BasePosterController posterController) {
        super.onFocusChangePoster(v, hasFocus, posterController);

        if (hasFocus) {
            Poster poster = posterController.getData();
            if (poster == null) {
                Log.i(TAG, "onFocusChangePoster.poster==null");
                return;
            }
            if (poster.getVarietyTopBottom() == 1) {
                currentTopView = v;
                Log.i(TAG, "poster.getBottomPostion()=" + poster.getBottomPostion()
                        + ",currentBottomPostion=" + currentBottomPostion);
                if (poster.getBottomPostion() != currentBottomPostion && bottomRecler != null
                        && bottomRecler.getLayoutManager() != null) {
                    currentBottomPostion = poster.getBottomPostion();
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) bottomRecler
                            .getLayoutManager();
                    linearLayoutManager.smoothScrollToPosition(bottomRecler,
                            new RecyclerView.State(), currentBottomPostion);
                    currentBottomView = linearLayoutManager
                            .findViewByPosition(currentBottomPostion);
                    if (currentBottomView == null) {
                        currentBottomView = bottomRecler.getChildAt(currentBottomPostion);
                    }
                    if (currentBottomController != null) {
                        currentBottomController.onViewSelect(false);
                    }
                    Log.i(TAG, "currentBottomView=" + currentBottomView);
                    if (BlockUtils.getPosterInfo(currentBottomView) != null) {
                        currentBottomController = BlockUtils.getPosterInfo(currentBottomView)
                                .getPosterController();
                        Log.i(TAG, "onViewSelect=true");
                        currentBottomController.onViewSelect(true);
                    }
                }

            } else if (poster.getVarietyTopBottom() == 2) {
                if (currentBottomView != v) {
                    if (currentBottomController != null) {
                        currentBottomController.onViewSelect(false);
                    }
                    currentBottomView = v;
                    if (BlockUtils.getPosterInfo(currentBottomView) != null) {
                        currentBottomController = BlockUtils.getPosterInfo(currentBottomView)
                                .getPosterController();
                    }
                }
                if ((poster.getTopStartPostion() > currentTopPostion
                        || currentTopPostion > poster.getTopEndPostion()) && topRecler != null
                        && topRecler.getLayoutManager() != null) {
                    currentTopPostion = poster.getTopStartPostion();
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) topRecler
                            .getLayoutManager();
                    linearLayoutManager.smoothScrollToPosition(topRecler, new RecyclerView.State(),
                            currentTopPostion);

                    currentTopView = linearLayoutManager.findViewByPosition(currentTopPostion);

                }
            }
        }
    }

    @Nullable
    @Override
    public View handleFocus(@NonNull RecyclerView parent, @NonNull View focus, int direction) {

        SunLog.INSTANCE.i(TAG, "handleFocus----view.id=" + focus.getId());

//        View nextView = mVideoInfoView.handleFocus(parent, focus, direction, currentTopView);
//        if (nextView != null) {
//            return nextView;
//        }

        if (focus == currentTopView && direction == View.FOCUS_DOWN && currentBottomView != null) {
            return currentBottomView;
        } else if (focus == currentBottomView) {
            if (direction == View.FOCUS_UP) {
                if (currentBottomController != null) {
                    currentBottomController.onViewSelect(true);
                }
                if (topRecler.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    return currentBottomView;
                }
                return currentTopView;
            } else if (direction == View.FOCUS_DOWN && currentBottomController != null) {
                currentBottomController.onViewSelect(true);
            }
        }

        return super.handleFocus(parent, focus, direction);
    }

    /**
     * 综艺选集(正片)
     * 1.有期数按月分组 做多显示 6 个月的区间多则显示更多按钮
     */
    private void initVarietyShowSelection(List<Poster> posterList, int type) {
        // TODO 两边,执行局部刷新
        View varietyToot = LayoutInflater.from(mFrameLayout.getContext())
                .inflate(R.layout.block_header_variety, mFrameLayout, false);
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(varietyToot);

        boolean sectionByMonth = isSectionByMonth(posterList);
        if (posterList.size() <= NUMBER_OF_VARIETY_SHOW_TOTAL) { // 总数8以内，直接展示节目列表
            showVarietyTopList(posterList, varietyToot, type);
        } else {// 节目列表+选集按钮
            List<Poster> tabPoster;
            if (sectionByMonth) {
                tabPoster = getNumberOfVarietyByMonth(posterList);
            } else {
                tabPoster = getNumberOfVarietyByNumber(posterList);
            }
            showVarietyTopList(posterList, varietyToot, type);
            if (tabPoster.size() > 0) {
                showVarietyBottomList(tabPoster, varietyToot);
            }
            currentTopPostion = 0;
            currentBottomPostion = 0;
        }
    }

    private void showVarietyTopList(List<Poster> posterList, View rootView, final int type) {

        TextView mTitle = rootView.findViewById(R.id.title);
        topRecler = rootView.findViewById(R.id.recycler_view);
        HorizontalAdapter.AdapterCallback callback = new HorizontalAdapter.AdapterCallback() {
            @Override
            public void onBindController(@NonNull BasePosterController controller,
                                         @Nullable Poster poster, int position) {
                if (controller instanceof T10000WidgetController) {

                    if (type == 3) {
                        T10000WidgetController controller1 = (T10000WidgetController) controller;
                        controller1.reset(WidgetType.WIDGET_101, WidgetType.WIDGET_102);
                    } else {
                        T10000WidgetController controller1 = (T10000WidgetController) controller;
                        controller1.reset(WidgetType.WIDGET_103);
                    }
                    bindControllerData(controller, poster, position);
                }
                View view = controller.getView();
                if (position == 1 && view != null) {
                    view.setId(R.id.detail_header_variety_down);
                } else if (position == 0) {
                    currentTopView = view;
                }

            }

            @Override
            public int getControllerType(int position) {
                return ControllerType.WIDGET_10000;
            }
        };

        int itemWidth;
        ViewGroup.LayoutParams params = topRecler.getLayoutParams();
        if (type == 3) {
            params.height = ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.block_varity_recler_h);
            itemWidth = ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.block_s1002_height);
        } else {
            itemWidth = ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.block_number_w_h);
            params.height = itemWidth;
        }
        topRecler.setLayoutParams(params);

        HorizontalAdapter mAdapter = HorizontalAdapter.Builder.create().setCallback(callback)
                .setControllerFactory(mControllerFactory).setRecyclerView(topRecler)
                .setICtrTrigger(mCtrTrigger).setWidth(itemWidth).setScrollSpeed(0.9f)
                .setRecyclerViewPool(null).build();
        topRecler.setAdapter(mAdapter);
        mAdapter.bindData(posterList);

    }

    private void showVarietyBottomList(List<Poster> posterList, View rootView) {

        bottomRecler = rootView.findViewById(R.id.recycler_view_bottom);
        bottomRecler.setVisibility(View.VISIBLE);
        HorizontalAdapter.AdapterCallback callback = new HorizontalAdapter.AdapterCallback() {
            @Override
            public void onBindController(@NonNull BasePosterController controller,
                                         @Nullable Poster poster, int position) {
                if (controller instanceof T10000WidgetController) {

                    T10000WidgetController controller1 = (T10000WidgetController) controller;
                    controller1.reset(WidgetType.WIDGET_103);

                    bindControllerData(controller, poster, position);
                }
                View view = controller.getView();
                if (position == 0) {
                    currentBottomController = controller;
                    controller.onViewSelect(true);
                    currentBottomView = view;
                }
            }

            @Override
            public int getControllerType(int position) {
                return ControllerType.WIDGET_10000;
            }
        };

        HorizontalAdapter mAdapter = HorizontalAdapter.Builder.create().setCallback(callback)
                .setControllerFactory(mControllerFactory).setRecyclerView(bottomRecler)
                .setWidth(ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.variety_tab_width))
                .setScrollSpeed(0.4f).setRecyclerViewPool(null).build();
        bottomRecler.setAdapter(mAdapter);
        mAdapter.bindData(posterList);

    }

    /**
     * 解析综艺正片list判断按月份分组还是按常量分组
     */
    private boolean isSectionByMonth(List<Poster> modelList) {

        boolean result = false;
        for (int i = 0; i < modelList.size(); i++) {
            Poster series = modelList.get(i);
            // 20190623
            String period = series.getEpisode();
            // 如果字段为null,或者长度<8
            result = !TextUtils.isEmpty(period) && period.length() >= 8;
        }
        return result;
    }

    /**
     * 综艺正片按月分组
     */
    private List<Poster> getNumberOfVarietyByMonth(List<Poster> positiveSeries) {
        List<Poster> seriesTabs = new ArrayList<>();
        Poster mTabModel;
        String tabText = "";
        int tabPostion = -1;
        Poster preTopModel = null;
        for (int i = 0; i < positiveSeries.size(); i++) {
            String period = positiveSeries.get(i).getEpisode();
            if (TextUtils.isEmpty(period)) {
                continue;
            }

            period = period.trim();
            String yearHeader = period.substring(2, 4); // 截取年后两位 18年
            String month = period.substring(4, 6); // 截取 10月
            // 获取格式如: 17年10月;
            String yearMonth = mContext.getString(R.string.n_year_n_month, yearHeader, month);
            if (!TextUtils.equals(tabText, yearMonth)) {
                tabPostion++;
                tabText = yearMonth;
                mTabModel = new Poster();
                mTabModel.setName(yearMonth);
                mTabModel.setTopStartPostion(i);
                mTabModel.setVarietyTopBottom(2);
                if (preTopModel != null) {
                    seriesTabs.add(preTopModel);
                }
                preTopModel = mTabModel;
            } else {
                if (preTopModel != null)
                    preTopModel.setTopEndPostion(i);
            }
            positiveSeries.get(i).setBottomPostion(tabPostion);
            positiveSeries.get(i).setVarietyTopBottom(1);
        }
        if (preTopModel != null) {
            seriesTabs.add(preTopModel);
        }
        return seriesTabs;
    }

    /**
     * 按数字分组
     */
    private List<Poster> getNumberOfVarietyByNumber(List<Poster> positiveSeries) {

        List<Poster> seriesTabs = new ArrayList<>();
        int i = 0;
        Poster poster;
        Poster preTopModel = null;
        int startEisode = 0, endEisode;
        int tabPostion = 0;

        for (int index = 0; index < positiveSeries.size(); index++) {
            String period = positiveSeries.get(i).getEpisode();
            if (TextUtils.isEmpty(period)) {
                continue;
            }

            if (i == 0) {
                startEisode = index + 1;
                endEisode = index + 1;
            } else {
                endEisode = index + 1;
            }
            if (++i == NUMBER_OF_PER_SCREEN_MOVIE_VARIETY) {
                tabPostion++;
                poster = new Poster();
                poster.setTopStartPostion(index);
                poster.setVarietyTopBottom(2);
                poster.setName(startEisode + "-" + endEisode);
                if (preTopModel != null) {
                    seriesTabs.add(preTopModel);
                }
                preTopModel = poster;
                i = 0;
            } else {
                if (preTopModel != null)
                    preTopModel.setTopEndPostion(index);
            }
            positiveSeries.get(index).setBottomPostion(tabPostion);
            positiveSeries.get(index).setVarietyTopBottom(1);
        }
        if (preTopModel != null) {
            seriesTabs.add(preTopModel);
        }

        return seriesTabs;
    }

    public void setCtrTrigger(ICtrTrigger mCtrTrigger) {
        this.mCtrTrigger = mCtrTrigger;
    }

    public void setPlayerViewHelper(IPlayerViewHelper mPlayerViewHelper) {
        this.mPlayerViewHelper = mPlayerViewHelper;
    }
}
