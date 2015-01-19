package com.example.hitroki.anarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
     private  String shour;
    private String smin;
   private int ahour;
    private int amin;
    private String stime;
    public final static String EXTRA_MYID = "com.example.hitroki.anarm.MYID";


   private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String [] from ={MyContract.Alarms.COLUMN_TIME};
        int[] to = {
                android.R.id.text1
        };
      adapter = new CustomAdapter (this,
             null ,
              true);
        ListView list = (ListView) findViewById(R.id.listView);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                intent.putExtra(EXTRA_MYID, id);
                startActivity(intent);
            }
        }

        );

        getLoaderManager().initLoader(0,null,this);

        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final boolean[] chkSBooleans = {true, false, false};





        SharedPreferences myprefs = getSharedPreferences(
                "myprefs", 0);
        myprefs.getInt("Weather", 0);
        myprefs.getInt("Event", 0);
        myprefs.getInt("News", 0);

        final SharedPreferences.Editor editor = myprefs.edit();

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        int id = item.getItemId();
        if (id == R.id.action_add) {



            final TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            if (hourOfDay < 10) {
                                shour = "0" + String.valueOf(hourOfDay);
                            } else {
                                shour = String.valueOf(hourOfDay);
                            }
                            if (minute < 10) {
                                smin = "0" + String.valueOf(minute);
                            } else {
                                smin = String.valueOf(minute);
                            }
                            stime = shour + ":" + smin;
//                            ahour = hourOfDay;
//                            amin = minute;

                        }


                        }, hour, minute, true);



            timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                public void onDismiss(DialogInterface dialog){

                    //DBへの書き込み
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MyContract.Alarms.COLUMN_TIME,stime);
                    getContentResolver().insert(MyContentprovider.CONTENT_URI,contentValues);
                    MyAlarmManager am = new MyAlarmManager(getApplicationContext());

                 am.setAlarm();
                }
            });
        timePickerDialog.show();




        }
        else if(id == R.id.action_setting){
            final CharSequence[] chkItems = {"Weather", "Event", "News"};

            AlertDialog.Builder checkDlg = new AlertDialog.Builder(MainActivity.this);
            checkDlg.setTitle("鳴らすアラーム");
            checkDlg.setMultiChoiceItems(
                    chkItems,
                    chkSBooleans,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which, boolean isChecked) {
                            // 項目選択時の処理
                            // i は、選択されたアイテムのインデックス
                            // isChecked は、チェック状態
                            chkSBooleans[which] = isChecked;
                        }
                    });
            checkDlg.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < chkItems.length; i++) {
                                if (chkSBooleans[i]) {
                                    switch (i) {
                                        case 0:
                                            editor.putInt("Weather", 1);
                                            editor.commit();
                                            break;
                                        case 1:
                                            editor.putInt("Event", 1);
                                            editor.commit();
                                            break;
                                        case 2:
                                            editor.putInt("News", 1);
                                            editor.commit();
                                            break;
                                    }

                                } else {
                                    switch (i) {
                                        case 0:
                                            editor.putInt("Weather", 0);
                                            editor.commit();
                                            break;
                                        case 1:
                                            editor.putInt("Event", 0);
                                            editor.commit();
                                            break;
                                        case 2:
                                            editor.putInt("News", 0);
                                            editor.commit();
                                            break;
                                    }
                                }
                            }
                        }
                    });

            // 表示
            checkDlg.create().show();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                MyContract.Alarms.COLUMN_ID,
                MyContract.Alarms.COLUMN_TIME
        };
        return new CursorLoader(
                this,
                MyContentprovider.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
adapter.swapCursor(null);
    }

}
