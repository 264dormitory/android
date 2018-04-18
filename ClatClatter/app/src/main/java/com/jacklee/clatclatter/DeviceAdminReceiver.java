package com.jacklee.clatclatter;

import android.content.ComponentName;
import android.content.Context;

/**
 * Created by liming on 18-4-11.
 * 用来得到Device Owner权限
 */

public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {
    private static final String TAG = DeviceAdminReceiver.class.getSimpleName();

    /**
     * @param context The context of the application.
     * @return The component name of this component in the given context.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), DeviceAdminReceiver.class);
    }
}
