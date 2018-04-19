package org.smartautomation.user.smartclassroom.Schedule_Guard;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 26/01/2018.
 */

public class Schedule_Guard_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Properties> model;
    CardView cardView;
    TextView tv_id;
    TextView tv_name;
    CircleImageView circleImageView;
    TextView tv_course,tv_day,tv_time,tv_room,tv_coursename,tv_section;
    public Schedule_Guard_Adapter(Context context, ArrayList<Properties> model) {
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
            convertview = View.inflate(context, R.layout.schedule_design_guard_student,null);
        }
        tv_course = (TextView) convertview.findViewById(R.id.course_tv);
        tv_day= (TextView) convertview.findViewById(R.id.day_tv);
        tv_time=(TextView)convertview.findViewById(R.id.time_tv);
        tv_room = (TextView)convertview.findViewById(R.id.room_tv);
        tv_section = (TextView)convertview.findViewById(R.id.section_tv);
        tv_coursename = (TextView)convertview.findViewById(R.id.tv_coursename);
        tv_id = (TextView)convertview.findViewById(R.id.tv_id);
        tv_name = (TextView)convertview.findViewById(R.id.tv_name_schedule);
        circleImageView = (CircleImageView)convertview.findViewById(R.id.imageView_schedule_guard_student);

        Properties properties=model.get(i);
        tv_time.setText(properties.getStartTime()+" "+"-"+" "+ properties.getEndTime());
        tv_course.setText("Course: "+properties.getCourseId());
        tv_coursename.setText(properties.getCoursename());
        tv_section.setText("Section: "+properties.getSection());
        tv_name.setText(properties.getProffname());
        tv_id.setText(String.valueOf(properties.getProf_id()));
        Picasso.with(context)
                .load(properties.getPic())
                .resize(50, 50)
                .centerCrop()
                .into(circleImageView);
        String get_Day=properties.getDay();
        tv_day.setText(get_Day);
        tv_room.setText("Room: "+properties.getRoom());
        //matchWeek(); //change nicole feb 18
        return convertview;
    }
}

