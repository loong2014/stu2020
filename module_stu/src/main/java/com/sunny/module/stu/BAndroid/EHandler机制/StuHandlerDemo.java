package com.sunny.module.stu.BAndroid.EHandler机制;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Printer;

import timber.log.Timber;

public class StuHandlerDemo {

    public static void startMonitoring() {
        Looper.getMainLooper().setMessageLogging(new Printer() {

            @Override
            public void println(String x) {
                if (x.startsWith(">>>>> Dispatching")) {
                    loggingRunnable.begin(x);
                } else if (x.startsWith("<<<<< Finished")) {
                    loggingRunnable.finish();
                }
            }
        });

        MessageQueue queue = Looper.myQueue();
        queue.addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Timber.w("消息处理完成，当前时间：" + System.currentTimeMillis());
                // 返回 true，这个 IdleHandler 将会被保留在消息队列中，以后每次队列出现空闲，都会调用一次
                return true;  // 如果返回 false，这个 IdleHandler 将会被移除
            }
        });

    }

    private static final Handler logHandler = buildLogHandler();

    public static Handler buildLogHandler() {
        HandlerThread h = new HandlerThread("logHandler");
        h.start();
        return new Handler(h.getLooper());
    }

    private static final AnrRunnable loggingRunnable = new AnrRunnable();

    private static final class AnrRunnable implements Runnable {

        private static final long MONITOR_INTERVAL = 3000L;

        private String curMsgInfo;

        public void begin(String info) {
            this.curMsgInfo = info;
            logHandler.postDelayed(this, MONITOR_INTERVAL);
        }

        public void finish() {
            logHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            Timber.i("Detected possible ANR on the main thread by %s", curMsgInfo);
        }
    }

}
