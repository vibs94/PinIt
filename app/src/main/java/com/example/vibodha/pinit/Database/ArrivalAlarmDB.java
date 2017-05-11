package com.example.vibodha.pinit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Contact;

import java.util.ArrayList;

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

}
