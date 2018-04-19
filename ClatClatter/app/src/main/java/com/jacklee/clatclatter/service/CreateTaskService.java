package com.jacklee.clatclatter.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jacklee.clatclatter.CreateTaskActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liming on 18-4-19.
 */

public class CreateTaskService extends Service {
    private final String TAG = CreateTaskActivity.class.getSimpleName();
    private String taskTime;
    private String taskEndTime;
    private static String strCycle = "不重复";
    private static long DAY = 24 * 60 * 60 * 1000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        taskTime = intent.getStringExtra("startTime");
        taskEndTime = intent.getStringExtra("endTime");
        strCycle = intent.getStringExtra("strCycle");

        final Intent intent1 = new Intent(CreateTaskService.this, DetectionService.class);
        intent1.putExtra("endTime", taskEndTime);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG,"开始服务");
                startService(intent1);
            }
        };

        Timer timer = new Timer(true);
        if (getCycleTime() != 0) {
            timer.schedule(task, strToDateLong(taskTime), getCycleTime());

        } else {
            timer.schedule(task, strToDateLong(taskTime));
        }

        return START_STICKY;
    }

    /**
     * string类型时间转换为date
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public  static long getCycleTime() {
        switch (strCycle) {
            case "不重复":
                return 0;
            case "每日":
                return DAY;
            case "每周":
                return DAY * 7;
            case "每月":
                return DAY * 30;
            case "每年":
                return DAY * 365;
        }

        return 0;
    }
}
