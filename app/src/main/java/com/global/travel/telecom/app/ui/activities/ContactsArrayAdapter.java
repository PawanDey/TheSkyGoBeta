package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.ContactsModel;

import java.util.ArrayList;

public class ContactsArrayAdapter extends ArrayAdapter<ContactsModel> implements Filterable {

    private Context mContext;
    private int mResouce;
    private ArrayList<ContactsModel> contactListValues = null;
    private ItemFilter mFilter = new ItemFilter();

    ContactsArrayAdapter(Context context, int resource, ArrayList<ContactsModel> object) {
        super(context, resource, object);
        mContext = context;
        contactListValues = object;
        mResouce = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = contactListValues.get(position).getName();
        String mobleNumber = contactListValues.get(position).getMobileNumber();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResouce, parent, false);

        TextView textView_name = convertView.findViewById(R.id.PersonName);
        TextView textView_contact = convertView.findViewById(R.id.PersonContact);

        textView_name.setText(name);
        textView_contact.setText(mobleNumber);

        return convertView;
    }


    @Override
    public int getCount() {
        return contactListValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<ContactsModel> list = SkyGoDialer.mobileArray;

            int count = list.size();
            final ArrayList<ContactsModel> nlist = new ArrayList<ContactsModel>(count);

            String filterableName;

            for (int i = 0; i < count; i++) {
                filterableName = list.get(i).name;

                if (filterableName.toLowerCase().contains(filterString)) {
                    ContactsModel info = new ContactsModel(filterableName, list.get(i).mobileNumber);
                    nlist.add(info);
                }
            }

            results.values = nlist;
            results.count = nlist.size();


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactListValues = (ArrayList<ContactsModel>) results.values;
            notifyDataSetChanged();

        }
    }
}






















