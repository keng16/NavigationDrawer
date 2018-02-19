package com.example.user.smartclassroom.Logs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.smartclassroom.Attendance.Attendance_Daily;
import com.example.user.smartclassroom.Attendance.Student_Attendance;
import com.example.user.smartclassroom.Attendance.student_attendance_adaptor;
import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.R;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kenonnegammad on 31/01/2018.
 */

public class Logs_Daily extends Fragment implements View.OnClickListener{
    View myView;
    Button course_btn,date;
    Properties p=new Properties();
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Logs_Adapter logs_adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner mySpinner;
    String[] splitter;
    ListView listView;
    View view1;
    String course;
    String sections;
    String room;
    String stud_id;
    String date_final="";
    String month_final;
    String year_final;
    String day;
    String day_final;
    ProgressDialog dialog;
    TimerTask timerTask;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.logs_prof_daily,container,false);
        stud_id = getArguments().getString("Stud_id").toString();
        course_btn = (Button) myView.findViewById(R.id.btn_logs_course);
        date = (Button) myView.findViewById(R.id.btn_logs_date);
        listView = (ListView)myView.findViewById(R.id.logs_daily);
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swipe_refresh_logs_daily);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Logs_Daily_Task().execute();
            }
        });
        date.setOnClickListener(this);
        course_btn.setOnClickListener(this);
        date_final = datetoday();
        date.setText(datetoday());
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                month_final = String.valueOf(month);
                year_final = String.valueOf(year);
                day_final = String.valueOf(day);
                if(day<10 && month<10) {
                    date_final = year_final + "-" + "0" + month_final + "-" + "0"+day_final;
                }else if(day>9 && month>9){
                    date_final = year_final + "-" + month_final + "-" + day_final;
                }else if(day>9&&month<10){
                    date_final = year_final + "-" + "0"+month_final + "-" + day_final;
                }else if(day<10&&month>9){
                    date_final = year_final + "-" + month_final + "-" + "0"+day_final;
                }
                MessageBox(date_final);
                if(course!=null&&sections!=null&&room!=null&&date_final!=null) {
                    final Handler handler=new Handler();
                    date.setText(date_final);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Logs_Daily_Task logs_daily_task=new Logs_Daily_Task();
                            logs_daily_task.execute();
                        }
                    });
                    //Toast.makeText(getActivity(), month_final + "/" + day_final + "/" + year_final, Toast.LENGTH_SHORT).show();
                }
                else {
                    MessageBox("Error");
                }
            }
        };

        return myView;
    }
    public void MessageBox(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_logs_course){
            final Handler handler = new Handler();
            final LoadClass loadClass=new LoadClass();
                loadClass.execute();
                AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity());
                view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
                mBuilder.setTitle("Pick Course");
                mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        splitter = mySpinner.getSelectedItem().toString().split(" ");
                        course = splitter[0];
                        room = splitter[1];
                        sections = splitter[2];
                        if(course.length()>0&&sections.length()>0&&room.length()>0&&date_final.length()>0&&date_final!=null) {
                            Toast.makeText(getActivity(), mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                            System.out.println(course);
                            System.out.println(room);
                            System.out.println(sections);
//                   student_attendance_task.execute();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Logs_Daily_Task logs_daily_task=new Logs_Daily_Task();
                                    logs_daily_task.execute();
                                }
                            });
                            course_btn.setText(mySpinner.getSelectedItem().toString());
                            //dialogInterface.dismiss();
                        }else {
                          MessageBox("Select Date");
                        }

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

        }else if (view.getId()==R.id.btn_logs_date){
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity()
                    ,android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener
                    ,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();

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
                String Url = p.getIP()+"Prof_Course.php";
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
            properties = parseJSON(s);
            addData(properties);
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
    public class Logs_Daily_Task extends AsyncTask<Void,Void,String> {
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
        //nameValuePairs.add(new BasicNameValuePair("term","3")); //mga laman ng spinner para madetermine ko yung ilalabas kong attendance data nasa where clause ito ng php
        nameValuePairs.add(new BasicNameValuePair("course",course));
        nameValuePairs.add(new BasicNameValuePair("sections",sections));
        nameValuePairs.add(new BasicNameValuePair("date_today",date_final ));
        nameValuePairs.add(new BasicNameValuePair("room",room));
        try {
            //ip= new Properties();
            String Url = p.getIP()+"getLogs.php";
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
        MessageBox(s);
        properties = logsUse(s);
        logs_adapter = new Logs_Adapter(getActivity(),properties);
        listView.setAdapter(logs_adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
    public String datetoday()
    {
        Calendar c = Calendar.getInstance();
        //	System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        day = df.format(c.getTime());
        MessageBox(day);
        return day;
    }
    public ArrayList<Properties> logsUse(String result) {
        ArrayList<Properties> logsusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();


//                if( json_data.getString("entity_type").equals("Professor")) //pag walang student na nag in at prof lang
//                {
//                    user.setProffname(json_data.getString("fname"));
//                    user.setProfmname(json_data.getString("mname"));
//                    user.setProflname(json_data.getString("lname"));
//                    user.setStartTime(json_data.getString("time"));
//                    user.setEntity(json_data.getString("entity_type"));
//                    user.setTransact(json_data.getString("transaction"));
//                    logsusers.add(user);
//                }
//                else
//                {
                    user.setStudentfname(json_data.getString("fname")); //ipinasa dito
                    user.setStudentmname(json_data.getString("mname"));
                    user.setStudentlname(json_data.getString("lname"));

                    // time convert to normal change feb/19
                    String timetap=json_data.getString("time");
                    DateFormat start = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                    Date d = start.parse(timetap);
                    DateFormat f2 = new SimpleDateFormat("h:mma");
                    String time_tap =f2.format(d).toLowerCase(); // "12:18am"
                    user.setStartTime(time_tap);
                    //end

                    user.setEntity(json_data.getString("entity_type"));
                    user.setTransact(json_data.getString("transaction"));
                    logsusers.add(user);
//                }
            }
            //attendanceusers.get(index)

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return logsusers;
    }

}
