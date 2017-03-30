package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.vibodha.pinit.R.layout.activity_list;
import static com.example.vibodha.pinit.R.layout.reminder_list;

/**
 * Created by vibodha on 3/30/17.
 */

public class ActivityListAdapter extends ArrayAdapter<Activity> {
    public ActivityListAdapter(Context context, Activity[] activities) {
        super(context, activity_list,activities);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(activity_list,parent,false);
        Activity singleActivity = getItem(position);
        TextView activityDesk = (TextView) customView.findViewById(R.id.activity_desc);
        TextView completeDate = (TextView) customView.findViewById(R.id.compete_date);
        ImageView isCompleted = (ImageView) customView.findViewById(R.id.is_activity_completed);

        activityDesk.setText(singleActivity.getDescription());
        Date completionDate = singleActivity.getTimeofCompletion();
        if(completionDate!=null){
            Format dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = dateFormate.format(completionDate);
            completeDate.setText("Activity was completed on "+date);
            isCompleted.setImageResource(R.drawable.tik);
        }

        else {
            completeDate.setText("Activity not completed yet!");
            isCompleted.setImageResource(R.drawable.cross);
        }


        return convertView;
    }
}
