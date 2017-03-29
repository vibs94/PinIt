package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by vibodha on 3/29/17.
 */

public class NewActivityListAdapter extends ArrayAdapter<String> {


    public NewActivityListAdapter(Context context, String[] objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);

        return customView;
    }
}
