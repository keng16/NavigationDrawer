package com.example.user.smartclassroom.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kenonnegammad on 26/01/2018.
 */

public class Schedule_Adapter extends BaseAdapter {
   private Context context;
   private ArrayList<Properties> model;
   CardView cardView;

   TextView tv_course,tv_day,tv_time,tv_room,tv_section;
    public Schedule_Adapter(Context context, ArrayList<Properties> model) {
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return model.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        if(convertview == null){
            convertview = View.inflate(context, R.layout.schedule_design,null);
        }
        tv_course = (TextView) convertview.findViewById(R.id.course_tv);
        tv_day= (TextView) convertview.findViewById(R.id.day_tv);
        tv_time=(TextView)convertview.findViewById(R.id.time_tv);
        tv_room = (TextView)convertview.findViewById(R.id.room_tv);
        cardView = (CardView)convertview.findViewById(R.id.cardview);
        Properties properties=model.get(i);
        tv_time.setText(properties.getStartTime()+" "+"-"+" "+ properties.getEndTime());
        tv_course.setText(properties.getCourseId()+" "+"-"+" "+properties.getSection());
        tv_day.setText(properties.getDay());
        tv_room.setText(properties.getRoom());
        return convertview;
    }

}
