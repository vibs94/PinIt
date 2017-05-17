package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Controller.TaskController;
import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ////////////// initialize activity ///////////////////
        EditText txtName = (EditText) findViewById(R.id.txt_locationName);
        EditText txtLat = (EditText) findViewById(R.id.txt_lat);
        EditText txtLon = (EditText) findViewById(R.id.txt_lon);
        txtName.setText(getIntent().getStringExtra("placeName"));
        txtLat.setText(getIntent().getStringExtra("placeLat"));
        txtLon.setText(getIntent().getStringExtra("placeLon"));
        ////////////////// add new activity //////////////////
        Button btnAddActivity = (Button) findViewById(R.id.btn_add_activity);
        FloatingActionButton home = (FloatingActionButton) findViewById(R.id.btn_home);
        final EditText newActivity = (EditText) findViewById(R.id.txt_activity);
        final ArrayList<String> newActivities = new ArrayList<String>();

        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newActivity.getText().toString().trim().equals("") && !newActivity.getText().toString().trim().equals(null)) {
                    Toast.makeText(AddReminder.this, "Activity added.", Toast.LENGTH_SHORT).show();
                    newActivities.add(newActivity.getText().toString());
                    ListView listView = (ListView) findViewById(R.id.activity_list);
                    ListAdapter listAdapter = new ArrayAdapter<String>(AddReminder.this, android.R.layout.simple_list_item_1, newActivities.toArray(new String[newActivities.size()]));
                    listView.setAdapter(listAdapter);
                    newActivity.setText("");
                }
                else{
                    Toast.makeText(AddReminder.this, "Activity can not be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /////////////////// add reminder /////////////////
        Button btnAddReminder = (Button) findViewById(R.id.btn_add_reminder);
        final ReminderDB reminderDB = ReminderDB.getInstance(this);
        final TaskController taskController = TaskController.getInstance(this);

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reminder reminder;
                Location location;
                ArrayList<Activity> activities = new ArrayList<Activity>();
                Activity activity;
                // set location
                String locationName = ((EditText) findViewById(R.id.txt_locationName)).getText().toString();
                try {
                    double locationLon = Double.parseDouble(((EditText) findViewById(R.id.txt_lon)).getText().toString().trim());
                    double locationLat = Double.parseDouble(((EditText) findViewById(R.id.txt_lat)).getText().toString().trim());
                    location = new Location(locationName, locationLon, locationLat);

                    // set activities
                    int nextID = reminderDB.getNextActivityID();
                    for (int i = 0; i < newActivities.size(); i++) {
                        activity = new Activity(nextID, newActivities.get(i));
                        activities.add(activity);
                        nextID++;
                    }
                    //set range
                    RatingBar ratingBar = (RatingBar) findViewById(R.id.reminder_ratingBar);
                    float ratingBarValue = ratingBar.getRating();
                    int range = Math.round(ratingBarValue );
                    if (range > 0) {
                        // set Reminder
                        int reminderID = reminderDB.getNextReminderID();
                        String note = ((EditText) findViewById(R.id.note)).getText().toString();
                        //Toast.makeText(AddReminder.this, "Set the Priority Level!!!", Toast.LENGTH_SHORT).show();
                        reminder = new Reminder(reminderID, location, false, range, activities, note);
                        if (reminderDB.addReminder(reminder)) {
                            try {
                                taskController.setTask(reminderID,"reminder");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AddReminder.this, "Reminder added successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddReminder.this, MainActivity.class);
                            startActivity(intent);
                        } else {
//                            com.example.vibodha.pinit
                            Toast.makeText(AddReminder.this, "Unsuccessful!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddReminder.this, "Set the Priority Level!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(AddReminder.this,"Longitude and Latitude should be decimal values!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddReminder.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
