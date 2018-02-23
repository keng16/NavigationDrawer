package com.example.user.smartclassroom.Notif;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.smartclassroom.Global.NotificationModel;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenonnegammad on 31/01/2018.
 */

public class Notif_Class extends Fragment {
    View myView;
    private ListView listView;
    Properties p =new Properties();
    private ArrayList<NotificationModel> notificationModels;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> attendanceusers = new ArrayList<NotificationModel>();
    SwipeRefreshLayout swipeRefreshLayout;
    String stud_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.notification,container,false);
        listView = (ListView)myView.findViewById(R.id.listview_notifications);
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swipe_notifications);
        stud_id = getArguments().getString("Stud_id").toString();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                attendanceusers.clear();
                new Notification_Task().execute();
                new Holiday_Task().execute();

            }
        });
        new Notification_Task().execute();
        new Holiday_Task().execute();
        return myView;
    }
    public class Notification_Task extends AsyncTask<Void,Void,String>{
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
                String Url = p.getIP()+"notification_android.php";
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
            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            ArrayList<NotificationModel> notificationModels = attendanceUse(s);
            NotificationAdapter notificationAdapter = new NotificationAdapter(notificationModels,getActivity());
            listView.setAdapter(notificationAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    public ArrayList<NotificationModel> attendanceUse(String result) {
        //attendanceusers = new ArrayList<NotificationModel>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setMessage(json_data.getString("notification_body"));
                notificationModel.setDate(json_data.getString("notification_date"));
                attendanceusers.add(notificationModel);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return attendanceusers;
    }
    public class Holiday_Task extends AsyncTask<Void,Void,String>{
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
            try {
                //ip= new Properties();
                String Url = p.getIP()+"getNotif.php";
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
            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            ArrayList<NotificationModel> notificationModels = Holiday(s);
            NotificationAdapter notificationAdapter = new NotificationAdapter(notificationModels,getActivity());
            listView.setAdapter(notificationAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    public ArrayList<NotificationModel> Holiday(String result) {

        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setMessage(json_data.getString("Holiday_Description"));
                notificationModel.setDate(json_data.getString("Holiday_Date"));
                attendanceusers.add(notificationModel);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return attendanceusers;
    }

}
