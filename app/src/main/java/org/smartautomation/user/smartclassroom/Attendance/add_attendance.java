package org.smartautomation.user.smartclassroom.Attendance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 04/03/2018.
 */

public class add_attendance extends Fragment implements View.OnClickListener{

    View myView;
    CircleImageView circleImageView;
    TextView tv_studentnumber;
    TextView tv_fullname;
    TextView tv_program;
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String date_final;
    Spinner mySpinner;
    TextView tv_course;
    TextView tv_date;
    TextView tv_status;
    View view1;
    Button btn_status;
    String status;
    String status_attedance;
    String stud_id;
    String course;
    String sections;
    String room;
    String[] splitter;
    String date;
    String month_final;
    String day_final;
    String year_final;
    String prof_id;
    String url;
    String name;
    String program;
    String attendance_id;
    Properties p =new Properties();
    String condition;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.add_attendance,container,false);
        circleImageView = (CircleImageView)myView.findViewById(R.id.imageView_ProfilePic);
        tv_studentnumber = (TextView)myView.findViewById(R.id.tv_studentnumber);
        tv_fullname = (TextView)myView.findViewById(R.id.tv_fullname);
        tv_program = (TextView)myView.findViewById(R.id.tv_program);
        tv_course = (TextView)myView.findViewById(R.id.tv_course);
        tv_date = (TextView)myView.findViewById(R.id.tv_date);
        tv_status = (TextView)myView.findViewById(R.id.tv_status);
        btn_status = (Button)myView.findViewById(R.id.btn_change_status);
        prof_id = getArguments().getString("Stud_id").toString();

        tv_course.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_status.setOnClickListener(this);
        tv_studentnumber.setOnClickListener(this);
        btn_status.setOnClickListener(this);
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
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
                tv_date.setText(date_final);
                MessageBox(date_final);
            }
        };

        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_course){
            final Handler handler = new Handler();
            new LoadClass().execute();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
            view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Course");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(mySpinner!=null&&mySpinner.getSelectedItem()!=null) {
                        Toast.makeText(getActivity(), mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        splitter = mySpinner.getSelectedItem().toString().split(" ");
                        course = splitter[0];
                        room = splitter[1];
                        sections = splitter[2];
                        tv_course.setText(mySpinner.getSelectedItem().toString());
                        //dialogInterface.dismiss();
                    }else{
                        MessageBox("No Subject");
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
        }else if (v.getId()==R.id.tv_date){
            date_final = "";
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity()
                    ,android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener
                    ,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }else if (v.getId()==R.id.tv_status){
            final Handler handler = new Handler();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Day");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.attendance_status));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(),mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    status = mySpinner.getSelectedItem().toString();
                    if(status.equals("Present")){
                        status_attedance = "1";
                    }else if(status.equals("Late")){
                        status_attedance = "2";
                    }else if(status.equals("Absent")){
                        status_attedance = "3";
                    }else if(status.equals("Excused")){
                        status_attedance = "4";
                    }
                    tv_status.setText(status);
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

        }else if (v.getId()==R.id.tv_studentnumber){
            final Handler handler = new Handler();
            condition = "getClass";
            new add_attendance_task().execute();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
            view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Student");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(mySpinner!=null&&mySpinner.getSelectedItem()!=null) {
                        Toast.makeText(getActivity(), mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        //splitter = mySpinner.getSelectedItem().toString().split(" ");
                        tv_studentnumber.setText(mySpinner.getSelectedItem().toString());
                        stud_id = mySpinner.getSelectedItem().toString();
                        condition = "getStudent";
                        //dialogInterface.dismiss();
                        new add_attendance_task().execute();
                    }else{
                        MessageBox("No Students");
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
        }else if(v.getId()==R.id.btn_change_status){
            condition = "AddAttendance";
            if(course!=null||sections!=null||room!=null||stud_id!=null||status_attedance!=null){
                new add_attendance_task().execute();
            }else{
                MessageBox("Please fill out fields!");
            }

        }
    }
    public class LoadClass extends AsyncTask<Void,Void,String> {
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
            nameValuePairs.add(new BasicNameValuePair("id",prof_id));
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
    public void MessageBox(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
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
    public class add_attendance_task extends AsyncTask<Void,Void,String>{

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
        nameValuePairs.add(new BasicNameValuePair("condition",condition));
        nameValuePairs.add(new BasicNameValuePair("course",course));
        nameValuePairs.add(new BasicNameValuePair("sections",sections));
        nameValuePairs.add(new BasicNameValuePair("rooms",room));
        nameValuePairs.add(new BasicNameValuePair("student_id",stud_id));
        nameValuePairs.add(new BasicNameValuePair("status",status_attedance));
        nameValuePairs.add(new BasicNameValuePair("date",date_final));
        try {
            //ip= new Properties();
            String Url = p.getIP()+"add_attendance.php";
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
            if(condition.equals("getClass")){
                MessageBox(s);
                properties = getparseStudent(s);
                addStudent(properties);
            }else if(condition.equals("getStudent")){
                parseStudent(s);
            }else if (condition.equals("AddAttendance")){
                MessageBox(s);
            }
        }
    }
    public void parseStudent(String result) {

        ArrayList<Properties> courseusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                url = json_data.getString("student_ImgUrl");
                program = json_data.getString("student_program");
                name = json_data.getString("student_lname")+","+json_data.getString("student_fname")+" "+json_data.getString("student_mname");
                tv_fullname.setText(name);
                tv_program.setText(program);
                Picasso.with(getActivity())
                        .load(url)
                        .resize(144, 144)
                        .centerCrop()
                        .into(circleImageView);

                //pic=getBitmapFromURL(json_data.getString(""));
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }
    public ArrayList<Properties> getparseStudent(String result) {

        ArrayList<Properties> courseusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setStud_id(json_data.getInt("student_id"));
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
    public void addStudent(ArrayList<Properties> courseusers) {

        List<String> itemList=new ArrayList<String>();
        for (Iterator i = courseusers.iterator(); i.hasNext();) {
            Properties p = (Properties) i.next();
            itemList.add(String.valueOf(p.getStud_id()));
        }
        adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        //mySpinner.setAdapter(adapter);
    }
}
