package com.example.vibodha.pinit.Model;

import java.util.ArrayList;

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

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ArrivalAlarm getSuccessorAlarm() {
        return successorAlarm;
    }
}
