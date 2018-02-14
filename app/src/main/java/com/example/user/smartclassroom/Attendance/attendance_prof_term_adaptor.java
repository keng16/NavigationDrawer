package com.example.user.smartclassroom.Attendance;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by kenonnegammad on 13/02/2018.
 */

public class attendance_prof_term_adaptor extends BaseAdapter {
    public attendance_prof_term_adaptor(Context context, ArrayList<Properties> model) {
        this.context = context;
        this.model = model;
    }

    private Context context;
    private ArrayList<Properties> model;
    String date=null;
    CardView cardView;
    ImageView student_pic;
    int pos;
    TextView tv_name;
    TextView tv_date;
    TextView tv_studnum;
    int c=0;
    TextView tv_present,tv_late,tv_absent;
    TextView tv_status;
    private static final int Food_Item = 0;
    private static final int header = 1;
    private LayoutInflater mInflater;



    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Properties properties=model.get(position);

            if (convertView==null) {
                date = properties.getDate();
                convertView = View.inflate(context,R.layout.header, null);
                tv_date = (TextView)convertView.findViewById(R.id.textSeparator);
                tv_date.setText(date);
            }else if (date.equals(properties.getDate())||convertView!=null){
                convertView = View.inflate(context,R.layout.attendance_design_student, null);
                tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                student_pic = (ImageView)convertView.findViewById(R.id.imageView_ProfilePic);
                tv_studnum = (TextView) convertView.findViewById(R.id.tv_studnum);
                tv_status = (TextView)convertView.findViewById(R.id.tv_status);
                Picasso.with(context)
                        .load(properties.getPic())
                        .resize(90, 90)
                        .into(student_pic);
                tv_name.setText(properties.getStudentlname()+" , "+properties.getStudentfname()+" "+properties.getStudentmname()+".");
                tv_status.setText(properties.getStatdescript());
                tv_studnum.setText(String.valueOf(properties.getStud_id()));
            }else if(!date.equals(properties.getDate())){
                date = properties.getDate();
                convertView = View.inflate(context,R.layout.header, null);
                tv_date = (TextView)convertView.findViewById(R.id.textSeparator);
                tv_date.setText(date);
                c=0;
            }

        return convertView;
    }
}
