package com.sunny.lib.base.log;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LogCollect {
    private static boolean isSending = false;
    //shell 写日志到本地上传服务端
    public static void writeLog(Context context){
        if(isSending) return;
        isSending = true;
        String path = Environment.getExternalStorageDirectory()+"/log/writelog.txt";
        try {
            java.lang.Process p = Runtime.getRuntime().exec("logcat -v time");
            final InputStream is = p.getInputStream();
            new Thread() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    FileOutputStream os = null;
                    try {
                        os = new FileOutputStream(path);
                        int len = 0;
                        byte[] buf = new byte[1024];
                        while (-1 != (len = is.read(buf)) && (System.currentTimeMillis() - startTime < 20000)) {
                            os.write(buf, 0, len);
                            os.flush();
                        }
                    } catch (Exception e) {
                        Log.d("writelog", "read logcat process failed. message: " + e.getMessage());
                    } finally {
                        if (null != os) {
                            try {
                                os.close(); os = null;
                            } catch (IOException e) {
                                // Do nothing
                            }
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
            Log.d("writelog", "open logcat process failed. message: " + e.getMessage());
        }
//        Observable.timer(25L, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
//            public final void accept(Long code) {
//                isSending = false;
//                new SettingHttpTools(context).postLogData(1);
//            }
//        });
    }
}
