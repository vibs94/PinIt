package com.example.vibodha.pinit.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.ListAdapters.ActivityListAdapter;
import com.example.vibodha.pinit.ListAdapters.IncompleteActivityListAdapter;
import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

public class ShowReminder extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener {

    TextView locationName;
    TextView note;
    ImageView isCompleted;
    Button mark;
    ListAdapter activityListAdapter;
    ListView activityListView;
    ListAdapter incompleteActivityListAdapter;
    ListView incompleteActivityListView;
    ArrayList<Activity> activities = new ArrayList<Activity>();
    ArrayList<Activity> completeActivities = new ArrayList<Activity>();
    ArrayList<Activity> inCompleteActivities = new ArrayList<Activity>();
    Activity activity;
    ReminderDB reminderDB;
    Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reminder);
        int reminderID = getIntent().getIntExtra("id",-1);
        reminderDB = ReminderDB.getInstance(this);
        locationName = (TextView) findViewById(R.id.show_reminder_location);
        note = (TextView) findViewById(R.id.show_reminder_note);
        isCompleted = (ImageView) findViewById(R.id.show_complete_state);
        mark = (Button) findViewById(R.id.btn_mark);
        try {
            reminder = reminderDB.getReminder(reminderID);
            locationName.setText(reminder.getLocation().getLocationName());
            note.setText(reminder.getNote());
            if(reminder.isCompleted()){
                isCompleted.setImageResource(R.drawable.ic_tik);
            }
            else{
                isCompleted.setImageResource(R.drawable.ic_cross);
            }
            activities = reminder.getActivities();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //// separate complete activities and incpmplete activities
        for(int i=0;i<activities.size();i++){
            if(activities.get(i).getTimeofCompletion()==null){
                inCompleteActivities.add(activities.get(i));
            }
            else{
                completeActivities.add(activities.get(i));
            }
        }
        //complete activities
        if(completeActivities.size()>0) {
            Activity[] completeActivityList = completeActivities.toArray(new Activity[completeActivities.size()]);
            activityListAdapter = new ActivityListAdapter(this, completeActivityList);
            activityListView = (ListView) findViewById(R.id.show_reminder_complete_activities);
            activityListView.setAdapter(activityListAdapter);
        }

        //incomplete activities
        if(inCompleteActivities.size()>0) {
            Activity[] incompleteActivityList = inCompleteActivities.toArray(new Activity[inCompleteActivities.size()]);
            incompleteActivityListAdapter = new IncompleteActivityListAdapter(this, incompleteActivityList);
            incompleteActivityListView = (ListView) findViewById(R.id.show_reminder_incomplete_activities);
            incompleteActivityListView.setAdapter(incompleteActivityListAdapter);
        }

        if(inCompleteActivities.size()==0){
            reminder.completeReminder();
            if(reminderDB.markReminder(reminder)){
                //Toast.makeText(ShowReminder.this,"Reminder marked"+reminder.getTimeOfCompletion(),Toast.LENGTH_SHORT).show();
            }
            isCompleted.setImageResource(R.drawable.ic_tik);
        }

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.w("listner in","");
        int pos = incompleteActivityListView.getPositionForView(buttonView);
        activity = inCompleteActivities.remove(pos);
        if(inCompleteActivities.size()==0){
            reminder.completeReminder();
            if(reminderDB.markReminder(reminder)){
                //Toast.makeText(ShowReminder.this,"Reminder marked"+reminder.getTimeOfCompletion(),Toast.LENGTH_SHORT).show();
            }
            isCompleted.setImageResource(R.drawable.ic_tik);
        }
        completeActivities.add(activity);
        activity.markActivity();
        if(reminderDB.markActivity(activity)){
            //Toast.makeText(ShowReminder.this,"activity marked"+activity.getTimeofCompletion(),Toast.LENGTH_SHORT).show();
        }
        //complete activities
        if(completeActivities.size()>0) {
            Activity[] completeActivityList = completeActivities.toArray(new Activity[completeActivities.size()]);
            activityListAdapter = new ActivityListAdapter(this, completeActivityList);
            activityListView = (ListView) findViewById(R.id.show_reminder_complete_activities);
            activityListView.setAdapter(activityListAdapter);
        }

        //incomplete activities
        if(inCompleteActivities.size()>-1) {
            Activity[] incompleteActivityList = inCompleteActivities.toArray(new Activity[inCompleteActivities.size()]);
            incompleteActivityListAdapter = new IncompleteActivityListAdapter(this, incompleteActivityList);
            incompleteActivityListView = (ListView) findViewById(R.id.show_reminder_incomplete_activities);
            incompleteActivityListView.setAdapter(incompleteActivityListAdapter);
        }
    }
}
