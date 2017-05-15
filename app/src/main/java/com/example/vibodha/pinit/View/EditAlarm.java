package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.ParseException;

public class EditAlarm extends AppCompatActivity {

    final int REQUEST_CODE_PLACEPICKER = 1;
    Place place;
    EditText name;
    EditText longitude;
    EditText latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        name = (EditText) findViewById(R.id.edit_alarm_txt_name);
        longitude = (EditText) findViewById(R.id.edit_alarm_txt_longitude);
        latitude = (EditText) findViewById((R.id.edit_alarm_txt_latitude));
        Button changeLocation = (Button) findViewById(R.id.edit_alarm_btn_change_location);
        Button editAlarm = (Button) findViewById(R.id.btn_edit_alarm);

        int alarmID = getIntent().getIntExtra("id",-1);
        final ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(this);
        ArrivalAlarm arrivalAlarm = null;
        try {
            final ArrivalAlarm alarm = arrivalAlarmDB.getArrivalAlarm(alarmID);
            arrivalAlarm = alarm;
            name.setText(arrivalAlarm.getLocation().getLocationName());
            longitude.setText(String.valueOf(arrivalAlarm.getLocation().getLongitude()));
            latitude.setText(String.valueOf(arrivalAlarm.getLocation().getLatitude()));

        }
        catch (ParseException e){

        }
        //////////////////// change location //////////////////////////
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent map = new Intent(MainActivity.this,MapsActivity.class);
//                startActivity(map);

                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                try{
                    Intent intent = intentBuilder.build(EditAlarm.this);
                    startActivityForResult(intent,REQUEST_CODE_PLACEPICKER);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        /////////////////// edit reminder /////////////
        final ArrivalAlarm finalAlarm = arrivalAlarm;
        editAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location;
                // set location
                String locationName = name.getText().toString();
                try {
                    double locationLon = Double.parseDouble(longitude.getText().toString().trim());
                    double locationLat = Double.parseDouble(latitude.getText().toString().trim());
                    location = new Location(locationName, locationLon, locationLat);

                        // set Alarm

                        //Toast.makeText(AddReminder.this, "Set the Priority Level!!!", Toast.LENGTH_SHORT).show();
                        finalAlarm.setLocation(location);
                        if (arrivalAlarmDB.editAlarm(finalAlarm)) {
                            Toast.makeText(EditAlarm.this, "Alarm edited successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditAlarm.this, MainActivity.class);
                            startActivity(intent);
                        } else {
//                            com.example.vibodha.pinit
                            Toast.makeText(EditAlarm.this, "Unsuccessful!!!", Toast.LENGTH_SHORT).show();
                        }

                }
                catch (NumberFormatException e){
                    Toast.makeText(EditAlarm.this,"Longitude and Latitude should be decimal values!!!",Toast.LENGTH_SHORT).show();
                }
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
