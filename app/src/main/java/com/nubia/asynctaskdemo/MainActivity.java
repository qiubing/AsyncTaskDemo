package com.nubia.asynctaskdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ProgressDialog mDialog;
    private MyAsyncTask mTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDialog = new ProgressDialog(this);
        mDialog.setMax(100);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        mTask = new MyAsyncTask(this);

        //开始执行任务
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"start task");
                mTask.execute();
            }
        });

        //取消任务执行
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"cancel task");
                mTask.cancel(false);
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void,Integer,Void>{
        private Context mContext;
        public MyAsyncTask(Context context){
            mContext = context;
        }
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()...on: " + Thread.currentThread().getName());
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG,"doInBackground()...on: " + Thread.currentThread().getName());
            //模拟后台执行
            for (int i = 0;i < 100; i++){
                //如果任务被取消了，则直接返回
                if (isCancelled()){
                    break;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG, "onProgressUpdate()...on: " + Thread.currentThread().getName());
            mDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG,"onPostExecute()...on: " + Thread.currentThread().getName());
            mDialog.dismiss();
            Toast.makeText(mContext,"任务执行完成",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled(Void result) {
            Log.i(TAG,"onCancelled()...on: " + Thread.currentThread().getName());
            mDialog.dismiss();
            Toast.makeText(mContext,"任务被取消了",Toast.LENGTH_LONG).show();
        }
    }
}