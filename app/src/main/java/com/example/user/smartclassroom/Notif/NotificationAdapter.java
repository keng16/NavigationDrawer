package com.example.user.smartclassroom.Notif;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.smartclassroom.Global.NotificationModel;
import com.example.user.smartclassroom.R;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by kenonnegammad on 14/02/2018.
 */

public class NotificationAdapter extends BaseAdapter {
    protected ArrayList<NotificationModel> model;
    protected Context context;
    TextView tv_message;
    TextView tv_date;

    public NotificationAdapter(ArrayList<NotificationModel> model, Context context) {
        this.model = model;
        this.context = context;
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
        if(convertView==null){
            convertView = View.inflate(context, R.layout.notification_design,null);
        }
        tv_date = (TextView)convertView.findViewById(R.id.tv_date_notification);
        tv_message = (TextView)convertView.findViewById(R.id.tv_message_notification);
        NotificationModel notificationModel = model.get(position);
        tv_date.setText(notificationModel.getDate());
        tv_message.setText(notificationModel.getMessage());

        return convertView;
    }
}
