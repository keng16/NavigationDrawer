package org.smartautomation.user.smartclassroom.send_notification;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import org.smartautomation.user.smartclassroom.Attendance.Attendance_Daily;
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kenonnegammad on 01/04/2018.
 */

public class Send_Notification extends Fragment implements View.OnClickListener {
    View myView;
    View view1;
    Spinner mySpinner;
    Timer timer;
    Button btn_course;
    Properties p = new Properties();
    Button btn_send;
    EditText et_message;
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    String course = "";
    String condition;
    String stud_id;
    String sections="";
    String room;
    String[] splitter;
    TimerTask timerTask;
    EditText et_title;
    String title;
    String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.send_notification, container, false);
        et_message = (EditText) myView.findViewById(R.id.et_message);
        et_title = (EditText)myView.findViewById(R.id.et_title);
        btn_course = (Button) myView.findViewById(R.id.btn_notification_course);
        btn_send = (Button) myView.findViewById(R.id.btn_send);
        stud_id = getArguments().getString("Stud_id");
        btn_send.setOnClickListener(this);
        btn_course.setOnClickListener(this);
        AutoDetect();

        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_notification_course) {
            timer.cancel();
            condition = "Manual";
//            course = "";
//            room = "";
//            sections = "";
            final Handler handler = new Handler();
            final LoadClass loadClass = new LoadClass();
            loadClass.execute();
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
            view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner, null);
            mBuilder.setTitle("Pick Course");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mySpinner != null && mySpinner.getSelectedItem() != null) {
                        Toast.makeText(getActivity(), mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        splitter = mySpinner.getSelectedItem().toString().split(" ");
                        course = splitter[0];
                        room = splitter[1];
                        sections = splitter[2];
                        btn_course.setText(mySpinner.getSelectedItem().toString());
                    } else {
                        MessageBox("No Subject");
                    }
                }
            });
            mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    AutoDetect();
                }
            });
            mBuilder.setView(view1);
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        } else if (v.getId() == R.id.btn_send) {
            title = et_title.getText().toString();
            message = et_message.getText().toString();
            if (message.length()>0&&title.length()>0&&course!=null&sections!=null){
                new Send_Notification_Task().execute();
            }else if (message.length()<=0||title.length()<=0||course!=null||sections!=null){
                MessageBox("Enter your message first");
            }
        }
    }

    public void AutoDetect() {

        condition = "AutoDetect";
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new LoadClass().execute();
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 3000);
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();

    }

    public void MessageBox(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public class LoadClass extends AsyncTask<Void, Void, String> {
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
            String resultString = "";
            Bundle extras = new Bundle();
            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", stud_id));
            nameValuePairs.add(new BasicNameValuePair("condition", condition));
            nameValuePairs.add(new BasicNameValuePair("day", dayToday()));
            try {
                //ip= new Properties();
                String Url = p.getIP() + "Prof_Course.php";
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(Url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
                inputStream = response.getEntity().getContent();

                data = new byte[256];

                buffer = new StringBuffer();

                int len = 0;

                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len));
                }
                //for the output or echo
                String bufferedInputString = buffer.toString();

                inputStream.close();
            } catch (final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "error: " + e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (condition.equals("AutoDetect")) {
                System.out.println(s);
                properties = parseJSON(s);
            } else if (condition.equals("Manual")) {
                System.out.println(s);
                properties = parseJSON(s);
                addData(properties);
            }

        }
    }

    //parseJSON courses
    public ArrayList<Properties> parseJSON(String result) {

        ArrayList<Properties> courseusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setRoom(json_data.getString("rooms_id")); //ipinasa dito
                user.setCourse(json_data.getString("course_id"));
                user.setSection(json_data.getString("sections_id"));
                course = json_data.getString("course_id");
                room = json_data.getString("rooms_id");
                sections = json_data.getString("sections_id");
                //pic=getBitmapFromURL(json_data.getString(""));
                btn_course.setText(course+" "+room+" "+sections);
                courseusers.add(user);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return courseusers;
    }

    //end
    //spinner add data
    public void addData(ArrayList<Properties> courseusers) {

        List<String> itemList = new ArrayList<String>();
        for (Iterator i = courseusers.iterator(); i.hasNext(); ) {
            Properties p = (Properties) i.next();
            itemList.add(p.getCourseId() + " " + p.getRoom() + " " + p.getSection());
        }
        adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        //mySpinner.setAdapter(adapter);
    }

    //end
    public class Send_Notification_Task extends AsyncTask<Void,Void,String>{
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
            String resultString = "";
            Bundle extras = new Bundle();
            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("professor_id", stud_id));
            nameValuePairs.add(new BasicNameValuePair("title", title));
            nameValuePairs.add(new BasicNameValuePair("message", message));
            nameValuePairs.add(new BasicNameValuePair("course", course));
            nameValuePairs.add(new BasicNameValuePair("section", sections));

            try {
                //ip= new Properties();
                String Url = p.getIP() + "sendNotification.php";
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(Url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
                inputStream = response.getEntity().getContent();

                data = new byte[256];

                buffer = new StringBuffer();

                int len = 0;

                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len));
                }
                //for the output or echo
                String bufferedInputString = buffer.toString();

                inputStream.close();
            } catch (final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "error: " + e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);

            if (s.contains("OK")){
                MessageBox("Notification Sent");
                et_message.setText("");
                et_title.setText("");
            }else{
                //MessageBox(s);
                MessageBox("Error Sending N");
            }
        }
    }
    public String dayToday() {
        String Day = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                Day = "Sunday";
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                Day = "Monday";
                break;
            case Calendar.TUESDAY:
                // etc.
                Day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                Day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                Day = "Thursday";
                break;
            case Calendar.FRIDAY:
                Day = "Friday";
                break;
            case Calendar.SATURDAY:
                Day = "Saturday";
                break;

        }
        return Day;
    }
}
