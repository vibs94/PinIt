package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.ParseException;

public class EditReminder extends AppCompatActivity {

    final int REQUEST_CODE_PLACEPICKER = 1;
    Place place;
    EditText name;
    EditText longitude;
    EditText latitude;
    EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        name = (EditText) findViewById(R.id.txt_name);
        longitude = (EditText) findViewById(R.id.txt_longitude);
        latitude = (EditText) findViewById((R.id.txt_latitude));
        note = (EditText) findViewById(R.id.txt_note);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button changeLocation = (Button) findViewById(R.id.btn_change_location);
        Button editReminder = (Button) findViewById(R.id.btn_edit_reminder);
        FloatingActionButton home = (FloatingActionButton) findViewById(R.id.btn_home);
        //Toast.makeText(EditReminder.this,getIntent().getStringExtra("id"),Toast.LENGTH_SHORT).show();
        int reminderID = getIntent().getIntExtra("id",-1);
        final ReminderDB reminderDB = ReminderDB.getInstance(this);
        Reminder reminder = null;


        try {
            Reminder rem = reminderDB.getReminder(reminderID);
            name.setText(rem.getLocation().getLocationName());
            longitude.setText(String.valueOf(rem.getLocation().getLongitude()));
            latitude.setText(String.valueOf(rem.getLocation().getLatitude()));
            note.setText(rem.getNote());
            float priorityLevel = (float) rem.getRange();
            ratingBar.setRating(priorityLevel);
            reminder = rem;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //////////////////// change location //////////////////////////
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent map = new Intent(MainActivity.this,MapsActivity.class);
//                startActivity(map);

                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                try{
                    Intent intent = intentBuilder.build(EditReminder.this);
                    startActivityForResult(intent,REQUEST_CODE_PLACEPICKER);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        /////////////////// edit reminder /////////////
        final Reminder finalReminder = reminder;
        editReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location;
                // set location
                String locationName = name.getText().toString();
                try {
                    double locationLon = Double.parseDouble(longitude.getText().toString().trim());
                    double locationLat = Double.parseDouble(latitude.getText().toString().trim());
                    location = new Location(locationName, locationLon, locationLat);

                    //set range
                    float ratingBarValue = ratingBar.getRating();
                    int range = Math.round(ratingBarValue);
                    if (range > 0) {
                        // set Reminder
                        int reminderID = reminderDB.getNextReminderID();
                        String notetxt = note.getText().toString();
                        //Toast.makeText(AddReminder.this, "Set the Priority Level!!!", Toast.LENGTH_SHORT).show();
                        finalReminder.setNote(notetxt);
                        finalReminder.setRange(range);
                        finalReminder.setLocation(location);
                        if (reminderDB.editReminder(finalReminder)) {
                            Toast.makeText(EditReminder.this, "Reminder edited successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditReminder.this, MainActivity.class);
                            startActivity(intent);
                        } else {
//                            com.example.vibodha.pinit
                            Toast.makeText(EditReminder.this, "Unsuccessful!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(EditReminder.this, "Set the Priority Level!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(EditReminder.this,"Longitude and Latitude should be decimal values!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditReminder.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqestCode, int resultCode, Intent data){
        if(reqestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK){
            place = PlacePicker.getPlace(data,this);
            name.setText(place.getName().toString() + "," + place.getAddress().toString());
            longitude.setText(Double.toString(place.getLatLng().longitude));
            latitude.setText(Double.toString(place.getLatLng().latitude));
        }
    }
}
