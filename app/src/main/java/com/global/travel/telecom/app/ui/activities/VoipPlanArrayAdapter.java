package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.GetVoipPlanModel;

import java.util.ArrayList;

public class VoipPlanArrayAdapter extends ArrayAdapter<GetVoipPlanModel> {

    private Context mContext;
    int mResouce;

    public VoipPlanArrayAdapter(Context context, int resource, ArrayList<GetVoipPlanModel> object) {
        super(context, resource, object);
        mContext = context;
        mResouce = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String PlanName = getItem(position).getPlanName();
        String PlanDetails = getItem(position).getPlanDetails();
        String AmountCharge = getItem(position).getAmountCharge();
        String PlanMin = getItem(position).getPlanMin();
        String Validity = getItem(position).getValidity();
        String MonikerValue = getItem(position).getMonikerValue();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResouce, parent, false);

        TextView text_PlanName = convertView.findViewById(R.id.menu_plan_name);
        TextView text_PlanDetails = convertView.findViewById(R.id.menu_plan_Details);
        TextView menu_validityWithMin = convertView.findViewById(R.id.menu_validityWithMin);
        TextView text_plan_price = convertView.findViewById(R.id.plan_price);

        text_PlanName.setText(PlanName);
        text_PlanDetails.setText(PlanDetails);
        menu_validityWithMin.setText("Validity "+Validity+" Days ("+ PlanMin+"mins)");
        text_plan_price.setText("$"+AmountCharge);
        return convertView;
    }
}





















