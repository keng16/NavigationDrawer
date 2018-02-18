package com.example.user.smartclassroom.Attendance;

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

/**
 * Created by kenonnegammad on 30/01/2018.
 */

public class student_attendance_adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<Properties> model;
    TextView tv_course,tv_date,tv_time,tv_room,tv_status;
    CardView cardView;
    public student_attendance_adaptor(Context context, ArrayList<Properties> model) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = View.inflate(context, R.layout.attendance_design_student,null);
        }
        tv_course = (TextView) view.findViewById(R.id.course_tv);
        tv_date= (TextView) view.findViewById(R.id.tv_date);
        tv_room = (TextView)view.findViewById(R.id.room_tv);
        tv_status = (TextView)view.findViewById(R.id.tv_status);
        cardView=(CardView)view.findViewById(R.id.cardviewAttendance);

        Properties properties=model.get(i);
        tv_course.setText(properties.getCourseId()+" "+"-"+" "+properties.getSection());
        tv_date.setText(properties.getDate());
        tv_room.setText(properties.getRoom());
        tv_status.setText(properties.getStatdescript());
        cardStatus();  //change 2/18 -nicole
        return view;
    }
    private void cardStatus()
    {
        if (tv_status.getText().toString().equals("Absent")) {
            cardView.setCardBackgroundColor(Color.parseColor("#EE9859"));
        }
        else if (tv_status.getText().toString().equals("Late")) {
            cardView.setCardBackgroundColor(Color.parseColor("#ffc94d"));
        }
        else if (tv_status.getText().toString().equals("Present")) {
            cardView.setCardBackgroundColor(Color.parseColor("#477956"));
        }
    }
}
