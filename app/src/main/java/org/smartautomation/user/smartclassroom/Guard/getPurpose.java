package org.smartautomation.user.smartclassroom.Guard;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenonnegammad on 16/04/2018.
 */

public class getPurpose extends Fragment {
    View myView;
    Button btndate,btnstart_time,btnend_time,btn_save,btnpurpose;
    EditText et_name,et_purpose;
    String room,starttime,date;
    Properties p =new Properties();
    String [] splitter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.room_purpose,container,false);
        btnstart_time = (Button)myView.findViewById(R.id.btn_purpose_start_time_guard);
        btnend_time = (Button)myView.findViewById(R.id.btn_purpose_end_time_guard);
        btn_save = (Button)myView.findViewById(R.id.btn_purpose_send_guard);
        et_name = (EditText)myView.findViewById(R.id.et_name_guard);
        btndate = (Button)myView.findViewById(R.id.btn_purpose_date_guard);
        et_purpose = (EditText)myView.findViewById(R.id.et_purpose);
        btnpurpose = (Button)myView.findViewById(R.id.btn_purpose_guard);
        room = getArguments().getString("room").toString();
        starttime = getArguments().getString("starttime").toString();
        date = getArguments().getString("date").toString();
        try{
            //start time convertion from 12 hours to 24 hours
            SimpleDateFormat inFormat = new SimpleDateFormat("h:mma");
            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
            String end_time24 = outFormat.format(inFormat.parse(starttime));
            //end of start time convertion
            starttime = end_time24;
        }catch (ParseException e){

        }
        btndate.setText(date);
        new getPurposeRoom().execute();
        btn_save.setVisibility(View.GONE);
        return myView;
    }
    public class getPurposeRoom extends AsyncTask<Void,Void,String>{

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
            nameValuePairs.add(new BasicNameValuePair("time",starttime));
            nameValuePairs.add(new BasicNameValuePair("room",room));
            nameValuePairs.add(new BasicNameValuePair("date",date));

            try {
                //ip= new Properties();
                String Url = p.getIP()+"getPurpose.php";
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
            //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            try {
                JSONArray jArray = new JSONArray(s);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    et_name.setText(json_data.getString("name"));
                    if (json_data.getString("purpose").contains(":")) {
                        splitter = json_data.getString("purpose").split(":");
                        et_purpose.setText(splitter[1]);
                        btnpurpose.setText(splitter[0]);
                    }else{
                        et_purpose.setText(json_data.getString("purpose"));
                        btnpurpose.setVisibility(View.GONE);
                    }
                    btnstart_time.setText(json_data.getString("start"));
                    btnend_time.setText(json_data.getString("end"));
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }
}
