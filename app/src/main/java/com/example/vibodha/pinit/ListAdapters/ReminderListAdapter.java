package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.AddReminder;

import static com.example.vibodha.pinit.R.layout.reminder_list;


/**
 * Created by vibodha on 3/29/17.
 */

public class ReminderListAdapter extends ArrayAdapter<Reminder> {


    public ReminderListAdapter(Context context, Reminder[] reminders) {
        super(context, reminder_list,reminders);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(reminder_list,parent,false);

        Reminder singleReminder = getItem(position);
        TextView locationName = (TextView) customView.findViewById(R.id.location_name);
        TextView note = (TextView) customView.findViewById(R.id.reminder_note);
        ImageView isCompleted = (ImageView) customView.findViewById(R.id.is_completed);
        LinearLayout linearLayout = (LinearLayout) customView.findViewById(R.id.checkbox_layout);

        locationName.setText(singleReminder.getLocation().getLocationName());
        note.setText(singleReminder.getNote());
        if(singleReminder.isCompleted()){
            isCompleted.setImageResource(R.drawable.tik);
        }
        else{
            isCompleted.setImageResource(R.drawable.cross);
        }




        return customView;
    }
}
