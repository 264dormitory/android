package com.jacklee.clatclatter.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jacklee.clatclatter.MainActivity;
import com.jacklee.clatclatter.R;
import com.jacklee.clatclatter.database.task;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liming on 18-4-20.
 * 提醒服务
 */

public class NotificationService extends Service {
    private final String TAG = NotificationService.class.getSimpleName();
    private String remindTime = "";
    private String secondRemindTime = "";
    private static int NOTIFICATION_ID = 0x123;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        remindTime         = intent.getStringExtra("remindTime");
        secondRemindTime   = intent.getStringExtra("secondRemindTime");

        final List<task> tasks = DataSupport.where("remind_time = ?", secondRemindTime).find(task.class);

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "获取系统的NotificationManager服务");
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Log.i(TAG, "遍历数组,设置提醒");
                for (task t: tasks) {
                    String remindMessage   = tasks.get(0).getStart_time() + ": "
                            + tasks.get(0).getMark();

                    Log.i(TAG, "创建通知将要启动的Activity");
                    Intent intent1   = new Intent(NotificationService.this, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(NotificationService.this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

                    Notification notification = new Notification.Builder(NotificationService.this)
                            .setAutoCancel(true)
                            .setTicker("有新消息")
                            .setSmallIcon(R.drawable.ic_remind)
                            .setContentTitle(tasks.get(0).getTitle())
                            .setContentText(remindMessage)
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pi)
                            .build();

                    notification.defaults = Notification.DEFAULT_ALL;

                    Log.i(TAG, "发送通知");
                    nm.notify(NOTIFICATION_ID, notification);
                }
            }
        };

        Log.i(TAG, "设置定时任务");
        Timer timer = new Timer(true);
        timer.schedule(task, CreateTaskService.strToDateLong(remindTime));

        return START_STICKY;
    }
}
