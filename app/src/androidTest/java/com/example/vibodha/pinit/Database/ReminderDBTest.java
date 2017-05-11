package com.example.vibodha.pinit.Database;

import android.support.test.InstrumentationRegistry;

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

}