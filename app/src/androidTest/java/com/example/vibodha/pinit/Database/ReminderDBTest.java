package com.example.vibodha.pinit.Database;

import android.support.test.InstrumentationRegistry;

import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.Model.Reminder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vibodha on 5/6/17.
 */
public class ReminderDBTest {
    @Test
    public void getNextReminderID() throws Exception {
        ReminderDB reminderDB = ReminderDB.getInstance(InstrumentationRegistry.getTargetContext());
        int id = reminderDB.getNextReminderID();
        assertEquals(1,id);


    }
    ReminderDB reminderDB = ReminderDB.getInstance(InstrumentationRegistry.getTargetContext());
    Location location = new Location("myPlace",79.9,6.8);
    Reminder reminder = new Reminder(100,location,false,1,null,"note");
    Reminder reminderFromDB;
    @Test
    public void addReminder() throws Exception {

        reminderDB.addReminder(reminder);
        reminderFromDB = reminderDB.getReminder(100);

        assertEquals(location.getLocationName(),reminderFromDB.getLocation().getLocationName());
        assertEquals(location.getLatitude(),reminderFromDB.getLocation().getLatitude());
        assertEquals(location.getLongitude(),reminderFromDB.getLocation().getLongitude());
        assertEquals(false,reminderFromDB.isCompleted());
        assertEquals("note",reminderFromDB.getNote());
        assertEquals(1,reminderFromDB.getRange());
    }

    @Test
    public void editReminder() throws Exception {
        location = new Location("myplace2",1.0,2.0);
        reminder = new Reminder(100,location,false,2,null,"note2");
        reminderDB.editReminder(reminder);
        reminderFromDB = reminderDB.getReminder(100);

        assertEquals(location.getLocationName(),reminderFromDB.getLocation().getLocationName());
        assertEquals(location.getLatitude(),reminderFromDB.getLocation().getLatitude());
        assertEquals(location.getLongitude(),reminderFromDB.getLocation().getLongitude());
        assertEquals(true,reminderFromDB.isCompleted());
        assertEquals("note2",reminderFromDB.getNote());
        assertEquals(2,reminderFromDB.getRange());

    }


}