package com.jacklee.clatclatter.database;

import org.litepal.crud.DataSupport;

/**
 * 李世杰创建app_backlist类，对应相应的数据库中task表的映射
 */

public class app_backlist extends DataSupport {

    private String name;  //应用名称
    private boolean enabled = false;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
