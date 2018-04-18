package com.jacklee.clatclatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


/**
 * Created by liming on 18-4-12.
 */
@RunWith(AndroidJUnit4.class)
public class DBManagerTest {
    private  final String TAG = DBManagerTest.class.getSimpleName();
    @Test
    public void test() {
        Log.i(TAG, "获取上下文");
        Context appContext = InstrumentationRegistry.getTargetContext();

        Log.i(TAG, "删除原有数据库");
        appContext.deleteDatabase("task.db");

        Log.i(TAG, "获取数据库对象");
        DBManager db = new DBManager(appContext, "task.db", null, 3);

        Log.i(TAG, "插入数据测试");
        ContentValues task = new ContentValues();
        task.put("title", "起床");
        task.put("mark", "无");
        task.put("focus", "1");
        task.put("is_repeat", "1");
        task.put("sleep_pattern", "1");
        task.put("repeat_pattern", "每周");
        task.put("priority", "中级");
        task.put("sleep_pattern_kind", "1");
        task.put("start_time", "2016-02-21 12:36:20");
        task.put("end_time", "2016-02-21 14:36:20");
        task.put("remind_time", "2016-02-21 11:36:20");
        task.put("task_date", "2016-02-21");
        db.getReadableDatabase().insert("task", null, task);

        Cursor cursor1 = db.getReadableDatabase().rawQuery("select * from task", null);
        if (cursor1 != null)
            while (cursor1.moveToNext())
                cursor1.getString(cursor1.getColumnIndex("title"));

        db.close();
    }
}