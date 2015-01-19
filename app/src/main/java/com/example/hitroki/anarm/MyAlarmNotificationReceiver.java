package com.example.hitroki.anarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by hitroki on 2014/12/11.
 */
public class MyAlarmNotificationReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("レシーバログ", "action: " + intent.getAction());
        Intent notification = new Intent(context,
                AlarmNotifiction.class);
        //ここがないと画面を起動できません
        notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(notification);
    }

}
