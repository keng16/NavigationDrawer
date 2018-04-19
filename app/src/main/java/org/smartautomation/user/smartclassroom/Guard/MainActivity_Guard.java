package org.smartautomation.user.smartclassroom.Guard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartautomation.user.smartclassroom.Account.Account_Class;
import org.smartautomation.user.smartclassroom.Account.Student_Profile;
import org.smartautomation.user.smartclassroom.Attendance.Attendance_History;
import org.smartautomation.user.smartclassroom.Attendance.Student_Attendance;
import org.smartautomation.user.smartclassroom.Attendance.update_attendance;
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.LoginActivity;
import org.smartautomation.user.smartclassroom.Guard.*;
import org.smartautomation.user.smartclassroom.Logs.Logs_Daily;
import org.smartautomation.user.smartclassroom.Notif.Notification_Class;
import org.smartautomation.user.smartclassroom.R;
import org.smartautomation.user.smartclassroom.Schedule.Schedule;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.smartautomation.user.smartclassroom.Schedule_Guard.Schedule_Guard;

import java.io.InputStream;
import java.security.Guard;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_Guard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    TextView textName;
    TextView textEmail;
    Properties p=new Properties();
    Bundle extras;
    String token;
    String name, email,user,url,stud_id;
    String control;
    Bundle bundle = new Bundle();
    Button btnendtime;
    Button btnroom;
    ArrayList<Properties> properties;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ArrayAdapter<String> adapter;
    Button btnstarttime;
    Button btndate;
    String condition;
    Spinner mySpinner;
    String date_today;
    final FragmentManager fragmentManager = getFragmentManager();
    Guard_Logs_Adapter guard_logs_adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    SwipeMenuListView listView;
    String[] splitter;
    View view1;
    String start_time;
    String end_time;
    String daytoday;
    String month_final;
    String year_final;
    String day_final;
    String date_final;

    //FragmentManager fragmentManager = getFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Bundle extras = getIntent();
        control="Login";
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
       // url = getIntent().getStringExtra("Url");
        user = getIntent().getStringExtra("User");
        stud_id = getIntent().getStringExtra("Stud_id");
//        FirebaseMessaging.getInstance().subscribeToTopic("test");
        token=FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(MainActivity_Student.this,url,Toast.LENGTH_LONG).show();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        //imageview
        new registerToken().execute();


        date_today = datenow();
       //
        // MessageBox(date_today);
        //MessageBox(daytoday);

        CircleImageView imgView = (CircleImageView) hView.findViewById(R.id.imageView);
        Picasso.with(MainActivity_Guard.this)
                .load(url)
                .resize(60, 60)
                .centerCrop()
                .into(imgView);
        //end
        //email and name
        textName = (TextView) hView.findViewById(R.id.textName);
        textEmail = (TextView) hView.findViewById(R.id.textEmail);
        textName.setText(name);
        textEmail.setText(email);
        //end
        //content
        View cView = findViewById(R.id.contentView);
        btnroom = (Button)cView.findViewById(R.id.btn_logs_room_guard);
        btnstarttime = (Button)cView.findViewById(R.id.btn_logs_start_time_guard);
        btndate = (Button)cView.findViewById(R.id.btn_logs_date_guard);
        listView = (SwipeMenuListView) cView.findViewById(R.id.logs_guard);
        btnendtime = (Button)cView.findViewById(R.id.btn_logs_end_time_guard);
        btndate.setText(date_today());
        swipeRefreshLayout = (SwipeRefreshLayout)cView.findViewById(R.id.swipe_refresh_logs_guard);
        btnroom.setOnClickListener(this);
        btnstarttime.setOnClickListener(this);
        btnendtime.setOnClickListener(this);
        btndate.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Logs_Task().execute();
            }
        });
        //end
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                month_final = String.valueOf(month);
                year_final = String.valueOf(year);
                day_final = String.valueOf(day);
                String dates = dateToday(year,month-1,day);

                if(day<10 && month<10) {
                    date_final = year_final + "-" + "0" + month_final + "-" + "0"+day_final;
                }else if(day>9 && month>9){
                    date_final = year_final + "-" + month_final + "-" + day_final;
                }else if(day>9&&month<10){
                    date_final = year_final + "-" + "0"+month_final + "-" + day_final;
                }else if(day<10&&month>9){
                    date_final = year_final + "-" + month_final + "-" + "0"+day_final;
                }
                date_today = date_final;
                //MessageBox(date_final);
                if(date_final!=null&&end_time!=null&&start_time!=null) {
                    final Handler handler=new Handler();
                    btndate.setText(dates);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            condition = "getlogs";
                            new Logs_Task().execute();
                        }
                    });
                    //Toast.makeText(getActivity(), month_final + "/" + day_final + "/" + year_final, Toast.LENGTH_SHORT).show();
                }
                else {
                    btndate.setText(dates);
                    MessageBox("Error");
                }
            }
        };
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else{
//            drawer.openDrawer(GravityCompat.START);
//        }
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        bundle.putString("User",user);
        bundle.putString("Stud_id",stud_id);
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
            alertDialogBuilder.setMessage("Are you sure?");
            alertDialogBuilder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int arg1) {
                            //Toast.makeText(MainActivity_Student.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity_Guard.this,LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            control = "Logout";
                            token = null;
                            new registerToken().execute();
                            startActivity(i);
                            dialogInterface.dismiss();

                        }
                    });
            alertDialogBuilder.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }else if (id==R.id.nav_schedule){
            Schedule_Guard schedule_guard=new Schedule_Guard();
//                    ((LinearLayout) findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            schedule_guard.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , schedule_guard).addToBackStack(null)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();

        }else if (id==R.id.nav_purpose){
            Purpose_Guard purpose_guard=new Purpose_Guard();
            purpose_guard.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , purpose_guard).addToBackStack(null)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void MessageBox(String message){
        Toast.makeText(MainActivity_Guard.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_logs_start_time_guard){
            MessageBox("Time");
            final Handler handler = new Handler();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(MainActivity_Guard.this,R.style.MyDialogTheme);
            View view1 = getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Day");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity_Guard.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.start_time));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(MainActivity_Guard.this,mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    start_time = mySpinner.getSelectedItem().toString();
                    try{
                        //start time convertion from 12 hours to 24 hours
                        SimpleDateFormat inFormat = new SimpleDateFormat("h:mma");
                        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
                        String start_time24 = outFormat.format(inFormat.parse(start_time));
                        //end of start time convertion

                        start_time = start_time24;
                        System.out.println(start_time);
                        MessageBox(start_time);
                    }catch (ParseException e){

                    }
                    if (start_time!=null&&end_time!=null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                condition = "getlogs";
                                new Logs_Task().execute();
                            }
                        });
                    }else{
                        MessageBox("Complete All Fields!");
                    }
                    btnstarttime.setText(mySpinner.getSelectedItem().toString());
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
        }else if (view.getId()==R.id.btn_logs_end_time_guard){
            final Handler handler = new Handler();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(MainActivity_Guard.this,R.style.MyDialogTheme);
            View view1 = getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Day");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity_Guard.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.end_time));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(MainActivity_Guard.this,mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    end_time = mySpinner.getSelectedItem().toString();
                    try{
                        //start time convertion from 12 hours to 24 hours
                        SimpleDateFormat inFormat = new SimpleDateFormat("h:mma");
                        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
                        String end_time24 = outFormat.format(inFormat.parse(end_time));
                        //end of start time convertion

                        end_time = end_time24;
                        System.out.println(end_time);
                        MessageBox(end_time);
                    }catch (ParseException e){

                    }
//                   student_attendance_task.execute();

                    if (start_time!=null&&end_time!=null&&end_time!=start_time){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                condition = "getlogs";
                                new Logs_Task().execute();
                            }
                        });
                    }else{
                        MessageBox("Complete All Fields!");
                    }
                    btnendtime.setText(mySpinner.getSelectedItem().toString());
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
        else if (view.getId()==R.id.btn_logs_room_guard){
            MessageBox("Room");
        }else if (view.getId()==R.id.btn_logs_date_guard){
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog= new DatePickerDialog(this
                    ,android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener
                    ,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // add to menu
                    SwipeMenuItem openItem = new SwipeMenuItem(
                            MainActivity_Guard.this);
                    // set item background
                    openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    // set item width
                    openItem.setWidth(130);
                    // set item title
                    openItem.setTitle("Purpose");
                    // set item title fontsize
                    openItem.setTitleSize(16);
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(openItem);

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
                        MessageBox("Remarks");
                        getPurpose purpose=new getPurpose();
                        p = properties.get(position);
                        //FragmentManager fragmentManager = getFragmentManager();
//                        bundle.putString("start",start_time);
//                        bundle.putString("end",end_time);
//                        bundle.putString("name",name);
                        bundle.putString("starttime",p.getStartTime());
                        bundle.putString("room","R206");
                        bundle.putString("date",date_final);
                        purpose.setArguments(bundle);
                        fragmentManager.beginTransaction().addToBackStack(null)
                                .replace(R.id.contentFrame_Student
                                        , purpose)
                                .commit();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }
    public class registerToken extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("control",control));
            nameValuePairs.add(new BasicNameValuePair("id",stud_id));
            nameValuePairs.add(new BasicNameValuePair("Token",token));

            try {
                //ip= new Properties();
                String Url = p.getIP()+"register.php";
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
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MainActivity_Guard.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    //parseJSON courses
    public ArrayList<Properties> getTime(String result) {

        ArrayList<Properties> courseusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
//                user.setRoom(json_data.getString("rooms_id")); //ipinasa dito
//                user.setCourse(json_data.getString("course_id"));
//                user.setSection(json_data.getString("sections_id"));

                //convert start time
                String timetap=json_data.getString("start_time");
                DateFormat start = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                Date d = start.parse(timetap);
                DateFormat f2 = new SimpleDateFormat("h:mma");
                String start_time =f2.format(d).toLowerCase(); // "12:18am"

                //convert end time
                String end_tap=json_data.getString("end_time");
                DateFormat end = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                Date d2 = end.parse(end_tap);
                DateFormat f3 = new SimpleDateFormat("h:mma");
                String end_time =f3.format(d2).toLowerCase(); // "12:18am"
                //
                user.setStartTime(start_time);
                user.setEndTime(end_time);
                //pic=getBitmapFromURL(json_data.getString(""));
                courseusers.add(user);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return courseusers;
    }
    //end
    //spinner add data
    public void addData(ArrayList<Properties> courseusers) {

        List<String> itemList=new ArrayList<String>();
        for (Iterator i = courseusers.iterator(); i.hasNext();) {
            Properties p = (Properties) i.next();
            itemList.add(p.getStartTime()+"-"+p.getEndTime());
        }
        adapter = new ArrayAdapter(MainActivity_Guard.this,
                android.R.layout.simple_spinner_item,
                itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        //mySpinner.setAdapter(adapter);
    }
    public class Logs_Task extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("date_today",date_today));
            nameValuePairs.add(new BasicNameValuePair("start",start_time));
            nameValuePairs.add(new BasicNameValuePair("end",end_time));
            nameValuePairs.add(new BasicNameValuePair("term","3"));
            nameValuePairs.add(new BasicNameValuePair("day_today",daytoday));
            nameValuePairs.add(new BasicNameValuePair("room","R206"));

            try {
                //ip= new Properties();
                String Url = p.getIP()+"getGuardLogs.php";
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
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MainActivity_Guard.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //MessageBox(s);
            System.out.println(s);
            if (condition.equals("getlogs")) {
                properties = logsUse(s);
                guard_logs_adapter = new Guard_Logs_Adapter(getApplicationContext(), properties);
                listView.setAdapter(guard_logs_adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
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

                user.setLogsid(json_data.getString("logs_id"));
                splitter = json_data.getString("entity_data").split("\\+");
                user.setStudentfname(splitter[0]);
                user.setStudentmname(splitter[1]);
                user.setStudentlname(splitter[2]);
                user.setPic(splitter[3]);

                // time convert to normal change feb/19
                String timetap=json_data.getString("time");
                DateFormat start = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                Date d = start.parse(timetap);
                DateFormat f2 = new SimpleDateFormat("h:mma");
                String time_tap =f2.format(d).toLowerCase(); // "12:18am"
                user.setStartTime(time_tap);
                //end
                user.setTransact(json_data.getString("transaction"));
                user.setStudRemark(json_data.getString("remarks"));
                logsusers.add(user);
            }
            //attendanceusers.get(index)

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return logsusers;
    }
    public String date_today(){

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
        daytoday=dayToday(year,month,day-1);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date dates = new Date(year, month, day-1);
        String dayOfWeek = simpledateformat.format(dates);
        String date_final = String.valueOf(day);
        String year_final = String.valueOf(year);
        String date = month_name+" "+date_final+" , "+year_final+" "+dayOfWeek;
        return date;
    }
    public String dateToday(int year,int month,int day)
    {
        Calendar cal = Calendar.getInstance();

        String month_name = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (month >= 0 && month <= 11 ) {
            month_name = months[month];
        }
        daytoday=dayToday(year,month,day-1);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date dates = new Date(year, month, day-1);
        String dayOfWeek = simpledateformat.format(dates);
        String date_final = String.valueOf(day);
        String year_final = String.valueOf(year);
        String date = month_name+" "+date_final+" , "+year_final+" "+dayOfWeek;
        return date;
    }
    public String dayToday(int year, int month, int day){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date dates = new Date(year, month, day);
        String dayOfWeek = simpledateformat.format(dates);
        return dayOfWeek;
    }
    public String datenow(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month=month+1;
        String year1 = String.valueOf(year);
        String month1 = String.valueOf(month);
        String day1 = String.valueOf(day);
        String date = year1+"-"+month1+"-"+day1;
        return date;
    }
}
