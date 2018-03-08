package com.example.user.smartclassroom.Attendance;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    ImageView imageView_Status;
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
        tv_date= (TextView) view.findViewById(R.id.tv_date);
        tv_time = (TextView)view.findViewById(R.id.tv_time);
        imageView_Status = (ImageView)view.findViewById(R.id.imageView_status);
        cardView=(CardView)view.findViewById(R.id.cardviewAttendance);
        Properties properties=model.get(i);
        tv_date.setText(properties.getDate());
        tv_time.setText(properties.getStartTime());
        if(properties.getStatdescript().equals("Present")){
            imageView_Status.setBackgroundResource(R.drawable.present);
        }else if(properties.getStatdescript().equals("Late")){
            imageView_Status.setBackgroundResource(R.drawable.late);
        }else if(properties.getStatdescript().equals("Absent")){
            imageView_Status.setBackgroundResource(R.drawable.absent);
        }
//change 2/18 -nicole
        return view;
    }
}
