package org.smartautomation.user.smartclassroom.Attendance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.Global.full_status_model;
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
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sonu on 08/02/17.
 */
public class ListViewFragment extends Fragment {
    private View myView;
    private Context context;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter2;
    private ArrayList<Properties> properties;
    private ArrayList<full_status_model> full_status_models;
    private Spinner mySpinner;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String day;
    Properties p = new Properties();
    View view1;
    ListView listView;
    String stud_id;
    private String date_final;
    private String course;
    private String sections;
    private String[] splitter;
    private String room;
    private GridListAdapter gridListAdapter;
    private Button btn_course;
    private Button btn_date;
    private String month_final;
    private String year_final;
    private String day_final;
    private String student_ids;

    public ListViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.list_view_fragment, container, false);
        btn_course = (Button) myView.findViewById(R.id.btn_course);
        btn_date = (Button) myView.findViewById(R.id.btn_date);
        stud_id = getArguments().getString("Stud_id").toString();
        date_final = datetoday();
        btn_date.setText(datetoday());
        listView = (ListView) myView.findViewById(R.id.list_view);
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                month_final = String.valueOf(month);
                year_final = String.valueOf(year);
                day_final = String.valueOf(day);
                String date = dateToday(year, month - 1, day);
                if (day < 10 && month < 10) {
                    date_final = year_final + "-" + "0" + month_final + "-" + "0" + day_final;
                } else if (day > 9 && month > 9) {
                    date_final = year_final + "-" + month_final + "-" + day_final;
                } else if (day > 9 && month < 10) {
                    date_final = year_final + "-" + "0" + month_final + "-" + day_final;
                } else if (day < 10 && month > 9) {
                    date_final = year_final + "-" + month_final + "-" + "0" + day_final;
                }

                if (course != null && sections != null && room != null && date_final != null) {
                    final Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //new Attendance_Daily.getAttendance_Daily().execute();
                        }
                    });
                    //date_pick.setText(date_final);

                } else {
                    btn_date.setText(date);
                    Toast.makeText(getActivity(), "Pick Course", Toast.LENGTH_SHORT).show();
                }
            }
        };
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickEvent(view);
    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected position
                student_ids = gridListAdapter.getSelectedItem();
                MessageBox(student_ids);
                new AddAttendanceTask().execute();
            }
        });

        view.findViewById(R.id.btn_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBox("Hello Course");
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
                                btn_course.setText(mySpinner.getSelectedItem().toString());
                                new getStudentList().execute();
                            } else {
                                MessageBox("Pick Date");
                            }
                        } else {
                            MessageBox("No Subject");
                        }
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
        });
        view.findViewById(R.id.btn_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBox("Hello Date");
                date_final = "";
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity()
                        , android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener
                        , year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

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
        adapter2 = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                itemList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter2);
        //mySpinner.setAdapter(adapter);
    }

    //end
    public class getStudentList extends AsyncTask<Void, Void, String> {
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
            // nameValuePairs.add(new BasicNameValuePair("id",stud_id));
            nameValuePairs.add(new BasicNameValuePair("year", "2018"));
            nameValuePairs.add(new BasicNameValuePair("course", course));
            nameValuePairs.add(new BasicNameValuePair("section", sections));
            nameValuePairs.add(new BasicNameValuePair("term", "3"));

            try {
                //ip= new Properties();
                String Url = p.getIP() + "getStudentList.php";
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
//        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            MessageBox(s);
            full_status_models = attendanceUse(s);
            gridListAdapter = new GridListAdapter(getActivity(), full_status_models);
            listView.setAdapter(gridListAdapter);
            // swipeRefreshLayout.setRefreshing(false);
            // timerTask.cancel();
        }
    }

    public ArrayList<full_status_model> attendanceUse(String result) {
        ArrayList<full_status_model> attendanceusers = new ArrayList<full_status_model>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                full_status_model full_status_model = new full_status_model();
                full_status_model.setStudent_name(json_data.getString("student_lname") + " " + json_data.getString("student_fname") + " " + json_data.getString("student_mname"));
                full_status_model.setStudent_id(json_data.getString("student_id"));
                full_status_model.setStudent_ImgUrl(json_data.getString("student_ImgUrl"));
                attendanceusers.add(full_status_model);
                //attendanceusers.get(index)
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return attendanceusers;
    }

    public String datetoday() {
        Calendar c = Calendar.getInstance();
        //	System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        day = df.format(c.getTime());
        MessageBox(day);
        return day;
    }

    public void MessageBox(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public String dateToday(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();

        String month_name = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (month >= 0 && month <= 11) {
            month_name = months[month];
        }
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date dates = new Date(year, month, day - 1);
        String dayOfWeek = simpledateformat.format(dates);
        String date_final = String.valueOf(day);
        String year_final = String.valueOf(year);
        String date = month_name + " " + date_final + " , " + year_final + " " + dayOfWeek;

        return date;
    }
    public class AddAttendanceTask extends AsyncTask<Void,Void,String>{
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
            // nameValuePairs.add(new BasicNameValuePair("id",stud_id));
            nameValuePairs.add(new BasicNameValuePair("entity_id", student_ids));
            nameValuePairs.add(new BasicNameValuePair("course", course));
            nameValuePairs.add(new BasicNameValuePair("section", sections));
            nameValuePairs.add(new BasicNameValuePair("status", "1"));
            nameValuePairs.add(new BasicNameValuePair("room", room));
            nameValuePairs.add(new BasicNameValuePair("term", "3"));
            nameValuePairs.add(new BasicNameValuePair("date", date_final));
            try {
                //ip= new Properties();
                String Url = p.getIP()+"addAttendance.php";
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
            if (s.contains("false")) {
                MessageBox("Can't add attendance");
            }else if (s.contains("true")){
                MessageBox("Success");
            }
        }
    }
    public String date_today(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month_name = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (month >= 0 && month <= 11 ) {
            month_name = months[month];
        }
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date dates = new Date(year, month, day-1);
        String dayOfWeek = simpledateformat.format(dates);
        String date_final = String.valueOf(day);
        String year_final = String.valueOf(year);
        String date = month_name+" "+date_final+" , "+year_final+" "+dayOfWeek;
        return date;
    }

}


