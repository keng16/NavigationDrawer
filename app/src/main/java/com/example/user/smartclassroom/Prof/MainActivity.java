package com.example.user.smartclassroom.Prof;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.smartclassroom.Account.Account_Class;
import com.example.user.smartclassroom.Attendance.Attendance_Daily;
import com.example.user.smartclassroom.Attendance.add_attendance;
import com.example.user.smartclassroom.Attendance.full_status;
import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.LoginActivity;
import com.example.user.smartclassroom.Logs.Logs_Daily;
import com.example.user.smartclassroom.Notif.Notification_Class;
import com.example.user.smartclassroom.R;
import com.example.user.smartclassroom.Schedule.Schedule;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TextView textName;
    TextView textEmail;
    Bundle extras;
    CardView cardAttendace, cardLogs,cardSchedule,cardController;

    Properties p =new Properties();
    Bundle bundle=new Bundle();
    String stud_id;
    String name, email,url,user;
    String control;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        extras = getIntent().getExtras();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        control="Login";
        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        url = getIntent().getStringExtra("Url");
        user = getIntent().getStringExtra("User");
        stud_id = getIntent().getStringExtra("Stud_id");
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        token= FirebaseInstanceId.getInstance().getToken();
        new registerToken().execute();
        //

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        CircleImageView imgView = (CircleImageView) hView.findViewById(R.id.imageView);
        Picasso.with(MainActivity.this)
                .load(url)
                .resize(60, 60)
                .centerCrop()
                .into(imgView);
        textName = (TextView) hView.findViewById(R.id.textName);
        textEmail = (TextView) hView.findViewById(R.id.textEmail);
        textName.setText(extras.getString("Name"));
        textEmail.setText(extras.getString("Email"));
        //
        //
        View cView = findViewById(R.id.contentView);
        cardAttendace = (CardView) cView.findViewById(R.id.Attendance);
        cardLogs = (CardView) cView.findViewById(R.id.Logs);
        cardController = (CardView) cView.findViewById(R.id.Controller);
        cardSchedule = (CardView) cView.findViewById(R.id.Schedule);
        cardAttendace.setOnClickListener(this);
        cardLogs.setOnClickListener(this);
        cardController.setOnClickListener(this);
        cardSchedule.setOnClickListener(this);
        //
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            //zdrawer.openDrawer(GravityCompat.START);
//        }
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        bundle.putString("Stud_id",stud_id);
        bundle.putString("User",user);
        bundle.putString("Name",name);
        bundle.putString("Url",url);


        if (id == R.id.Attendance) {
            Attendance_Daily attendance_daily=new Attendance_Daily();
            attendance_daily.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack("Attendance")
                    .replace(R.id.contentFrame
                            , attendance_daily)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }else if (id==R.id.Controller) {
            new ControllerTask().execute();

        }else if (id==R.id.logs) {
            Logs_Daily logs_daily=new Logs_Daily();
            logs_daily.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack("logs")
                    .replace(R.id.contentFrame
                            , logs_daily)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }
        else if(id == R.id.Notification){
            Notification_Class notification_class = new Notification_Class();
            notification_class.setArguments(bundle);
//            ((LinearLayout)findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack("Notification")
                    .replace(R.id.contentFrame
                            ,notification_class)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }else if (id == R.id.Schedule) {
            Schedule schedule=new Schedule();
            schedule.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack("Schedule")
                    .replace(R.id.contentFrame
                            , schedule)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        } else if (id==R.id.nav_Account) {
            Account_Class account_class=new Account_Class();

            account_class.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack("Account")
                    .replace(R.id.contentFrame
                            , account_class)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        } else if(id==R.id.full_status){
            full_status full_status = new full_status();

            full_status.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack("Full Status")
                    .replace(R.id.contentFrame
                            , full_status)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();

        }else if(id==R.id.add_attendance){
            add_attendance add_attendance=new add_attendance();
            add_attendance.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack("Account")
                    .replace(R.id.contentFrame
                            , add_attendance)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }
        else {
            if (id == R.id.logout) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure?");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int arg1) {
                                //Toast.makeText(MainActivity_Student.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                control = "Logout";
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
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        //content
        FragmentManager fragmentManager = getFragmentManager();
        bundle.putString("Stud_id",stud_id);
        bundle.putString("User",user);
        bundle.putString("Name",name);
        bundle.putString("Url",url);

        if (view.getId()==R.id.Attendance) {
            Attendance_Daily attendance_daily=new Attendance_Daily();
            attendance_daily.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
                    fragmentManager.beginTransaction().addToBackStack("Attendance")
                            .replace(R.id.contentFrame
                                    , attendance_daily)
                            .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                            .commit();
        }
        else if (view.getId()==R.id.Controller) {
            new ControllerTask().execute();

        }else if (view.getId()==R.id.Logs) {
            Logs_Daily logs_daily=new Logs_Daily();
            logs_daily.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
                    fragmentManager.beginTransaction().addToBackStack("Logs")
                            .replace(R.id.contentFrame
                                    , logs_daily)
                            .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                            .commit();
        }else if (view.getId()==R.id.Schedule) {
            Schedule schedule=new Schedule();
            schedule.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
                    fragmentManager.beginTransaction().addToBackStack("Schedule")
                            .replace(R.id.contentFrame
                                    , schedule)
                            .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                            .commit();

        }
        //end
    }
    public class registerToken extends AsyncTask<Void,Void,String> {
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

                        Toast.makeText(MainActivity.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

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
    public class ControllerTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
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
            nameValuePairs.add(new BasicNameValuePair("id",stud_id));
            nameValuePairs.add(new BasicNameValuePair("condition","control"));
            try {
                String Url = p.getIP()+"controlDevice.php";
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

                        Toast.makeText(MainActivity.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            FragmentManager fragmentManager = getFragmentManager();
            bundle.putString("Stud_id",stud_id);
            bundle.putString("User",user);
            if(s.equals("Not time yet")){
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }else{
                Controller controller=new Controller();
                controller.setArguments(bundle);
//                ((LinearLayout) findViewById(R.id.Linear)).removeAllViews();
//                ((LinearLayout) findViewById(R.id.Linear2)).removeAllViews();
                fragmentManager.beginTransaction().addToBackStack("Controller")
                        .replace(R.id.contentFrame
                                , controller)
                        .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                        .commit();
            }
        }
    }
}
