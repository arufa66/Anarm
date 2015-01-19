package com.example.hitroki.anarm;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by hitroki on 2015/01/15.
 */
public class CustomAdapter extends CursorAdapter {
    private LayoutInflater mInflater;
    public CustomAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        TextView time;
    }








    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        final View view;

        // Viewを再利用している場合は新たにViewを作らない
        ViewHolder holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_layout, null);
            TextView times = (TextView)view.findViewById(R.id.tv);
            holder = new ViewHolder();
            holder.time = times;
            view.setTag(holder);






        // 行毎に背景色を変える


        // XMLで定義したアニメーションを読み込む
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.item_motion);
        // リストアイテムのアニメーションを開始
        view.startAnimation(anim);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder  = (ViewHolder) view.getTag();
        final String Time = cursor.getString(cursor
                .getColumnIndexOrThrow(MyContract.Alarms.COLUMN_TIME));
        holder.time.setText(Time);
    }
}
