package com.example.hitroki.anarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hitroki on 2015/01/13.
 */
public class MyDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "myapp.db";
    public static final int DB_VERSION= 1;
    public MyDbHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyContract.Alarms.CREATE_TABLE);
        db.execSQL(MyContract.Alarms.INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyContract.Alarms.DROP_TABLE);
       onCreate(db);
    }
}
