package com.sunny.family.livedata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import androidx.lifecycle.LiveData;

import com.sunny.lib.utils.SunLog;

import java.lang.ref.WeakReference;

/**
 * Created by zhangxin17 on 2020/7/13
 */
public class MyLiveData extends LiveData<Integer> {
    private static final String TAG = "MyLiveData";

    private static MyLiveData sData;
    private WeakReference<Context> mContextWeakReference;

    public static MyLiveData getInstance(Context context) {
        if (sData == null) {
            sData = new MyLiveData(context);
            sData.setValue(-1);
        }
        return sData;
    }

    private MyLiveData(Context context) {
        mContextWeakReference = new WeakReference<>(context);
    }

    /**
     * observer个数从0到1时，被调用
     */
    @Override
    protected void onActive() {
        super.onActive();
        registerReceiver();
    }

    /**
     * observer个数从1到0时，被调用
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        unregisterReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mContextWeakReference.get().registerReceiver(mReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        mContextWeakReference.get().unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            SunLog.i(TAG, "onReceive  action :" + action);

            if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
                int wifiRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -200);
                int wifiLevel = WifiManager.calculateSignalLevel(wifiRssi, 4);
//                sData.setValue(wifiLevel);
            }
        }
    };

    public void updateValue(int num){
        sData.setValue(num);
    }
}
