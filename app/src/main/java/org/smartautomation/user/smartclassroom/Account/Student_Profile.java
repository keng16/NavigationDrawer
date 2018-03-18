package org.smartautomation.user.smartclassroom.Account;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.smartautomation.user.smartclassroom.Attendance.Attendance_History;
import org.smartautomation.user.smartclassroom.Attendance.update_attendance;
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.Attendance.student_attendance_adaptor;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.squareup.picasso.Picasso;

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
import org.smartautomation.user.smartclassroom.R;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    org.smartautomation.user.smartclassroom.Attendance.student_attendance_adaptor student_attendance_adaptor;
    String[] splitter;
    SwipeMenuListView listView;
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
    Bundle bundle=new Bundle();
    String attendance_id;
    String program;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(org.smartautomation.user.smartclassroom.R.layout.attendance_student,container,false);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Attendance...");
        listView = (SwipeMenuListView) myView.findViewById(org.smartautomation.user.smartclassroom.R.id.list_view_attendance_student);
        imageView = (CircleImageView)myView.findViewById(org.smartautomation.user.smartclassroom.R.id.imageView3);
        tv_present = (TextView)myView.findViewById(org.smartautomation.user.smartclassroom.R.id.tv_present_attendance);
        tv_late = (TextView)myView.findViewById(org.smartautomation.user.smartclassroom.R.id.tv_late_attendance);
        tv_absent = (TextView) myView.findViewById(org.smartautomation.user.smartclassroom.R.id.tv_absent_attendance);
        tv_studentnumber = (TextView)myView.findViewById(org.smartautomation.user.smartclassroom.R.id.tv_studentnumber);
        tv_name = (TextView)myView.findViewById(org.smartautomation.user.smartclassroom.R.id.tv_name);
        stud_id = String.valueOf(getArguments().getInt("Stud_id"));
        course = getArguments().getString("course").toString();
        attendance_id = String.valueOf(getArguments().getInt("attendance_id"));
        sections = getArguments().getString("sections").toString();
        room = getArguments().getString("room").toString();
          date = getArguments().getString("date").toString();
        url = getArguments().getString("url").toString();
        name = getArguments().getString("name").toString();
        status = getArguments().getString("status").toString();
        program = getArguments().getString("Program").toString();
        tv_name.setText(name);
        tv_studentnumber.setText(stud_id);
        new Student_Attendance_Task().execute();
        new getAttendance_Task().execute();
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(org.smartautomation.user.smartclassroom.R.id.swipe_attendance_student);
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
        btnPickCourse = (Button)myView.findViewById(org.smartautomation.user.smartclassroom.R.id.btnPickSubject);
        btnPickCourse.setText(course+" "+sections);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(
                            getActivity());
                    // set item background
                    openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    // set item width
                    openItem.setWidth(130);
                    // set item title
                    openItem.setTitle("Edit");
                    // set item title fontsize
                    openItem.setTitleSize(16);
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(openItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getActivity());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    // set item width
                    deleteItem.setWidth(130);
                    // set a icon
                    deleteItem.setTitle("Status");
                    deleteItem.setTitleSize(16);
                    deleteItem.setTitleColor(Color.WHITE);
                    menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                FragmentManager fragmentManager = getFragmentManager();
                switch (index) {
                    case 0:
                        p = properties.get(position);
                        //Toast.makeText(getActivity(),p.getStudentlname(),Toast.LENGTH_SHORT).show();
                        update_attendance update_attendance = new update_attendance();
                        //FragmentManager fragmentManager = getFragmentManager();
                        bundle.putInt("Stud_id",Integer.parseInt(stud_id));
                        bundle.putString("course",course);
                        bundle.putString("sections",sections);
                        bundle.putString("room",room);
                        bundle.putString("date",p.getDate());
                        bundle.putString("url",url);
                        bundle.putInt("attendance_id",p.getAttendID());
                        bundle.putString("name", name);
                        bundle.putString("status",p.getStatdescript());
                        bundle.putString("Program",program);
                        update_attendance.setArguments(bundle);
                        fragmentManager.beginTransaction().addToBackStack("Profile")
                                .replace(R.id.contentFrame
                                        , update_attendance)
                                .commit();
                        break;
                    case 1:
                        // delete
                        p = properties.get(position);
                        //Toast.makeText(getActivity(),p.getStudentlname(),Toast.LENGTH_SHORT).show();
                        Student_Profile student_profile=new Student_Profile();
                        //FragmentManager fragmentManager = getFragmentManager();
                        bundle.putInt("Stud_id",Integer.parseInt(stud_id));
                        bundle.putString("course",course);
                        bundle.putString("sections",sections);
                        bundle.putString("room",room);
                        bundle.putString("date",p.getDate());
                        bundle.putString("url",url);
                        bundle.putString("name", name);
                        bundle.putString("status",p.getStatdescript());
                        student_profile.setArguments(bundle);
                        fragmentManager.beginTransaction().addToBackStack("Profile")
                                .replace(R.id.contentFrame
                                        , student_profile)
                                .commit();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
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
            //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
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
