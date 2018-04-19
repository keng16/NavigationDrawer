package org.smartautomation.user.smartclassroom.Attendance;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.smartautomation.user.smartclassroom.Global.Properties;
import org.smartautomation.user.smartclassroom.R;
import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 03/03/2018.
 */

public class update_attendance extends Fragment implements View.OnClickListener{
    View myView;
    CircleImageView circleImageView;
    TextView tv_studentnumber;
    TextView tv_fullname;
    TextView tv_program;
    Spinner mySpinner;
    TextView tv_course;
    TextView tv_date;
    Button btn_status;
    String status;
    String status_attedance;
    String stud_id;
    String course;
    String sections;
    String room;
    String date;
    String url;
    String name;
    String program;
    String attendance_id;
    Properties p =new Properties();
    EditText et_Remarks;
    String remarks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.update_attendance,container,false);
        circleImageView = (CircleImageView)myView.findViewById(R.id.imageView_ProfilePic);
        tv_studentnumber = (TextView)myView.findViewById(R.id.tv_studentnumber);
        tv_fullname = (TextView)myView.findViewById(R.id.tv_fullname);
        tv_program = (TextView)myView.findViewById(R.id.tv_program);
        tv_course = (TextView)myView.findViewById(R.id.tv_course);
        tv_date = (TextView)myView.findViewById(R.id.tv_date);
        btn_status = (Button)myView.findViewById(R.id.btn_change_status);
        et_Remarks = (EditText)myView.findViewById(R.id.et_Remarks);
        attendance_id = String.valueOf(getArguments().getInt("attendance_id"));
        stud_id = String.valueOf(getArguments().getInt("Stud_id"));
        course = getArguments().getString("course").toString();
        sections = getArguments().getString("sections").toString();
        room = getArguments().getString("room").toString();
        date = getArguments().getString("date").toString();
        url = getArguments().getString("url").toString();
        name = getArguments().getString("name").toString();
        status = getArguments().getString("status").toString();
        program = getArguments().getString("Program").toString();
        tv_studentnumber.setText(stud_id);
        tv_fullname.setText(name);
        tv_program.setText(program);
        btn_status.setText("Select Remarks");
        tv_course.setText(course+" "+room);
        tv_date.setText(date);
        btn_status.setText(status);
        Picasso.with(getActivity())
                .load(url)
                .resize(144, 144)
                .centerCrop()
                .into(circleImageView);

        btn_status.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_change_status){
//            mySpinner.getSelectedItem().equals(selectedday);
            if (et_Remarks.length()>0){
                remarks = et_Remarks.getText().toString();
                final Handler handler = new Handler();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                View view1 = getActivity().getLayoutInflater().inflate(R.layout.spinner, null);
                mBuilder.setTitle("Pick Day");
                mySpinner = (Spinner) view1.findViewById(R.id.spinner_design);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.attendance_status));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mySpinner.setAdapter(adapter);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        if (!status.equals(mySpinner.getSelectedItem().toString())) {
                            status = mySpinner.getSelectedItem().toString();
                            if (status.equals("Excused")) {
                                btn_status.setText("Selected: " + status);
                                status_attedance = "4";
                            } else if (status.equals("Cutting")) {
                                btn_status.setText("Selected: " + status);
                                status_attedance = "5";
                            }
                            btn_status.setText(status);
                            AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(getActivity());
                            mBuilder2.setTitle("Are you sure to update attendance?");
                            mBuilder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            new update_attendance_task().execute();
                                        }
                                    });
                                }
                            });
                            mBuilder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = mBuilder2.create();
                            dialog.show();
                        } else {
                            MessageBox("Can't select same status");
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
            }else if (et_Remarks.length()==0){
                MessageBox("Remarks Empty!");
            }
        }
    }
    public class update_attendance_task extends AsyncTask<Void,Void,String>{
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
            nameValuePairs.add(new BasicNameValuePair("attendance",attendance_id));
            nameValuePairs.add(new BasicNameValuePair("status",status_attedance));
            nameValuePairs.add(new BasicNameValuePair("remarks",remarks));


            try {
                //ip= new Properties();
                String Url = p.getIP()+"Update_Attendance.php";
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

                        MessageBox("error: "+e.toString());

                    }
                });
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MessageBox(s);
        }
    }
    public void MessageBox(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
