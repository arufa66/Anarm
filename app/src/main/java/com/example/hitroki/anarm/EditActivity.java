package com.example.hitroki.anarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;


public class EditActivity extends Activity {

    private long timeId;
    private  String shour = null;
    private String smin = null;
    private int ahour;
    private int amin;
    private String stime = null;
    TimePicker timer;
    MyAlarmManager am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        timeId = intent.getLongExtra(MainActivity.EXTRA_MYID, 0L);

        Uri uri = MyContentprovider.CONTENT_URI;
        String[] projection = {
        MyContract.Alarms.COLUMN_TIME
        };
        String selection = MyContract.Alarms.COLUMN_ID + " = ?";
        String[] selectionArgs = new String[] { Long.toString(timeId) };

            Cursor cursor = getContentResolver().query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    null);
            cursor.moveToFirst();
            String displayName = MyContract.Alarms.COLUMN_TIME;
            int timeIndex = cursor.getColumnIndex(displayName);
            Log.v("EditActivity", cursor.getString(timeIndex));
            String time = cursor.getString(timeIndex);
                cursor.close();
        String[] times = time.split(":");
        timer = (TimePicker)findViewById(R.id.timePicker);
//
        timer.setCurrentHour(Integer.valueOf(times[0]));
        timer.setCurrentMinute(Integer.valueOf(times[1]));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      switch (id){
          case R.id.action_save:
              ahour = timer.getCurrentHour();
              amin = timer.getCurrentMinute();
              if ( ahour  < 10) {
                  shour = "0" + String.valueOf(ahour);
              } else {
                  shour = String.valueOf(ahour);
              }
              if (amin < 10) {
                  smin = "0" + String.valueOf(amin);
              } else {
                  smin = String.valueOf(amin);
              }
              stime = shour + ":" + smin;
             am = new MyAlarmManager(getApplicationContext());

              if (shour == null || smin ==null) {
                  Toast.makeText(
                          this,
                          "Please enter timer",
                          Toast.LENGTH_LONG
                  ).show();
              } else {
                  ContentValues values = new ContentValues();
                  values.put(MyContract.Alarms.COLUMN_TIME, stime);


                      // updated

                      Uri uri = ContentUris.withAppendedId(MyContentprovider.CONTENT_URI, timeId);
                      String selection = MyContract.Alarms.COLUMN_ID + " = ?";
                      String[] selectionArgs = new String[] { Long.toString(timeId) };
                      getContentResolver().update(
                              uri,
                              values,
                              selection,
                              selectionArgs
                      );


                  Intent intent = new Intent(EditActivity.this, MainActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  am.setAlarm();
                  startActivity(intent);
                  finish();
              }

              break;
          case R.id.action_delete:

              AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
              alertDialog.setTitle("アラーム削除");
              alertDialog.setMessage("このアラームを削除してもいいですか?");

              alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {

                      am = new MyAlarmManager(getApplicationContext());
                      Uri uri = ContentUris.withAppendedId(MyContentprovider.CONTENT_URI, timeId);
                      String selection = MyContract.Alarms.COLUMN_ID + " = ?";
                      String[] selectionArgs = new String[] { Long.toString(timeId) };
                      getContentResolver().delete(
                              uri,
                              selection,
                              selectionArgs
                      );

                      Intent intent = new Intent(EditActivity.this, MainActivity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      am.setAlarm();
                      startActivity(intent);
                      finish();
                  }
              }).show();

              break;
      }

        return super.onOptionsItemSelected(item);
    }
}
