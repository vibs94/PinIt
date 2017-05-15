package com.example.vibodha.pinit.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vibodha on 3/26/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String databaseName = "pinit_db";
    private static DatabaseHelper dbHelper = null;

    private DatabaseHelper(Context context) {

        super(context, databaseName, null, 1);
    }
    //singleton
    public static DatabaseHelper getInstance(Context context){
        if(dbHelper==null){
            dbHelper = new DatabaseHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table PRIORITY(priority_id integer primary key autoincrement, range integer);");
        db.execSQL("create table LOCATION(location_id integer primary key autoincrement, name varchar(30), lon float, lat float);");
        db.execSQL("create table TIME(time_id integer primary key autoincrement,datee date,hour integer,min integer);");
        db.execSQL("create table CONTACT(contact_id integer primary key autoincrement,arrival_alarm_id integer, name varchar(30), phone_number varchar(15), " +
                "foreign key(arrival_alarm_id) references arrival_alarm_task(arrival_alarm_id)); ");
        db.execSQL("create table ARRIVAL_ALARM_TASK(arrival_alarm_id integer primary key, location_id integer, time_id_of_completion integer, " +
                "successor_alarm_id integer,is_wakeup integer, range integer," +
                "foreign key(location_id) references location(location_id)," +
                "foreign key(time_id_of_completion) references time(time_id)," +
                "foreign key(successor_alarm_id) references arrival_alarm_task(arrival_alarm_id));");
        db.execSQL("create table REMINDER_TASK(reminder_id integer primary key, location_id integer, time_id_of_completion integer, priority_id integer, " +
                "is_completed integer, note varchar(100)," +
                "foreign key(location_id) references location(location_id)," +
                "foreign key(time_id_of_completion) references time(time_id)," +
                "foreign key(priority_id) references priority(priority_id));");
        db.execSQL("create table ACTIVITY(activity_id integer primary key, reminder_id integer, time_id_of_completion integer, description varchar(100)," +
                "foreign key(reminder_id) references reminder_task(reminder_id)," +
                "foreign key(time_id_of_completion) references time(time_id));");
        db.execSQL("create table WAKEUPS(reminder_id integer, time_id_of_wakeup integer," +
                "foreign key(reminder_id) references reminder_task(reminder_id),"+
                "foreign key(time_id_of_wakeup) references time(time_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists PRIORITY;");
        db.execSQL("drop table if exists LOCATION;");
        db.execSQL("drop table if exists TIME;");
        db.execSQL("drop table if exists CONTACT;");
        db.execSQL("drop table if exists ARRIVAL_ALARM_TASK;");
        db.execSQL("drop table if exists REMINDER_TASK;");
        db.execSQL("drop table if exists ACTIVITY;");
        db.execSQL("drop table if exists WAKEUPS;");
        onCreate(db);
    }
}
