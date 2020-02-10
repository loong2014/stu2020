package com.sunny.lib.report;

import android.content.Context;
import android.view.View;

/**
 * Created by ZhangBoShi on 2019/12/9.
 */
public class ReportTool {
    private static final String TAG = "ReportTool";

    /**
     * Agnes初始化
     */
    public static void initAgnes(Context context) {
        if (context == null) {
            return;
        }
        initAgnesData(context);
    }

    private static void initAgnesData(Context context) {
    }

    /**
     * 页面曝光上报
     */
    public static void reportPage() {
    }

    /**
     * 区域的上报事件
     *
     * @param spm3 xxxxxx.xxxxxx.spm3
     */
    public static void reportArea(int spm3) {
    }

    /**
     * 资源位的上报事件
     * 上报类型 expose、click、move
     *
     * @param spm3 xxxxxx.xxxxxx.spm3.xxxxxx
     * @param spm4 xxxxxx.xxxxxx.xxxx.spm4
     */
    public static void reportContentClick(int spm3, int spm4) {
    }

    public static void reportContentExpose(int spm3, int spm4) {
    }

    /**
     * 推荐，周围看点等内容位的上报事件
     */
    public static void reportContent(ReportEvent info, String type) {
        reportContent(info, type, -1, -1);
    }

    /**
     * 顶部会员运营位的数据上报
     */
    public static void reportContent(ReportEvent info, String type, int spm3, int spm4) {
    }


    /**
     * 资源位的曝光上报事件绑定到view上，等view完全曝光两秒后，由系统上报工具ICtrTrigger上报
     *
     * @param view 本身
     * @param spm3 xxxxxx.xxxxxx.spm3.xxxxxx
     * @param spm4 xxxxxx.xxxxxx.xxxx.spm4
     */
    public static void bindContentExpose(View view, int spm3, int spm4) {
        ReportEvent event = new ReportEvent();
        bindContentExpose(view, event, spm3, spm4);
    }

    public static void bindContentExpose(View view, ReportEvent event, int spm3, int spm4) {
    }


}
