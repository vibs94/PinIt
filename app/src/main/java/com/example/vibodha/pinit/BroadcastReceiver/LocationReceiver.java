package com.example.vibodha.pinit.BroadcastReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.vibodha.pinit.Constants;
import com.example.vibodha.pinit.Controller.AlarmController;
import com.example.vibodha.pinit.Controller.NotificationController;
import com.example.vibodha.pinit.Controller.TaskController;
import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Contact;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.Model.Reminder;

import java.text.ParseException;
import java.util.ArrayList;

public class LocationReceiver extends BroadcastReceiver {
    public LocationReceiver() {
    }
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        TaskController taskController=TaskController.getInstance(context);

        ReminderDB reminderDB = ReminderDB.getInstance(context);
        ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(context);

        final boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

        int id = intent.getIntExtra("id", -1);
        String taskType = intent.getStringExtra("type");
        Toast.makeText(context, "Location Receiver works", Toast.LENGTH_SHORT).show();
        Log.w("Loc Receiver works", "");

        if (id >-1) {

            Log.w("type",taskType);
            Log.w("id",String.valueOf(id));
            if(taskType.equals("reminder")) {
                try {
                    Reminder reminder = reminderDB.getReminder(id);
                    if (!reminder.isCompleted()&&checkLocation(reminder.getLocation())) {
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
                    if(!arrivalAlarm.isCompleted()&&checkLocation(arrivalAlarm.getLocation())) {
                        Log.w("location", arrivalAlarm.getLocation().getLocationName());
                        Intent alarmIntent = new Intent(context, AlarmController.class);
                        context.startService(alarmIntent);
                        arrivalAlarmDB.markWakeupAlarm(id);

                        ArrayList<Contact> contacts = arrivalAlarm.getContacts();
                        int i;
                        for (i = 0; i < contacts.size(); i++) {
                            SmsManager.getDefault().sendTextMessage(contacts.get(i).getPhoneNumber(), null, contacts.get(i).getMessage(), null, null);
                        }
                        NotificationController.viewAlarmNotification(context, arrivalAlarm);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private boolean checkLocation(Location location){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

            return false;
        }
        android.location.Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double accuracy = 100.0;
        Log.e("current location",String.valueOf((Math.round(myLocation.getLatitude()*accuracy)/accuracy)));
        Log.e("location",String.valueOf((Math.round(location.getLatitude()*accuracy)/accuracy)));
        if((Math.round(myLocation.getLatitude()*accuracy)/accuracy)!=(Math.round(location.getLatitude()*accuracy)/accuracy)){
            return false;
        }
        Log.e("current location",String.valueOf((Math.round(myLocation.getLongitude()*accuracy)/accuracy)));
        Log.e("location",String.valueOf((Math.round(location.getLongitude()*accuracy)/accuracy)));
        if((Math.round(myLocation.getLongitude()*accuracy)/accuracy)!=(Math.round(location.getLongitude()*accuracy)/accuracy)){
            return false;
        }
        return true;
    }
}
