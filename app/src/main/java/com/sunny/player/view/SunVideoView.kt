package com.sunny.player.view

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.sunny.lib.utils.SunLog
import com.sunny.player.control.IVideoControl
import com.sunny.player.control.IVideoViewListener

class SunVideoView : SurfaceView, IVideoControl {

    private val logTag = SunLog.buildPlayerTag("SunVideoView")

    companion object {
        val STATE_ERROR: Int = -1
        val STATE_IDLE: Int = 0
        val STATE_PREPARING: Int = 1
        val STATE_PREPARED: Int = 2
        val STATE_PLAYING: Int = 3
        val STATE_PAUSED: Int = 4
        val STATE_PLAYBACK_COMPLETED: Int = 5
        val STATE_STOPBACK: Int = 6
    }

    private var mContext: Context = context
    private var mCurrentState = STATE_IDLE

    private var mMediaPlayer: SunMediaPlayer? = null
    private var mSurfaceHolder: SurfaceHolder? = null

    private var mUri: Uri? = null
    private var mHeaders: Map<String, String>? = null

    private var mVideoViewListener: IVideoViewListener? = null

    private var mVideoWidth: Int = 0
    private var mVideoHeight: Int = 0
    private var mSurfaceWidth: Int = 0
    private var mSurfaceHeight: Int = 0

    private var mLeftVolume: Float = 1F
    private var mRightVolume: Float = 1F


    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        showLog("initView")

        onStateChanged(STATE_IDLE)
        visibility = View.GONE
        holder.addCallback(mSurfaceHolderCallback)
    }

    override val videoView: View = this

    override val mediaPlayer: SunMediaPlayer? = mMediaPlayer

    override fun setVideoViewListener(listener: IVideoViewListener) {
        mVideoViewListener = listener
    }

    override fun setVideoPath(uri: Uri, headers: Map<String, String>?) {
        mUri = uri
        mHeaders = headers
        showLog("setVideoPath  $uri , $headers")
        showLog("openVideo by setVideoPath")
        openVideo()
    }

    private fun openVideo() {
        if (mUri == null) {
            showLog("openVideo  uri is null")
            return
        }

        if (mSurfaceHolder == null) {
            showLog("openVideo surfaceHolder is null")
            visibility = VISIBLE
            return
        }

        doVideoRelease(false)
//
        try {
            mMediaPlayer = SunMediaPlayer()
            mMediaPlayer?.let {

                it.setAudioStreamType(AudioManager.STREAM_MUSIC)
                it.setOnPreparedListener(mPreparedListener)
                it.setOnCompletionListener(mCompletionListener)
                it.setOnSeekCompleteListener(mSeekCompleteListener)
                it.setOnVideoSizeChangedListener(mVideoSizeChangedListener)
                it.setOnBufferingUpdateListener(mBufferingUpdateListener)
                it.setOnErrorListener(mErrorListener)
                it.setOnInfoListener(mInfoListener)

                it.setDataSource(mContext, mUri!!, mHeaders)

                it.setDisplay(mSurfaceHolder)

                // 屏幕常亮
                it.setScreenOnWhilePlaying(true)

                it.prepareAsync()
            }
            onStateChanged(STATE_PREPARING)

        } catch (e: Exception) {
            e.printStackTrace()

            onStateChanged(STATE_ERROR)
        }
    }

    private val mPreparedListener: MediaPlayer.OnPreparedListener =
            MediaPlayer.OnPreparedListener { mp ->
                showLog("onPrepared")

                mp?.let {

                    it.setVolume(mLeftVolume, mRightVolume)

                    onStateChanged(STATE_PREPARED)

                    mVideoWidth = it.videoWidth
                    mVideoHeight = it.videoHeight

                    showLog("onPrepared  width :$mVideoWidth , height :$mVideoHeight")

                    if (mVideoWidth != 0 && mVideoHeight != 0) {

                        holder.setFixedSize(mVideoWidth, mVideoHeight)

                        if (mVideoWidth == mSurfaceWidth && mVideoHeight == mSurfaceHeight) {
                            if (mCurrentState == STATE_PLAYING) {
                                startPlay()
                            }
                        }

                    } else {
                        if (mCurrentState == STATE_PLAYING) {
                            startPlay()
                        }
                    }
                    mVideoViewListener?.onPrepared()
                }
            }

    private val mCompletionListener: MediaPlayer.OnCompletionListener =
            MediaPlayer.OnCompletionListener {
                showLog("onCompletion")

                onStateChanged(STATE_PLAYBACK_COMPLETED)

                mVideoViewListener?.onCompletion()
            }

    private val mSeekCompleteListener: MediaPlayer.OnSeekCompleteListener =
            MediaPlayer.OnSeekCompleteListener {
                showLog("onSeekComplete")

                mVideoViewListener?.onSeekComplete()
            }

    private val mVideoSizeChangedListener: MediaPlayer.OnVideoSizeChangedListener =
            MediaPlayer.OnVideoSizeChangedListener { mp, width, height ->
                showLog("onVideoSizeChanged  width :$width , height :$height")

                mVideoWidth = width
                mVideoHeight = height

                if (mVideoWidth != 0 && mVideoHeight != 0) {
                    holder.setFixedSize(mVideoWidth, mVideoHeight)
                }

                mVideoViewListener?.onVideoSizeChanged(width, height)
            }

    private val mBufferingUpdateListener: MediaPlayer.OnBufferingUpdateListener =
            MediaPlayer.OnBufferingUpdateListener { mp, percent ->
                showLog("onBufferingUpdate  percent :$percent")

                mVideoViewListener?.onBufferTotalPercentUpdate(percent)
            }

    private val mInfoListener: MediaPlayer.OnInfoListener =
            MediaPlayer.OnInfoListener { mp, what, extra ->
                showLog("onInfo  what :$what , extra :$extra")

                mVideoViewListener?.onInfo(what, extra)
                false
            }

    private val mErrorListener: MediaPlayer.OnErrorListener =
            MediaPlayer.OnErrorListener { mp, what, extra ->
                showLog("onError  what :$what , extra :$extra")

                onStateChanged(STATE_ERROR)

                mVideoViewListener?.onError(what, extra)
                true
            }

    private val mSurfaceHolderCallback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder?) {
            showLog("surfaceCreated")
            mSurfaceHolder = holder

            showLog("openVideo by surfaceCreated")
            openVideo()
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            showLog("surfaceChanged  format :$format , width :$width , height :$height")
            mSurfaceWidth = width
            mSurfaceHeight = height

            if (mMediaPlayer != null && mCurrentState == STATE_PLAYING
                    && mVideoWidth == mSurfaceWidth && mVideoHeight == mSurfaceHeight) {
                showLog("openVideo by surfaceChanged")
                openVideo()
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            showLog("surfaceDestroyed")
            mSurfaceHolder = null

            doVideoRelease(true)
        }
    }


    override fun startPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.start()
            onStateChanged(STATE_PLAYING)
        }
    }

    override fun pausePlay() {
        if (inPlaybackState()) {
            mMediaPlayer?.pause()
            onStateChanged(STATE_PAUSED)
        }
    }

    override fun stopPlay() {
        doVideoRelease(false)
    }

    override fun seekTo(position: Int) {
        if (inPlaybackState()) {
            mMediaPlayer?.seekTo(position)
        }
    }

    override val videoWidth: Int = mVideoWidth

    override val videoHeight: Int = mVideoHeight

    override val videoDuration: Int
        get() {
            return if (inPlaybackState()) {
                mMediaPlayer?.duration ?: -1
            } else {
                -1
            }
        }

    override val currentPosition: Int
        get() {
            return if (inPlaybackState()) {
                mMediaPlayer?.duration ?: -1
            } else {
                -1
            }
        }

    override val isPlaying: Boolean
        get() {
            return if (inPlaybackState() && mCurrentState != STATE_STOPBACK) {
                mMediaPlayer?.isPlaying ?: false
            } else {
                false
            }
        }

    override fun inPlaybackState(): Boolean {
        return (mMediaPlayer != null && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING)
    }

    private fun doVideoRelease(resetUrl: Boolean) {

        if (mMediaPlayer != null) {
            mCurrentState = STATE_IDLE
            mMediaPlayer?.reset()
            mMediaPlayer?.release()
            mMediaPlayer = null

            onStateChanged(mCurrentState)
        }
        if (resetUrl) {
            mUri = null
        }
    }

    //
    private fun onStateChanged(state: Int) {
        showLog("updateState  $mCurrentState >>> $state")
        mCurrentState = state
    }

    private fun showLog(msg: String) {
        SunLog.i(logTag, msg)
    }
}
