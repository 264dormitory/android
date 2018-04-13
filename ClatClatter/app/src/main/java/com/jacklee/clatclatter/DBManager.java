package com.jacklee.clatclatter;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * 数据库对象
 * Created by liming on 18-4-11.
 */

public class DBManager extends SQLiteOpenHelper {
    private final String TAG = DBManager.class.getSimpleName();

    private final String CREATE_TASK_TABLE =
            "create table task(" +
            "id integer primary key AUTOINCREMENT," +
            "title varchar(20) NOT NULL," +
            "mark varchar(255)," +
            "focus char(1) default 0," +
            "is_repeat char(1) default 0," +
            "sleep_pattern char(1) default 0," +
            "repeat_pattern char(10)," +
            "priority char(10)," +
            "sleep_pattern_kind integer," +
            "start_time datetime," +
            "end_time datetime," +
            "remind_time datetime," +
            "task_date date)";

    private final String CREATE_APPBACKLIST_TABLE =
            "create table app_backlist(" +
                    "name char(10) primary key" +
                    ")";


    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * 创建数据库的时候回调该函数
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "创建task表");
        sqLiteDatabase.execSQL(CREATE_TASK_TABLE);
        Log.i(TAG, "创建app_backlist表");
        sqLiteDatabase.execSQL(CREATE_APPBACKLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
