package com.example.vibodha.pinit.View;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vibodha.pinit.Constants;
import com.example.vibodha.pinit.Database.ArrivalAlarmDB;
import com.example.vibodha.pinit.ListAdapters.ContactListAdapter;
import com.example.vibodha.pinit.Model.ArrivalAlarm;
import com.example.vibodha.pinit.Model.Contact;
import com.example.vibodha.pinit.Model.Location;
import com.example.vibodha.pinit.R;

import java.util.ArrayList;

public class AddArrivalAlarm extends AppCompatActivity {
    //private static final int RESULT_PICK_CONTACT = 1;
    final Contact[] contact = new Contact[1];
    final ArrayList<Contact> contacts = new ArrayList<Contact>();
    TextView newContactname;
    TextView newContactno;


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

        Button btnAddContact = (Button) findViewById(R.id.btn_add_contact);
        newContactname = (TextView) findViewById(R.id.txt_contactname);
        newContactno = (TextView) findViewById(R.id.txt_contactno);

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
                    int range = 1;

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
    public void pickContact(View v)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                    Toast.makeText(AddArrivalAlarm.this,"contact picked",Toast.LENGTH_SHORT).show();
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the list
            newContactname.setText(name);
            newContactno.setText(phoneNo);
        } catch (Exception e) {
            Toast.makeText(AddArrivalAlarm.this,"ex",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
