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
import com.global.travel.telecom.app.model.RecentSetDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecentCallHistoryArrayAdapter extends ArrayAdapter<RecentSetDataModel> {

    private Context mContext;
    int mResouce;
    String inputPattern = "yyyy-MM-dd HH:mm:ss";
    String outputPattern = "dd-MMM-yyyy h:mm a";
    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
    Date date = null;

    public RecentCallHistoryArrayAdapter(Context context, int resource, ArrayList<RecentSetDataModel> object) {
        super(context, resource, object);
        mContext = context;
        mResouce = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String CreateTime = getItem(position).getCreate_time();
        String Duration = getItem(position).getDuration();
        String Leg2 = getItem(position).getLeg2();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResouce, parent, false);

        TextView leg2 = convertView.findViewById(R.id.leg2);
        TextView createTime = convertView.findViewById(R.id.createTime);
        TextView duration = convertView.findViewById(R.id.duration);

        leg2.setText(Leg2);
        duration.setText("Call duration: " + Duration);

        try {
            CreateTime = CreateTime.trim().substring(0, CreateTime.length() - 1);
            date = inputFormat.parse(CreateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CreateTime = outputFormat.format(date);

        createTime.setText(CreateTime);

        return convertView;
    }
}





















