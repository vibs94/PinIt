package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.EditAlarm;
import com.example.vibodha.pinit.View.ViewArrivalAlarm;
import com.example.vibodha.pinit.View.ViewReminder;

import static com.example.vibodha.pinit.R.layout.alarm_list;
import static com.example.vibodha.pinit.R.layout.reminder_list;

/**
 * Created by vibodha on 5/12/17.
 */

public class AlarmListAdapter extends ArrayAdapter<ArrivalAlarm> {


    public AlarmListAdapter(Context context, ArrivalAlarm[] arrivalAlarms) {
        super(context, alarm_list, arrivalAlarms);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(alarm_list, parent, false);

        final ArrivalAlarm singleAlarm = getItem(position);
        final Context context = getContext();
        TextView alarmLocationName = (TextView) customView.findViewById(R.id.location_name_alarm);
        ImageView isCompleted = (ImageView) customView.findViewById(R.id.is_completed_alarm);
        Button menuButton = (Button) customView.findViewById(R.id.menu_button_alarm);

        alarmLocationName.setText(singleAlarm.getLocation().getLocationName());

        if(singleAlarm.isCompleted()){
            isCompleted.setImageResource(R.drawable.ic_tik);
        }
        else{
            isCompleted.setImageResource(R.drawable.ic_cross);
        }
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu=new PopupMenu(customView.getContext(),customView);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(context,""+singleReminder.getActivities().size(),Toast.LENGTH_SHORT).show();
                        int id=item.getItemId();
                        switch (id){
                            case R.id.view_id:
                                // implementation for view arrival alarm
                                Intent inte=new Intent(context, ViewArrivalAlarm.class);
                                inte.putExtra("id",singleAlarm.getTaskId());
                                context.startActivity(inte);
                                break;
                            case R.id.edit_id:
                                // implementation for edit arrival alarm
                                Intent inten = new Intent(context, EditAlarm.class);
                                inten.putExtra("id",singleAlarm.getTaskId());
                                context.startActivity(inten);
                                break;
                            case R.id.select_succ:
                                //implement for select successor alarm
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.alarm_list_menu);
                popupMenu.show();
            }

        });

        //TextView id = (TextView) customView.findViewById(R.id.task_id);

        return customView;
    }
}
