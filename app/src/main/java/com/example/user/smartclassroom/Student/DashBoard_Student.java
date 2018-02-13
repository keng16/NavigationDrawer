package com.example.user.smartclassroom.Student;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.user.smartclassroom.Attendance.Student_Attendance;
import com.example.user.smartclassroom.R;
import com.example.user.smartclassroom.Schedule.Schedule;

/**
 * Created by kenonnegammad on 25/01/2018.
 */

public class DashBoard_Student extends Fragment implements View.OnClickListener {
    private View myView;
    private String user,stud_id;
    private CardView cardAttendace, cardLogs,cardSchedule,cardController;
    private Student_Attendance attendance=new Student_Attendance();
    private Schedule schedule=new Schedule();
    Bundle bundle=new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.dashboard_student, container, false);
        stud_id = getArguments().getString("Stud_id").toString();
        cardAttendace = (CardView) myView.findViewById(R.id.Attendance);
        cardSchedule = (CardView) myView.findViewById(R.id.Schedule);
        cardAttendace.setOnClickListener(this);
        cardSchedule.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        bundle.putString("Stud_id",stud_id);

        if (view.getId()==R.id.Attendance) {
            attendance.setArguments(bundle);
            ((LinearLayout) myView.findViewById (R.id.Linear_Student)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , attendance)
                    .commit();
        }else if (view.getId()==R.id.Schedule) {
            schedule.setArguments(bundle);
            ((LinearLayout) myView.findViewById(R.id.Linear_Student)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , schedule)
                    .commit();

        }
    }
}
