package com.example.user.smartclassroom.Account;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.smartclassroom.R;

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
import com.example.user.smartclassroom.Global.Properties;

/**
 * Created by kenonnegammad on 31/01/2018.
 */

public class Account_Class extends Fragment implements View.OnClickListener{

    View myView;
    Properties p = new Properties();
    String id;
    String user;
    String newpassword;
    String oldpassword;
    String name;
    TextView tv_name;
    EditText et_oldpassword;
    EditText et_newpassword;
    EditText et_newpassword2;
    Button btn_save;
    private ProgressDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.change_password,container,false);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");
        tv_name = (TextView)myView.findViewById(R.id.tv_name);
        et_oldpassword = (EditText)myView.findViewById(R.id.et_oldpassword);
        et_newpassword = (EditText)myView.findViewById(R.id.et_newpassword);
        et_newpassword2 = (EditText)myView.findViewById(R.id.et_newpassword2);
        btn_save = (Button)myView.findViewById(R.id.btn_save);
        id = getArguments().getString("Stud_id").toString();
        user = getArguments().getString("User").toString();
        name = getArguments().getString("Name").toString();
        tv_name.setText(name);
        btn_save.setOnClickListener(this);


        return myView;
    }

    @Override
    public void onClick(View v) {
        if(et_newpassword.getText().toString().equals(et_newpassword2.getText().toString())&&!et_oldpassword.getText().toString().equals("")){

            oldpassword = et_oldpassword.getText().toString();
            newpassword = et_newpassword.getText().toString();
            if (newpassword.length()>4) {
                new Change_Password_Task().execute();
            }else{
                MessageBox("Password is short!");
            }
        }else if(et_oldpassword.getText().equals("")) {
            MessageBox("Enter Old Password");
            et_oldpassword.requestFocus();
        }else if (!et_newpassword.getText().toString().equals(et_newpassword2.getText().toString())){
            MessageBox("Password not Match");
            et_newpassword.requestFocus();
        }
    }

    public class Change_Password_Task extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
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
            nameValuePairs.add(new BasicNameValuePair("id",id));
            nameValuePairs.add(new BasicNameValuePair("User",user));
            nameValuePairs.add(new BasicNameValuePair("newpassword",newpassword));
            nameValuePairs.add(new BasicNameValuePair("oldpassword",oldpassword));
            try {
                //ip= new Properties();
                String Url = p.getIP()+"Update_Password.php";
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
            dialog.dismiss();
            MessageBox(s);
        }
    }
    public void MessageBox(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
