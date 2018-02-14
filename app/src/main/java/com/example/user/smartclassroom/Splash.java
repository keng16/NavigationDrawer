package com.example.user.smartclassroom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.Prof.MainActivity;
import com.example.user.smartclassroom.Student.MainActivity_Student;
import com.google.firebase.iid.FirebaseInstanceId;

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

/**
 * Created by kenonnegammad on 09/02/2018.
 */

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private Properties p=new Properties();
    private String token;
    String status;
    String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        token= FirebaseInstanceId.getInstance().getToken();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                new CheckOnlineTask().execute();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    public class CheckOnlineTask extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("token", token));
            try {
                //ip= new Properties();
                String Url = p.getIP()+"check_online.php";
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

                        Toast.makeText(Splash.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("False")){
                Intent mainIntent = new Intent(Splash.this,LoginActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }else{
                status="Online";
                MessageBox(s);
                String[] splitted = s.split(":");
                id = splitted[0];
                new OnlineStatus().execute();

            }
        }
    }
    public void MessageBox(String message){
        Toast.makeText(Splash.this,message,Toast.LENGTH_SHORT).show();
    }
    public class OnlineStatus extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("status", status));
            nameValuePairs.add(new BasicNameValuePair("id", id));

            try {
                //ip= new Properties();
                String Url = p.getIP()+"LOGIN.php";
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

                        Toast.makeText(Splash.this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final String user;
            MessageBox(s);
            String sname = null;
            String url = null;
            if (!s.equals("none")) {
                // Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                String[] usergetter = s.split(":");
                user = usergetter[3];
                sname = usergetter[0]+" "+usergetter[1]+" "+usergetter[2];

                if (user.equals("Prof")) {
                    String id= usergetter[4];
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    i.putExtra("Name",sname);
                    i.putExtra("Email",usergetter[5]);
                    i.putExtra("User",user);
                    i.putExtra("Stud_id",id);
                    startActivity(i);
                } else if (user.equals("Student")){
                    url = usergetter[5];
                    String id= usergetter[6];
                    Intent i = new Intent(Splash.this, MainActivity_Student.class);
                    i.putExtra("Name",sname);
                    i.putExtra("Email",usergetter[7]);
                    i.putExtra("Url",url);
                    i.putExtra("User",user);
                    i.putExtra("Stud_id",id);
                    startActivity(i);
                }

            }
        }
    }
}
