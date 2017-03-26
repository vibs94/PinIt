package com.example.vibodha.pinit.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vibodha on 3/26/17.
 */

public class Task {

    private int id;
    private Location location;
    private boolean isCompleted=false;
    private Date timeOfCompletion=null;
    private int range;



    public Task(int id, Location location, boolean isCompleted, int range) {
        this.id = id;
        this.location = location;
        this.isCompleted = isCompleted;
        this.range = range;
    }

    public void setTimeOfCompletion(Date timeOfCompletion) {
        this.timeOfCompletion = timeOfCompletion;
    }

    public int getTaskId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public Date getTimeOfCompletion() {
        return timeOfCompletion;
    }

    public int getRange() {
        return range;
    }
}
