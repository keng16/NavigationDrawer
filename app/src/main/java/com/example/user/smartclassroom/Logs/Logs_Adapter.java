package com.example.user.smartclassroom.Logs;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    TextView tv_fullname;
    ImageView img_status;
    CircleImageView img_profile;
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
        tv_name = (TextView) convertView.findViewById(R.id.tv_name_logs);
        img_profile = (CircleImageView) convertView.findViewById(R.id.imageView_logs_profile);
        img_status = (ImageView) convertView.findViewById(R.id.imageView_Status_logs);
        cardView = (CardView)convertView.findViewById(R.id.cardview);
        tv_time = (TextView)convertView.findViewById(R.id.tv_time);
        tv_fullname = (TextView)convertView.findViewById(R.id.tv_fullname);
        tv_fullname.setText(properties.getStudentfname()+" "+properties.getStudentmname());
        tv_name.setText(properties.getStudentlname()+",");
//        tv_transaction.setText(properties.getTransact());
//        tv_time.setText(properties.getStartTime());
        Picasso.with(context)
                .load(properties.getPic())
                .resize(50, 50)
                .centerCrop()
                .into(img_profile);
        tv_time.setText(properties.getStartTime());
        if(properties.getTransact().equals("In")){
            img_status.setBackgroundResource(R.drawable.tapin);
        }else if(properties.getTransact().equals("Out")){
            img_status.setBackgroundResource(R.drawable.tapout);
        }

        //feb19



        return convertView;

    }
}
