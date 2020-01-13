package com.sunny.player.control;

import android.net.Uri;
import android.view.View;

import com.sunny.player.view.SunMediaPlayer;

import java.util.Map;

public interface IVideoControl {

    void setVideoViewListener(IVideoViewListener listener);

    View getVideoView();

    SunMediaPlayer getMediaPlayer();

    void setVideoPath(Uri uri, Map<String, String> headers);

    void startPlay();

    void pausePlay();

    void seekTo(int position);

    int getVideoWidth();

    int getVideoHeight();

    int getVideoDuration();

    int getCurrentPosition();

    boolean isPlaying();

    boolean inPlaybackState();
}
