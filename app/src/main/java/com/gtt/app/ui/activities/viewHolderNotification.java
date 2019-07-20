package com.gtt.app.ui.activities;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gtt.app.R;

public class viewHolderNotification extends RecyclerView.ViewHolder {
    TextView Name;
    TextView Message;
    TextView Date;

    viewHolderNotification(View itemView)
    {
        super(itemView);
        Name = (TextView)itemView.findViewById(R.id.Name);
        Date = (TextView)itemView.findViewById(R.id.Date);
        Message = (TextView)itemView.findViewById(R.id.Message);
    }
}
