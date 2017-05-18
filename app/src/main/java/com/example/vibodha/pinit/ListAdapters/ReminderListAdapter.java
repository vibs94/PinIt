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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.vibodha.pinit.Model.Reminder;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.EditReminder;
import com.example.vibodha.pinit.View.ViewReminder;

import static com.example.vibodha.pinit.R.layout.reminder_list;


/**
 * Created by vibodha on 3/29/17.
 */

public class ReminderListAdapter extends ArrayAdapter<Reminder> {


    public ReminderListAdapter(Context context, Reminder[] reminders) {
        super(context, reminder_list, reminders);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(reminder_list, parent, false);

        final Reminder singleReminder = getItem(position);
        final Context context = getContext();
        TextView locationName = (TextView) customView.findViewById(R.id.location_name);
        TextView note = (TextView) customView.findViewById(R.id.reminder_note);
        ImageView isCompleted = (ImageView) customView.findViewById(R.id.is_completed);
        Button menuButton = (Button) customView.findViewById(R.id.menu_button);

        locationName.setText(singleReminder.getLocation().getLocationName());
        note.setText(singleReminder.getNote());

        if (singleReminder.isCompleted()) {
            isCompleted.setImageResource(R.drawable.ic_tik);
        } else {
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
                                //Toast.makeText(context,""+singleReminder.getActivities().size(),Toast.LENGTH_SHORT).show();
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