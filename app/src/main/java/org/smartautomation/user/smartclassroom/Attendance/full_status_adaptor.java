package org.smartautomation.user.smartclassroom.Attendance;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.smartautomation.user.smartclassroom.Global.full_status_model;
import org.smartautomation.user.smartclassroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 05/03/2018.
 */

public class full_status_adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<full_status_model> model;
    CircleImageView circleImageView;
    TextView tv_studentnumber;
    TextView tv_fullname;
    TextView tv_present;
    TextView tv_late;
    TextView tv_absent;


    public full_status_adaptor(Context context, ArrayList<full_status_model> model) {
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
            view = View.inflate(context, R.layout.full_status_design,null);
        }
        circleImageView = (CircleImageView)view.findViewById(R.id.imageView_ProfilePic);
        tv_studentnumber = (TextView)view.findViewById(R.id.tv_studentnumber);
        tv_fullname = (TextView)view.findViewById(R.id.tv_fullname);
        tv_present = (TextView)view.findViewById(R.id.tv_present);
        tv_late = (TextView)view.findViewById(R.id.tv_late);
        tv_absent = (TextView)view.findViewById(R.id.tv_absent);

        full_status_model properties=model.get(i);
        Picasso.with(context)
                .load(properties.getStudent_ImgUrl())
                .resize(90,90)
                .into(circleImageView);
        tv_studentnumber.setText(properties.getStudent_id());
        tv_fullname.setText(properties.getStudent_name());
        tv_present.setText(properties.getPresent());
        tv_late.setText(properties.getLate());
        tv_absent.setText(properties.getAbsent());
        return  view;
    }
}
