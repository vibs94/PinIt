package com.example.vibodha.pinit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Contact;
import com.example.vibodha.pinit.Model.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vibodha on 5/3/17.
 */

public class ArrivalAlarmDB {

    private static ArrivalAlarmDB arrivalAlarmDB= null;
    private DatabaseHelper databaseHelper;

    private ArrivalAlarmDB(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }
    //Singleton
    public static ArrivalAlarmDB getInstance(Context context){
        if(arrivalAlarmDB==null){
            arrivalAlarmDB=new ArrivalAlarmDB(context);
        }
        return arrivalAlarmDB;
    }

    public boolean addArrivalAlarm(ArrivalAlarm arrivalAlarm){
        int locationID=0;
        int arrivalAlarmID=0;
        ArrayList<Contact> contacts;
        long result;
        String query;

        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        ContentValues arrivalAlarmTableValues = new ContentValues();
        ContentValues locationTableValues = new ContentValues();
        ContentValues contactTableValues;

        //insert to location table
        locationTableValues.put("name",arrivalAlarm.getLocation().getLocationName());
        locationTableValues.put("lon",arrivalAlarm.getLocation().getLongitude());
        locationTableValues.put("lat",arrivalAlarm.getLocation().getLatitude());
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

        //insert reminder
        arrivalAlarmTableValues.put("arrival_alarm_id",arrivalAlarm.getTaskId());
        arrivalAlarmTableValues.put("location_id",locationID);
        arrivalAlarmTableValues.put("time_id_of_completion","-1");
        arrivalAlarmTableValues.put("is_wakeup","0");
        arrivalAlarmTableValues.put("range",arrivalAlarm.getRange());
        arrivalAlarmTableValues.put("successor_alarm_id","-1");
        result = dbWrite.insert("ARRIVAL_ALARM_TASK",null,arrivalAlarmTableValues);
        if(result<0){
            return false;
        }

        //insert contacts
        contacts = arrivalAlarm.getContacts();
        for(int i=0;i<contacts.size();i++){
            contactTableValues = new ContentValues();
            contactTableValues.put("arrival_alarm_id",arrivalAlarm.getTaskId());
            contactTableValues.put("name",contacts.get(i).getName());
            contactTableValues.put("phone_number",contacts.get(i).getPhoneNumber());
            result = dbWrite.insert("CONTACT",null,contactTableValues);
            if(result==-1) {
                return false;
            }
        }

        dbRead.close();
        dbWrite.close();
        return true;

    }

    public int getNextArrivalAlarmID(){

        int maxID=0;
        String query;
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        query = "Select Max(arrival_alarm_id) as max_id from ARRIVAL_ALARM_TASK;";
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

    public ArrivalAlarm getArrivalAlarm(int id) throws ParseException{
        ArrivalAlarm arrivalAlarm =  null;
        Location location;
        Contact contact;
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String conName;
        String conNo;
        int location_id =0 ;
        int time_id=0;
        int range = 0;
        int successor_alarm_id=0;
        boolean isWakeup=false;
        String locationName="";
        double lon=0.0;
        double lat=0.0;
        String time ="";
        java.text.DateFormat dateFormat;
        Date timeOfCompletion = null;
        ArrivalAlarm successorAlarm =  null;

        String query;
        Cursor cursor;
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();

        ////////////// Set Contacts ///////////////
        query = String.format("select * from CONTACT where arrival_alarm_id = %s",id);
        cursor = dbRead.rawQuery(query,null);
        while(cursor.moveToNext()){
            conName = cursor.getString(cursor.getColumnIndex("name"));
            conNo = cursor.getString(cursor.getColumnIndex("phone_number"));
            contact = new Contact(conName,conNo);
            contacts.add(contact);
        }

        //////////////////////// Set Alarm ////////////
        query = String.format("select * from ARRIVAL_ALARM_TASK where arrival_alarm_id = %s",id);
        cursor = dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            location_id = cursor.getInt(cursor.getColumnIndex("location_id"));
            time_id = cursor.getInt(cursor.getColumnIndex("time_id_of_completion"));
            range = cursor.getInt(cursor.getColumnIndex("range"));
            successor_alarm_id = cursor.getInt(cursor.getColumnIndex("successor_alarm_id"));
            if(cursor.getInt(cursor.getColumnIndex("is_wakeup"))==1){
                isWakeup=true;
            }

        }

        /////////////// Set Location //////////////
        query = String.format("select * from LOCATION where location_id = %s;",location_id);
        cursor = dbRead.rawQuery(query,null);

        if(cursor.moveToNext()){
            locationName = cursor.getString(cursor.getColumnIndex("name"));
            lon = cursor.getDouble(cursor.getColumnIndex("lon"));
            lat = cursor.getDouble(cursor.getColumnIndex("lat"));
        }
        location = new Location(locationName,lon,lat);

        ///////////// set time of completion /////////////////
        query = "select * from TIME where time_id="+time_id;
        cursor = dbRead.rawQuery(query,null);
        if (cursor.moveToNext()) {
            time = cursor.getString(cursor.getColumnIndex("datee")) + " " + cursor.getString(cursor.getColumnIndex("hour")) + ":" + cursor.getString(cursor.getColumnIndex("min"));
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            try {
                timeOfCompletion = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(successor_alarm_id>0) {
            successorAlarm = getArrivalAlarm(successor_alarm_id);
        }

        arrivalAlarm = new ArrivalAlarm(id,location,isWakeup,range,contacts);
        //Log.w("#contacts",""+arrivalAlarm.getContacts().size());
        arrivalAlarm.setSuccessorAlarm(successorAlarm);
        arrivalAlarm.setTimeOfCompletion(timeOfCompletion);

        return arrivalAlarm;
    }

    public ArrayList<ArrivalAlarm> getArrivalAlarms() throws ParseException{
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();
        Cursor cursor;
        int alarmID;
        String query;
        ArrivalAlarm arrivalAlarm;
        ArrayList<ArrivalAlarm> arrivalAlarms = new ArrayList<ArrivalAlarm>();

        query = "select reminder_id from REMINDER_TASK;";
        cursor = dbRead.rawQuery(query,null);
        while (cursor.moveToNext()){
            alarmID = cursor.getInt(cursor.getColumnIndex("reminder_id"));
            try {
                arrivalAlarm = getArrivalAlarm(alarmID);
                arrivalAlarms.add(arrivalAlarm);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return arrivalAlarms;
    }

    public boolean markWakeupAlarm(int id) throws ParseException {
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        ContentValues timeContent = new ContentValues();
        ContentValues alarmContent = new ContentValues();
        ArrivalAlarm arrivalAlarm = getArrivalAlarm(id);
        arrivalAlarm.markAlarm();
        Date date = arrivalAlarm.getTimeOfCompletion();
        long result;
        int timeID;
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
        timeID = getCurrentTimeID();
        alarmContent.put("time_id_of_completion",timeID);
        alarmContent.put("is_wakeup",1);
        result = dbWrite.update("ARRIVAL_ALARM_TASK",alarmContent,"arrival_alarm_id=?",new String[] {String.valueOf(id)});
        if(result<0){
            return false;
        }
        return true;
    }

    public boolean editAlarm(ArrivalAlarm arrivalAlarm){
        String query;
        int locationID=-1;
        long result;
        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        Cursor cursor;
        //insert to location table
        ContentValues locationTableValues = new ContentValues();
        locationTableValues.put("name",arrivalAlarm.getLocation().getLocationName());
        locationTableValues.put("lon",arrivalAlarm.getLocation().getLongitude());
        locationTableValues.put("lat",arrivalAlarm.getLocation().getLatitude());
        query = "select location_id from ARRIVAL_ALARM_TASK where arrival_alarm_id="+arrivalAlarm.getTaskId();
        cursor =  dbRead.rawQuery(query,null);
        if(cursor.moveToNext()){
            locationID =  cursor.getInt(cursor.getColumnIndex("location_id"));
        }
        result = dbWrite.update("LOCATION",locationTableValues,"location_id=?",new String[] {String.valueOf(locationID)});
        if(result<0){

            return false;
        }
        return true;
    }

    public boolean addSuccessor(ArrivalAlarm arrivalAlarm){
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        long result;
        ContentValues alarmContents = new ContentValues();
        alarmContents.put("successor_alarm_id",arrivalAlarm.getSuccessorAlarm().getTaskId());
        result = dbWrite.update("ARRIVAL_ALARM_TASK",alarmContents,"arrival_alarm_id=?",new String[] {String.valueOf(arrivalAlarm.getTaskId())});
        if(result<0){
            return  false;
        }
        return true;
    }

}
