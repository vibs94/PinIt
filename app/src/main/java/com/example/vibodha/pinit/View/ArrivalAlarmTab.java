package com.example.vibodha.pinit.View;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.ListAdapters.AlarmListAdapter;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by vibodha on 3/23/17.
 */

public class ArrivalAlarmTab extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.arrival_alarm_tab,container,false);
        ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(this.getContext());
        try {
            // create alarm list
            ArrayList<ArrivalAlarm> alarmArrayList = arrivalAlarmDB.getArrivalAlarms();
            ArrivalAlarm[] alarms = alarmArrayList.toArray(new ArrivalAlarm[alarmArrayList.size()]);
            ListAdapter alarmListAdapter = new AlarmListAdapter(this.getContext(),alarms);
            ListView alarmListView = (ListView) view.findViewById(R.id.allalarm_list);
            alarmListView.setAdapter(alarmListAdapter);

        } catch (ParseException e) {
            e.printStackTrace();
        }



        return view;

    }

}
