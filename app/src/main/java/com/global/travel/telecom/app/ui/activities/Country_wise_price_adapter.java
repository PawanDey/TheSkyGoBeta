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

import java.util.ArrayList;

public class Country_wise_price_adapter extends ArrayAdapter<Country_wise_rate_list> {
    public Country_wise_price_adapter(Context context, ArrayList<Country_wise_rate_list> countryItems) {
        super(context, 0, countryItems);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.snipper_country_wise_price, parent, false
            );
        }
        TextView countryName = convertView.findViewById(R.id.country_name);
        Country_wise_rate_list country_wise_rateList = getItem(position);

        countryName.setText(country_wise_rateList.getmCountryName());
        return convertView;
    }
}