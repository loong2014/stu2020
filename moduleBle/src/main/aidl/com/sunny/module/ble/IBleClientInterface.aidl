// IBleClientInterface.aidl
package com.sunny.module.ble;

// Declare any non-default types here with import statements

interface IBleClientInterface {

    boolean sendMsg(String msg);

    String readMsg();

    String readDeviceInfo(int opt);

    boolean startScan();

    boolean stopScan();

    boolean startConnect(String address);

    boolean stopConnect();

    boolean sendOpt(int opt);
}