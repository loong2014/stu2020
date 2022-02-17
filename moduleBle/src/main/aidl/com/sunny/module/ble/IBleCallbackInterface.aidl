// IBleCallbackInterface.aidl
package com.sunny.module.ble;

// Declare any non-default types here with import statements

interface IBleCallbackInterface {

    void onRcvClientMsg(in String msg);

    void onRcvServiceMsg(in String msg);

    void onConnectStateChanged(in int type,in String tip);
}