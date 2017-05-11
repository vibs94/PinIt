package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.ListAdapters.ContactListAdapter;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Contact;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.R;

import java.util.ArrayList;

public class AddArrivalAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_arrival_alarm);
        ////////////// initialize activity ///////////////////
        EditText txtName = (EditText) findViewById(R.id.txt_alarmlocationName);
        EditText txtLat = (EditText) findViewById(R.id.txt_alarmlat);
        EditText txtLon = (EditText) findViewById(R.id.txt_alarmlon);
        txtName.setText(getIntent().getStringExtra("placeName"));
        txtLat.setText(getIntent().getStringExtra("placeLat"));
        txtLon.setText(getIntent().getStringExtra("placeLon"));
        ////////////////// add new contact //////////////////
        final Contact[] contact = new Contact[1];
        final ArrayList<Contact> contacts = new ArrayList<Contact>();
        Button btnAddContact = (Button) findViewById(R.id.btn_add_contact);
        final EditText newContactname = (EditText) findViewById(R.id.txt_contactname);
        final EditText newContactno = (EditText) findViewById(R.id.txt_contactno);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newContactno.getText().toString().trim().equals("") || newContactno.getText().toString().trim().equals(null) || newContactname.getText().toString().trim().equals("") || newContactname.getText().toString().trim().equals(null)) {

                    Toast.makeText(AddArrivalAlarm.this, "Contact name or number can not be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!checkNumber(newContactno.getText().toString())){
                    Toast.makeText(AddArrivalAlarm.this, "Contact number format is wrong!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddArrivalAlarm.this, "Contact added.", Toast.LENGTH_SHORT).show();
                    contact[0] = new Contact(newContactname.getText().toString(),newContactno.getText().toString());
                    contacts.add(contact[0]);
                    ListView listView = (ListView) findViewById(R.id.contact_list);
                    Contact[] contacts1 = contacts.toArray(new Contact[contacts.size()]);
                    ListAdapter contactListAdapter = new ContactListAdapter(AddArrivalAlarm.this,contacts1);
                    listView.setAdapter(contactListAdapter);
                    newContactname.setText("");
                    newContactno.setText("");
                }
            }
        });

        /////////////////// add alarm /////////////////
        Button btnAddAlarm = (Button) findViewById(R.id.btn_add_alarm);
        final ArrivalAlarmDB arrivalAlarmDB = ArrivalAlarmDB.getInstance(this);

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrivalAlarm arrivalAlarm;
                Location location;
                // set contact
                String locationName = ((EditText) findViewById(R.id.txt_alarmlocationName)).getText().toString();
                try {
                    double locationLon = Double.parseDouble(((EditText) findViewById(R.id.txt_alarmlon)).getText().toString().trim());
                    double locationLat = Double.parseDouble(((EditText) findViewById(R.id.txt_alarmlat)).getText().toString().trim());
                    location = new Location(locationName, locationLon, locationLat);

                    //set range
                    int range = 100;

                        // set Arrival Alarm
                        int arrivalAlarmID = arrivalAlarmDB.getNextArrivalAlarmID();

                        arrivalAlarm = new ArrivalAlarm(arrivalAlarmID,location, false, range, contacts);
                        if (arrivalAlarmDB.addArrivalAlarm(arrivalAlarm)) {
                            Toast.makeText(AddArrivalAlarm.this, "Arrival Alarm added successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddArrivalAlarm.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddArrivalAlarm.this, "Unsuccessful!!!", Toast.LENGTH_SHORT).show();
                        }

                }
                catch (NumberFormatException e){
                    Toast.makeText(AddArrivalAlarm.this,"Longitude and Latitude should be decimal values!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean checkNumber(String num){
        if(num.length()==9 && num.charAt(0)=='0'){
            return false;
        }
        else if(num.length()==12 && num.charAt(0)!='+'){
            return false;
        }
        else if(num.length()==10 && num.charAt(0)!='0'){
            return false;
        }
        else if(num.length()>12 || num.length()<9){
            return false;
        }
        else {
            return true;
        }
    }
}
