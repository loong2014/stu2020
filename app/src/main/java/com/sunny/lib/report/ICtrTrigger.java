package com.sunny.lib.report;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ZhangBoShi on 2019/3/1.
 */

public interface ICtrTrigger {
    void triggerReport();

    void clearAndTriggerReport();

    void clearCache();

    void cancelReport();

    // 默认实现
    class BaseCtrTrigger implements ICtrTrigger {
        private final int userId;

        // todo ctr manager使用tag+hashcode区分用户
        public BaseCtrTrigger(@NonNull String tag, @Nullable ReportTool reporter,
                              @Nullable View root) {
            if (reporter == null)
                throw new IllegalArgumentException("reporter can not be null!");
            if (root == null)
                throw new IllegalArgumentException("root can not be null!");

            userId = hashCode();
            CtrConfig.Builder builder = new CtrConfig.Builder();
            CtrManager.init(userId, root, builder.isInitOn918(false).build(),
                    new CtrManager.CallBack() {
                        @Override
                        public void ctrReport(Object reportData) {
                            if (reportData instanceof ReportEvent) {
                                ReportEvent reportEvent = (ReportEvent) reportData;
//                                ReportTool.reportContent(reportEvent, ReportPropKeys.TYPE_EXPOSE);
                            }
                        }
                    });
        }

        @Override
        public void triggerReport() {
            CtrManager.triggerReport(userId);
        }

        @Override
        public void clearAndTriggerReport() {
            CtrManager.clearAndTriggerReport(userId);
        }

        @Override
        public void clearCache() {
            CtrManager.clearCache(userId);
        }

        @Override
        public void cancelReport() {
            CtrManager.cancelReport(userId);
        }

        public void destroy() {
            CtrManager.destroy(userId);
        }

    }

}
