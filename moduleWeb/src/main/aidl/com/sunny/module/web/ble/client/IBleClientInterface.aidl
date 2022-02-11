// IBleClientInterface.aidl
package com.sunny.module.web.ble.client;

// Declare any non-default types here with import statements

interface IBleClientInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    boolean sendMsg(String msg);

    String readMsg();
}