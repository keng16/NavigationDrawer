package com.example.user.smartclassroom.Account;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.smartclassroom.Attendance.Attendance_Term;
import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenonnegammad on 31/01/2018.
 */

public class Student_Profile extends Fragment implements RadioButton.OnClickListener{
    View myView;
    String status;
    String status_update;
    String id;
    String course;
    String sections;
    String room;
    String date;
    String url;
    String name;
    String present;
    String late;
    String absent;
    TextView tv_name;
    TextView tv_date;
    RadioButton rb_present;
    RadioButton rb_late;
    RadioButton rb_absent;
    ImageView imgProfile;
    TextView tv_course;
    TextView tv_present;
    TextView tv_late;
    TextView tv_absent;
    String[] splitter;
    Properties p=new Properties();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_student_override_attendance,container,false);
//        status = getArguments().getString("Status_Description").toString();
        tv_name = (TextView)myView.findViewById(R.id.tv_name);
        tv_date = (TextView)myView.findViewById(R.id.tv_date);
        rb_present = (RadioButton)myView.findViewById(R.id.rb_present);
        rb_late = (RadioButton)myView.findViewById(R.id.rb_late);
        rb_absent = (RadioButton)myView.findViewById(R.id.rb_absent);
        imgProfile = (ImageView)myView.findViewById(R.id.ImgView_Profile);
        tv_course = (TextView)myView.findViewById(R.id.tv_course);
        tv_present = (TextView)myView.findViewById(R.id.tv_present);
        tv_late = (TextView)myView.findViewById(R.id.tv_late);
        tv_absent = (TextView)myView.findViewById(R.id.tv_absent);
        rb_absent.setOnClickListener(this);
        rb_late.setOnClickListener(this);
        rb_present.setOnClickListener(this);
        id = String.valueOf(getArguments().getInt("Stud_id"));
        course = getArguments().getString("course").toString();
        sections = getArguments().getString("sections").toString();
        room = getArguments().getString("room").toString();
        date = getArguments().getString("date").toString();
        url = getArguments().getString("url").toString();
        name = getArguments().getString("name").toString();
        status = getArguments().getString("status").toString();
        Picasso.with(getActivity())
                .load("https://scontent.fmnl4-4.fna.fbcdn.net/v/t1.0-9/25498243_1940597495956755_8655756564574274692_n.jpg?_nc_eui2=v1%3AAeFwwatFfrOIXkER7QX4sbvpIH6mDnx1y85GjjiiZg-x4Sliu9sgMegLiNC3ikLf4A9z39rVXgAJCznsbRO-V5nhiAzJUAU7g8YOboUh8R-uXw&oh=4765dcc5dfa8337457abcb570082d608&oe=5B170219")
                .resize(120, 120)
                .into(imgProfile);
        if (status.equals("Present")){
            rb_present.setChecked(true);
        }
        if (status.equals("Absent")){
            rb_absent.setChecked(true);
        }
        if (status.equals("Late")){
            rb_late.setChecked(true);
        }

        new getAttendance_Task().execute();
        tv_name.setText(name);
        tv_date.setText(date);
        tv_course.setText(course+"-"+sections+"-"+room);
        return myView;
    }

    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.rb_present:
                if (checked){
                    rb_late.setChecked(false);
                    rb_absent.setChecked(false);
                    status_update = "1";
                    new Update_Attendance_Task().execute();
                    new getAttendance_Task().execute();
                }
                    break;
            case R.id.rb_late:
                if (checked){
                    rb_present.setChecked(false);
                    rb_absent.setChecked(false);
                    status_update = "2";
                    new Update_Attendance_Task().execute();
                    new getAttendance_Task().execute();
                }
                    break;
            case R.id.rb_absent:
                if(checked){
                    rb_present.setChecked(false);
                    rb_late.setChecked(false);
                    status_update = "3";
                    new Update_Attendance_Task().execute();
                    new getAttendance_Task().execute();
                }
                break;
        }
    }

    public class Update_Attendance_Task extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String output = "";
            byte[] data;
            HttpPost httppost;
            HttpClient httpclient;
            StringBuffer buffer = null;
            HttpResponse response;
            InputStream inputStream;
            HttpGet httpGet;
            Intent intent;
            String resultString="";
            Bundle extras=new Bundle();
            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id",id));
            nameValuePairs.add(new BasicNameValuePair("course",course));
            nameValuePairs.add(new BasicNameValuePair("sections",sections));
            nameValuePairs.add(new BasicNameValuePair("date",date));
            nameValuePairs.add(new BasicNameValuePair("room",room));
            nameValuePairs.add(new BasicNameValuePair("status",status_update));
            try {
                //ip= new Properties();
                String Url = p.getIP()+"Update_Attendance.php";
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(Url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
                inputStream = response.getEntity().getContent();

                data = new byte[256];

                buffer = new StringBuffer();

                int len = 0;

                while (-1 != (len = inputStream.read(data)) ) {
                    buffer.append(new String(data, 0, len));
                }
                //for the output or echo
                String bufferedInputString = buffer.toString();

                inputStream.close();
            }
            catch (final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "error: "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();

        }
    }
    public class getAttendance_Task extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String output = "";
            byte[] data;
            HttpPost httppost;
            HttpClient httpclient;
            StringBuffer buffer = null;
            HttpResponse response;
            InputStream inputStream;
            HttpGet httpGet;
            Intent intent;
            String resultString="";
            Bundle extras=new Bundle();
            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id",id));
            nameValuePairs.add(new BasicNameValuePair("course",course));
            nameValuePairs.add(new BasicNameValuePair("sections",sections));
            nameValuePairs.add(new BasicNameValuePair("room",room));
            try {
                //ip= new Properties();
                String Url = p.getIP()+"count_attendance.php";
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(Url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
                inputStream = response.getEntity().getContent();

                data = new byte[256];

                buffer = new StringBuffer();

                int len = 0;

                while (-1 != (len = inputStream.read(data)) ) {
                    buffer.append(new String(data, 0, len));
                }
                //for the output or echo
                String bufferedInputString = buffer.toString();

                inputStream.close();
            }
            catch (final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "error: "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            splitter = s.split(":");
            present = splitter[0];
            late = splitter[1];
            absent = splitter[2];
            tv_present.setText(present);
            tv_late.setText(late);
            tv_absent.setText(absent);

        }
    }
}
