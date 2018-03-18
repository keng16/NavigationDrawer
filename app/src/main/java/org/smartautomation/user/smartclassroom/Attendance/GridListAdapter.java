package org.smartautomation.user.smartclassroom.Attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import org.smartautomation.user.smartclassroom.Global.full_status_model;
import org.smartautomation.user.smartclassroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sonu on 08/02/17.
 */

public class GridListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<full_status_model> arrayList;
    private LayoutInflater inflater;
    private boolean isListView;
    private int selectedPosition = -1;
    private int counter = -1;
    private String append;

    public GridListAdapter(Context context, ArrayList<full_status_model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.list_custom_row_layout, viewGroup, false);
            viewHolder.lblname = (TextView) view.findViewById(R.id.lbl_name);
            viewHolder.lbl_stud_number = (TextView)view.findViewById(R.id.lbl_stud_number);
            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radio_button);
            viewHolder.circleImageView = (CircleImageView)view.findViewById(R.id.imageView_logs_profile);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();


        final full_status_model full_status_model= arrayList.get(i);
        viewHolder.lblname.setText(full_status_model.getStudent_name());
        viewHolder.lbl_stud_number.setText(full_status_model.getStudent_id());
        Picasso.with(context)
                .load(full_status_model.getStudent_ImgUrl())
                .resize(50,50)
                .into(viewHolder.circleImageView);

        //check the radio button if both position and selectedPosition matches
       // viewHolder.radioButton.setChecked(i == selectedPosition);


        //Set the position tag to both radio button and label
        viewHolder.radioButton.setTag(i);
        viewHolder.lblname.setTag(i);

        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
                counter++;
                System.out.println(counter);
                if (counter==0) {
                    append = full_status_model.getStudent_id();
                }else if (counter>0){
                    append = append+":"+full_status_model.getStudent_id();
                }
            }
        });

        viewHolder.lblname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }


        });
        return view;
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView lblname;
        private TextView lbl_stud_number;
        private RadioButton radioButton;
        private CircleImageView circleImageView;
    }

    //Return the selectedPosition item
    public String getSelectedItem() {
        if (selectedPosition != -1) {
            //Toast.makeText(context, "Selected Item/s : " + append, Toast.LENGTH_SHORT).show();
            return append;
        }
        return "";
    }

    //Delete the selected position from the arrayList
    public void deleteSelectedPosition() {
        if (selectedPosition != -1) {
            arrayList.remove(selectedPosition);
            selectedPosition = -1;//after removing selectedPosition set it back to -1
            notifyDataSetChanged();
        }
    }
}
