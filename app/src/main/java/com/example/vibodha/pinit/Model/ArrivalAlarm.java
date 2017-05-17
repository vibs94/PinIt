package com.example.vibodha.pinit.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vibodha on 3/26/17.
 */

public class ArrivalAlarm extends Task{

    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private ArrivalAlarm successorAlarm=null;

    public ArrivalAlarm(int id, Location location, boolean isCompleted, int range, ArrayList<Contact> contacts) {
        super(id, location, isCompleted, range);
        this.contacts = contacts;
    }

    public void setSuccessorAlarm(ArrivalAlarm successorAlarm) {
        this.successorAlarm = successorAlarm;
    }

    public void markAlarm(){
        this.setCompleted(true);
        this.setTimeOfCompletion(new Date(System.currentTimeMillis()));
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ArrivalAlarm getSuccessorAlarm() {
        return successorAlarm;
    }

    public double getDistanceToSuccAlarm(){
        double dis;
        double val1 = Math.pow(getLocation().getLongitude()-successorAlarm.getLocation().getLongitude(),2) + Math.pow(getLocation().getLatitude()-successorAlarm.getLocation().getLatitude(),2);
        dis = Math.pow(val1,0.5);
        return dis;
    }
}
