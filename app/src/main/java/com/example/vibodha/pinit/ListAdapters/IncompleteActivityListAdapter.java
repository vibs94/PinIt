package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.vibodha.pinit.Model.Activity;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.ShowReminder;

import static com.example.vibodha.pinit.R.layout.incomplete_activity_list;
import static com.example.vibodha.pinit.R.layout.reminder_list;

/**
 * Created by vibodha on 5/15/17.
 */

public class IncompleteActivityListAdapter extends ArrayAdapter<Activity> {

    public IncompleteActivityListAdapter(Context context, Activity[] activities) {
        super(context, R.layout.incomplete_activity_list,activities);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(incomplete_activity_list, parent, false);

        Activity singleActivity = getItem(position);

        CheckBox checkBox = (CheckBox) customView.findViewById(R.id.incomplete_activity_checkbox);
        TextView activityDesc = (TextView) customView.findViewById(R.id.incomplete_activity_desc);

        activityDesc.setText(singleActivity.getDescription());

        checkBox.setOnCheckedChangeListener((ShowReminder) getContext());

        return customView;
    }

}
