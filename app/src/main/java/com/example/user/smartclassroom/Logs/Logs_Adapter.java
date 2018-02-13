package com.example.user.smartclassroom.Logs;

import android.content.Context;
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
 * Created by kenonnegammad on 03/02/2018.
 */

public class Logs_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Properties> model;
    CardView cardView;
    ImageView student_pic;
    TextView tv_name;
    TextView tv_transaction;
    TextView tv_time;
    public Logs_Adapter(Context context, ArrayList<Properties> model) {
        this.context = context;
        this.model = model;
    }


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
        if (convertView==null){
            convertView = View.inflate(context, R.layout.logs_prof_design,null);
        }
        Properties properties =model.get(position);
        tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_transaction = (TextView) convertView.findViewById(R.id.tv_transaction);
        tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        tv_name.setText(properties.getStudentlname()+", "+properties.getStudentfname()+" "+properties.getStudentlname()+".");
        tv_transaction.setText(properties.getTransact());
        tv_time.setText(properties.getStartTime());
        return convertView;

    }
}
