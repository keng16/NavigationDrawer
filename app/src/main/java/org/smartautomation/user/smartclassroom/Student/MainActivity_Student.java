package org.smartautomation.user.smartclassroom.Student;

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

import org.smartautomation.user.smartclassroom.Account.Account_Class;
import org.smartautomation.user.smartclassroom.Attendance.Student_Attendance;
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.LoginActivity;
import org.smartautomation.user.smartclassroom.Notif.Notification_Class;
import org.smartautomation.user.smartclassroom.R;
import org.smartautomation.user.smartclassroom.Schedule.Schedule;
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

public class MainActivity_Student extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    TextView textName;
    TextView textEmail;
    Properties p=new Properties();
    Bundle extras;
    String token;
    CardView cardAttendace, cardLogs,cardSchedule,cardController;
    String name, email,user,url,stud_id;
    String control;
    Bundle bundle = new Bundle();
    final FragmentManager fragmentManager = getFragmentManager();

    //FragmentManager fragmentManager = getFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
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
        url = getIntent().getStringExtra("Url");
        user = getIntent().getStringExtra("User");
        stud_id = getIntent().getStringExtra("Stud_id");
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        token=FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(MainActivity_Student.this,url,Toast.LENGTH_LONG).show();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        //imageview
        new registerToken().execute();
        new CheckProfNotif().execute();

        CircleImageView imgView = (CircleImageView) hView.findViewById(R.id.imageView);
        Picasso.with(MainActivity_Student.this)
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


        cardAttendace = (CardView) cView.findViewById(R.id.Attendance);
        cardLogs = (CardView) cView.findViewById(R.id.Account);
        cardController = (CardView) cView.findViewById(R.id.Notification);
        cardSchedule = (CardView) cView.findViewById(R.id.Schedule);
        cardAttendace.setOnClickListener(this);
        cardController.setOnClickListener(this);
        cardSchedule.setOnClickListener(this);
        cardLogs.setOnClickListener(this);

        //end
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
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        bundle.putString("Stud_id", stud_id);
        bundle.putString("User",user);
        bundle.putString("Name",name);
        bundle.putString("Url",url);
        if (id == R.id.nav_Schedule) {
            Schedule schedule=new Schedule();
            schedule.setArguments(bundle);
//            ((LinearLayout)findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , schedule).addToBackStack(null)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        } else if (id==R.id.nav_Account) {
            Account_Class account_class=new Account_Class();
            account_class.setArguments(bundle);
//            ((LinearLayout) findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , account_class).addToBackStack(null)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }
        else if (id == R.id.nav_Attendance) {
            Student_Attendance student_attendance=new Student_Attendance();
            student_attendance.setArguments(bundle);
//            ((LinearLayout)findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , student_attendance).addToBackStack(null)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }else if(id == R.id.nav_Notif){
            FragmentManager fragmentManager2 = getFragmentManager();
            Notification_Class notif_class = new Notification_Class();
            notif_class.setArguments(bundle);
//            ((LinearLayout)findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            fragmentManager2.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , notif_class).addToBackStack(null)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }
        else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
            alertDialogBuilder.setMessage("Are you sure?");
                    alertDialogBuilder.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int arg1) {
                                    //Toast.makeText(MainActivity_Student.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(MainActivity_Student.this,LoginActivity.class);
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        bundle.putString("Stud_id", stud_id);
        bundle.putString("User",user);
        bundle.putString("Name",name);
        bundle.putString("Url",url);
        FragmentManager fragmentManager = getFragmentManager();
        if(view.getId()==R.id.Attendance) {
            Student_Attendance student_attendance=new Student_Attendance();
//            ((LinearLayout) findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            student_attendance.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.contentFrame_Student
                                    , student_attendance).addToBackStack(null)
                            .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                            .commit();
        }
        else if(view.getId()==R.id.Notification){
            Notification_Class notification_class = new Notification_Class();
            notification_class.setArguments(bundle);
//            ((LinearLayout)findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.contentFrame_Student
                            , notification_class)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }
        else if (view.getId()==R.id.Schedule) {
            Schedule schedule=new Schedule();
//                    ((LinearLayout) findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            schedule.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.contentFrame_Student
                                    , schedule).addToBackStack(null)
                            .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                            .commit();

        }
        else if (view.getId()==R.id.Account){
            Account_Class account_class=new Account_Class();
//            ((LinearLayout) findViewById(R.id.Linear_Student)).removeAllViews();
//            ((LinearLayout) findViewById(R.id.Linear_Student2)).removeAllViews();
            account_class.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFrame_Student
                            , account_class).addToBackStack(null)
                    .setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_right_exit)
                    .commit();
        }

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

                        Toast.makeText(MainActivity_Student.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

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
    public class CheckProfNotif extends AsyncTask<Void,Void,String>{

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
                String Url = p.getIP()+"Notification.php";
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

                        Toast.makeText(MainActivity_Student.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

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
}
