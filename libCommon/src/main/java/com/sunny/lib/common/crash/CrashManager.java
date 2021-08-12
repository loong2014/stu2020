package com.sunny.lib.common.crash;

import android.content.Context;

import com.sunny.lib.base.log.SunLog;
import com.sunny.lib.common.base.AppInitUtils;


/**
 * crash管理
 */
public class CrashManager {
    private static final String TAG = "CrashManager";

    private static class SingletonHolder {
        private static final CrashManager INSTANCE = new CrashManager();
    }

    public static CrashManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private CrashManager() {
    }

    public void init(Context context) {

        initBugReporter(context);

        CrashHandler.init(mCrashCallback);
    }

    private void initBugReporter(Context context) {
    }

    private final CrashHandler.CrashCallback mCrashCallback = new CrashHandler.CrashCallback() {
        @Override
        public boolean handleCrashException(Thread thread, Throwable throwable) {
            SunLog.i(TAG, "handleCrashException  :" + throwable);

            if (throwable == null) {
                return true;
            }

            if (interceptSysException(throwable)) {
                throwable.printStackTrace();
                AppInitUtils.doExitAppByCrash();
                return true;
            }

            dealCrashReport(throwable);

            return false;
        }
    };

    /**
     * 需要拦截的系统异常
     */
    private boolean interceptSysException(Throwable throwable) {

        /* 乐视系统报的传感器空指针异常 */
        if (throwable instanceof NullPointerException) {
            if (throwable.getMessage() != null && throwable.getMessage().equals("rhs == null")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 处理crash上报
     */
    private void dealCrashReport(Throwable throwable) {
        if (interceptCrashReport(throwable)) {
            return;
        }
    }

    /**
     * 拦截不需要上报的异常
     */
    private boolean interceptCrashReport(Throwable throwable) {
        /* 内存不足造成的崩溃 */
        if (throwable instanceof OutOfMemoryError) {
            return true;
        }

        return false;
    }

    static class CrashHandler implements Thread.UncaughtExceptionHandler {

        /**
         * 系统默认的异常处理
         */
        private final Thread.UncaughtExceptionHandler mDefaultHandler;

        /**
         * 系统crash回调处理
         */
        private final CrashCallback mCrashCallback;

        public static void init(CrashCallback callback) {
            new CrashHandler(callback);
        }

        CrashHandler(CrashCallback callback) {
            this.mCrashCallback = callback;
            this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            if (mCrashCallback != null) {
                if (mCrashCallback.handleCrashException(thread, ex)) {
                    return;
                }
            }

            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex);
            }
        }

        /**
         * 崩溃信息上传接口回调
         */
        public interface CrashCallback {
            /**
             * 处理系统异常
             *
             * @return 是否已经处理
             */
            boolean handleCrashException(Thread thread, Throwable throwable);
        }
    }
}
