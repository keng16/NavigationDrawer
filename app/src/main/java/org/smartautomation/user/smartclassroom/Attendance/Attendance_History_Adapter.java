package org.smartautomation.user.smartclassroom.Attendance;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 16/03/2018.
 */

public class Attendance_History_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Properties> model;
    TextView tv_time;
    TextView tv_date;
    ImageView img_status;

    public Attendance_History_Adapter(Context context, ArrayList<Properties> model) {
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
            convertView = View.inflate(context, R.layout.attendance_history_design,null);
        }
        img_status = (ImageView)convertView.findViewById(R.id.imageView_status);
        tv_date = (TextView)convertView.findViewById(R.id.tv_date);
        tv_time = (TextView)convertView.findViewById(R.id.tv_time);
        Properties p=model.get(position);
        tv_time.setText(p.getStartTime());
        tv_date.setText(p.getDate());
        if (p.getStatdescript().equals("Present")){
            img_status.setBackgroundResource(R.drawable.present);
        }else if(p.getStatdescript().equals("Late")){
            img_status.setBackgroundResource(R.drawable.late);
        }else if (p.getStatdescript().equals("Absent")){
            img_status.setBackgroundResource(R.drawable.absent);
        }

        return convertView;
    }
}
