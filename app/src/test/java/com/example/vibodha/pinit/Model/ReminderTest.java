package com.example.vibodha.pinit.Model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vibodha on 5/6/17.
 */
public class ReminderTest {
    @Test
    public void reportWakeup() throws Exception {
        String note = "test";
        Reminder reminder = new Reminder(1,null,false,10,null,note);
        reminder.reportWakeup();
        assertEquals(1,reminder.getWakeUpCount());
        assertEquals(note,reminder.getNote());
    }
    @Test
    public void getNote() throws Exception {
        String note = "test";
        Reminder reminder = new Reminder(1,null,false,10,null,note);
        assertEquals(note,reminder.getNote());
    }

}