package org.smartautomation.user.smartclassroom.Notif;

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

import org.smartautomation.user.smartclassroom.Global.NotificationModel;
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kenonnegammad on 05/03/2018.
 */

public class notification_today extends Fragment {
    View myView;
    Properties p=new Properties();
    private ArrayList<NotificationModel> notificationModels;
    private  notification_adapter notification_adapter;
    private NotificationModel notificationModel=new NotificationModel();
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    String user;
    String id;
    Bundle bundle=new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.notification_today,container,false);
        listView = (ListView)myView.findViewById(R.id.list_view_notification_today);
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.attendance_today_swipe);
        user = getArguments().getString("user").toString();
        id = getArguments().getString("id").toString();
        new notification_today_task().execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new notification_today_task().execute();
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                view_notification view_notification=new view_notification();
//                FragmentManager fragmentManager = getFragmentManager();
//                notificationModel = notificationModels.get(position);
//                bundle.putString("id",notificationModel.getId());
//                bundle.putString("title",notificationModel.getTitle());
//                bundle.putString("body",notificationModel.getBody());
//                bundle.putString("time",notificationModel.getTime());
//                bundle.putString("date",notificationModel.getDate());
//                view_notification.setArguments(bundle);
//                if(user.equals("Student")){
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.contentFrame_Student
//                                    , view_notification)
//                            .addToBackStack(null)
//                            .commit();
//                }else if(user.equals("Professor")){
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.contentFrame
//                                    , view_notification)
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
//        });
        return myView;
    }


    public class notification_today_task extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("entity_id",id));
            nameValuePairs.add(new BasicNameValuePair("type",user));
            try {
                //ip= new Properties();
                String Url = p.getIP()+"getTodayNotification.php";
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
            notificationModels = logsUse(s);
            notification_adapter = new notification_adapter(getActivity(),notificationModels);
            listView.setAdapter(notification_adapter);
            swipeRefreshLayout.setRefreshing(false);

        }
    }
    public ArrayList<NotificationModel> logsUse(String result) {
        ArrayList<NotificationModel> logsusers = new ArrayList<NotificationModel>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setId(json_data.getString("id").toString());
                notificationModel.setTitle(json_data.getString("title"));
                notificationModel.setBody(json_data.getString("body"));
                notificationModel.setDate(json_data.getString("date"));

                // time convert to normal change feb/19
                String timetap=json_data.getString("time");
                DateFormat start = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
                Date d = start.parse(timetap);
                DateFormat f2 = new SimpleDateFormat("h:mma");
                String time_tap =f2.format(d).toLowerCase(); // "12:18am"
                notificationModel.setTime(time_tap);
                //end
                notificationModel.setStatus(json_data.getString("status"));
                logsusers.add(notificationModel);
//                }
            }
            //attendanceusers.get(index)

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return logsusers;
    }
}
