package com.jacklee.clatclatter.service;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListView;

import com.jacklee.clatclatter.CreateTaskActivity;
import com.jacklee.clatclatter.ShowActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liming on 18-4-18.
 */

public class DetectionService extends AccessibilityService {
    private static final List<String> apps = new ArrayList<>();
    private static final String TAG = DetectionService.class.getSimpleName();
    private static DetectionService mInstance = null;

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

        Log.i(TAG, "初始化app功能");
        apps.add("com.android.chrome");
        apps.add("com.tencent.tim");
        apps.add("com.eg.android.AlipayGphone");
        apps.add("com.sina.weibo");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY; // 根据需要返回不同的语义值 0
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            /*
             * 如果 与 DetectionService 相同进程，直接比较 foregroundPackageName 的值即可
             * 如果在不同进程，可以利用 Intent 或 bind service 进行通信
             */
            foregroundPackageName = event.getPackageName().toString();


            if (getrRunningAppEqualBackList()) {
                Log.i(TAG, "是黑名单中的app,启动自己的页面");
                Intent i = new Intent();
                i.setClass(this, ShowActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
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
        for (String i: apps) {
            if (i.equals(foregroundPackageName))
                return true;
        }

        return false;
    }
}
