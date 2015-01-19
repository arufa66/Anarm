package com.example.hitroki.anarm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by hitroki on 2015/01/13.
 */
public class MyContentprovider extends ContentProvider {

    private static final String AUTHORITY =
            "com.example.hitroki.anarm.mycontentprovider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + MyContract.Alarms.TABLE_NAME);
private MyDbHelper myDbHelper;
    private static final int USERS =1;
    private static final int USER_ITEM =2;

    private  static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,MyContract.Alarms.TABLE_NAME,USERS);
        uriMatcher.addURI(AUTHORITY,MyContract.Alarms.TABLE_NAME + "/#",USER_ITEM);
    }
    @Override
    public boolean onCreate() {
        myDbHelper = new MyDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcher.match(uri) != USERS){
            throw new IllegalArgumentException("Unknow URI:" + uri);
        }
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(

                MyContract.Alarms.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(uriMatcher.match(uri) != USERS){
            throw new IllegalArgumentException("Unknow URI:" + uri);
        }
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
      long newId =  db.insert(MyContract.Alarms.TABLE_NAME,
                null,
                values);
        Uri newUri = ContentUris.withAppendedId(MyContentprovider.CONTENT_URI,newId);
        getContext().getContentResolver().notifyChange(uri,null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) != USER_ITEM){
            throw new IllegalArgumentException("Unknow URI:" + uri);
        }
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        int count =  db.delete(MyContract.Alarms.TABLE_NAME,
                selection,
                selectionArgs
                );

        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != USER_ITEM) {
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        int count = db.update(
                MyContract.Alarms.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
