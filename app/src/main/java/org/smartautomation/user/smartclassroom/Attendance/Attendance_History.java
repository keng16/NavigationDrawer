package org.smartautomation.user.smartclassroom.Attendance;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 16/03/2018.
 */

public class Attendance_History extends Fragment {
    View myView;
    CircleImageView circleImageView;
    TextView tv_name;
    ArrayList<Properties> properties;
    TextView tv_id;
    String name;
    String url;
    String stud_id;
    int attendance_id;
    Properties p =new Properties();
    ListView listView;
    String user;
    Attendance_History_Adapter attendance_history_adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.attendance_history,container,false);
        circleImageView = (CircleImageView)myView.findViewById(R.id.imageView3);
        tv_name = (TextView)myView.findViewById(R.id.tv_name);
        listView = (ListView)myView.findViewById(R.id.list_view_attendance_history_student);
        tv_id = (TextView)myView.findViewById(R.id.tv_studentnumber);
        name = getArguments().getString("name").toString();
        url = getArguments().getString("url").toString();

        stud_id = String.valueOf(getArguments().getInt("Stud_id"));
        attendance_id = getArguments().getInt("attendance_id");
        tv_name.setText(name);
        Picasso.with(getActivity())
                .load(url)
                .resize(50,50)
                .into(circleImageView);
        new getAttendanceHistoryTask().execute();
        return myView;
    }
    public class getAttendanceHistoryTask extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("attendance_id",String.valueOf(attendance_id)));
            try {
                //ip= new Properties();
                String Url = p.getIP()+"getAttendanceHistory.php";
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
            MessageBox(s);
            System.out.println(s);
            properties = attendanceUse(s);
            attendance_history_adapter = new Attendance_History_Adapter(getActivity(),properties);
            listView.setAdapter(attendance_history_adapter);

        }
    }
    public void MessageBox(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
    public ArrayList<Properties> attendanceUse(String result) {
        ArrayList<Properties> attendanceusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setStatdescript(json_data.getString("status"));
                user.setDate(json_data.getString("date"));
                //user.setStartTime(json_data.getString("time"));
                String formatstart_time = json_data.getString("time").toString();
                try {
                    DateFormat start = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                    Date d = start.parse(formatstart_time);
                    DateFormat f2 = new SimpleDateFormat("h:mma");
                    String startingtime =f2.format(d).toLowerCase(); // "12:18am"


                    user.setStartTime(startingtime);//error
                    //if string contains su and myday is sunday bla bla
                    attendanceusers.add(user);
                    System.out.println(attendanceusers);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //attendanceusers.get(index)
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return attendanceusers;
    }
}
