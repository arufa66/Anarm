package com.example.hitroki.anarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by hitroki on 2014/12/11.
 */
public class MyAlarmManager {
    Context c;
   private AlarmManager am;
    private int ahour;
    private int amin;

    private PendingIntent mAlarmSender;


    public MyAlarmManager(Context c){
        this.c = c;
        am = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Log.v("MyAlarmManger", "初期化完了");
    }

    void addAlarm(int hour,int minute){
        Intent intent= new Intent(c, MyAlarmService.class);

        mAlarmSender = PendingIntent.getService(c, 0, intent, 0);
        // アラーム時間設定
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(System.currentTimeMillis());

        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        Log.v("MyAlarmManagerログ", cal.getTimeInMillis() + "ms");
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), mAlarmSender);
        Log.v("MyAlarmManagerログ","アラームセット完了");
    }
    void setAlarm(){

        Uri uri = MyContentprovider.CONTENT_URI;
        String[] projection = {
                MyContract.Alarms.COLUMN_ID,
                MyContract.Alarms.COLUMN_TIME
        };
        Cursor cursor;
        String saveAlarm[] = new String[2];

     cursor = c.getContentResolver().query(
            uri,
            projection,
            null,
            null,
            null);
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    Calendar cal3 = Calendar.getInstance();
    cal3.set(2006, 4, 11, 12, 58, 15);

    cursor.moveToFirst();
    while (cursor.moveToNext()) {

        String sAlarm = cursor.getString(cursor.getColumnIndex(MyContract.Alarms.COLUMN_TIME));
        String[] sAlarms = sAlarm.split(":");
        cal2.set(Calendar.HOUR_OF_DAY, Integer.valueOf(sAlarms[0]));
        cal2.set(Calendar.MINUTE, Integer.valueOf(sAlarms[1]));
        if (cal2.after(cal1)) {
            int diff = cal3.compareTo(cal2);
            if (diff > 0) {

            } else {
                cal3 = cal2;
                saveAlarm = sAlarms;
            }
        }


    }

    cursor.close();

        Log.v("MyAlarmManager#setAlarm", saveAlarm[0] + "時");
        Log.v("MyAlarmManager#setAlarm", saveAlarm[1] + "分");
        if(saveAlarm[0] != null || saveAlarm[1] != null) {
            ahour = Integer.valueOf(saveAlarm[0]);
            amin = Integer.valueOf(saveAlarm[1]);
            addAlarm(ahour, amin);
        }
    }
//コンテントプロバイダのURI生成に変更
}
