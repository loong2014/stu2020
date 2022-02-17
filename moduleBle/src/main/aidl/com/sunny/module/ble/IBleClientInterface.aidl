// IBleClientInterface.aidl
package com.sunny.module.ble;

import  com.sunny.module.ble.IBleCallbackInterface;

// Declare any non-default types here with import statements

interface IBleClientInterface {

    void registerCallBack(IBleCallbackInterface callback);

    void unRegisterCallBack(IBleCallbackInterface callback);

    boolean sendMsg(String msg);

    String readMsg();

    String readDeviceInfo(int opt);

    boolean startScan();

    boolean stopScan();

    boolean startConnect(String address);

    boolean stopConnect();

    boolean sendOpt(int opt);
}