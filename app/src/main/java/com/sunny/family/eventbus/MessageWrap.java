package com.sunny.family.eventbus;

/**
 * Created by zhangxin17 on 2020/9/27
 * 事件封装对象
 */
public class MessageWrap {

    public final String message;

    public static MessageWrap getInstance(String msg) {
        return new MessageWrap(msg);
    }

    private MessageWrap(String msg) {
        this.message = msg;
    }
}
