package com.example.vibodha.pinit.View;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vibodha.pinit.R;

/**
 * Created by vibodha on 3/23/17.
 */

public class ArrivalAlarmTab extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.arrival_alarm_tab, container, false);

    }

}
