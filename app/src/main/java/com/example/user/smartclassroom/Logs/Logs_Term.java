package com.example.user.smartclassroom.Logs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kenonnegammad on 31/01/2018.
 */

public class Logs_Term extends Fragment implements View.OnClickListener{
    View myView;
    Properties p=new Properties();
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;

    Logs_Adapter logs_adapter;
    Button course_logs_term;
    Spinner mySpinner;
    String[] splitter;
    ListView listView;
    View view1;
    String course;
    String sections;
    String room;
    String stud_id;
    String date_final;
    SwipeRefreshLayout swipeRefreshLayout;
    String month_final;
    String year_final;
    String day_final;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.logs_prof_term,container,false);
        stud_id = getArguments().getString("Stud_id").toString();
        course_logs_term = (Button) myView.findViewById(R.id.btn_logs_term_course);
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swipe_refresh_term);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Logs_Term_Task().execute();
            }
        });
        course_logs_term.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_logs_term_course){
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
                    Toast.makeText(getActivity(),mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    splitter = mySpinner.getSelectedItem().toString().split(" ");
                    course = splitter[0];
                    room = splitter[1];
                    sections = splitter[2];
                    System.out.println(course);
                    System.out.println(room);
                    System.out.println(sections);
//                   student_attendance_task.execute();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                new Logs_Term_Task().execute();
                            }
                        });
                        course_logs_term.setText(mySpinner.getSelectedItem().toString());
                    dialogInterface.dismiss();

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
public class Logs_Term_Task extends AsyncTask<Void,Void,String> {
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
        nameValuePairs = new ArrayList<NameValuePair>(2); //mga laman ng spinner para madetermine ko yung ilalabas kong attendance data nasa where clause ito ng php
        nameValuePairs.add(new BasicNameValuePair("course",course));
        nameValuePairs.add(new BasicNameValuePair("sections",sections));
        nameValuePairs.add(new BasicNameValuePair("room",room));
        try {
            //ip= new Properties();
            String Url = p.getIP()+"getTermLogs.php";
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
//        MessageBox(s);
        properties = logsUse(s);
        logs_adapter = new Logs_Adapter(getActivity(),properties);
        listView.setAdapter(logs_adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
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
//                {//select student.student_fname as fname,  student.student_mname as mname, student.student_lname as lname,
// logs.time, logs.transaction,logs.entity_type,logs.date
                user.setStudentfname(json_data.getString("fname")); //ipinasa dito
                user.setStudentmname(json_data.getString("mname"));
                user.setStudentlname(json_data.getString("lname"));
                user.setStartTime(json_data.getString("time"));
                user.setEntity(json_data.getString("entity_type"));
                user.setTransact(json_data.getString("transaction"));
                user.setDate(json_data.getString("date"));
                logsusers.add(user);
//                }
            }
            //attendanceusers.get(index)

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return logsusers;
    }
}
