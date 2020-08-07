package com.sunny.family.flutter;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

/**
 * Created by zhangxin17 on 2020/8/6
 */
public class StuFlutterDemo extends FlutterActivity {

    // channel名称，唯一标识，用于与flutter之间的通信
    private static final String CHANNEL_A = "com.sunny.flutter.io/stu/android";
    private static final String CHANNEL_F = "com.sunny.flutter.io/stu/flutter";
    // 通过这个channel提供给flutter的方法
    private static final String ChannelMethodGerMsgFromA = "getMsgFromAndroid";
    private static final String ChannelMethodGetMsgFromF = "getMsgFromFlutter";


    private MethodChannel methodChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GeneratedPluginRegistrant.registerWith(this);
        doRegisterMethod();
    }

    private void doRegisterMethod() {

        // 注册监听处理fluttr端的调用
        new MethodChannel(getFlutterView(), CHANNEL_A)
                .setMethodCallHandler(new MethodChannel.MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                        // 在这里处理从flutter过来的调用
                        if (ChannelMethodGerMsgFromA.equals(methodCall.method)) {
                            String msg = getMsgFromAndroid();
                            if (!TextUtils.isEmpty(msg)) {
                                result.success(msg);
                            } else {
                                result.error("WAITE", "no new message", null);
                            }
                        } else {
                            result.notImplemented();
                        }
                    }
                });

        //
        methodChannel = new MethodChannel(getFlutterView(), CHANNEL_F);

    }

    // 异步获取返回结果
    private void getMsgFromFlutterAsync() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        methodChannel.invokeMethod(ChannelMethodGetMsgFromF, map,
                new MethodChannel.Result() {
                    @Override
                    public void success(Object o) {
                        // 获取返回结果
                        String msg = (String) o;
                    }

                    @Override
                    public void error(String s, String s1, Object o) {
                        // 调用异常

                    }

                    @Override
                    public void notImplemented() {
                        // 方法不存在

                    }
                });
    }

    private String getMsgFromAndroid() {

        return "来自android的信息：" + System.currentTimeMillis();
    }
}
