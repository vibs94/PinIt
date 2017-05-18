package com.example.vibodha.pinit.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vibodha.pinit.Model.Contact;
import com.example.vibodha.pinit.R;

import static com.example.vibodha.pinit.R.layout.contacts_list;



/**
 * Created by vibodha on 5/11/17.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> {

    public ContactListAdapter(Context context, Contact[] contacts) {
        super(context, contacts_list, contacts);
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(contacts_list, parent, false);

        final Contact singleContact = getItem(position);
        final Context context = getContext();

        TextView contactName = (TextView) customView.findViewById(R.id.con_name);
        TextView contactNo = (TextView) customView.findViewById(R.id.con_no);

        contactName.setText(singleContact.getName());
        contactNo.setText(singleContact.getPhoneNumber());

        return customView;
    }
}
