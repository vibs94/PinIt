package com.example.vibodha.pinit.View;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vibodha.pinit.R;

/**
 * Created by vibodha on 3/23/17.
 */

public class ReminderTab extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_tab,container,false);
        Button btnAddReminder = (Button) view.findViewById(R.id.btn_reminder);
        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addReminder = new Intent(getActivity(), AddReminder.class);
                startActivity(addReminder);
            }
        });
        return view;
    }
}
