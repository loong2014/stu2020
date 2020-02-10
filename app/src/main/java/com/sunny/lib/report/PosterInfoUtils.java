package com.sunny.lib.report;

import androidx.annotation.Nullable;

import com.sunny.family.detail.view.common.PosterInfo;

/**
 * Created by ZhangBoshi on 2019/7/3.
 * 专门处理content层级数据的工具类(content对应spmId的第四级)
 * 包括上报和跳转信息
 */
public class PosterInfoUtils {
    private PosterInfoUtils() {
    }

    public static void commonProcess(@Nullable PosterInfo posterInfo) {
        if (posterInfo == null)
            return;

        int row = posterInfo.getBlockInfo().getRow();
        int contentIdx = posterInfo.getIdx() + 1;
        doPosterBaseProcess(posterInfo, row, contentIdx);
    }

    private static void doPosterBaseProcess(@Nullable PosterInfo posterInfo, int row, int content) {
        if (posterInfo == null)
            return;
//        // 数据上报
//        String spmId = ReportEventUtils.buildContentSpmId(6, row, content);
//        ReportEvent reportModel = posterInfo.getmReportEvent();
//        reportModel.put(ReportPropKeys.KEY_SPM_ID, spmId);
//        Poster poster = posterInfo.getPoster();
//        if (poster != null) {
//            // reportModel.put(ReportPropKeys.KEY_CONTENT_FROM,poster)
//            reportModel.put(ReportPropKeys.KEY_REC_ID, poster.getReid());
//             reportModel.put(ReportPropKeys.KEY_EXP_VAR_ID,poster.getBucket());
//            // reportModel.put(ReportPropKeys.KEY_REC_SOURCES,poster.)
//            reportModel.put(ReportPropKeys.KEY_AREA, poster.getAreaRec());
//            reportModel.put(ReportPropKeys.KEY_VID, poster.getVideoId());
//            reportModel.put(ReportPropKeys.KEY_ALBUM_ID, poster.getAlbumId());
//            reportModel.put(ReportPropKeys.KEY_CID, poster.getCategoryId());
//        }

    }
}
