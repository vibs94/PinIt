package com.example.vibodha.pinit.Controller;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.vibodha.pinit.Constants;
import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.Database.ReminderDB;
import com.example.vibodha.pinit.Manifest;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Reminder;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by vibodha on 3/26/17.
 */

public class TaskController {

    private Context context;

    private static TaskController taskController = null;
    private ReminderDB reminderDB;
    private ArrivalAlarmDB arrivalAlarmDB;

    private TaskController(Context context) {
        reminderDB = reminderDB.getInstance(context);
        arrivalAlarmDB = ArrivalAlarmDB.getInstance(context);
        this.context = context;
    }

    public static TaskController getInstance(Context context) {
        if (taskController == null) {
            taskController = new TaskController(context);
        }
        return taskController;
    }

    public void setReminder(Reminder reminder) {

        Intent intent = new Intent(Constants.ACTION_PROXIMITY_ALERT);

        intent.putExtra("id", reminder.getTaskId());

        intent.setAction(Constants.ACTION_PROXIMITY_ALERT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getTaskId(), intent, 0);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

            return;
        }
        Log.w("range: ",String.valueOf(reminder.getRange()));
        locationManager.addProximityAlert(reminder.getLocation().getLatitude(), reminder.getLocation().getLongitude(), reminder.getRange(), -1, pendingIntent);
    }

    public ArrayList<Integer> bestOrdertList() throws ParseException {
        ArrayList<Integer> bestOrder = new ArrayList<>();
        ArrayList<Integer> startNodes = new ArrayList<>();
        ArrayList<Integer> endNodes = new ArrayList<>();
        ArrayList<ArrivalAlarm> arrivalAlarms = arrivalAlarmDB.getArrivalAlarms();
        ArrivalAlarm arrivalAlarm;
        int size = arrivalAlarms.size();
        Integer[][] successorList= new Integer[size][size];
        // colume indices consider as successors
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                successorList[i][j] = -1;
            }
        }
        for(int i=0;i<size;i++){
            arrivalAlarm = arrivalAlarms.get(i);
            successorList[i][arrivalAlarm.getSuccessorAlarm().getTaskId()] = 1;
        }


        return  bestOrder;
    }



}
