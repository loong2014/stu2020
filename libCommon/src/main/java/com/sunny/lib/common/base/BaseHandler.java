package com.sunny.lib.common.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * 自定义Handler
 * 使用弱引用防止内存泄漏
 */
public abstract class BaseHandler<T> extends Handler {
    public WeakReference<T> weakReference;

    public BaseHandler(@NonNull T data) {
        this.weakReference = new WeakReference<>(data);
    }

    public BaseHandler(@NonNull T data, @NonNull @NotNull Looper looper) {
        super(looper);
        this.weakReference = new WeakReference<>(data);
    }

    public BaseHandler(@NonNull T data, Callback callback) {
        super(callback);
        this.weakReference = new WeakReference<>(data);
    }

    @Override
    public void handleMessage(Message msg) {
        if (weakReference != null && weakReference.get() != null) {
            disposeMessage(msg);
        }
    }

    /**
     * 引用未被销毁继续执行事件
     */
    public abstract void disposeMessage(Message msg);
}
