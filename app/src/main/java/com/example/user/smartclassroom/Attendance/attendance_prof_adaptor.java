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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kenonnegammad on 02/02/2018.
 */

public class attendance_prof_adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<Properties> model;
    CardView cardView;
    ImageView student_pic;
    TextView tv_name;
    TextView tv_studnum;
    TextView tv_present,tv_late,tv_absent;
    TextView tv_status,tv_date;

    public attendance_prof_adaptor(Context context, ArrayList<Properties> model) {
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
        if (view==null){
            view = View.inflate(context,R.layout.attendance_design_prof,null);
        }
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        student_pic = (ImageView)view.findViewById(R.id.imageView_ProfilePic);
        tv_studnum = (TextView) view.findViewById(R.id.tv_studnum);
        tv_status = (TextView)view.findViewById(R.id.tv_status);
        tv_date = (TextView)view.findViewById(R.id.tv_date);
        cardView=(CardView)view.findViewById(R.id.cardviewAttendance);

        Properties properties=model.get(i);
        Picasso.with(context)
                .load(properties.getPic())
                .resize(90,90)
                .into(student_pic);

        tv_name.setText(properties.getStudentlname()+" , "+properties.getStudentfname()+" "+properties.getStudentmname());
        tv_status.setText(properties.getStatdescript());
        cardStatus();  //change 2/18 -nicole
        dateInvi();   //change 2/18 -nicole
        tv_studnum.setText(String.valueOf(properties.getStud_id()));
        return  view;
    }
    private void dateInvi()
    {
        if(tv_date.getText().toString().equals("TextView"));
        {
            tv_date.setText("");
        }
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
