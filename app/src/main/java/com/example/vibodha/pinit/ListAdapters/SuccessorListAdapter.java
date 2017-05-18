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
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.R;
import com.example.vibodha.pinit.View.AddSuccessor;
import com.example.vibodha.pinit.View.ShowReminder;

import static com.example.vibodha.pinit.R.layout.incomplete_activity_list;
import static com.example.vibodha.pinit.R.layout.successor_suggestions_list;

/**
 * Created by vibodha on 5/18/17.
 */

public class SuccessorListAdapter extends ArrayAdapter<ArrivalAlarm> {

    public SuccessorListAdapter(Context context, ArrivalAlarm[] arrivalAlarms) {
        super(context, R.layout.successor_suggestions_list,arrivalAlarms);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(successor_suggestions_list, parent, false);

        ArrivalAlarm arrivalAlarm = getItem(position);

        CheckBox checkBox = (CheckBox) customView.findViewById(R.id.succ_alarm_checkbox);
        TextView activityDesc = (TextView) customView.findViewById(R.id.succ_alarm_loc);

        activityDesc.setText(arrivalAlarm.getLocation().getLocationName());

        checkBox.setOnCheckedChangeListener((AddSuccessor) getContext());

        return customView;
    }

}
