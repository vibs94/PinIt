package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.AddReminder;

import java.util.ArrayList;

import static com.example.vibodha.pinit.R.layout.activity_list;
import static com.example.vibodha.pinit.R.layout.reminder_list;


/**
 * Created by vibodha on 3/29/17.
 */

public class ReminderListAdapter extends ArrayAdapter<Reminder> {


    public ReminderListAdapter(Context context, Reminder[] reminders) {
        super(context, reminder_list,reminders);
    }

    LinearLayout checkBoxLayout;
    CheckBox checkBox;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(reminder_list,parent,false);

        Reminder singleReminder = getItem(position);
        TextView locationName = (TextView) customView.findViewById(R.id.location_name);
        TextView note = (TextView) customView.findViewById(R.id.reminder_note);
        ImageView isCompleted = (ImageView) customView.findViewById(R.id.is_completed);
        //final LinearLayout linearLayout = (LinearLayout) customView.findViewById(R.id.checkbox_layout);

        locationName.setText(singleReminder.getLocation().getLocationName());
        note.setText(singleReminder.getNote());
        if(singleReminder.isCompleted()){
            isCompleted.setImageResource(R.drawable.tik);
        }
        else{
            isCompleted.setImageResource(R.drawable.cross);
        }

        final ArrayList<Activity> activities = singleReminder.getActivities();

        checkBoxLayout = (LinearLayout) customView.findViewById(R.id.list_layout);

        for(int i=0;i<activities.size();i++){
            checkBox = new CheckBox(customView.getContext());
            checkBox.setText(activities.get(i).getDescription());
            if(activities.get(i).getTimeofCompletion()!=null){
                checkBox.setChecked(true);
            }
            checkBoxLayout.addView(checkBox);
        }
        Activity[] activityArray = activities.toArray(new Activity[activities.size()]);
        Log.w("act_co",""+activityArray.length);
        ListAdapter activityListAdapter = new ActivityListAdapter(this.getContext(),activityArray);
        final ListView activityListView = (ListView) customView.findViewById(R.id.reminder_activity_list);
        activityListView.setAdapter(activityListAdapter);
        final LinearLayout listLayout = (LinearLayout) customView.findViewById(R.id.list_layout) ;
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Clickbefore",""+activityListView.getVisibility());
                if(checkBoxLayout.getVisibility()==View.GONE) {
                    checkBoxLayout.setVisibility(View.VISIBLE);
                }
                else{
                    checkBoxLayout.setVisibility(View.GONE);
                }
                Log.w("Clickafter",""+activityListView.getVisibility());
            }
        });

//        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.w("Clickbefore",""+activityListView.getVisibility());
//                if(checkBoxLayout.getVisibility()==View.GONE) {
//                    checkBoxLayout.setVisibility(View.VISIBLE);
//                }
//                else{
//                    checkBoxLayout.setVisibility(View.GONE);
//                }
//                Log.w("Clickafter",""+activityListView.getVisibility());
//            }
//
//        });

        return customView;
    }
}
