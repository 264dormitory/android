package com.jacklee.clatclatter.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jacklee.clatclatter.CreateTaskActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by liming on 18-4-16.
 */

public class LockService extends Service {
    public MyBinder iBinder;
    private final String TAG = LockService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (iBinder == null)
            iBinder = new MyBinder();

        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "创建服务调用");
        iBinder = new MyBinder();
    }

    public void onDestory() {
        Log.i(TAG, "服务销毁");
    }

    public class MyBinder extends Binder {
        /**
         * 获取 Service 实例
         */
        public LockService getService(){
            return LockService.this;
        }

        public void listenerApps() {
            //获取现在启动的Activity
            final ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);

            new Thread(){
                public void run() {
                    while(true){
                        try {
                                Log.e(TAG, getAppName(getApplicationContext()));

//                            List<ActivityManager.RunningTaskInfo > runningTaskInfo = am.getRunningTasks(1);
//
//                            ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
//                            Log.i(TAG, componentInfo.getPackageName());
//
//                            if (componentInfo.getClassName().equals("com.android.chrome"))
//                                Log.i(TAG, "SUCCESS---------------------------------");
                            sleep(200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
            }.start();

            Log.i(TAG, "启动自己的Activity");
//            Intent dialogIntent = new Intent(getBaseContext(), CreateTaskActivity.class);
//            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getApplication().startActivity(dialogIntent);
        }

        public  String getAppName(Context context) {
            String appName = null;
            PackageManager packageManager = context.getPackageManager();
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                //CharSequence这两者效果是一样的.
                appName = packageManager.getApplicationLabel(applicationInfo).toString();
                appName = (String) packageManager.getApplicationLabel(applicationInfo);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("GsonUtils", "Exception=" + e.toString());
                return null;
            }
            return appName;
        }

    }
}
