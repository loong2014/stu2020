package com.sunny.family.多线程;

import android.os.AsyncTask;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class test {

    static class MyAsyncTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("11111");
        }

        @Override
        protected Void doInBackground(String... strings) {
            System.out.println("22222");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("33333");

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
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

        MyAsyncTask task = new MyAsyncTask();

        task.execute("sunny");


    }
}
