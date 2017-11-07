package com.wizardev.smallplanmvp.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.wizardev.smallplanmvp.R;
import com.wizardev.smallplanmvp.plans.PlansActivity;

/**
 * anthor : wizardev
 * email : wizarddev@163.com
 * time : 17-11-6
 * desc :
 * version : 1.0
 */

public class AlarmService extends Service {
    private static final String TAG = "AlarmService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        beginForeService();
    }

    private void beginForeService() {
        Log.i(TAG, "beginForeService: ");
        //创建通知
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText("2017-2-27")
                .setContentText("您有一条未读短信...");
        //创建点跳转的Intent(这个跳转是跳转到通知详情页)
        Intent intent = new Intent(this,PlansActivity.class);
        //创建通知详情页的栈
        TaskStackBuilder stackBulider = TaskStackBuilder.create(this);
        //为其添加父栈 当从通知详情页回退时，将退到添加的父栈中
        stackBulider.addParentStack(PlansActivity.class);
//        PendingIntent pendingIntent = stackBulider.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置跳转Intent到通知中
        mBuilder.setContentIntent(pendingIntent);
        //获取通知服务
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //构建通知
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = mBuilder.build();
        }
        //显示通知
        nm.notify(0,notification);
        //启动前台服务
        startForeground(0,notification);
    }
}
