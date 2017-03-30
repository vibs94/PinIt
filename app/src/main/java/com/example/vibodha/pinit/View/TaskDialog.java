package com.example.vibodha.pinit.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vibodha.pinit.R;

/**
 * Created by vibodha on 3/28/17.
 */

public class TaskDialog extends DialogFragment{

    TaskDialogListner taskDialogListner;

    public interface TaskDialogListner{
        void showAddReminder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_dialog,container,false) ;

        Log.e("weda","dddddd");


        Button mbtnReminder = (Button) view.findViewById(R.id.button4);

        mbtnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText((Context) taskDialogListner,"clicked",Toast.LENGTH_SHORT).show();
                Log.e("gh","fgfhfhfhfh");
                taskDialogListner.showAddReminder();
            }
        });


        return view;

    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        try {
            taskDialogListner = (TaskDialogListner) activity;
        }
        catch (Exception e){

        }
    }
}
