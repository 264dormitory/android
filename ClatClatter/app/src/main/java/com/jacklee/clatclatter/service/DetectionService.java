package com.jacklee.clatclatter.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import com.jacklee.clatclatter.ShowActivity;

import org.litepal.crud.DataSupport;
import com.jacklee.clatclatter.database.app_backlist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liming on 18-4-18.
 */

public class DetectionService extends AccessibilityService {
    private static List<app_backlist> apps = new ArrayList<>();
    private static final String TAG = DetectionService.class.getSimpleName();
    private static DetectionService mInstance = null;
    private static String taskEndTime = "";

    static String foregroundPackageName;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "引导用户开启辅助功能");
        // 判断辅助功能是否开启
        if (!DetectionService.isAccessibilitySettingsOn(getBaseContext())) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getBaseContext().startActivity(intent);
        }

        Log.i(TAG, "获取数据库中所有的应用程序黑名单");
        apps = DataSupport.findAll(app_backlist.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        taskEndTime = intent.getStringExtra("endTime");
        return START_STICKY_COMPATIBILITY; // 根据需要返回不同的语义值 0
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Date date = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf2.format(date);
        Date dateTime = null;
        Date endDateTime = null;
        try {
             dateTime = sdf2.parse(now);
             endDateTime = sdf2.parse(taskEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e(TAG, dateTime.compareTo(endDateTime) + "");
        Log.e(TAG, now);
        Log.e(TAG, taskEndTime);

        if (dateTime.compareTo(endDateTime) == 1) {     //如果到达结束日期就关闭服务
            Log.e(TAG, "STOP");
            stopSelf();
        } else {

            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            /*
             * 如果 与 DetectionService 相同进程，直接比较 foregroundPackageName 的值即可
             * 如果在不同进程，可以利用 Intent 或 bind service 进行通信
             */
                foregroundPackageName = event.getPackageName().toString();

                Log.i(TAG, "启动服务");
                if (getrRunningAppEqualBackList()) {
                    Log.i(TAG, "是黑名单中的app,启动自己的页面");
                    Intent i = new Intent();
                    i.setClass(this, ShowActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected  void onServiceConnected() {
        super.onServiceConnected();
    }

    /**
     * 方法6：使用 Android AccessibilityService 探测窗口变化，跟据系统回传的参数获取 前台对象 的包名与类名
     *
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    public static boolean isForegroundPkgViaDetectionService(String packageName) {
        return packageName.equals(DetectionService.foregroundPackageName);
    }

    // 此方法用来判断当前应用的辅助功能服务是否开启
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

    public static DetectionService getInstance() {
        if (mInstance == null) {
            synchronized (DetectionService.class) {
                if (mInstance == null) {
                    mInstance = new DetectionService();
                }
            }
        }
        return mInstance;
    }

    public String getForegroundPackage() {
        return foregroundPackageName;
    }

    private static boolean getrRunningAppEqualBackList() {
        for (int i = 0; i < apps.size(); i++)
            if (apps.get(i).getName().equals(foregroundPackageName))
                return true;

        return false;
    }
}
