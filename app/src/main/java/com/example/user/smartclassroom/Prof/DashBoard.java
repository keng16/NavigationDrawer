package com.example.user.smartclassroom.Prof;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.user.smartclassroom.Attendance.Attendance;
import com.example.user.smartclassroom.Logs.Logs_Dash;
import com.example.user.smartclassroom.R;
import com.example.user.smartclassroom.Schedule.Schedule;

/**
 * Created by kenonnegammad on 25/01/2018.
 */

public class DashBoard extends Fragment implements View.OnClickListener {
    View myView;
    String user;
    CardView cardAttendace, cardLogs,cardSchedule,cardController;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.dashboard, container, false);
        cardAttendace = (CardView) myView.findViewById(R.id.Attendance);
        cardLogs = (CardView) myView.findViewById(R.id.Logs);
        cardController = (CardView) myView.findViewById(R.id.Controller);
        cardSchedule = (CardView) myView.findViewById(R.id.Schedule);
        cardAttendace.setOnClickListener(this);
        cardLogs.setOnClickListener(this);
        cardController.setOnClickListener(this);
        cardSchedule.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle=new Bundle();
        bundle.putString("User",user);
        Attendance attendance=new Attendance();
        attendance.setArguments(bundle);

        if (view.getId()==R.id.Attendance) {
            ((LinearLayout) myView.findViewById (R.id.Linear)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame
                            , new Attendance())
                    .commit();
        }
        else if (view.getId()==R.id.Controller) {
            Controller controller=new Controller();
            ((LinearLayout) myView.findViewById(R.id.Linear)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame
                            , controller)
                    .commit();

        }else if (view.getId()==R.id.Logs) {
            Logs_Dash logs_dash=new Logs_Dash();
            ((LinearLayout) myView.findViewById(R.id.Linear)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame
                            ,logs_dash)
                    .commit();
        }else if (view.getId()==R.id.Schedule) {
            ((LinearLayout) myView.findViewById(R.id.Linear)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , new Schedule())
                    .commit();

        }
    }
}
