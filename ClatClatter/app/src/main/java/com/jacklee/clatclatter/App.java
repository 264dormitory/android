package com.jacklee.clatclatter;

import android.graphics.drawable.Drawable;

/**
 * Created by 陈鸿编 on 2018/4/9.
 */

public class App {
    private String name;
    private  int ImageId;
    private String packageName;
    private boolean enabled  = false;

    Drawable drawable;

    public App(String name)
    {
        this.name=name;
    }

    public App(String name,int ImageId)
    {
        this.name=name;
        this.ImageId=ImageId;
    }

    public App(String name,Drawable drawable, String packageName)
    {
        this.name=name;
        this.drawable = drawable;
        this.packageName = packageName;
    }

    public String getAppName() {
        if(null == name)
            return "";
        else
            return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setAppName(String name) {
        this.name = name;
    }
    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getName()
    {
        return name;
    }
    public int getImageId()
    {
        return ImageId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
