package com.example.hitroki.anarm;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by hitroki on 2014/12/11.
 */
public class AlarmNotifiction extends Activity {
    private PowerManager.WakeLock wakelock;
    private KeyguardManager.KeyguardLock keylock;
    int Weather;
    int Event;
    int News;


    MediaPlayer mp1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences myprefs = getSharedPreferences(
                "myprefs",0);

        final SharedPreferences.Editor editor = myprefs.edit() ;

        Weather =  myprefs.getInt("Weather",0);
        Event =  myprefs.getInt("Event",0);
        News =  myprefs.getInt("News",0);

        Log.v("Weather", String.valueOf(Weather));
        Log.v("Event",String.valueOf(Event));
        Log.v("News",String.valueOf(News));





        setContentView(R.layout.alarm);
        Log.v("通知ログ", "create");
        // スリープ状態から復帰する
        Button stop = (Button)findViewById(R.id.stop);
        wakelock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "disableLock");
        wakelock.acquire();

        // スクリーンロックを解除する
        KeyguardManager keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keylock = keyguard.newKeyguardLock("disableLock");
        keylock.disableKeyguard();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mp1 == null) {


            mp1 = MediaPlayer.create(this, R.raw.weather);
            mp1.start();
        }




        Button stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener(){
            public void  onClick(View v){
                stopAndRelaese();
                Intent intent = new Intent(AlarmNotifiction.this,MainActivity.class);
                startActivity(intent);
            }

        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAndRelaese();
    }

    private void stopAndRelaese() {
        if (mp1 != null) {
            mp1.stop();
            MyAlarmManager am = new MyAlarmManager(getApplicationContext());
            am.setAlarm();
            mp1.release();
        }

    }
}
