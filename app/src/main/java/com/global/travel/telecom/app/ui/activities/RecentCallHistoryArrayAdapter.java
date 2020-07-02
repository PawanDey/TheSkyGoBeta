package com.global.travel.telecom.app.ui.activities;

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
import com.global.travel.telecom.app.model.RecentSetDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecentCallHistoryArrayAdapter extends ArrayAdapter<RecentSetDataModel> implements Filterable {

    private Context mContext;
    int mResouce;
    String inputPattern = "yyyy-MM-dd HH:mm:ss";
    String outputPattern = "dd-MMM-yyyy h:mm a";
    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
    Date date = null;
    private ArrayList<RecentSetDataModel> RecentcontactListValues = null;
    private RecentCallHistoryArrayAdapter.ItemFilter mFilter = new RecentCallHistoryArrayAdapter.ItemFilter();

    public RecentCallHistoryArrayAdapter(Context context, int resource, ArrayList<RecentSetDataModel> object) {
        super(context, resource, object);
        mContext = context;
        RecentcontactListValues = object;
        mResouce = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String CreateTime = RecentcontactListValues.get(position).getCreate_time();
        String Duration = RecentcontactListValues.get(position).getDuration();
        String Leg2 = RecentcontactListValues.get(position).getLeg2();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResouce, parent, false);

        TextView leg2 = convertView.findViewById(R.id.leg2);
        TextView createTime = convertView.findViewById(R.id.createTime);
        TextView duration = convertView.findViewById(R.id.duration);

        leg2.setText(Leg2);
        duration.setText(mContext.getResources().getString(R.string.textCallduration) + ":" + Duration);

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

    @Override
    public int getCount() {
        return RecentcontactListValues.size();
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

            final ArrayList<RecentSetDataModel> list = SkyGoDialer.recentCallHistoryModels;

            int count = list.size();
            final ArrayList<RecentSetDataModel> nlist = new ArrayList<RecentSetDataModel>(count);

            String filterableName;

            for (int i = 0; i < count; i++) {
                filterableName = list.get(i).getLeg2();

                if (filterableName.toLowerCase().contains(filterString)) {
                    RecentSetDataModel info = new RecentSetDataModel(list.get(i).getCreate_time(),list.get(i).getDuration(),list.get(i).getLeg2(),list.get(i).getOutcome(),list.get(i).getRetail_charge());
                    nlist.add(info);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            RecentcontactListValues = (ArrayList<RecentSetDataModel>) results.values;
            notifyDataSetChanged();

        }
    }
}





















