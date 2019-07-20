package com.gtt.app.ui.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gtt.app.R;
import com.gtt.app.model.GetNotifications;
import com.gtt.app.presenter.implementation.AuthenticationPresenter;
import com.gtt.app.service.UserDetails;
import java.util.Collections;
import java.util.List;

class ImageGalleryAdapterNotificationEN extends RecyclerView.Adapter<viewHolderNotifications> {

    List<GetNotifications> list = Collections.emptyList();
    Context context;

    public ImageGalleryAdapterNotificationEN(List<GetNotifications> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public viewHolderNotifications onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.notifications_card_view,
                parent, false);

        viewHolderNotifications viewHolder = new viewHolderNotifications(photoView);
        return viewHolder;
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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
//                    list.add(new imageGalleryAdapterNotification(getNotifications.setAlertTime(translateDone[i]);
}
