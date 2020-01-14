package com.sunny.player.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.VideoView;

import com.sunny.lib.utils.SunLog;
import com.sunny.player.control.IVideoControl;
import com.sunny.player.control.IVideoViewListener;

import java.util.Map;

public class SunVideoView extends GLSurfaceView implements IVideoControl {

    private static final String TAG = "SunVideoView";

    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;
    public static final int STATE_STOPBACK = 6;
    public static final int STATE_ENFORCEMENT = 7;

    private Context mContext;
    private int mCurrentState = STATE_IDLE;

    private SunMediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;

    private Uri mUri;
    private Map<String, String> mHeaders;

    private IVideoViewListener mVideoViewListener;

    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private VideoView videoView;

    private float mLeftVolume = 1f;
    private float mRightVolume = 1f;

    public SunVideoView(Context context) {
        super(context);
        initView(context);
    }

    public SunVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void initView(Context context) {
        mContext = context;

        onStateChanged(STATE_IDLE);

        setVisibility(GONE);

        getHolder().addCallback(mSHCallback);

    }

    @Override
    public void setVideoViewListener(IVideoViewListener listener) {
        mVideoViewListener = listener;
    }

    @Override
    public View getVideoView() {
        return this;
    }

    @Override
    public SunMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void setVideoPath(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;

        openVideo();
    }

    private void openVideo() {
        if (mUri == null) {
            showLog("openVideo  uri is null");
            return;
        }

        if (mSurfaceHolder == null) {
            showLog("openVideo surfaceHolder is null");
            setVisibility(VISIBLE);
            return;
        }

        doVideoRelease(false);


        try {
            mMediaPlayer = new SunMediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);

            mMediaPlayer.setDataSource(mContext, mUri, mHeaders);

            mMediaPlayer.setDisplay(mSurfaceHolder);

            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            onStateChanged(STATE_PREPARING);

        } catch (Exception e) {
            e.printStackTrace();

            onStateChanged(STATE_ERROR);
        }
    }


    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            showLog("onPrepared");

            mp.setVolume(mLeftVolume, mRightVolume);

            onStateChanged(STATE_PREPARED);

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            showLog("onPrepared  width :" + mVideoWidth + " , height :" + mVideoHeight);

            if (mVideoWidth != 0 && mVideoHeight != 0) {

                getHolder().setFixedSize(mVideoWidth, mVideoHeight);

                if (mVideoWidth == mSurfaceWidth && mVideoHeight == mSurfaceHeight) {
                    if (mCurrentState == STATE_PLAYING) {
                        startPlay();
                    }
                }

            } else {
                if (mCurrentState == STATE_PLAYING) {
                    startPlay();
                }
            }
            mVideoViewListener.onPrepared();
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            showLog("onCompletion");

            onStateChanged(STATE_PLAYBACK_COMPLETED);

            mVideoViewListener.onCompletion();
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            showLog("onVideoSizeChanged  width :" + width + " , height :" + height);

            mVideoWidth = width;
            mVideoHeight = height;
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                getHolder().setFixedSize(mVideoWidth, mVideoHeight);
            }

            mVideoViewListener.onVideoSizeChanged(width, height);
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener
            = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            showLog("onBufferingUpdate  percent :" + percent);

            mVideoViewListener.onBufferTotalPercentUpdate(percent);
        }
    };

    private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            showLog("onSeekComplete");

            mVideoViewListener.onSeekComplete();
        }
    };


    private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            showLog("onInfo  what :" + what + " , extra :" + extra);

            mVideoViewListener.onInfo(what, extra);
            return false;
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            showLog("onError  what :" + what + " , extra :" + extra);

            onStateChanged(STATE_ERROR);

            mVideoViewListener.onError(what, extra);
            return true;
        }
    };

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            showLog("surfaceCreated");
            mSurfaceHolder = holder;

            showLog("openVideo by surfaceCreated");
            openVideo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            showLog("surfaceChanged  format :" + format + " , width :" + width + " , height :" + height);
            mSurfaceWidth = width;
            mSurfaceHeight = height;

            if (mMediaPlayer != null && mCurrentState == STATE_PLAYING
                    && mVideoWidth == mSurfaceWidth && mVideoHeight == mSurfaceHeight) {
                showLog("openVideo by surfaceChanged");
                openVideo();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            showLog("surfaceDestroyed");
            mSurfaceHolder = null;

            doVideoRelease(true);
        }
    };


    @Override
    public void startPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            onStateChanged(STATE_PLAYING);
        }
    }

    @Override
    public void pausePlay() {
        if (inPlaybackState()) {
            mMediaPlayer.pause();
            onStateChanged(STATE_PAUSED);
        }
    }

    @Override
    public void seekTo(int position) {
        if (inPlaybackState()) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public int getVideoWidth() {
        return mVideoWidth;
    }

    @Override
    public int getVideoHeight() {
        return mVideoHeight;
    }

    @Override
    public int getVideoDuration() {
        if (inPlaybackState()) {
            return mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (inPlaybackState()) {
            return mMediaPlayer.getCurrentPosition();
        }
        return -1;
    }

    @Override
    public boolean isPlaying() {
        return inPlaybackState() && mCurrentState != STATE_STOPBACK && mMediaPlayer.isPlaying();
    }

    @Override
    public boolean inPlaybackState() {
        return (mMediaPlayer != null && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
    }

    private void doVideoRelease(boolean resetUrl) {
        if (mMediaPlayer != null) {
            mCurrentState = STATE_IDLE;
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            onStateChanged(mCurrentState);
        }
        if (resetUrl) {
            mUri = null;
        }
    }

    private void onStateChanged(int state) {
        showLog("updateState  " + mCurrentState + " >>> " + state);
        mCurrentState = state;
    }

    private void showLog(String msg) {
        SunLog.INSTANCE.i(TAG, msg);
    }
}
