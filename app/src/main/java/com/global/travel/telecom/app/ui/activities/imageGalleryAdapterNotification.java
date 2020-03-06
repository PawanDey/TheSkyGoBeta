package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.global.travel.telecom.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

class imageGalleryAdapterNotification extends RecyclerView.Adapter<viewHolderNotifications> {

    private List<translatPassdata> list;
    Context context;

    imageGalleryAdapterNotification(List<translatPassdata> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NotNull
    @Override
    public viewHolderNotifications onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.notifications_card_view,
                parent, false);

        return new viewHolderNotifications(photoView);
    }

    @Override
    public void onBindViewHolder(final viewHolderNotifications viewHolder,
                                 final int position) {

        viewHolder.Name.setText(list.get(position).mDealerName);
        viewHolder.Date.setText(list.get(position).mMessage);
        viewHolder.Message.setText(list.get(position).mAlertTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
//                    list.add(new imageGalleryAdapterNotification(getNotifications.setAlertTime(translateDone[i]);
}
