package com.example.vibodha.pinit.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.ListAdapters.AlarmListAdapter;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

public class AddSuccessor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_successor);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.9),(int) (height*0.9));

        ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(this);
        int id = getIntent().getIntExtra("id",-1);
        try {
            // create alarm list
            ArrayList<ArrivalAlarm> alarmArrayList = arrivalAlarmDB.getArrivalAlarms();
            ArrayList<ArrivalAlarm> successors = new ArrayList<>();
            for(int i=0;i<alarmArrayList.size();i++){
                if(alarmArrayList.get(i).getTaskId()!=id&&!alarmArrayList.get(i).isCompleted()){
                    successors.add(alarmArrayList.get(i));
                }
            }
            ArrivalAlarm[] alarms = successors.toArray(new ArrivalAlarm[successors.size()]);
            ListAdapter alarmListAdapter = new AlarmListAdapter(this,alarms);
            ListView alarmListView = (ListView) findViewById(R.id.successor_list);
            alarmListView.setAdapter(alarmListAdapter);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
