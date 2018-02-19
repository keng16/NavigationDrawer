package com.example.user.smartclassroom.Schedule;

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

import com.example.user.smartclassroom.Attendance.Attendance_Daily;
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
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 12/31/15.
 */
public class Schedule extends Fragment implements View.OnClickListener{

    private View myView;
    Properties p= new Properties();
    private ListView listView;
    private ArrayList<Properties> properties;


    private Schedule_Adapter schedule_adapter;
    Button btn_day;
    Spinner mySpinner;
    String stud_id;
    String user;
    String selectedday;
    String PhpFile;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.schedule_student, container, false);
        btn_day = (Button)myView.findViewById(R.id.btn_select_day);
        user = getArguments().getString("User").toString();
        stud_id=this.getArguments().getString("Stud_id").toString();
        if(user.equals("Prof")){
            PhpFile = "SCHEDULE-PROF.php";
        }else if (user.equals("Student")){
            PhpFile = "STUDENT-SCHED.php";
        }
        selectedday = dayToday();
        btn_day.setText("TODAY IS "+selectedday);
        ScheduleTask scheduleTask;
        btn_day.setOnClickListener(this);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Schedule...");
        scheduleTask = new ScheduleTask();
        scheduleTask.execute();
        listView = (ListView) myView.findViewById(R.id.list_view_schedule_student);

        return myView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_select_day){
//            mySpinner.getSelectedItem().equals(selectedday);
            final Handler handler = new Handler();

            final ScheduleTask scheduleTask = new ScheduleTask();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity());
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Day");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.schedule_days));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(),mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    selectedday = mySpinner.getSelectedItem().toString();
//                   student_attendance_task.execute();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scheduleTask.execute();
                        }
                    });
                    btn_day.setText( "Selected day:"+" "+mySpinner.getSelectedItem().toString());
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

    public class ScheduleTask extends AsyncTask<Void,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... strings) {

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
            nameValuePairs.add(new BasicNameValuePair("day",selectedday));

            try {
                //ip= new Properties();
                String Url = p.getIP()+PhpFile;
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
            properties = new ArrayList<Properties>();
            //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            properties = parseSchedule(s);
            schedule_adapter = new Schedule_Adapter(getActivity(),properties);
            listView.setAdapter(schedule_adapter);
            //parseSchedule(s);
        }
    }


    public ArrayList<Properties> parseSchedule(String result) {
        ArrayList<Properties> mysched = new ArrayList<>();

        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setRoom(json_data.getString("rooms_id"));
                user.setCourse(json_data.getString("course_id"));
                user.setSection(json_data.getString("sections_id"));

                String formatstart_time=json_data.getString("start_time");
                String formatend_time=json_data.getString("end_time");


                try {
                    DateFormat start = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                    Date d = start.parse(formatstart_time);
                    DateFormat f2 = new SimpleDateFormat("h:mma");
                    String startingtime =f2.format(d).toLowerCase(); // "12:18am"

                    DateFormat end = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                    Date d2 = start.parse(formatend_time);
                    DateFormat f3 = new SimpleDateFormat("h:mma");
                    String endingtime =f3.format(d2).toLowerCase();

                    user.setStartTime(startingtime);//error
                    user.setEndTime(endingtime);//error
                    //if string contains su and myday is sunday bla bla
                    user.setDay(json_data.getString("day"));
                    mysched.add(user);
                    System.out.println(mysched);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return mysched;
    }
    public String dayToday()
    {
        String Day="";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                Day = "Sunday";
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                Day = "Monday";
                break;
            case Calendar.TUESDAY:
                // etc.
                Day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                Day = "Wednesday";
                break;
            case  Calendar.THURSDAY:
                Day = "Thursday";
                break;
            case  Calendar.FRIDAY:
                Day = "Friday";
                break;
            case Calendar.SATURDAY:
                Day = "Saturday";
                break;

        }
        return Day;
    }
    public String dateToday()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month_name = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (month >= 0 && month <= 11 ) {
            month_name = months[month];
        }
        String date_final = String.valueOf(day);
       String year_final = String.valueOf(year);
        String date = month_name+" "+date_final+" , "+year_final+", "+dayToday();
        //date_today.setText(date);
        return date;
    }
}
