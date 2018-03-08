package com.example.user.smartclassroom.Account;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.smartclassroom.R;
import  com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.Schedule.Schedule_Adapter;
import com.squareup.picasso.Picasso;
import  com.example.user.smartclassroom.Attendance.student_attendance_adaptor;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 29/01/2018.
 */

public class Student_Profile extends Fragment{
    View myView;
    Properties p=new Properties();
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    SwipeRefreshLayout swipeRefreshLayout;

    Spinner mySpinner;
    Button btnPickCourse;
    private Student_Attendance_Task student_attendance_task= new Student_Attendance_Task();
    student_attendance_adaptor student_attendance_adaptor;
    String[] splitter;
    ListView listView;
    View view1;
    String present;
    String late;
    String absent;
    String course;
    String sections;
    String room;
    String stud_id;
    String url;
    ProgressDialog dialog;
    CircleImageView imageView;
    TextView tv_name;
    TextView tv_studentnumber;
    TextView tv_present;
    String name;
    TextView tv_late;
    String id;
    String date;
    TextView tv_absent;
    String status;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.attendance_student,container,false);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Attendance...");
        listView = (ListView) myView.findViewById(R.id.list_view_attendance_student);
        imageView = (CircleImageView)myView.findViewById(R.id.imageView3);
        tv_present = (TextView)myView.findViewById(R.id.tv_present_attendance);
        tv_late = (TextView)myView.findViewById(R.id.tv_late_attendance);
        tv_absent = (TextView) myView.findViewById(R.id.tv_absent_attendance);
        tv_studentnumber = (TextView)myView.findViewById(R.id.tv_studentnumber);
        tv_name = (TextView)myView.findViewById(R.id.tv_name);
        stud_id = String.valueOf(getArguments().getInt("Stud_id"));
        course = getArguments().getString("course").toString();
        sections = getArguments().getString("sections").toString();
        room = getArguments().getString("room").toString();
//        date = getArguments().getString("date").toString();
        url = getArguments().getString("url").toString();
        name = getArguments().getString("name").toString();
//        status = getArguments().getString("status").toString();
        tv_name.setText(name);
        tv_studentnumber.setText(stud_id);
        new Student_Attendance_Task().execute();
        new getAttendance_Task().execute();
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swipe_attendance_student);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Student_Attendance_Task().execute();
                new getAttendance_Task().execute();
            }
        });
        Picasso.with(getActivity())
                .load(url)
                .resize(50, 50)
                .centerCrop()
                .into(imageView);
        //loadClass.execute();
        btnPickCourse = (Button)myView.findViewById(R.id.btnPickSubject);
        btnPickCourse.setText(course+" "+sections);
//        btnPickCourse.setOnClickListener(this);
        return  myView;
    }

    public class Student_Attendance_Task extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
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
            nameValuePairs.add(new BasicNameValuePair("id",stud_id));
            nameValuePairs.add(new BasicNameValuePair("course",course));
            nameValuePairs.add(new BasicNameValuePair("sections",sections));
            nameValuePairs.add(new BasicNameValuePair("room",room));

            try {
                //ip= new Properties();
                String Url = p.getIP()+"AttendanceStudent.php";
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
            dialog.dismiss();
            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            properties = attendanceStudent(s);
            student_attendance_adaptor = new student_attendance_adaptor(getActivity(),properties);
            listView.setAdapter(student_attendance_adaptor);
            swipeRefreshLayout.setRefreshing(false);
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
            nameValuePairs.add(new BasicNameValuePair("id",stud_id));
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
            swipeRefreshLayout.setRefreshing(false);

        }
    }
    //attendance
    public ArrayList<Properties> attendanceStudent(String result) {
        ArrayList<Properties> attendanceusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setAttendID(json_data.getInt("attendance_id"));
                user.setStatdescript(json_data.getString("status_description"));
                user.setDate(json_data.getString("date"));
                String formatstart_time=json_data.getString("time");


                try {
                    DateFormat start = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                    Date d = start.parse(formatstart_time);
                    DateFormat f2 = new SimpleDateFormat("h:mma");
                    String startingtime =f2.format(d).toLowerCase(); // "12:18am"


                    user.setStartTime(startingtime);//error
                    //if string contains su and myday is sunday bla bla
                    attendanceusers.add(user);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //attendanceusers.get(index)
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return attendanceusers;
    }
    //end
}
