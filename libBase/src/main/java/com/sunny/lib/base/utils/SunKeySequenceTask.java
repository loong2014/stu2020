package com.sunny.lib.base.utils;

import android.os.SystemClock;
import android.view.KeyEvent;

import com.sunny.lib.base.log.SunLog;

/**
 * 处理按键序列任务
 */
public class SunKeySequenceTask {

    private final String TAG = "SunKeySequenceTask";

    private static final int MAX_KEYVENT_DURATION = 2000;// max key event interval
    private final String name;
    private final int[] keyCodes;
    private final int keyCodeCount;
    private int lastIndex;
    private long lastOnKeyTime;
    private KeySequenceTaskCallback callback;

    public SunKeySequenceTask(String name, int[] keyCodes, KeySequenceTaskCallback callback) {
        this.name = name;
        this.keyCodes = keyCodes;
        this.keyCodeCount = keyCodes != null ? keyCodes.length : 0;
        this.callback = callback;
        this.reset();
    }

    public void onKeyEvent(int keyCode, KeyEvent event) {
        if (keyCodeCount <= 0) {
            return;
        }
        if (event.getAction() == KeyEvent.ACTION_UP) {
            long curTime = SystemClock.elapsedRealtime();
            if (curTime - this.lastOnKeyTime > MAX_KEYVENT_DURATION) {
                this.reset();// if the interval time bigger than the max key event interval, reset
            }
            int curIndex = this.lastIndex + 1;
            if (curIndex < this.keyCodeCount && this.keyCodes[curIndex] == keyCode) {//find the matching key code
                if (curIndex == this.keyCodeCount - 1) {// the last key code
                    this.log("task " + this.name + " is triggered");
                    this.reset();
                    // callback method
                    if (this.callback != null) {
                        this.callback.onTrigger();
                    }
                } else { // set the current key code and time
                    this.lastOnKeyTime = curTime;
                    this.lastIndex = curIndex;
                }
            } else {// the wrong value, reset
                this.reset();
            }
        }
    }

    /**
     * Reset the last key value and the time when pressed
     */
    private void reset() {
        this.lastIndex = -1;
        this.lastOnKeyTime = 0;
    }

    private void log(String msg) {
        SunLog.i(TAG, msg);
    }

    /**
     * Called when find the matching key group
     */
    public static interface KeySequenceTaskCallback {
        public void onTrigger();
    }
}
