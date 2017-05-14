package com.example.vibodha.pinit.Model;

import java.util.Date;

/**
 * Created by vibodha on 3/26/17.
 */

public class Activity {

    private int id;
    private String description;
    private Date timeofCompletion=null;

    public Activity(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public void markActivity() {
        this.timeofCompletion = new Date(System.currentTimeMillis());
    }

    public void setTimeofCompletion(Date timeofCompletion) {
        this.timeofCompletion = timeofCompletion;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimeofCompletion() {
        return timeofCompletion;
    }
}
