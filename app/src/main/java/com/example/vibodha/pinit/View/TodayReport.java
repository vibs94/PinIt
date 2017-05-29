package com.example.vibodha.pinit.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.ListAdapters.ReminderListAdapter;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodayReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report);
        ReminderDB reminderDB = ReminderDB.getInstance(this);
        ArrayList<Reminder> reminderList = null;
        try {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            reminderList = reminderDB.getRemindersAddedOn(day.format(date));
            Reminder[] reminders = reminderList.toArray(new Reminder[reminderList.size()]);
            ListAdapter reminderListAdapter = new ReminderListAdapter(this,reminders);
            ListView reminderListView = (ListView) findViewById(R.id.today_list);
            reminderListView.setAdapter(reminderListAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
