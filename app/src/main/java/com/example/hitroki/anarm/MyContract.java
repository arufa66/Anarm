package com.example.hitroki.anarm;

import android.provider.BaseColumns;

/**
 * Created by hitroki on 2015/01/13.
 */
public class MyContract {
    public MyContract(){

    }
    public static abstract class Alarms implements BaseColumns {
        public  static final String TABLE_NAME = "alarms";
        public  static final String COLUMN_ID = "_id";
        public  static final String COLUMN_TIME = "time";

        public  static final String CREATE_TABLE =
                "create table " + TABLE_NAME + "(" +
               COLUMN_ID + " integer primary key autoincrement, " +
               COLUMN_TIME + " text)";
        public  static final String INIT_TABLE =
                "insert into " + TABLE_NAME + " (time) values " +
                "('6:00'),('7:00')";
        public  static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;
    }
}
