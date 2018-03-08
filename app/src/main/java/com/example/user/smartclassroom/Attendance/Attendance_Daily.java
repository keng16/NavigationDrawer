package com.example.user.smartclassroom.Attendance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.user.smartclassroom.Account.Student_Profile;
import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.Logs.Logs_Daily;
import com.example.user.smartclassroom.Prof.Controller;
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
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kenonnegammad on 01/02/2018.
 */

public class Attendance_Daily extends Fragment implements View.OnClickListener {
    View myView;
    Button course_pick,date_pick;
    TextView date_today;
    Properties p=new Properties();
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private attendance_prof_adaptor attendance_prof_adaptor;

    String day;
    Spinner mySpinner;
    String[] splitter;
    SwipeMenuListView listView;
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
    Bundle bundle=new Bundle();
    TimerTask timerTask;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.attendance_prof_daily,container,false);
        stud_id = getArguments().getString("Stud_id").toString();

        course_pick = (Button) myView.findViewById(R.id.btn_attendance_course);
        date_pick = (Button) myView.findViewById(R.id.btn_attendance_date);
        listView = (SwipeMenuListView) myView.findViewById(R.id.listview_attendance_daily);
        swipeRefreshLayout = (SwipeRefreshLayout) myView.findViewById(R.id.swipe_refresh_attendance_daily);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getAttendance_Daily().execute();
            }
        });
        //datetoday();
        date_final = datetoday();
        date_pick.setText(datetoday());
        date_pick.setOnClickListener(this);
        course_pick.setOnClickListener(this);
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
                MessageBox(date_final);
                if(course!=null&&sections!=null&&room!=null&&date_final!=null){
                    final Handler handler=new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            new getAttendance_Daily().execute();
                        }
                    });
                    date_pick.setText(date_final);
                }else {
                    date_pick.setText(date_final);
                    Toast.makeText(getActivity(),"Pick Course",Toast.LENGTH_SHORT).show();
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
        if (view.getId()==R.id.btn_attendance_course){
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
                    if(course.length()>0&&sections.length()>0&&room.length()>0&&date_final.length()>0&&date_final.length()>0) {
                        System.out.println(course);
                        System.out.println(room);
                        System.out.println(sections);
//                   student_attendance_task.execute();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                new getAttendance_Daily().execute();
                            }
                        });

                        course_pick.setText(mySpinner.getSelectedItem().toString());
                    }else {
                        MessageBox("Pick Date");
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
        }else if(view.getId()==R.id.btn_attendance_date){
//            timerTask.cancel();
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
        }
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
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

//                deleteItem.setIcon(R.drawable.ic_wifi_black_24dp);
                // add to menu
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
                        Toast.makeText(getActivity(),"Edit",Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"Show",Toast.LENGTH_LONG).show();
                        p = properties.get(position);
                        Toast.makeText(getActivity(),p.getStudentlname(),Toast.LENGTH_SHORT).show();
                        update_attendance update_attendance = new update_attendance();
                        //FragmentManager fragmentManager = getFragmentManager();
                        bundle.putInt("Stud_id",p.getStud_id());
                        bundle.putString("course",course);
                        bundle.putString("sections",sections);
                        bundle.putString("room",room);
                        bundle.putString("date",p.getDate());
                        bundle.putString("url",p.getPic());
                        bundle.putInt("attendance_id",p.getAttendID());
                        bundle.putString("name", p.getStudentfname()+" "+p.getStudentmname()+" "+p.getStudentlname());
                        bundle.putString("status",p.getStatdescript());
                        bundle.putString("Program",p.getStudent_program());
                        update_attendance.setArguments(bundle);
                        fragmentManager.beginTransaction().addToBackStack("Profile")
                                .replace(R.id.contentFrame
                                        , update_attendance)
                                .commit();
                        break;
                    case 1:
                        // delete
                        Toast.makeText(getActivity(),"Show",Toast.LENGTH_LONG).show();
                        p = properties.get(position);
                        Toast.makeText(getActivity(),p.getStudentlname(),Toast.LENGTH_SHORT).show();
                        Student_Profile student_profile=new Student_Profile();
                        //FragmentManager fragmentManager = getFragmentManager();
                        bundle.putInt("Stud_id",p.getStud_id());
                        bundle.putString("course",course);
                        bundle.putString("sections",sections);
                        bundle.putString("room",room);
                        bundle.putString("date",p.getDate());
                        bundle.putString("url",p.getPic());
                        bundle.putString("name", p.getStudentfname()+" "+p.getStudentmname()+" "+p.getStudentlname());
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
        date_final = String.valueOf(day);
        year_final = String.valueOf(year);
        String date = month_name+" "+date_final+" , "+year_final+", "+dayToday();

        return date;
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
    public class getAttendance_Daily extends AsyncTask<Void,Void,String>{
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
        nameValuePairs.add(new BasicNameValuePair("date_today",date_final));
        nameValuePairs.add(new BasicNameValuePair("course",course));
        nameValuePairs.add(new BasicNameValuePair("sections",sections));
        nameValuePairs.add(new BasicNameValuePair("room",room));
        try {
            //ip= new Properties();
            String Url = p.getIP()+"getStudentAttendance.php";
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
//        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
        properties = attendanceUse(s);
        attendance_prof_adaptor=new attendance_prof_adaptor(getActivity(),properties);
        listView.setAdapter(attendance_prof_adaptor);
        swipeRefreshLayout.setRefreshing(false);
       // timerTask.cancel();
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
                user.setStudent_program(json_data.getString("student_program"));
                user.setDate(json_data.getString("date"));
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
