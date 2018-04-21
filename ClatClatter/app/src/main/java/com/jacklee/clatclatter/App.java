package com.jacklee.clatclatter;

import android.graphics.drawable.Drawable;

/**
 * Created by 陈鸿编 on 2018/4/9.
 */

public class App {
    private String name;
    private  int ImageId;

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

    public App(String name,Drawable drawable)
    {
        this.name=name;
        this.drawable = drawable;
    }

    public String getAppName() {
        if(null == name)
            return "";
        else
            return name;
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
}
