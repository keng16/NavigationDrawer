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
import java.text.SimpleDateFormat;
import java.util.Locale;
/**
 * Created by kenonnegammad on 26/01/2018.
 */

public class Schedule_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Properties> model;
    CardView cardView;
    String allday="Monday-Tuesday-Wednesday-Thursday-Friday-Saturday-Sunday";
    TextView tv_course,tv_day,tv_time,tv_room,tv_coursename;
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
        tv_coursename = (TextView)convertview.findViewById(R.id.tv_coursename);
        cardView = (CardView)convertview.findViewById(R.id.cardview);
        Properties properties=model.get(i);
        tv_time.setText(properties.getStartTime()+" "+"-"+" "+ properties.getEndTime());
        tv_course.setText(properties.getCourseId()+" "+"-"+" "+properties.getSection());
        tv_coursename.setText(properties.getCoursename());
        String get_Day=properties.getDay();
        tv_day.setText(get_Day);
        if (get_Day.equals(allday))
        {
            tv_day.setText("All days of the Week");
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }

        tv_room.setText(properties.getRoom());
        matchWeek(); //change nicole feb 18
        return convertview;
    }

    private void matchWeek()
    {
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        if (weekday_name.equals("Monday") && tv_day.getText().toString().contains("Monday"))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
        else if (weekday_name.equals("Tuesday") && tv_day.getText().toString().contains("Tuesday"))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
        else if (weekday_name.equals("Wednesday") && tv_day.getText().toString().contains("Wednesday"))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
        else if (weekday_name.equals("Thursday") && tv_day.getText().toString().contains("Thursday"))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
        else if (weekday_name.equals("Friday") && tv_day.getText().toString().contains("Friday"))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
        else if (weekday_name.equals("Saturday") && tv_day.getText().toString().contains("Saturday"))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
        else if (weekday_name.equals("Sunday") && tv_day.getText().toString().contains("Sunday"))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
    }
}

