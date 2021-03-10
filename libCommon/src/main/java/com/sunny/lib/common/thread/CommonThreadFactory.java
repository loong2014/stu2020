package com.sunny.lib.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程创建工厂，添加线程名称的前缀，用于排查anr等线程问题
 */
public class CommonThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    CommonThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();

        if (prefix != null && prefix.trim().length() > 0) {
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-" + prefix + "-";
        } else {
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
