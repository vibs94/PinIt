package com.example.vibodha.pinit.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.example.vibodha.pinit.Controller.NotificationController;
import com.example.vibodha.pinit.Controller.TaskController;
import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.Model.Reminder;

import java.text.ParseException;

public class LocationReceiver extends BroadcastReceiver {
    public LocationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TaskController taskController=TaskController.getInstance(context);

        ReminderDB reminderDB = ReminderDB.getInstance(context);

        final boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

        int reminderID = intent.getIntExtra("id", -1);
        Toast.makeText(context, "Loc Receiver works", Toast.LENGTH_SHORT).show();
        Log.w("Loc Receiver works", "");

        if (reminderID >-1) {
//................Check entering or leaving
            /*if (entering) {*/
                try {
                    Reminder reminder = reminderDB.getReminder(reminderID);
                    if(!reminder.isCompleted()) {
                        NotificationController.viewReminderNotification(context, reminder);
                        reminder.reportWakeup();
                        reminderDB.markWakeupReminder(reminder);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
           /* } else {

            }*/

        }
    }
}
