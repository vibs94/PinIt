package com.example.vibodha.pinit.View;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.ListAdapters.ActivityListAdapter;
import com.example.vibodha.pinit.ListAdapters.ReminderListAdapter;
import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

public class ViewReminder extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        int reminderID = getIntent().getIntExtra("id",-1);
        TextView reminderNote = (TextView) findViewById(R.id.note);
        TextView reminderLocation = (TextView) findViewById(R.id.reminder_location);
        ImageView completeState = (ImageView) findViewById(R.id.complete_state);
        ReminderDB reminderDB = ReminderDB.getInstance(this);
        try {

            Reminder reminder = reminderDB.getReminder(reminderID);

            reminderNote.setText(reminder.getNote());
            reminderLocation.setText(reminder.getLocation().getLocationName());
            if(reminder.isCompleted()){
                completeState.setImageResource(R.drawable.tik);
            }
            else {
                completeState.setImageResource(R.drawable.cross);
            }
            //Toast.makeText(this,""+reminder.getActivities().size(),Toast.LENGTH_SHORT).show();
            ArrayList<Activity> activityArrayList = reminder.getActivities();
            // create reminder list
            //Toast.makeText(this,""+activityArrayList.size(),Toast.LENGTH_SHORT).show();
            if(activityArrayList.size()>0) {
                Activity[] activities = activityArrayList.toArray(new Activity[activityArrayList.size()]);
                ListAdapter activityListAdapter = new ActivityListAdapter(this, activities);
                ListView activityListView = (ListView) findViewById(R.id.reminder_activities);
                activityListView.setAdapter(activityListAdapter);
            }
//            registerForContextMenu(activityListView);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
