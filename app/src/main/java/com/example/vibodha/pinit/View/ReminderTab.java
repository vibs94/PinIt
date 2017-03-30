package com.example.vibodha.pinit.View;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.ListAdapters.ReminderListAdapter;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by vibodha on 3/23/17.
 */

public class ReminderTab extends android.support.v4.app.Fragment {






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_tab,container,false);
        ReminderDB reminderDB = ReminderDB.getInstance(this.getContext());
        try {
            ArrayList<Reminder> reminderList = reminderDB.getReminders();
            Reminder[] reminders = reminderList.toArray(new Reminder[reminderList.size()]);
            ListAdapter reminderListAdapter = new ReminderListAdapter(this.getContext(),reminders);
            ListView reminderListView = (ListView) view.findViewById(R.id.allreminder_list);
            reminderListView.setAdapter(reminderListAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }
}
