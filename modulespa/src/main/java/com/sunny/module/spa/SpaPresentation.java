//package com.sunny.module.spa;
//
//import android.animation.Animator;
//import android.animation.ValueAnimator;
//import android.annotation.TargetApi;
//import android.app.Presentation;
//import android.content.Context;
//import android.graphics.SurfaceTexture;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.IBinder;
//import android.os.UserManager;
//import android.view.Display;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.WindowManager;
//import android.view.animation.LinearInterpolator;
//
//import androidx.annotation.Nullable;
//
//import java.lang.reflect.Method;
//
//import timber.log.Timber;
//
//public class SpaPresentation extends Presentation
//        implements TextureView.SurfaceTextureListener {
//    private WindowManager mWindowManager;
//    private TextureView mSurefaceView;
//    private Surface mSurface;
//    private MediaPlayer mMediaPlayer;
//    private final int TYPE_DREAM = 2023;
//    private final String TAG = "SpaPresentation";
//    private final long FPD_ID = 0;
//    private Context mContext;
//    private Boolean mIsFirstPrint = true;
//
//    public SpaPresentation(Context context, Display display, int theme) {
//        super(context, display, theme);
//        mContext = context;
//        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        try {
//            iniWindowToken();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Timber.e("iniWindowToken error :" + e);
//        }
//
//        getWindow().setType(TYPE_DREAM);
//        setTitle("SpaView");
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_sunny);
//        mSurefaceView = findViewById(R.id.surface);
//        mSurefaceView.setSurfaceTextureListener(this);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Timber.i("onStart");
//        try {
//            addWindowToken.invoke(IWindowManager, getWindow().getAttributes().token,
//                    TYPE_DREAM, getDisplay().getDisplayId());
//        } catch (Exception e) {
//            Timber.e(e);
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Timber.i("onStop");
//        try {
//            removeWindowToken.invoke(IWindowManager, getWindow().getAttributes().token,
//                    getDisplay().getDisplayId());
//        } catch (Exception e) {
//            Timber.e(e);
//        }
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//    }
//
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//        Timber.i("onSurfaceTextureAvailable");
//        mSurface = new Surface(surface);
//        try {
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/raw/debug.mp4");
////            mMediaPlayer.setDataSource("/media/spavideos/FF_SpaMode_170818_001.mp4");
//            mMediaPlayer.setSurface(mSurface);
//            mMediaPlayer.prepare();
//            mMediaPlayer.setLooping(true);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setOnCompletionListener(mCompletionListener);
//            if (isFPD()) {
//                Timber.d("FPD Side, request audio focus");
//                if (requestAudioFocus()) {
//                    mMediaPlayer.start();
//                    volumeGradient(mMediaPlayer, 0, 1, null);
//                }
//            } else {
//                Timber.d("RSD Side, don't request focus");
//                mMediaPlayer.start();
//            }
//        } catch (Exception e) {
//            Timber.e("meida :" + e);
//        }
//    }
//
//    private MediaPlayer.OnCompletionListener mCompletionListener =
//            new MediaPlayer.OnCompletionListener() {
//                public void onCompletion(MediaPlayer mp) {
//                    mMediaPlayer.start();
//                    mMediaPlayer.setLooping(true);
//                }
//            };
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//        Timber.i("onSurfaceTextureSizeChanged(" + width + " , " + height + ")");
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        Timber.i("onSurfaceTextureDestroyed");
//        mMediaPlayer.stop();
//        mMediaPlayer.release();
//        mMediaPlayer = null;
//        abandonAudioFocus();
//        return true;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        if (mIsFirstPrint) {
//            Timber.d("onSurfaceTextureUpdated");
//            mIsFirstPrint = false;
//        }
//
//    }
//
//    public interface DoneCallBack {
//        void onComplete();
//    }
//
//    public void volumeGradient(final MediaPlayer mediaPlayer,
//                               final float from, final float to,
//                               final @Nullable DoneCallBack doneCallBack) {
//        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
//        animator.setDuration(3000);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float volume = (float) valueAnimator.getAnimatedValue();
//                try {
//                    //此时可能 mediaPlayer 状态发生了改变,所以用try catch包裹,一旦发生错误,立马取消
//                    mediaPlayer.setVolume(volume, volume);
//                } catch (Exception e) {
//                    Timber.d("onAnimationUpdate" + e.getMessage());
//                    valueAnimator.cancel();
//                }
//            }
//        });
//
//        animator.addListener(new Animator.AnimatorListener() {
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                try {
//                    if (mediaPlayer != null) {
//                        Timber.d("onAnimationEnd, to = " + to);
//                        mediaPlayer.setVolume(to, to);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (doneCallBack != null) {
//                    doneCallBack.onComplete();
//                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                try {
//                    if (mediaPlayer != null) {
//                        Timber.d("onAnimationCancel, to = " + to);
//                        mediaPlayer.setVolume(from, from);
//                    }
//                } catch (Exception e) {
//                    //忽略
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        animator.start();
//    }
//
//    private boolean isFPD() {
//        UserManager userManager = (UserManager) getContext().getSystemService(Context.USER_SERVICE);
//        long userid = userManager.getSerialNumberForUser(android.os.Process.myUserHandle());
//        Timber.d("userid = " + userid);
//        if (FPD_ID == userid) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @TargetApi(23)
//    private void sendStopBroadcast() {
////        Intent intent = new Intent();
////        intent.setAction(SpaModeService.INNER_OFF);
////        try {
////            //反射机制获取UserHandle.ALL
////            Field all = UserHandle.class.getDeclaredField("ALL");
////            UserHandle userHandle = (UserHandle) all.get(null);
////            mContext.sendBroadcastAsUser(intent, userHandle);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
//
//    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
//        public void onAudioFocusChange(int focusChange) {
//            Timber.d("mAudioFocusListener: focusChange = " + focusChange);
//            switch (focusChange) {
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    //对应AUDIOFOCUS_GAIN
//                    //表示音频焦点请求者需要长期占有焦点，这里一般需要stop播放和释放
////                    mMediaPlayer.stop();
//                    sendStopBroadcast();
//                    abandonAudioFocus();
//
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    //对应AUDIOFOCUS_GAIN_TRANSIENT
//                    //表示音频焦点请求者需要短暂占有焦点，这里一般需要pause播放
//                    mMediaPlayer.pause();
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                    //对应AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
//                    //表示音频焦点请求者需要占有焦点，但是我也可以继续播放，只是需要降低音量或音量置为0
//                    break;
//                case AudioManager.AUDIOFOCUS_GAIN:
//                    //获得焦点，这里可以进行恢复播放
//                    mMediaPlayer.start();
//                    break;
//            }
//        }
//    };
//
//    private boolean requestAudioFocus() {
//        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        if (am != null) {
//            //请求焦点的参数说明：
//            //AUDIOFOCUS_GAIN：想要长期占有焦点，失去焦点者stop播放和释放
//            //AUDIOFOCUS_GAIN_TRANSIENT：想要短暂占有焦点，失去焦点者pause播放
//            //AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK：想要短暂占有焦点，失去焦点者可以继续播放但是音量需要调低
//            //AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE：想要短暂占有焦点，但希望失去焦点者不要有声音播放
//            int result = am.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//            Timber.i("result = " + result);
//            return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
//        }
//        return false;
//    }
//
//    private void abandonAudioFocus() {
//        Timber.d("abandonAudioFocus");
//        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        if (am != null) {
//            am.abandonAudioFocus(mAudioFocusListener);
//        }
//    }
//
//    public void dismissAsync(DoneCallBack doneCallBack) {
//        Timber.d("dismissAsync");
//        volumeGradient(mMediaPlayer, 1, 0, doneCallBack);
//    }
//
//    Object IWindowManager;
//    Method addWindowToken;
//    Method removeWindowToken;
//
//    public void iniWindowToken() throws Exception {
//        try {
//            Method getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
//            Object ServiceManager = getServiceMethod.invoke(null, new Object[]{"window"});
//            Class<?> cStub = Class.forName("android.view.IWindowManager$Stub");
//            Method asInterface = cStub.getMethod("asInterface", IBinder.class);
//            IWindowManager = asInterface.invoke(null, ServiceManager);
//            addWindowToken = IWindowManager.getClass().getMethod("addWindowToken", IBinder.class, int.class, int.class);
//            removeWindowToken = IWindowManager.getClass().getMethod("removeWindowToken", IBinder.class, int.class);
//        } catch (Exception e) {
//            Timber.e("--shutDown has an exception");
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//}
