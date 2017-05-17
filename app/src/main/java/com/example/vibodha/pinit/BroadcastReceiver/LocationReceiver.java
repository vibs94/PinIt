package com.example.vibodha.pinit.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.example.vibodha.pinit.Controller.AlarmController;
import com.example.vibodha.pinit.Controller.NotificationController;
import com.example.vibodha.pinit.Controller.TaskController;
import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Reminder;

import java.text.ParseException;

public class LocationReceiver extends BroadcastReceiver {
    public LocationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TaskController taskController=TaskController.getInstance(context);

        ReminderDB reminderDB = ReminderDB.getInstance(context);
        ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(context);

        final boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

        int id = intent.getIntExtra("id", -1);
        String taskType = intent.getStringExtra("type");
        Toast.makeText(context, "Loc Receiver works", Toast.LENGTH_SHORT).show();
        Log.w("Loc Receiver works", "");

        if (id >-1) {

            Log.w("type",taskType);
            Log.w("id",String.valueOf(id));
            if(taskType.equals("reminder")) {
                try {
                    Reminder reminder = reminderDB.getReminder(id);
                    if (!reminder.isCompleted()) {
                        NotificationController.viewReminderNotification(context, reminder);
                        reminder.reportWakeup();
                        reminderDB.markWakeupReminder(reminder);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if(taskType.equals("alarm")){
                try {
                    ArrivalAlarm arrivalAlarm = arrivalAlarmDB.getArrivalAlarm(id);
                    Log.w("location",arrivalAlarm.getLocation().getLocationName());
                    Intent alarmIntent = new Intent(context,AlarmController.class);
                    context.startService(alarmIntent);
                    arrivalAlarmDB.markWakeupAlarm(id);
                    NotificationController.viewAlarmNotification(context,arrivalAlarm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
