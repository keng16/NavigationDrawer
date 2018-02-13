package com.example.user.smartclassroom.Prof;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.user.smartclassroom.Global.Properties;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.smartclassroom.R;

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
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by kenonnegammad on 20/01/2018.
 */

public class Controller extends Fragment {

    View myView;
    Properties p=new Properties();
    Switch light_switch,socket_switch,front_switch,back_switch,all_switch;
    String light,socket,front,back,all;
    String condition;
    TimerTask timerTask;


     Handler handler = new Handler();

    //Properties p=new Properties();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.controller_dash, container, false);
        light_switch = (Switch) myView.findViewById(R.id.light_switch);
        socket_switch = (Switch) myView.findViewById(R.id.socket_switch);
        front_switch = (Switch) myView.findViewById(R.id.front_switch);
        back_switch = (Switch) myView.findViewById(R.id.back_switch);
        all_switch = (Switch) myView.findViewById(R.id.all_switch);
       Update_Device_Status();

        light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    condition = "control";
                    MessageBox("Light On");
                    light = "ON";
                }else {
                    condition = "control";
                    light = "OFF";
                }

                ControllerTask controllerTask = new ControllerTask();
                controllerTask.execute();
            }
        });
        socket_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    condition = "control";
                    MessageBox("Socket On");
                    socket = "ON";
                }else{
                    condition = "control";
                    socket = "OFF";
                }
                ControllerTask controllerTask = new ControllerTask();
                controllerTask.execute();
            }
        });
        front_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    condition = "control";
                    MessageBox("Front On");
                    front = "ON";
                }else{
                    condition = "control";
                    front = "OFF";
                }

                ControllerTask controllerTask = new ControllerTask();
                controllerTask.execute();
            }
        });
        back_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    condition = "control";
                    MessageBox("Back On");
                    back = "ON";
                }else{
                    condition = "control";
                    back = "OFF";
                }
                ControllerTask controllerTask = new ControllerTask();
                controllerTask.execute();
            }
        });
        all_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ControllerTask controllerTask = new ControllerTask();
                    controllerTask.execute();
                    condition = "control";
                    MessageBox("All");
                    back_switch.setChecked(true);
                    front_switch.setChecked(true);
                    light_switch.setChecked(true);
                    socket_switch.setChecked(true);
                    light = "ON";
                    socket = "ON";
                    front = "ON";
                    back = "ON";
                }else {
                    condition = "control";
                    back_switch.setChecked(false);
                    front_switch.setChecked(false);
                    light_switch.setChecked(false);
                    socket_switch.setChecked(false);
                    light = "OFF";
                    socket = "OFF";
                    front = "OFF";
                    back = "OFF";
                }
                ControllerTask controllerTask = new ControllerTask();
                controllerTask.execute();
            }
        });
        return myView;
    }

    @Override
    public void onStop() {
        super.onStop();
        timerTask.cancel();

    }

    public void Update_Device_Status(){

        Timer timer = new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Load_Device_Status load_device_status=new Load_Device_Status();
                        load_device_status.execute();
                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);

    }


    public void MessageBox(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
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
            nameValuePairs.add(new BasicNameValuePair("condition",condition));
            nameValuePairs.add(new BasicNameValuePair("frontlock",front));
            nameValuePairs.add(new BasicNameValuePair("light",light));
            nameValuePairs.add(new BasicNameValuePair("outlet",socket));
            nameValuePairs.add(new BasicNameValuePair("backlock",back));
            try {
                //Thread.sleep(1000);
                //ip= new Properties();
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
        }
    }
    public class Load_Device_Status extends AsyncTask<Void,Void,String>{

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
            nameValuePairs.add(new BasicNameValuePair("condition","None"));

            try {
                //ip= new Properties();

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
            String[] splitted;

            splitted = s.split(":");

            //Toast.makeText(getActivity(),splitted[1]+splitted[2]+splitted[3]+splitted[4],Toast.LENGTH_SHORT).show();
            light = splitted[1];
            socket = splitted[2];
            front = splitted[3];
            back = splitted[4];
            if (light.equals("ON")){
                light_switch.setChecked(true);
            }
            if(socket.equals("ON")){
                socket_switch.setChecked(true);
            }
            if (front.equals("ON")){
                front_switch.setChecked(true);
            }
            if(back.equals("ON")){
                back_switch.setChecked(true);
            }
            if (light.equals("OFF")){
                light_switch.setChecked(false);
            }
            if(socket.equals("OFF")){
                socket_switch.setChecked(false);
            }
            if (front.equals("OFF")){
                front_switch.setChecked(false);
            }
            if(back.equals("OFF")){
                back_switch.setChecked(false);
            }


        }
    }
}
