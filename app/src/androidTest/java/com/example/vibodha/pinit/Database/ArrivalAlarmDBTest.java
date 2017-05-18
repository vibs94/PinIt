package com.example.vibodha.pinit.Database;

import android.support.test.InstrumentationRegistry;

import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Location;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vibodha on 5/18/17.
 */
public class ArrivalAlarmDBTest {

    ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(InstrumentationRegistry.getTargetContext());
    ArrivalAlarm arrivalAlarm;
    ArrivalAlarm arrivalAlarmFromDB;
    Location location;

    @Test
    public void addArrivalAlarm() throws Exception {
        location = new Location("myPlace",1.0,2.0);
        arrivalAlarm = new ArrivalAlarm(10,location,false,1,null);
        arrivalAlarmDB.addArrivalAlarm(arrivalAlarm);

        arrivalAlarmFromDB = arrivalAlarmDB.getArrivalAlarm(10);

        assertEquals("myPlace",arrivalAlarmFromDB.getLocation().getLocationName());
        assertEquals(1.0,arrivalAlarmFromDB.getLocation().getLongitude());
        assertEquals(2.0,arrivalAlarmFromDB.getLocation().getLatitude());
        assertEquals(false,arrivalAlarmFromDB.isCompleted());
        assertEquals(1,arrivalAlarmFromDB.getRange());

    }

    @Test
    public void getNextArrivalAlarmID() throws Exception {
        int i = arrivalAlarmDB.getNextArrivalAlarmID();
        assertEquals(11,i);
    }

    @Test
    public void getCurrentTimeID() throws Exception {

    }

    @Test
    public void editAlarm() throws Exception {
        location = new Location("myPlace2",5.0,6.0);
        arrivalAlarm = new ArrivalAlarm(10,location,false,1,null);

        arrivalAlarmFromDB = arrivalAlarmDB.getArrivalAlarm(10);

        assertEquals("myPlace2",arrivalAlarmFromDB.getLocation().getLocationName());
        assertEquals(5.0,arrivalAlarmFromDB.getLocation().getLongitude());
        assertEquals(6.0,arrivalAlarmFromDB.getLocation().getLatitude());

    }

    @Test
    public void addSuccessor() throws Exception {

        ArrivalAlarm successorAlarm = new ArrivalAlarm(5,location,false,1,null);
        arrivalAlarm.setSuccessorAlarm(successorAlarm);

        arrivalAlarmDB.addSuccessor(arrivalAlarm);

        arrivalAlarmFromDB=arrivalAlarmDB.getArrivalAlarm(10);

        assertEquals(5,arrivalAlarmFromDB.getSuccessorAlarm().getTaskId());

    }

}