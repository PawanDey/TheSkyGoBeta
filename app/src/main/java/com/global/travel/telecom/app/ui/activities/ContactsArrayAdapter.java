package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.ContactsModel;

import java.util.ArrayList;

public class ContactsArrayAdapter extends ArrayAdapter<ContactsModel> {

    private Context mContext;
    int mResouce;

    public ContactsArrayAdapter(Context context, int resource, ArrayList<ContactsModel> object) {
        super(context, resource, object);
        mContext = context;
        mResouce = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String mobleNumber = getItem(position).getMobileNumber();
        ContactsModel contactsModel = new ContactsModel(name, mobleNumber);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResouce, parent, false);

        TextView textView_name = convertView.findViewById(R.id.PersonName);
        TextView textView_contact = convertView.findViewById(R.id.PersonContact);

        textView_name.setText(name);
        textView_contact.setText(mobleNumber);
        return convertView;
    }
}




















