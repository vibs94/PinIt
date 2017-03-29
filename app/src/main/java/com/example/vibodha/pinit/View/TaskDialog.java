package com.example.vibodha.pinit.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vibodha.pinit.R;

/**
 * Created by vibodha on 3/28/17.
 */

public class TaskDialog extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_dialod,container,false) ;

        Button mbtnReminder = (Button) view.findViewById(R.id.btn_reminder);
        mbtnReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){

            }
        });

        return view;

    }

}
