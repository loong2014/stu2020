package com.sunny.lib.report;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.family.R;
import com.sunny.lib.utils.AppConfigUtils;
import com.sunny.lib.utils.SunLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ZhangBoShi on 2019/3/1.
 * 默认配置在918不上报，可以自己利用config方法配置
 * <p>
 * 一、初始化（可以传唯一int值区分多用户，默认传 {@link CtrManager#DEFAULT_USER} ）
 * {@link CtrManager#init(int, View, CtrConfig, CallBack)}
 * 二、销毁
 * {@link CtrManager#destroy(int)}
 * 三、通知
 * {@link CtrManager#clearCache()}
 * {@link CtrManager#triggerReport()}
 * {@link CtrManager#clearAndTriggerReport()}
 * {@link CtrManager#cancelReport()}
 * 四、设置上报数据
 * CtrManager.setData(view, data);或者
 * CtrManager.setDataAndSkipTraversal(view, data);
 * 五、优化（跳过遍历）
 * CtrManager.skipTraversal(View)
 */
public class CtrManager {
    @NonNull
    private static final SparseArray<User> users = new SparseArray<>();
    @NonNull
    private static final Object mDefaultTag = new Object();

    private static final String TAG = CtrManager.class.getSimpleName();
    private static final int REPORT = R.id.CTR_REPORT;
    private static final int IGNORE = R.id.CTR_IGNORE;
    private static final int REPORT_IGNORE = R.id.CTR_REPORT_IGNORE;
    private static final long TIME_STATISTIC = 2000L;
    private static final int DEFAULT_USER = 0;

    private CtrManager() {
    }

    public static void init(@NonNull View rootView, @Nullable CtrConfig configuration,
                            @NonNull CallBack callback) {
        doInit(DEFAULT_USER, rootView, configuration, callback);
    }

    public static void init(int userId, @NonNull View rootView, @Nullable CtrConfig configuration,
                            @NonNull CallBack callback) {
        if (userId == DEFAULT_USER)
            throw new IllegalArgumentException("userId 0 is for default user!");
        doInit(userId, rootView, configuration, callback);
    }

    private static void doInit(int userId, @NonNull View rootView,
                               @Nullable CtrConfig configuration, @NonNull CallBack callback) {
        if (users.get(userId) == null) {
            users.put(userId, new User(userId, callback, rootView, configuration));
        }
    }

    /**
     * 设置Ctr上报数据(如果view是ViewGroup,CTR会继续向下遍历)
     *
     * @param view 需要完全展示时判断上报的View
     * @param data 上报data
     */
    public static void setData(@NonNull View view, @NonNull Object data) {
        view.setTag(REPORT, data);
    }

    /**
     * 标记当前View的child不要遍历
     */
    public static void skipTraversal(@NonNull View view) {
        view.setTag(IGNORE, mDefaultTag);
    }

    /**
     * 设置Ctr上报数据(同时标记当前View的child不要遍历)
     *
     * @param view 需要完全展示时判断上报的View
     * @param data 上报data
     */
    public static void setDataAndSkipTraversal(@NonNull View view, @Nullable Object data) {
        view.setTag(REPORT_IGNORE, data);
    }

    public static void triggerReport() {
        triggerReport(DEFAULT_USER);
    }

    public static void triggerReport(int userId) {
        if (users.get(userId) != null) {
            users.get(userId).triggerReport();
        }
    }

    public static void clearAndTriggerReport() {
        clearAndTriggerReport(DEFAULT_USER);
    }

    public static void clearAndTriggerReport(int userId) {
        if (users.get(userId) != null) {
            users.get(userId).clearCache();
            users.get(userId).triggerReport();
        }
    }

    public static void clearCache() {
        clearCache(DEFAULT_USER);
    }

    public static void clearCache(int userId) {
        if (users.get(userId) != null) {
            users.get(userId).clearCache();
        }
    }

    public static void cancelReport() {
        cancelReport(DEFAULT_USER);
    }

    public static void cancelReport(int userId) {
        if (users.get(userId) != null) {
            users.get(userId).cancelReport();
        }
    }

    public static void destroy() {
        destroy(DEFAULT_USER);
    }

    public static void destroy(int userId) {
        if (users.get(userId) != null) {
            users.get(userId).destroy();
            users.delete(userId);
        }
    }

    public interface CallBack {
        void ctrReport(Object reportData);
    }

    private static class User {
        @NonNull
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        @NonNull
        private final Set<Object> mReportCache = new HashSet<>();
        @NonNull
        private final ReportRunnable mReportRunnable = new ReportRunnable();
        @NonNull
        private final CtrConfig mConfig;
        @Nullable
        private CallBack mCallBack;
        @Nullable
        private View mRootView;

        private int id;
        private boolean isDestroy = false;

        private User(int id, @NonNull CallBack callBack, @NonNull View rootView,
                     @NonNull CtrConfig config) {
            this.id = id;
            mCallBack = callBack;
            mRootView = rootView;
            mConfig = config;

            handleConfig();
        }

        private static boolean isChildFullVisibleInParent(@NonNull ViewGroup parent, View child) {
            boolean visible = false;
            if (child != null) {
                Rect pBounds = new Rect();
                Rect cBounds = new Rect();
                parent.getDrawingRect(pBounds);
                child.getDrawingRect(cBounds);
                parent.offsetDescendantRectToMyCoords(child, cBounds);
                if (pBounds.contains(cBounds)) {
                    visible = true;
                }
            }
            return visible;
        }

        /**
         * 触发两秒上报任务，同时清除上报缓存（对应桌面切换和数据刷新等情况）
         */
        private void triggerReport() {
            if (!isDestroy) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(mReportRunnable, CtrManager.TIME_STATISTIC);
            }
        }

        /**
         * 触发两秒上报任务，同时清除上报缓存（对应桌面切换和数据刷新等情况）
         */
        private void clearCache() {
            if (!isDestroy) {
                mReportCache.clear();
            }
        }

        /**
         * 取消上报任务（对应桌面切走等情况）
         */
        private void cancelReport() {
            if (!isDestroy) {
                mHandler.removeCallbacksAndMessages(null);
            }
        }

        private void handleConfig() {
            if (!mConfig.isInitOn918() && AppConfigUtils.INSTANCE.isLowCostDevice()) {
                destroy();
            }
        }

        private boolean isFullVisibleInRootView(View child) {
            return mRootView instanceof ViewGroup
                    && isChildFullVisibleInParent((ViewGroup) mRootView, child);
        }

        private TraversalRecord findReportData(ViewGroup parent) {
            if (parent == null)
                return null;
            TraversalRecord record = new TraversalRecord();
            record.depth += 1;
            int childDepth = 0;
            int childNodes = 0;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE && child.getTag(IGNORE) == null) {
                    Object data;
                    if ((data = child.getTag(REPORT_IGNORE)) != null) {
                        if (isFullVisibleInRootView(child)) {
                            record.mayReportList.add(data);
                        }
                        continue;
                    }
                    if ((data = child.getTag(REPORT)) != null && isFullVisibleInRootView(child)) {
                        record.mayReportList.add(data);
                    }
                    if (child instanceof ViewGroup) {
                        TraversalRecord childRecord = findReportData((ViewGroup) child);
                        record.mayReportList.addAll(childRecord.mayReportList);
                        childNodes += childRecord.nodes;
                        if (childRecord.depth > childDepth) {
                            childDepth = childRecord.depth;
                        }
                    }
                }
            }
            record.nodes += childCount + childNodes;
            record.depth += childDepth;
            return record;
        }

        // /**
        // * @param data 上报数据
        // * @return data是否需要上报
        // */
        // private boolean isNeed2Report(Object data) {
        // return !mReportCache.contains(data);
        // }

        private class ReportRunnable implements Runnable {
            @Override
            public void run() {
                if (isDestroy || mRootView == null || mCallBack == null)
                    return;

                List<Object> mayReportList = null;
                if (mRootView instanceof ViewGroup) {
                    TraversalRecord record = findReportData((ViewGroup) mRootView);
                    mayReportList = record.mayReportList;
                    // 根处深度定义为1
                    SunLog.INSTANCE.i(TAG,
                            "user: " + id + ", 遍历节点数 = " + record.nodes + ", 深度 = " + record.depth);
                } else if (mRootView.getTag(REPORT) != null) {
                    mayReportList = new ArrayList<>();
                    mayReportList.add(mRootView.getTag(REPORT));
                    SunLog.INSTANCE.i(TAG, "user: " + id + ", 遍历节点数 = 1, 深度 = 1");
                }
                List<Object> trueReportList = new ArrayList<>();
                if (mayReportList != null) {
                    for (Object data : mayReportList) {
                        if (!mReportCache.contains(data)) {
                            mReportCache.add(data);
                            trueReportList.add(data);
                        }
                    }
                }
                for (Object obj : trueReportList) {
                    mCallBack.ctrReport(obj);
                }
            }
        }

        private void destroy() {
            mReportCache.clear();
            mHandler.removeCallbacksAndMessages(null);
            mCallBack = null;
            mRootView = null;
            isDestroy = true;
        }
    }

    private static class TraversalRecord {
        List<Object> mayReportList = new ArrayList<>();
        int nodes;
        int depth;
    }
}