package com.sunny.module.stu.BAndroid.BAsyncTask;

import android.os.AsyncTask;

public class BSunny {


    static class SunnyAsyncTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("11111");
        }

        @Override
        protected Void doInBackground(String... strings) {
            // TODO: 2021/7/26 工作线程处理耗时任务
            System.out.println("22222");

            // 发布任务的进度
            publishProgress(100);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println("33333");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("44444");

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            System.out.println("55555");
        }

    }

    /**
     * AsyncTask
     */
    public static void main(String[] args) {

        SunnyAsyncTask task = new SunnyAsyncTask();

        task.execute("sunny");

        // true 中断正在执行的任务
//        task.cancel(true);

    }

}
