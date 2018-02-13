package com.example.user.smartclassroom.Attendance;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.smartclassroom.Logs.Logs_Term;
import com.example.user.smartclassroom.R;

/**
 * Created by user on 12/31/15.
 */
public class Attendance extends Fragment implements View.OnClickListener{

    View myView;
    CardView today, term;
    String user;
    String stud_id;
    Bundle bundle=new Bundle();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // Toast.makeText(getActivity(),user,Toast.LENGTH_SHORT).show();
            myView = inflater.inflate(R.layout.attendance_dash, container, false);
            stud_id = getArguments().getString("Stud_id").toString();
            today = (CardView) myView.findViewById(R.id.AttenToday);
            term = (CardView) myView.findViewById(R.id.AttenTerm);
            today.setOnClickListener(this);
            term.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        final FragmentTransaction fragmentManager= getFragmentManager().beginTransaction();

        bundle.putString("Stud_id",stud_id);
        if(view.getId()==R.id.AttenToday) {
            Attendance_Daily attendance_daily=new Attendance_Daily();
            attendance_daily.setArguments(bundle);
            ((LinearLayout)myView.findViewById(R.id.LinearAttendance)).removeAllViews();
            fragmentManager
                    .replace(R.id.contentFrame
                            , attendance_daily,"Attendance_Daily")
                    .commit();
        }else if (view.getId()==R.id.AttenTerm){
            Attendance_Term attendance_term=new Attendance_Term();
            attendance_term.setArguments(bundle);
            ((LinearLayout)myView.findViewById(R.id.LinearAttendance)).removeAllViews();
            fragmentManager
                    .replace(R.id.contentFrame
                            , attendance_term,"Attendance_Daily")
                    .commit();
        }
    }

    public class AttendanceTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
