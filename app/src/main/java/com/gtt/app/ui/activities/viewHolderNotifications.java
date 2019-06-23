package com.gtt.app.ui.activities;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gtt.app.R;

public class viewHolderNotifications extends RecyclerView.ViewHolder {
    TextView Name;
    TextView Message;
    TextView Date;

    viewHolderNotifications(View itemView)
    {
        super(itemView);
        Name = (TextView)itemView.findViewById(R.id.dealerName);
        Date = (TextView)itemView.findViewById(R.id.dealerMessage);
        Message = (TextView)itemView.findViewById(R.id.dealerTimeAgo);
    }
}
