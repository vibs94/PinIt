package com.example.vibodha.pinit.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vibodha.pinit.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends AppCompatActivity implements TaskDialog.TaskDialogListner{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private final int REQUEST_CODE_PLACEPICKER = 1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Place place;

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent map = new Intent(MainActivity.this,MapsActivity.class);
//                startActivity(map);

                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                try{
                    Intent intent = intentBuilder.build(MainActivity.this);
                    startActivityForResult(intent,REQUEST_CODE_PLACEPICKER);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        // Set up navigation bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int reqestCode, int resultCode, Intent data){
        if(reqestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK){
//            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//            View mView = getLayoutInflater().inflate(R.layout.task_dialog,null);
//            mBuilder.setView(mView);
//            AlertDialog dialog = mBuilder.create();
//            dialog.show();
//            TaskDialog taskDialog = new TaskDialog();
//            taskDialog.show(fragmentManager,"TaskDialog");
            place = PlacePicker.getPlace(data,this);
            new AlertDialog.Builder(this)
                    .setTitle("PinIt")
                    .setMessage("What task do you want?")
                    .setPositiveButton("Reminder", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showAddReminder();
                        }
                    })
                    .setNegativeButton("Arrival Alarm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           // Toast.makeText(MainActivity.this,"N",Toast.LENGTH_SHORT).show();
                            showAddArrivalAlarm();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }
     }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //Action button in tool bar will navigate to the navigation bar
        else if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showAddReminder() {
        try {
            Intent addReminder = new Intent(this, AddReminder.class);
            addReminder.putExtra("placeName", place.getName().toString() + " " + place.getAddress().toString());
            addReminder.putExtra("placeLat", Double.toString(place.getLatLng().latitude));
            addReminder.putExtra("placeLon", Double.toString(place.getLatLng().longitude));
            startActivity(addReminder);
        }
        catch (NullPointerException e){
            Toast.makeText(MainActivity.this,"GPS not Functioning!",Toast.LENGTH_SHORT).show();
            Intent main = new Intent(this,MainActivity.class);
            startActivity(main);
        }
    }

    public void showAddArrivalAlarm(){
        try {
            Intent addArrivalAlarm = new Intent(this, AddArrivalAlarm.class);
            addArrivalAlarm.putExtra("placeName", place.getName().toString() + " " + place.getAddress().toString());
            addArrivalAlarm.putExtra("placeLat", Double.toString(place.getLatLng().latitude));
            addArrivalAlarm.putExtra("placeLon", Double.toString(place.getLatLng().longitude));
            startActivity(addArrivalAlarm);
        }
        catch (NullPointerException e){
            Toast.makeText(MainActivity.this,"GPS not Functioning!",Toast.LENGTH_SHORT).show();
            Intent main = new Intent(this,MainActivity.class);
            startActivity(main);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    ReminderTab reminderTab = new ReminderTab();
                    return reminderTab;
                case 1:
                    ArrivalAlarmTab arrivalAlarmTab= new ArrivalAlarmTab();
                    return arrivalAlarmTab;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Reminder";
                case 1:
                    return "Arrival Alarm";
            }
            return null;
        }
    }
}
