package org.smartautomation.user.smartclassroom.Remarks_List;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.smartautomation.user.smartclassroom.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kenonnegammad on 27/03/2018.
 */

public class Remarks_View extends Fragment {
    View myView;
    CircleImageView circleImageView;
    TextView tv_program;
    TextView tv_course;
    TextView tv_name;
    TextView tv_date;
    TextView tv_studnum;
    Button btnstatus;
    EditText et_remarks;
    String program;
    String course;
    String section;
    String name;
    String date;
    String status;
    String remarks;
    String stud_id;
    String url;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.remarks_view,container,false);
        tv_course = (TextView)myView.findViewById(R.id.tv_course_remarks);
        tv_program = (TextView)myView.findViewById(R.id.tv_program_remarks);
        tv_date = (TextView)myView.findViewById(R.id.tv_date_remarks);
        tv_name = (TextView)myView.findViewById(R.id.tv_fullname_remarks);
        tv_studnum = (TextView)myView.findViewById(R.id.tv_studentnumber_remarks);
        circleImageView = (CircleImageView)myView.findViewById(R.id.imageView_ProfilePic_Remarks);
        btnstatus = (Button)myView.findViewById(R.id.btn_change_status_remarks);
        et_remarks = (EditText)myView.findViewById(R.id.et_Remarks);

        stud_id = String.valueOf(getArguments().getInt("Stud_id"));
        program= getArguments().getString("program");
        course = getArguments().getString("course");
        section = getArguments().getString("sections");
        name = getArguments().getString("name");
        date = getArguments().getString("date");
        status = getArguments().getString("status");
        remarks = getArguments().getString("remarks");
        url = getArguments().getString("url");

        tv_course.setText(course+"-"+section);
        tv_program.setText(program);
        tv_date.setText(date);
        tv_name.setText(name);
        tv_studnum.setText(stud_id);
        Picasso.with(getActivity())
                .load(url)
                .resize(90,90)
                .into(circleImageView);
        btnstatus.setText(status);
        et_remarks.setText(remarks);
        return myView;
    }
}
