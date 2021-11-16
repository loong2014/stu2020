package com.sunny.lib.common.fresco;

import android.graphics.Bitmap;

/**
 * 获取图片对应bitmap的通用接口
 * @author jiwenjie
 * @date 2017/8/24
 */
public interface ICommonGetBitmap {
    /**
     * 获取图片对应bitmap的回调
     * @param bitmap null if no bitmap returned
     */
    void onGetBitmap(Bitmap bitmap);
}
