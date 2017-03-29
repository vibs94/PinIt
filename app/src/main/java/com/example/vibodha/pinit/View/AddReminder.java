package com.example.vibodha.pinit.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.R;

import java.util.ArrayList;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Button btnAddActivity = (Button) findViewById(R.id.btn_add_activity);
        final EditText newActivity = (EditText) findViewById(R.id.txt_activity);
        final ArrayList<String> newActivities = new ArrayList<String>();

        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newActivity.getText().toString().trim().equals("") && !newActivity.getText().toString().trim().equals(null)) {
                    Toast.makeText(AddReminder.this, "Activity added.", Toast.LENGTH_SHORT).show();
                    newActivities.add(newActivity.getText().toString());
                    ListView listView = (ListView) findViewById(R.id.activity_list);
                    ListAdapter listAdapter = new ArrayAdapter<String>(AddReminder.this, android.R.layout.simple_list_item_1, newActivities.toArray(new String[newActivities.size()]));
                    listView.setAdapter(listAdapter);
                    newActivity.setText("");
                }
                else{
                    Toast.makeText(AddReminder.this, "Activity can not be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
