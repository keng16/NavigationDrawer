package com.example.user.smartclassroom.Attendance;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.user.smartclassroom.Account.Student_Profile;
import com.example.user.smartclassroom.Global.Properties;
import com.example.user.smartclassroom.Global.full_status_model;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by kenonnegammad on 05/03/2018.
 */

public class full_status extends Fragment implements View.OnClickListener{
    View myView;
    Button btn_course;
    SwipeMenuListView swipeMenuListView;
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Properties> properties;
    private ArrayAdapter<String> adapter;
    private ArrayList<full_status_model> full_status_models;
    full_status_adaptor full_status_adaptor;
    Properties p=new Properties();
    full_status_model full_status_model=new full_status_model();

    View view1;
    Spinner mySpinner;
    String[] splitter;
    String room;
    String sections;
    String course;
    String stud_id;
    Bundle bundle=new Bundle();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.full_status,container,false);
        stud_id = getArguments().getString("Stud_id").toString();
        btn_course = (Button)myView.findViewById(R.id.btn_full_status_course);
        swipeMenuListView = (SwipeMenuListView)myView.findViewById(R.id.listview_full_status);
        swipeRefreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swipe_refresh_full_status_daily);
        btn_course.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        final Handler handler = new Handler();
        final LoadClass loadClass=new LoadClass();
        loadClass.execute();
        AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity());
        view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
        mBuilder.setTitle("Pick Course");
        mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                splitter = mySpinner.getSelectedItem().toString().split(" ");
                course = splitter[0];
                room = splitter[1];
                sections = splitter[2];
                    System.out.println(course);
                    System.out.println(room);
                    System.out.println(sections);
//                   student_attendance_task.execute();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            new full_status_task().execute();
                        }
                    });
                btn_course.setText(mySpinner.getSelectedItem().toString());
                //dialogInterface.dismiss();

            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setView(view1);
        AlertDialog dialog= mBuilder.create();
        dialog.show();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                deleteItem.setWidth(130);
                // set a icon
                deleteItem.setTitle("Status");
                deleteItem.setTitleSize(16);
                deleteItem.setTitleColor(Color.WHITE);

//                deleteItem.setIcon(R.drawable.ic_wifi_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                FragmentManager fragmentManager = getFragmentManager();
                switch (index) {
                    case 0:
                        // delete
                        Toast.makeText(getActivity(),"Show",Toast.LENGTH_LONG).show();
                        full_status_model = full_status_models.get(position);
                       // Toast.makeText(getActivity(),p.getStudentlname(),Toast.LENGTH_SHORT).show();
                        Student_Profile student_profile=new Student_Profile();
                        //FragmentManager fragmentManager = getFragmentManager();
                        bundle.putInt("Stud_id",Integer.parseInt(full_status_model.getStudent_id()));
                        bundle.putString("course",course);
                        bundle.putString("sections",sections);
                        bundle.putString("room",room);
                        bundle.putString("url",full_status_model.getStudent_ImgUrl());
                        bundle.putString("name", full_status_model.getStudent_name());
                        student_profile.setArguments(bundle);
                        fragmentManager.beginTransaction().addToBackStack("Profile")
                                .replace(R.id.contentFrame
                                        , student_profile)
                                .commit();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }
    public class LoadClass extends AsyncTask<Void,Void,String>{
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
                String Url = p.getIP()+"Prof_Course.php";
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
            properties = parseJSON(s);
            addData(properties);

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
                //pic=getBitmapFromURL(json_data.getString(""));
                courseusers.add(user);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return courseusers;
    }
    public void addData(ArrayList<Properties> courseusers) {

        List<String> itemList=new ArrayList<String>();
        for (Iterator i = courseusers.iterator(); i.hasNext();) {
            Properties p = (Properties) i.next();
            itemList.add(p.getCourseId()+" "+p.getRoom()+" "+p.getSection());
        }
        adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        //mySpinner.setAdapter(adapter);
    }
//end
    //end

    public class full_status_task extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("course",course));
            nameValuePairs.add(new BasicNameValuePair("section",sections));

            try {
                //ip= new Properties();
                String Url = p.getIP()+"getStatusAttendance.php";
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
            full_status_models = parseJSON2(s);
            full_status_adaptor = new full_status_adaptor(getActivity(),full_status_models);
            swipeMenuListView.setAdapter(full_status_adaptor);
        }
    }
    public ArrayList<full_status_model> parseJSON2(String result) {

        ArrayList<full_status_model> courseusers = new ArrayList<full_status_model>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                full_status_model user = new full_status_model();
                user.setStudent_id(json_data.getString("student_id"));
                user.setStudent_name(json_data.getString("student_lname")+", "+json_data.getString("student_fname")+" "+json_data.getString("student_mname"));
                user.setStudent_program(json_data.getString("student_program"));
                user.setStudent_ImgUrl(json_data.getString("student_ImgUrl"));
                user.setPresent(json_data.getString("present"));
                user.setLate(json_data.getString("late"));
                user.setAbsent(json_data.getString("absent"));
                courseusers.add(user);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return courseusers;
    }
}
