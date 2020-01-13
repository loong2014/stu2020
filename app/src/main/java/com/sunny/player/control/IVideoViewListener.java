package com.sunny.player.control;

public interface IVideoViewListener {

    /**
     * 播放器初始化完成
     */
    void onPrepared();

    /**
     * 视频播放完毕
     */
    void onCompletion();

    /**
     * seek结束
     */
    void onSeekComplete();

    /**
     * buffer开始
     */
    void onBufferStart(int progress);

    /**
     * buffer结束
     */
    void onBufferEnd();

    /**
     * 视频缓冲总进度，即当前视频缓冲进度
     *
     * @param percent 缓冲进度
     */
    void onBufferTotalPercentUpdate(int percent);

    /**
     * 视频大小发生变化
     */
    void onVideoSizeChanged(int width, int height);

    /**
     * 播放器异常
     */
    void onError(int what, int extra);

    /**
     * 播放器信息
     */
    boolean onInfo(int what, int extra);

}
