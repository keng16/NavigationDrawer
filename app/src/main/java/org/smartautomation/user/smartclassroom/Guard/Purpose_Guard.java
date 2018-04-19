package org.smartautomation.user.smartclassroom.Guard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.smartautomation.user.smartclassroom.Attendance.Attendance_Daily;
import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;

import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kenonnegammad on 12/04/2018.
 */

public class Purpose_Guard extends Fragment implements View.OnClickListener {
    View myView;
    Button btndate,btnstart_time,btnend_time, btnpurpose,btn_save;
    EditText et_name,et_purpose;
    String start_time="",end_time="",name,purpose,date_final;
    Spinner mySpinner;
    String month_final,year_final,day_final;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Properties p=new Properties();
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
        btn_save.setOnClickListener(this);
        btnstart_time.setOnClickListener(this);
        btnend_time.setOnClickListener(this);
        btndate.setOnClickListener(this);
        btnpurpose.setOnClickListener(this);
        et_purpose.setVisibility(View.INVISIBLE);
        date_final = datetoday();
        btndate.setText(date_today());
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month+1;
                month_final = String.valueOf(month);
                year_final = String.valueOf(year);
                day_final = String.valueOf(day);
                String date = dateToday(year,month-1,day);
                if(day<10 && month<10) {
                    date_final = year_final + "-" + "0" + month_final + "-" + "0"+day_final;
                }else if(day>9 && month>9){
                    date_final = year_final + "-" + month_final + "-" + day_final;
                }else if(day>9&&month<10){
                    date_final = year_final + "-" + "0"+month_final + "-" + day_final;
                }else if(day<10&&month>9){
                    date_final = year_final + "-" + month_final + "-" + "0"+day_final;
                }

                btndate.setText(date);
            }
        };
        return myView;
    }
    public String datetoday()
    {
        String day;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        day = df.format(c.getTime());
        MessageBox(day);
        return day;
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
    public String dateToday(int year,int month,int day)
    {
        Calendar cal = Calendar.getInstance();

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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_purpose_start_time_guard){
            MessageBox("Time");
            final Handler handler = new Handler();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner,null);
            mBuilder.setTitle("Pick Day");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.start_time));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(),mySpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    start_time = mySpinner.getSelectedItem().toString();
                    try{
                        //start time convertion from 12 hours to 24 hours
                        SimpleDateFormat inFormat = new SimpleDateFormat("h:mma");
                        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
                        String start_time24 = outFormat.format(inFormat.parse(start_time));
                        //end of start time convertion

                        start_time = start_time24;
                        System.out.println(start_time);
                        MessageBox(start_time);
                    }catch (ParseException e){

                    }
                    btnstart_time.setText(mySpinner.getSelectedItem().toString());
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
        }else if(v.getId()==R.id.btn_purpose_end_time_guard) {
            final Handler handler = new Handler();
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner, null);
            mBuilder.setTitle("Pick Day");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.end_time));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    end_time = mySpinner.getSelectedItem().toString();
                    try {
                        //start time convertion from 12 hours to 24 hours
                        SimpleDateFormat inFormat = new SimpleDateFormat("h:mma");
                        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
                        String end_time24 = outFormat.format(inFormat.parse(end_time));
                        //end of start time convertion

                        end_time = end_time24;
                        System.out.println(end_time);
                        MessageBox(end_time);
                    } catch (ParseException e) {

                    }
                    btnend_time.setText(mySpinner.getSelectedItem().toString());
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
        }else if(v.getId()==R.id.btn_purpose_send_guard){
            if (et_name.getText().length()>0&&et_purpose.getText().length()>0&&start_time.length()>0&&end_time.length()>0&&(mySpinner.getSelectedItem().toString().equals("Others")||mySpinner.getSelectedItem().toString().equals("Substitute"))){
                name = et_name.getText().toString();
               // purpose = et_purpose.getText().toString();
                    purpose = mySpinner.getSelectedItem().toString() + ": " + et_purpose.getText().toString();

                new Purpose_Send_Task().execute();
            }else{
                MessageBox("Please complete all required values");
            }
        }else if (v.getId()==R.id.btn_purpose_date_guard){
            date_final = "";
            //timer.cancel();
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity()
                    ,android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener
                    ,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }else if (v.getId()==R.id.btn_purpose_guard){
            final Handler handler = new Handler();
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner, null);
            mBuilder.setTitle("Pick Purpose");
            mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.purpose));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (mySpinner.getSelectedItem().toString().equals("Others")||mySpinner.getSelectedItem().toString().equals("Substitute")){
                        et_purpose.setVisibility(View.VISIBLE);

                    }else{
                        et_purpose.setVisibility(View.INVISIBLE);
                        purpose = mySpinner.getSelectedItem().toString();
                    }
                    btnpurpose.setText(mySpinner.getSelectedItem().toString());
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
    }
    public void MessageBox(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    public class Purpose_Send_Task extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("name",name));
            nameValuePairs.add(new BasicNameValuePair("purpose",purpose));
            nameValuePairs.add(new BasicNameValuePair("start",start_time));
            nameValuePairs.add(new BasicNameValuePair("end",end_time));
            nameValuePairs.add(new BasicNameValuePair("date",date_final));
            nameValuePairs.add(new BasicNameValuePair("room","R206"));

            try {
                //ip= new Properties();
                String Url = p.getIP()+"Insert_room_purpose.php";
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
            if (s.equals("Saved")){
                et_name.setText("");
                et_purpose.setText("");
                MessageBox(s);

            }else{
                MessageBox(s);
            }
        }
    }
}
