package org.smartautomation.user.smartclassroom.Attendance;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import org.smartautomation.user.smartclassroom.R;
import org.smartautomation.user.smartclassroom.Global.Properties;

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

public class Student_Attendance extends Fragment implements View.OnClickListener{
    View myView;
    Properties p=new Properties();
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    SwipeRefreshLayout swipeRefreshLayout;

    Spinner mySpinner;
    Button btnPickCourse;
    private LoadClass loadClass = new LoadClass();
    private Student_Attendance_Task student_attendance_task= new Student_Attendance_Task();
    student_attendance_adaptor student_attendance_adaptor;
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
    CircleImageView imageView;
    TextView tv_name;
    TextView tv_studentnumber;
    TextView tv_present;
    String name;
    TextView tv_late;
    TextView tv_absent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.attendance_student,container,false);
        stud_id = getArguments().getString("Stud_id").toString();
        url = getArguments().getString("Url").toString();
        name = getArguments().getString("Name").toString();
        listView = (SwipeMenuListView) myView.findViewById(R.id.list_view_attendance_student);
        imageView = (CircleImageView)myView.findViewById(R.id.imageView3);
        tv_present = (TextView)myView.findViewById(R.id.tv_present_attendance);
        tv_late = (TextView)myView.findViewById(R.id.tv_late_attendance);
        tv_absent = (TextView) myView.findViewById(R.id.tv_absent_attendance);
        tv_studentnumber = (TextView)myView.findViewById(R.id.tv_studentnumber);
        tv_name = (TextView)myView.findViewById(R.id.tv_name);
        tv_name.setText(name);
        tv_studentnumber.setText(stud_id);
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swipe_attendance_student);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Student_Attendance_Task().execute();
            }
        });
        Picasso.with(getActivity())
                .load(url)
                .resize(50, 50)
                .centerCrop()
                .into(imageView);
        //loadClass.execute();
        btnPickCourse = (Button)myView.findViewById(R.id.btnPickSubject);
        btnPickCourse.setOnClickListener(this);
        return  myView;
    }

    @Override
    public void onClick(View view) {
        final Handler handler = new Handler();
        final LoadClass loadClass=new LoadClass();

        if(view.getId()==R.id.btnPickSubject){
            loadClass.execute();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
            view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Course");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mySpinner!=null&&mySpinner.getSelectedItem()!=null) {
                        Toast.makeText(getActivity(), mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        splitter = mySpinner.getSelectedItem().toString().split(" ");
                        course = splitter[0];
                        room = splitter[1];
                        sections = splitter[2];
                        System.out.println(course);
                        System.out.println(room);
                        System.out.println(sections);
//                   student_attendance_task.execute();
                        if (course != null && sections != null && room != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    final Student_Attendance_Task student_attendance_task = new Student_Attendance_Task();
                                    student_attendance_task.execute();
                                    new getAttendance_Task().execute();
                                }
                            });
                            btnPickCourse.setText(mySpinner.getSelectedItem().toString());
                        }
                    }else{
                        Toast.makeText(getActivity(),"No Schedule",Toast.LENGTH_SHORT).show();
                    }

                    //dialogInterface.dismiss();

                }
            });
            mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            mBuilder.setView(view1);
            AlertDialog dialog= mBuilder.create();
            dialog.show();
        }
    }



    public class LoadClass extends AsyncTask<Void,Void,String>{

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
            try {
                //ip= new Properties();
                String Url = p.getIP()+"StudentCourse.php";
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
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            properties = parseJSON(s);
            addData(properties);
            //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
           // properties = parseJSON(s);
            //Toast.makeText(getActivity(),properties.toString(),Toast.LENGTH_SHORT).show();

        }
    }
//parseJSON courses
    public ArrayList<Properties> parseJSON(String result) {

        ArrayList<Properties> courseusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setRoom(json_data.getString("rooms_id")); //ipinasa dito
                user.setCourse(json_data.getString("course_id"));
                user.setSection(json_data.getString("sections_id"));
                //pic=getBitmapFromURL(json_data.getString(""));
                courseusers.add(user);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return courseusers;
    }
    //end
    //spinner add data
    public void addData(ArrayList<Properties> courseusers) {

        List<String> itemList=new ArrayList<String>();
        for (Iterator i = courseusers.iterator(); i.hasNext();) {
            Properties p = (Properties) i.next();
            itemList.add(p.getCourseId()+" "+p.getRoom()+" "+p.getSection());
        }
        adapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item,
                                 itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        //mySpinner.setAdapter(adapter);
    }
//end

    public class Student_Attendance_Task extends AsyncTask<Void,Void,String>{

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
//            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
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
    public void onBackPressed()
    {
        getActivity().finish();
        loadClass.cancel(true);
        student_attendance_task.cancel(true);
        swipeRefreshLayout.setRefreshing(false);
    }
    //end
}
