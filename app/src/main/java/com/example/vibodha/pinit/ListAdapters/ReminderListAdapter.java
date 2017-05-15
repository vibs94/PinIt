package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.AddReminder;
import com.example.vibodha.pinit.View.EditReminder;
import com.example.vibodha.pinit.View.ViewReminder;

import java.util.ArrayList;

import static com.example.vibodha.pinit.R.layout.activity_list;
import static com.example.vibodha.pinit.R.layout.reminder_list;


/**
 * Created by vibodha on 3/29/17.
 */

public class ReminderListAdapter extends ArrayAdapter<Reminder> {


    public ReminderListAdapter(Context context, Reminder[] reminders) {
        super(context, reminder_list, reminders);
    }

    LinearLayout checkBoxLayout;
    CheckBox checkBox;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(reminder_list, parent, false);

        final Reminder singleReminder = getItem(position);
        final Context context = getContext();
        TextView locationName = (TextView) customView.findViewById(R.id.location_name);
        TextView note = (TextView) customView.findViewById(R.id.reminder_note);
        //TextView id = (TextView) customView.findViewById(R.id.task_id);
        ImageView isCompleted = (ImageView) customView.findViewById(R.id.is_completed);
        Button menuButton = (Button) customView.findViewById(R.id.menu_button);
        //PopupMenu popupMenu = (android.support.v7.widget.MenuPopupWindow.MenuDropDownListView) customView.findViewById(R.id)
        //final LinearLayout linearLayout = (LinearLayout) customView.findViewById(R.id.checkbox_layout);

        locationName.setText(singleReminder.getLocation().getLocationName());
        note.setText(singleReminder.getNote());
        //id.setText(singleReminder.getTaskId());
        if (singleReminder.isCompleted()) {
            isCompleted.setImageResource(R.drawable.ic_tik);
        } else {
            isCompleted.setImageResource(R.drawable.ic_cross);
        }
        Toast.makeText(context,""+position+" "+singleReminder.getActivities().size(),Toast.LENGTH_SHORT).show();
        /*final ArrayList<Activity> activities = singleReminder.getActivities();

        checkBoxLayout = (LinearLayout) customView.findViewById(R.id.list_layout);

        for(int i=0;i<activities.size();i++){
            checkBox = new CheckBox(customView.getContext());
            checkBox.setText(activities.get(i).getDescription());
            if(activities.get(i).getTimeofCompletion()!=null){
                checkBox.setChecked(true);
            }
            checkBoxLayout.addView(checkBox);
        }*/
        /*Activity[] activityArray = activities.toArray(new Activity[activities.size()]);
        Log.w("act_co",""+activityArray.length);
        ListAdapter activityListAdapter = new ActivityListAdapter(this.getContext(),activityArray);
        final ListView activityListView = (ListView) customView.findViewById(R.id.reminder_activity_list);
        activityListView.setAdapter(activityListAdapter);
        final LinearLayout listLayout = (LinearLayout) customView.findViewById(R.id.list_layout) ;
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Clickbefore",""+activityListView.getVisibility());
                if(checkBoxLayout.getVisibility()==View.GONE) {
                    checkBoxLayout.setVisibility(View.VISIBLE);
                }
                else{
                    checkBoxLayout.setVisibility(View.GONE);
                }
                Log.w("Clickafter",""+activityListView.getVisibility());
            }
        });*/


//        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.w("Clickbefore",""+activityListView.getVisibility());
//                if(checkBoxLayout.getVisibility()==View.GONE) {
//                    checkBoxLayout.setVisibility(View.VISIBLE);
//                }
//                else{
//                    checkBoxLayout.setVisibility(View.GONE);
//                }
//                Log.w("Clickafter",""+activityListView.getVisibility());
//            }
//
//        });

        //      More button functionality
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
                                Toast.makeText(context,""+singleReminder.getActivities().size(),Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(context, ViewReminder.class);
                                i.putExtra("id",singleReminder.getTaskId());
                                context.startActivity(i);
                                break;
                            case R.id.edit_id:
                                // implementation for edit reminder
                                Intent in = new Intent(context, EditReminder.class);
                                in.putExtra("id",singleReminder.getTaskId());
                                context.startActivity(in);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.reminder_listitem_menu);
                popupMenu.show();
            }

        });

        return customView;
    }

}