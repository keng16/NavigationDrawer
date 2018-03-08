package com.example.user.smartclassroom.Notif;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.smartclassroom.Global.NotificationModel;
import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 05/03/2018.
 */

public class notification_adapter extends BaseAdapter {
    private Context context;
    private ArrayList<NotificationModel> model;
    CardView cardView;
    TextView tv_title;
    TextView tv_body;
    TextView tv_date;
    TextView tv_time;

    public notification_adapter(Context context, ArrayList<NotificationModel> model) {
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
            view = View.inflate(context, R.layout.notification_design,null);
        }
        cardView = (CardView)view.findViewById(R.id.cardview);
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_body = (TextView)view.findViewById(R.id.tv_body);
        tv_date = (TextView)view.findViewById(R.id.tv_date);
        tv_time = (TextView)view.findViewById(R.id.tv_time);

        NotificationModel notificationModel = model.get(i);
        tv_title.setText(notificationModel.getTitle());
        tv_body.setText(notificationModel.getBody());
        tv_date.setText(notificationModel.getDate());
        tv_time.setText(notificationModel.getTime());
        if(notificationModel.getStatus().equals("Unseen")){
            cardView.setCardBackgroundColor(Color.parseColor("#D99ece76"));
        }else if (notificationModel.getStatus().equals("Seen")){
            cardView.setCardBackgroundColor(Color.parseColor("#73008000"));
        }
        return  view;
    }
}
