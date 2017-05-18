package com.example.vibodha.pinit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

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
    Context context;

    private ReminderDB(Context context) {
        this.context = context;
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

    public int getCurrentTimeID(){
        int maxID=0;
        String query;
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        query = "Select Max(time_id) as max_id from TIME;";
        Cursor cursor=dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            maxID=cursor.getInt(cursor.getColumnIndex("max_id"));
        }

        return maxID;
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
            if(timeIDOfCompletion>-1){
                //Toast.makeText(context,""+id+" "+timeIDOfCompletion,Toast.LENGTH_SHORT).show();
                query = "select * from TIME where time_id="+timeIDOfCompletion;
                cursor1 = dbRead.rawQuery(query,null);
                if (cursor1.moveToNext()) {
                    //Log.w("date",cursor.getString(cursor.getColumnIndex("datee")) );
                    //Log.w("h",)
                    time = cursor1.getString(cursor1.getColumnIndex("datee")) + " " + cursor1.getInt(cursor1.getColumnIndex("hour")) + ":" + cursor1.getInt(cursor1.getColumnIndex("min"));
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

        //Log.w("#activities",Integer.toString(activities.size()));

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
            timeIDOfCompletion = cursor.getInt(cursor.getColumnIndex("time_id_of_wakeup"));
            //Log.w("timeID",String.valueOf(timeIDOfCompletion));
            query = "select * from TIME where time_id="+timeIDOfCompletion;
            cursor1 = dbRead.rawQuery(query,null);
            if (cursor1.moveToNext()) {
                time = cursor1.getString(cursor1.getColumnIndex("datee")) + " " + cursor1.getString(cursor1.getColumnIndex("hour")) + ":" + cursor1.getString(cursor1.getColumnIndex("min"));
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
        //reminder.setListOfwakeupTimes(listOfWakeupTimes);
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

    public boolean markActivity(Activity activity){
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        ContentValues timeContent = new ContentValues();
        ContentValues activityContent = new ContentValues();
        Date date = activity.getTimeofCompletion();
        long result;
        int timeID;
        //////////// add time to TIME table ////////////
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat min = new SimpleDateFormat("mm");
        timeContent.put("datee",day.format(date));
        timeContent.put("hour",hour.format(date));
        timeContent.put("min",min.format(date));
        result = dbWrite.insert("TIME",null,timeContent);
        if(result<0){
            return false;
        }
        ///////////// change time id in ACTIVITY table /////////////////
        timeID = getCurrentTimeID();
        activityContent.put("time_id_of_completion",timeID);
        result = dbWrite.update("ACTIVITY",activityContent,"activity_id="+activity.getId(),null);
        if(result<0){
            return false;
        }
        //Toast.makeText(context,"activity marked"+activity.getTimeofCompletion(),Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean markReminder(Reminder reminder){
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        ContentValues timeContent = new ContentValues();
        ContentValues reminderContent = new ContentValues();
        Date date = reminder.getTimeOfCompletion();
        long result;
        int timeID;
        ////////////// add time to TIME table //////////////
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat min = new SimpleDateFormat("mm");
        timeContent.put("datee",day.format(date));
        timeContent.put("hour",hour.format(date));
        timeContent.put("min",min.format(date));
        result = dbWrite.insert("TIME",null,timeContent);
        if(result<0){
            return false;
        }
        ////////// change time id in REMINDER table ////////////////
        timeID = getCurrentTimeID();
        reminderContent.put("time_id_of_completion",timeID);
        reminderContent.put("is_completed",1);
        result = dbWrite.update("REMINDER_TASK",reminderContent,"reminder_id=?",new  String[] {String.valueOf(reminder.getTaskId())});
        if(result<0){
            return false;
        }
        //Toast.makeText(context,"Reminder marked"+reminder.getTimeOfCompletion(),Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean markWakeupReminder(Reminder reminder){
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        ContentValues timeContent = new ContentValues();
        ContentValues wakeupsContent = new ContentValues();
        Date date = reminder.getLastWakeup();
        long result;
        int timeID;
        ////////// add time id to TIME table ///////////////
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat min = new SimpleDateFormat("mm");
        timeContent.put("datee",day.format(date));
        timeContent.put("hour",hour.format(date));
        timeContent.put("min",min.format(date));
        result = dbWrite.insert("TIME",null,timeContent);
        if(result<0){
            return false;
        }
        //////////// add time id to WAKEUPS table //////////////
        timeID = getCurrentTimeID();
        wakeupsContent.put("reminder_id",reminder.getTaskId());
        wakeupsContent.put("time_id_of_wakeup",timeID);
        result = dbWrite.insert("WAKEUPS",null,wakeupsContent);
        if(result<0){
            return false;
        }
        return true;
    }

    public boolean editReminder(Reminder reminder){

        int locationID=0;
        int priorityID=0;
        long result;
        String query;
        Cursor cursor;
        int isCompleted;

        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();
        /////////// edit location ///////////////
        ContentValues reminderTableValues = new ContentValues();
        ContentValues locationTableValues = new ContentValues();
        ContentValues priorityTableValues = new ContentValues();
        //insert to location table
        locationTableValues.put("name",reminder.getLocation().getLocationName());
        locationTableValues.put("lon",reminder.getLocation().getLongitude());
        locationTableValues.put("lat",reminder.getLocation().getLatitude());
        query = "select location_id from REMINDER_TASK where reminder_id="+reminder.getTaskId();
        cursor =  dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            locationID =  cursor.getInt(cursor.getColumnIndex("location_id"));
        }
        result = dbWrite.update("LOCATION",locationTableValues,"location_id=?",new String[] {String.valueOf(locationID)});
        if(result<0){

            return false;
        }
        //edit priority table
        priorityTableValues.put("range",reminder.getRange());
        query = "select priority_id from REMINDER_TASK where reminder_id="+reminder.getTaskId();
        cursor =  dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            priorityID =  cursor.getInt(cursor.getColumnIndex("priority_id"));
        }
        result = dbWrite.update("PRIORITY",priorityTableValues,"priority_id=?",new String[] {String.valueOf(priorityID)});
        if(result<0){
            return false;
        }
        //edit reminder
        if(reminder.isCompleted()){
            isCompleted = 1;
        }
        else {
            isCompleted = 0;
        }
        reminderTableValues.put("reminder_id",reminder.getTaskId());
        reminderTableValues.put("location_id",locationID);
        reminderTableValues.put("time_id_of_completion","-1");
        reminderTableValues.put("priority_id",priorityID);
        reminderTableValues.put("is_completed",isCompleted);
        reminderTableValues.put("note",reminder.getNote());
        result = dbWrite.update("REMINDER_TASK",reminderTableValues,"reminder_id=?",new String[] {String.valueOf(reminder.getTaskId())});
        if(result<0){
            return false;
        }

        dbWrite.close();
        return true;

    }

}
