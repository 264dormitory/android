package com.jacklee.clatclatter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * 初始化数据库文件
 * Created by liming on 18-4-11.
 */

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();
    private   static SQLiteDatabase database;
    public static  final String DATABASE_FILENAME = "app_info.db";
    public static final String PACKAGE_NAME = "com.jacklee.clatclatter";
    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory()
                                                .getAbsolutePath() + PACKAGE_NAME;

    public static SQLiteDatabase openDatabase(Context context) {
        try {
            //打开数据库文件
            String databaseName = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File file = new File(databaseName);

            if (!file.exists()) {
                Log.i(TAG, "数据库文件不存在,创建数据库");
                File dir = new File(DATABASE_PATH);

                if (!dir.exists()) {
                    Log.i(TAG, "创建数据库的目录");
                    dir.mkdir();
                }
        }
    }
}
