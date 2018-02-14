package com.example.user.smartclassroom.Attendance;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.example.user.smartclassroom.Logs.Logs_Term;
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
 * Created by kenonnegammad on 01/02/2018.
 */

public class Attendance_Term extends Fragment implements View.OnClickListener {
    View myView;
    Button course_pick;
    Properties p=new Properties();
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    private attendance_prof_term_adaptor attendance_prof_term_adaptor;

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
    String month_final;
    String year_final;
    String day_final;
    ProgressDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.attendance_prof_term,container,false);
        stud_id = getArguments().getString("Stud_id").toString();
        listView = (ListView)myView.findViewById(R.id.list_view_attendance_term);
        course_pick = (Button) myView.findViewById(R.id.btn_attendance_term_course);
        course_pick.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_attendance_term_course){
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
                                final GetAttendance_Task getAttendance_task=new GetAttendance_Task();
                                getAttendance_task.execute();
                            }
                        });

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
    public class GetAttendance_Task extends AsyncTask<Void,Void,String>{

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
        // nameValuePairs.add(new BasicNameValuePair("id",stud_id));
        nameValuePairs.add(new BasicNameValuePair("course",course));
        nameValuePairs.add(new BasicNameValuePair("sections",sections));
        nameValuePairs.add(new BasicNameValuePair("room",room));
        try {
            //ip= new Properties();
            String Url = p.getIP()+"getTermAttendance.php";
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
        properties = attendanceUse(s);
        attendance_prof_term_adaptor = new attendance_prof_term_adaptor(getActivity(),properties);
        listView.setAdapter(attendance_prof_term_adaptor);
    }
}
    //Attendance
    public ArrayList<Properties> attendanceUse(String result) {
        ArrayList<Properties> attendanceusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setAttendID(json_data.getInt("attendance_id"));
                user.setStud_id(json_data.getInt("entity_id"));
                user.setStudentfname(json_data.getString("student_fname")); //ipinasa dito
                user.setStudentmname(json_data.getString("student_mname"));
                user.setStudentlname(json_data.getString("student_lname"));
                user.setStatdescript(json_data.getString("status_description"));
                user.setDate(new String(json_data.getString("date")));
                user.setPic(json_data.getString("student_ImgUrl"));
                attendanceusers.add(user);
                //attendanceusers.get(index)
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return attendanceusers;
    }
}
