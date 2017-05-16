package com.example.vibodha.pinit.Controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.ShowReminder;

/**
 * Created by vibodha on 5/15/17.
 */

public class NotificationController {

    public static void viewReminderNotification(Context context, Reminder reminder) {
        Intent resultIntent = new Intent(context, ShowReminder.class);
        resultIntent.putExtra("id",reminder.getTaskId());



// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        reminder.getTaskId(),
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

/*
        Intent completeIntent = new Intent();
        completeIntent.putExtra("task_id", task_id);
        completeIntent.setAction("complete_action");
        PendingIntent completePendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, completeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent forgetIntent = new Intent();
        forgetIntent.putExtra("task_id", task_id);
        forgetIntent.setAction("forget_action");
        PendingIntent forgetPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, forgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notify)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_pinit2))
                        .setContentTitle("PinIt-Reminder")
                        .setContentText("Arrived at "+reminder.getLocation().getLocationName())
                        .setContentInfo("Tap here to check the reminder")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL);


        mBuilder.setContentIntent(resultPendingIntent);

// Sets an ID for the notification
        int mNotificationId = reminder.getTaskId();
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static void viewAlarmNotification(Context context, ArrivalAlarm alarm) {
        /*Intent resultIntent = new Intent(context, ShowReminder.class);
        resultIntent.putExtra("id",alarm.getTaskId());*/
        Intent resultIntent = new Intent(context,AlarmController.class);



// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        alarm.getTaskId(),
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

/*
        Intent completeIntent = new Intent();
        completeIntent.putExtra("task_id", task_id);
        completeIntent.setAction("complete_action");
        PendingIntent completePendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, completeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent forgetIntent = new Intent();
        forgetIntent.putExtra("task_id", task_id);
        forgetIntent.setAction("forget_action");
        PendingIntent forgetPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, forgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notify)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_pinit2))
                        .setContentTitle("PinIt-Alarm")
                        .setContentText("Arrived at "+alarm.getLocation().getLocationName())
                        .setContentInfo("Tap here to off the alarm")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL);


        mBuilder.setContentIntent(resultPendingIntent);

// Sets an ID for the notification
        int mNotificationId = alarm.getTaskId();
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
