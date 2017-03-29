package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.vibodha.pinit.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class TestActivity extends AppCompatActivity implements TaskDialog.TaskDialogListner{

    private static final int REQUEST_CODE_PLACEPICKER = 1 ;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void testButtonAction(View view){
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

        try{
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent,REQUEST_CODE_PLACEPICKER);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int reqestCode, int resultCode, Intent data){
        if(reqestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK){
//            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//            View mView = getLayoutInflater().inflate(R.layout.task_dialog,null);
//            mBuilder.setView(mView);
//            AlertDialog dialog = mBuilder.create();
//            dialog.show();
            FragmentManager fragmentManager = getSupportFragmentManager();
            TaskDialog taskDialog = new TaskDialog();
            taskDialog.show(fragmentManager,"");
            place = PlacePicker.getPlace(data,this);


        }
    }

    @Override
    public void dothis() {
        Intent addReminder = new Intent(this, AddReminder.class);
        addReminder.putExtra("placeName",place.getName().toString());
        addReminder.putExtra("placeLat",Double.toString(place.getLatLng().latitude));
        addReminder.putExtra("placeLon",Double.toString(place.getLatLng().longitude));
        startActivity(addReminder);
    }

}
