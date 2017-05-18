package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.ListAdapters.SuccessorListAdapter;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.R;

import java.text.ParseException;
import java.util.ArrayList;

public class AddSuccessor extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener{

    ListView alarmListView;
    int  pos= -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_successor);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.9),(int) (height*0.9));

        final ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(this);
        final int id = getIntent().getIntExtra("id",-1);
        final ArrayList<ArrivalAlarm> successors = new ArrayList<>();
        Button addSucce = (Button) findViewById(R.id.btn_add_succ);
        TextView message = (TextView) findViewById(R.id.add_succ_message);
        try {
            // create alarm list
            ArrayList<ArrivalAlarm> alarmArrayList = arrivalAlarmDB.getArrivalAlarms();

            for(int i=0;i<alarmArrayList.size();i++){
                if(alarmArrayList.get(i).getTaskId()!=id&&!alarmArrayList.get(i).isCompleted()){
                    successors.add(alarmArrayList.get(i));
                }
            }
            if(successors.size()>0) {
                ArrivalAlarm[] alarms = successors.toArray(new ArrivalAlarm[successors.size()]);
                ListAdapter alarmListAdapter = new SuccessorListAdapter(this, alarms);
                alarmListView = (ListView) findViewById(R.id.successor_list);
                alarmListView.setAdapter(alarmListAdapter);
            }
            else{

                message.setText("No options for successor");
                addSucce.setVisibility(View.GONE);
                //Toast.makeText(AddSuccessor.this,"No options for successor",Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        addSucce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrivalAlarm alarm = arrivalAlarmDB.getArrivalAlarm(id);
                    alarm.setSuccessorAlarm(successors.get(pos));
                    arrivalAlarmDB.addSuccessor(alarm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(AddSuccessor.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //Log.e("listner in","");
        if(isChecked && pos!=-1){
            Toast.makeText(AddSuccessor.this,"Cannot add two successors!!!",Toast.LENGTH_SHORT).show();
        }
        else if(!isChecked && pos==alarmListView.getPositionForView(buttonView)){
            pos=-1;
            Log.e("pos",String.valueOf(pos));
        }
        else if(isChecked && pos==-1) {
            pos = alarmListView.getPositionForView(buttonView);
            Log.e("pos",String.valueOf(pos));
        }

    }

}
