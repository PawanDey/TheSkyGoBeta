package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.RecentSetDataModel;

import java.util.Objects;

public class Fragment_recent extends Fragment {

    @SuppressLint("StaticFieldLeak")
    static ListView ListViewRecentCallHistory;
    private ProgressBar voip_progressBarRecent;
    private TextView progress_bar_message;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ListViewRecentCallHistory = view.findViewById(R.id.ListViewRecentCallHistory);
        voip_progressBarRecent = view.findViewById(R.id.voip_progressBarRecent);
        progress_bar_message = view.findViewById(R.id.progress_bar_message);

        try {
            if (SkyGoDialer.recentCallHistoryModels == null) {
                ListViewRecentCallHistory.setVisibility(View.GONE);
                voip_progressBarRecent.setVisibility(View.VISIBLE);
                progress_bar_message.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    while (SkyGoDialer.recentCallHistoryModels == null) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.postDelayed(() -> {
                        try {
                            ListViewRecentCallHistory.setVisibility(View.VISIBLE);
                            voip_progressBarRecent.setVisibility(View.GONE);
                            progress_bar_message.setVisibility(View.GONE);
                            RecentCallHistoryArrayAdapter adapter = new RecentCallHistoryArrayAdapter(getContext(), R.layout.recent_call_history_listview, SkyGoDialer.recentCallHistoryModels);
                            ListViewRecentCallHistory.setAdapter(adapter);
                            ListViewRecentCallHistory.setBackgroundColor(getResources().getColor(R.color.white));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 100);
                }).start();


            } else {
                ListViewRecentCallHistory.setVisibility(View.VISIBLE);
                voip_progressBarRecent.setVisibility(View.GONE);
                progress_bar_message.setVisibility(View.GONE);
                RecentCallHistoryArrayAdapter adapter = new RecentCallHistoryArrayAdapter(getContext(), R.layout.recent_call_history_listview, SkyGoDialer.recentCallHistoryModels);
                ListViewRecentCallHistory.setAdapter(adapter);
                ListViewRecentCallHistory.setBackgroundColor(getResources().getColor(R.color.white));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Fragment Contacts Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ListViewRecentCallHistory.setOnItemClickListener((adapterView, view1, i, l) -> {
            try {
                RecentSetDataModel arrayList = (RecentSetDataModel) ListViewRecentCallHistory.getItemAtPosition(i);
                String name = "";
                String mobileNumbers = arrayList.getLeg2();
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(), VoipOnCall.class);
                intent.putExtra("CallingNumber", mobileNumbers.trim());
                intent.putExtra("CallingName", name.trim());
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Fragment Contacts Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });

        return view;
    }
}
