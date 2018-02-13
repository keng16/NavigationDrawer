package com.example.user.smartclassroom.Logs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.user.smartclassroom.R;

/**
 * Created by kenonnegammad on 31/01/2018.
 */

public class Logs_Dash extends Fragment implements View.OnClickListener{
    View myView;
    CardView logs_today;
    CardView logs_term;
    String stud_id;
    Bundle bundle=new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.logs_dash,container,false);
        stud_id = getArguments().getString("Stud_id").toString();
        logs_term = (CardView) myView.findViewById(R.id.Logs_Term);
        logs_today = (CardView) myView.findViewById(R.id.Logs_Today);
        logs_today.setOnClickListener(this);
        logs_term.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View view) {
        final FragmentTransaction fragmentManager= getFragmentManager().beginTransaction();
        bundle.putString("Stud_id",stud_id);
        if (view.getId()==R.id.Logs_Term){

            Logs_Term logs_term=new Logs_Term();
            logs_term.setArguments(bundle);
            ((LinearLayout)myView.findViewById(R.id.Linear)).removeAllViews();
            fragmentManager
                    .replace(R.id.contentFrame
                            , logs_term,"Logs_Term")
                    .commit();
        }else if(view.getId()==R.id.Logs_Today){
            Logs_Daily logs_daily=new Logs_Daily();
            logs_daily.setArguments(bundle);
            ((LinearLayout)myView.findViewById(R.id.Linear)).removeAllViews();
            fragmentManager
                    .replace(R.id.contentFrame
                            , logs_daily,"Logs_Daily")
                    .commit();
        }
    }
}
