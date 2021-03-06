package org.smartautomation.user.smartclassroom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.Guard.MainActivity_Guard;
import org.smartautomation.user.smartclassroom.Prof.MainActivity;
import org.smartautomation.user.smartclassroom.R;
import org.smartautomation.user.smartclassroom.Student.MainActivity_Student;
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
    ImageView imglogo;
    String status;
    String id;
    ProgressBar progressBar;
    CheckNetwork checkNetwork=new CheckNetwork();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);


        token= FirebaseInstanceId.getInstance().getToken();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(checkNetwork.checkInternetConenction(Splash.this)){
                    new CheckOnlineTask().execute();
                }else{
                    Toast.makeText(Splash.this,"No internet",Toast.LENGTH_SHORT).show();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public class CheckOnlineTask extends AsyncTask<Void,Integer,String>{
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
    public class OnlineStatus extends AsyncTask<Void,Integer,String>{
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
            //MessageBox(s);
            String sname = null;
            String url;
            if (!s.equals("none")) {
                // Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                String[] usergetter = s.split(":");
                user = usergetter[3];
                sname = usergetter[0]+" "+usergetter[1]+" "+usergetter[2];

                if (user.equals("Professor")) {
                    url = usergetter[4]+":"+usergetter[5];
                    String id= usergetter[6];
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    i.putExtra("Name",sname);
                    i.putExtra("Email",usergetter[7]);
                    i.putExtra("Url",url);
                    i.putExtra("User",user);
                    i.putExtra("Stud_id",id);
                    startActivity(i);
                } else if (user.equals("Student")){
                    url = usergetter[4]+":"+usergetter[5];
                    String id= usergetter[6];
                    Intent i = new Intent(Splash.this, MainActivity_Student.class);
                    i.putExtra("Name",sname);
                    i.putExtra("Email",usergetter[7]);
                    i.putExtra("Url",url);
                    i.putExtra("User",user);
                    i.putExtra("Stud_id",id);
                    startActivity(i);
                }else if (user.equals("Guard")){
                    String id = usergetter[4];
                    Intent i = new Intent(Splash.this, MainActivity_Guard.class);
                    i.putExtra("Name",sname);
                    i.putExtra("Email",user);
                    i.putExtra("User",user);
                    i.putExtra("Stud_id",id);
                    startActivity(i);
                }
            }
        }
    }
}
