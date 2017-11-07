package com.wizardev.smallplanmvp.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wizardev.smallplanmvp.R;
import com.wizardev.smallplanmvp.plans.PlansActivity;

/**
 * anthor : wizardev
 * email : wizarddev@163.com
 * time : 17-11-6
 * desc :
 * version : 1.0
 */

public class AlarmReceiver extends BroadcastReceiver{
    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        //启动服务
       // Intent i = new Intent(context, AlarmService.class);
       // context.startService(i);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i =new Intent (context,PlansActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            noti = new Notification.Builder(context)
                    .setContentTitle("小计划")
                    .setContentText("计划时间到" )
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();
        }
        manager.notify(0, noti);
    }
}
