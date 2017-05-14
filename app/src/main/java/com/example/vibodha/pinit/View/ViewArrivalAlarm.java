package com.example.vibodha.pinit.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.ListAdapters.ContactListAdapter;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Contact;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

public class ViewArrivalAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_arrival_alarm);
        TextView locationName = (TextView) findViewById(R.id.alarm_location);
        ImageView isCompleted = (ImageView) findViewById(R.id.complete_alarm);
        ArrayList<Contact> contacts=null;

        int alarmID = getIntent().getIntExtra("id",-1);
        ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(this);
        try {
            ArrivalAlarm arrivalAlarm = arrivalAlarmDB.getArrivalAlarm(alarmID);
            locationName.setText(arrivalAlarm.getLocation().getLocationName());
            contacts = arrivalAlarm.getContacts();
            //Toast.makeText(this,""+contacts.size(),Toast.LENGTH_SHORT).show();

            if(arrivalAlarm.isCompleted()){
                isCompleted.setImageResource(R.drawable.ic_tik);
            }
            else{
                isCompleted.setImageResource(R.drawable.ic_cross);
            }
            if(contacts.size()>0) {
                ListView listView = (ListView) findViewById(R.id.alarm_contacts);
                Contact[] contactslist = contacts.toArray(new Contact[contacts.size()]);
                ListAdapter contactListAdapter = new ContactListAdapter(ViewArrivalAlarm.this, contactslist);
                listView.setAdapter(contactListAdapter);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
