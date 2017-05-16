package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Controller.TaskController;
import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Contact;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

public class ShowAlarm extends AppCompatActivity {

    TextView message;
    Button off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);
        final int id = getIntent().getIntExtra("id",-1);
        ArrivalAlarmDB alarmDB = ArrivalAlarmDB.getInstance(this);
        final TaskController taskController = TaskController.getInstance(this);
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            ArrivalAlarm alarm = alarmDB.getArrivalAlarm(id);
            message = (TextView) findViewById(R.id.txt_message);
            off = (Button) findViewById(R.id.off_alarm);
            contacts = alarm.getContacts();
            String contactName = "";
            int i;
            for (i = 0; i < contacts.size(); i++) {
                SmsManager.getDefault().sendTextMessage(contacts.get(i).getPhoneNumber(), null, "Arrived at " + alarm.getLocation().getLocationName(), null, null);
                contactName = contactName + contacts.get(i).getName() + " ";
            }
            if (i > 1){
                message.setText("Messages have been sent to " + contactName);
        }
        else {
                message.setText("Message has been sent to " + contactName);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
            off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        taskController.cancelAlarm(id);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }});

    }
}
