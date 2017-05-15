package com.example.vibodha.pinit.Model;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vibodha on 3/26/17.
 */

public class Reminder extends Task {

    private String note;
    private ArrayList<Date> listOfwakeupTimes = new ArrayList<Date>();
    private ArrayList<Activity> activities = new ArrayList<Activity>();



    public Reminder(int id, Location location, boolean isCompleted, int range, ArrayList<Activity> activities, String note) {
        super(id, location, isCompleted, range);
        this.activities = activities;
        this.note = note;
    }

    public void setListOfwakeupTimes(ArrayList<Date> listOfwakeupTimes) {
        this.listOfwakeupTimes = listOfwakeupTimes;
    }

    public void reportWakeup(){
        listOfwakeupTimes.add(new Date(System.currentTimeMillis()));
    }

    public Date getLastWakeup(){
        if(listOfwakeupTimes.size()>0) {
            return listOfwakeupTimes.get((listOfwakeupTimes.size() - 1));
        }
        else{
            return null;
        }
    }

    public int getWakeUpCount(){
        return listOfwakeupTimes.size();
    }

    public String getNote() {
        return note;
    }

    public ArrayList<Date> getListOfwakeupTimes() {
        return listOfwakeupTimes;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void completeReminder(){
        setTimeOfCompletion(new Date(System.currentTimeMillis()));
        this.setCompleted(true);
    }
}
