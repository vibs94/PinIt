package com.example.vibodha.pinit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.Model.Reminder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vibodha on 3/27/17.
 */

public class ReminderDB {

    private static ReminderDB reminderDB = null;
    private DatabaseHelper databaseHelper;

    private ReminderDB(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }
    //Singleton
    public static ReminderDB getInstance(Context context){
        if(reminderDB==null){
            reminderDB=new ReminderDB(context);
        }
        return reminderDB;
    }

    public boolean addReminder(Reminder reminder){

        int locationID=0;
        int priorityID=0;
        int reminderID=0;
        ArrayList<Activity> activities;
        long result;
        String query;

        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        ContentValues reminderTableValues = new ContentValues();
        ContentValues activityTableValues;
        ContentValues locationTableValues = new ContentValues();
        ContentValues priorityTableValues = new ContentValues();
        //insert to location table
        locationTableValues.put("name",reminder.getLocation().getLocationName());
        locationTableValues.put("lon",reminder.getLocation().getLongitude());
        locationTableValues.put("lat",reminder.getLocation().getLatitude());
        result = dbWrite.insert("LOCATION",null,locationTableValues);
        if(result>0){
            query = "Select Max(location_id) as max_id from LOCATION;";
            Cursor cursor=dbRead.rawQuery(query,null);
            if(cursor.moveToNext()){
                locationID=cursor.getInt(cursor.getColumnIndex("max_id"));
            }
        }
        else{
            return false;
        }
        //insert to priority table
        priorityTableValues.put("range",reminder.getRange());
        result = dbWrite.insert("PRIORITY",null,priorityTableValues);
        if(result>0){
            query = "Select Max(priority_id) as max_id from PRIORITY;";
            Cursor cursor=dbRead.rawQuery(query,null);
            if(cursor.moveToNext()){
                priorityID=cursor.getInt(cursor.getColumnIndex("max_id"));
            }
        }
        else{
            return false;
        }
        //insert reminder
        reminderTableValues.put("reminder_id",reminder.getTaskId());
        reminderTableValues.put("location_id",locationID);
        reminderTableValues.put("time_id_of_completion","-1");
        reminderTableValues.put("priority_id",priorityID);
        reminderTableValues.put("is_completed","0");
        reminderTableValues.put("note",reminder.getNote());
        result = dbWrite.insert("REMINDER_TASK",null,reminderTableValues);
        if(result<0){
            return false;
        }

        //insert activities
        activities = reminder.getActivities();
        for(int i=0;i<activities.size();i++){
            activityTableValues = new ContentValues();
            activityTableValues.put("activity_id",activities.get(i).getId());
            activityTableValues.put("reminder_id",reminder.getTaskId());
            activityTableValues.put("time_id_of_completion","-1");
            activityTableValues.put("description",activities.get(i).getDescription());
            result = dbWrite.insert("ACTIVITY",null,activityTableValues);
            if(result==-1) {
                return false;
            }
        }

        dbRead.close();
        dbWrite.close();
        return true;
    }

    public int getNextActivityID(){
        int maxID=0;
        String query;
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        query = "Select Max(activity_id) as max_id from ACTIVITY;";
        Cursor cursor=dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            maxID=cursor.getInt(cursor.getColumnIndex("max_id"));
        }

        return maxID+1;
    }

    public int getNextReminderID(){

        int maxID=0;
        String query;
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        query = "Select Max(reminder_id) as max_id from REMINDER_TASK;";
        Cursor cursor=dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            maxID=cursor.getInt(cursor.getColumnIndex("max_id"));
        }

        return maxID+1;

    }


    //get reminder by id
    public Reminder getReminder(int id) throws ParseException {

        String query;
        ArrayList<Activity> activities = new ArrayList<Activity>();
        Location location;
        int range=0;
        ArrayList<Date> listOfWakeupTimes = new ArrayList<Date>();
        Reminder reminder;
        Activity activity;
        int activityID=0;
        String activityDesc;
        int timeIDOfCompletion;
        Date timeOfCompletion;
        String time="";
        int locationID=0;
        int priorityID=0;
        String note="";
        boolean isCompleted=false;
        DateFormat dateFormat;
        String locationName="";
        double lon=0.0;
        double lat=0.0;
        Cursor cursor;
        Cursor cursor1;
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        //set activities
        query = String.format("select * from ACTIVITY where reminder_id = %s;",id);
        cursor = dbRead.rawQuery(query,null);
        while(cursor.moveToNext()){
            activityID = cursor.getInt(cursor.getColumnIndex("activity_id"));
            activityDesc = cursor.getString(cursor.getColumnIndex("description"));
            timeIDOfCompletion = cursor.getInt(cursor.getColumnIndex("time_id_of_completion"));
            activity = new Activity(activityID,activityDesc);
            if(timeIDOfCompletion>0){
                query = "select * from TIME where time_id="+timeIDOfCompletion;
                cursor1 = dbRead.rawQuery(query,null);
                if (cursor1.moveToNext()) {
                    time = cursor1.getString(cursor1.getColumnIndex("date")) + " " + cursor1.getString(cursor1.getColumnIndex("hour")) + ":" + cursor1.getString(cursor.getColumnIndex("min"));
                }
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                try {
                    timeOfCompletion = dateFormat.parse(time);
                    activity.setTimeofCompletion(timeOfCompletion);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            activities.add(activity);
        }

        Log.w("#activities",Integer.toString(activities.size()));

        //set reminder
        query = String.format("select * from REMINDER_TASK where reminder_id = %s;",id);
        cursor = dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            locationID = cursor.getInt(cursor.getColumnIndex("location_id"));
            priorityID = cursor.getInt(cursor.getColumnIndex("priority_id"));
            note = cursor.getString(cursor.getColumnIndex("note"));
            if(cursor.getInt(cursor.getColumnIndex("is_completed"))==1){
                isCompleted = true;
            }
            else{
                isCompleted = false;
            }
        }

        //set location
        query = String.format("select * from LOCATION where location_id = %s;",locationID);
        cursor = dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            locationName = cursor.getString(cursor.getColumnIndex("name"));
            lon = cursor.getDouble(cursor.getColumnIndex("lon"));
            lat = cursor.getDouble(cursor.getColumnIndex("lat"));
        }
        location = new Location(locationName,lon,lat);

        //set range
        query = String.format("select * from PRIORITY where priority_id = %s;",priorityID);
        cursor = dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            range = cursor.getInt(cursor.getColumnIndex("range"));
        }

        //set wakeuptimes
        query = String.format("select * from WAKEUPS where reminder_id = %s;",id);
        cursor = dbRead.rawQuery(query,null);
        while(cursor.moveToNext()){
            timeIDOfCompletion = cursor.getInt(cursor.getColumnIndex("time_id_of_completion"));
            query = "select * from TIME where time_id="+timeIDOfCompletion;
            cursor1 = dbRead.rawQuery(query,null);
            if (cursor1.moveToNext()) {
                time = cursor1.getString(cursor1.getColumnIndex("date")) + " " + cursor1.getString(cursor1.getColumnIndex("hour")) + ":" + cursor1.getString(cursor1.getColumnIndex("min"));
            }
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            try {
                timeOfCompletion = dateFormat.parse(time);
                listOfWakeupTimes.add(timeOfCompletion);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
        dbRead.close();

        reminder = new Reminder(id,location,isCompleted,range,activities,note);
        reminder.setListOfwakeupTimes(listOfWakeupTimes);
        return  reminder;
    }

    //get all the reminders
    public ArrayList<Reminder> getReminders() throws ParseException {

        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();
        Cursor cursor;
        int reminderID;
        String query;
        Reminder reminder;
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();

        query = "select reminder_id from REMINDER_TASK;";
        cursor = dbRead.rawQuery(query,null);
        while (cursor.moveToNext()){
            reminderID = cursor.getInt(cursor.getColumnIndex("reminder_id"));
            try {
                reminder = getReminder(reminderID);
                reminders.add(reminder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return reminders;
    }

}
