package org.smartautomation.user.smartclassroom.Remarks_List;

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
import org.smartautomation.user.smartclassroom.Account.Student_Profile;
import org.smartautomation.user.smartclassroom.Attendance.Attendance_Daily;
import org.smartautomation.user.smartclassroom.Attendance.Attendance_History;
import org.smartautomation.user.smartclassroom.Attendance.update_attendance;
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;

import java.io.InputStream;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kenonnegammad on 26/03/2018.
 */

public class Remarks_List extends Fragment implements View.OnClickListener {

    View myView;
    Button btn_course;
    SwipeMenuListView swipeMenuListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Properties> properties;
    View view1;
    String stud_id;
    Properties p = new Properties();
    Spinner mySpinner;
    String[] splitter;
    String course;
    String room;
    String sections;
    Bundle bundle=new Bundle();
    Remarks_LIst_Adapter remarks_lIst_adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.remarks_list, container, false);
        btn_course = (Button) myView.findViewById(R.id.btn_select_course_remarks);
        swipeMenuListView = (SwipeMenuListView) myView.findViewById(R.id.listview_remarks);
        stud_id = getArguments().getString("Stud_id").toString();
        btn_course.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_select_course_remarks) {
            final Handler handler = new Handler();
            final LoadClass loadClass = new LoadClass();
            loadClass.execute();
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
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
                        if (course != null && sections != null && room != null) {
                            System.out.println(course);
                            System.out.println(room);
                            System.out.println(sections);
//                   student_attendance_task.execute();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    new Remarks_List_Task().execute();
                                }
                            });

                            btn_course.setText(mySpinner.getSelectedItem().toString());
                        }
                    } else {
                        MessageBox("No Subject");
                    }
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
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        }
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item

                // add to menu
                    SwipeMenuItem history = new SwipeMenuItem(
                            getActivity());
                    // set item background
                    history.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    // set item width
                    history.setWidth(130);
                    // set a icon
                    history.setTitle("View");
                    history.setTitleSize(16);
                    history.setTitleColor(Color.WHITE);
                    menu.addMenuItem(history);

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
                        p = properties.get(position);
                        Toast.makeText(getActivity(),p.getStudentlname(),Toast.LENGTH_SHORT).show();
                        Remarks_View remarks_view=new Remarks_View();
                        //FragmentManager fragmentManager = getFragmentManager();
                        bundle.putInt("Stud_id",p.getStud_id());
                        bundle.putString("course",course);
                        bundle.putString("sections",sections);
                        bundle.putString("date",p.getDate());
                        bundle.putString("url",p.getPic());
                        bundle.putInt("attendance_id",p.getAttendID());
                        bundle.putString("name", p.getStudentfname()+" "+p.getStudentmname()+" "+p.getStudentlname());
                        bundle.putString("status",p.getStatdescript());
                        bundle.putString("program",p.getStudent_program());
                        bundle.putString("remarks",p.getStudRemark());
                        remarks_view.setArguments(bundle);
                        fragmentManager.beginTransaction().addToBackStack("Profile")
                                .replace(R.id.contentFrame
                                        , remarks_view)
                                .commit();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
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
            nameValuePairs.add(new BasicNameValuePair("condition", "Manual"));
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
            //MessageBox(s);
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
    }

    //end
    public class Remarks_List_Task extends AsyncTask<Void, Void, String> {
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
            nameValuePairs.add(new BasicNameValuePair("year", "2018"));
            nameValuePairs.add(new BasicNameValuePair("term", "3"));
            nameValuePairs.add(new BasicNameValuePair("section", sections));
            nameValuePairs.add(new BasicNameValuePair("course", course));
            try {
                //ip= new Properties();
                String Url = p.getIP() + "getRemarks.php";
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
            //MessageBox(s);
            properties = parseRemarks(s);
            remarks_lIst_adapter = new Remarks_LIst_Adapter(getActivity(),properties);
            swipeMenuListView.setAdapter(remarks_lIst_adapter);

        }
    }
    public ArrayList<Properties> parseRemarks(String result) {

        ArrayList<Properties> courseusers = new ArrayList<Properties>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Properties user = new Properties();
                user.setStud_id(Integer.parseInt(json_data.getString("entity_id")));
                user.setStudentfname(json_data.getString("student_fname")); //ipinasa dito
                user.setStudentmname(json_data.getString("student_mname"));
                user.setStudentlname(json_data.getString("student_lname"));
                //user.setStudentfname(json_data.getString("student_lname")+","+json_data.getString("student_fname")+" "+json_data.getString("student_mname"));
                user.setStudent_program(json_data.getString("student_program"));
                user.setPic(json_data.getString("student_ImgUrl"));
                user.setStatdescript(json_data.getString("status_description"));
                user.setAttendID(Integer.parseInt(json_data.getString("attendance_id")));
                user.setDate(json_data.getString("date"));
                user.setStudRemark(json_data.getString("remarks"));
                user.setHistory(json_data.getString("history"));
                courseusers.add(user);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return courseusers;
    }
}

