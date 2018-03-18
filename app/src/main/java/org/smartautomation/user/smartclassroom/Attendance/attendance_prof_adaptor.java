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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 02/02/2018.
 */

public class attendance_prof_adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<Properties> model;
    CardView cardView;
    CircleImageView student_pic;
    TextView tv_name;
    TextView tv_studnum;
    TextView tv_present,tv_late,tv_absent;
    TextView tv_status,tv_date;
    TextView tv_lname;
    ImageView img_status;
    ImageView img_history;

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
        tv_name = (TextView) view.findViewById(R.id.tv_fullname);
        student_pic = (CircleImageView)view.findViewById(R.id.imageView_ProfilePic);
        cardView=(CardView)view.findViewById(R.id.cardviewAttendance);
        tv_lname = (TextView)view.findViewById(R.id.tv_lname);
        img_status = (ImageView)view.findViewById(R.id.imageView_status);
        img_history = (ImageView)view.findViewById(R.id.image_history);


        Properties properties=model.get(i);
        Picasso.with(context)
                .load(properties.getPic())
                .resize(90,90)
                .into(student_pic);
        tv_lname.setText(properties.getStudentlname()+",");
        tv_name.setText(properties.getStudentfname()+" "+properties.getStudentmname());
        if(properties.getStatdescript().equals("Present")){
            img_status.setBackgroundResource(R.drawable.present);
        }else if(properties.getStatdescript().equals("Late")){
            img_status.setBackgroundResource(R.drawable.late);
        }else if(properties.getStatdescript().equals("Absent")){
            img_status.setBackgroundResource(R.drawable.absent);
        }
        //history
        if(properties.getHistory().equals("1")){
            img_history.setBackgroundResource(R.drawable.warninglogo);
        }
        return  view;
    }
}
