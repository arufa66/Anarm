package com.example.hitroki.anarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by hitroki on 2014/12/11.
 */
public class MyAlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.v("MyAlarmServiceログ", "Create");
        Thread thr = new Thread(null, mTask, "MyAlarmServiceThread");
        thr.start();
        Log.v("MyAlarmServiceログ","スレッド開始");
    }

    /**
     * アラームサービス
     */
    Runnable mTask = new Runnable() {
        public void run() {
            // ここでアラーム通知する前の処理など...

            Intent alarmBroadcast = new Intent();
            alarmBroadcast.setAction("MyAlarmAction");//独自のメッセージを送信します
            sendBroadcast(alarmBroadcast);
            Log.v("MyAlarmServiceログ","通知画面起動メッセージを送った");
            MyAlarmService.this.stopSelf();//サービスを止める
            Log.v("MyAlarmServiceログ","サービス停止");
        }
    };
}
