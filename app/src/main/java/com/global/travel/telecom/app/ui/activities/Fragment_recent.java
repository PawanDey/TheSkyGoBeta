package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.RecentSetDataModel;

import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.CallingName;
import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.CallingNumber;
import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.PERMISSIONS_REQUEST_MICROPHONE;

public class Fragment_recent extends Fragment {

    @SuppressLint("StaticFieldLeak")
    static ListView ListViewRecentCallHistory;
    private ProgressBar voip_progressBarRecent;
    private TextView progress_bar_message;
    private EditText RecentContactSearch;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private RecentCallHistoryArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ListViewRecentCallHistory = view.findViewById(R.id.ListViewRecentCallHistory);
        voip_progressBarRecent = view.findViewById(R.id.voip_progressBarRecent);
        progress_bar_message = view.findViewById(R.id.progress_bar_message);
        RecentContactSearch = view.findViewById(R.id.RecentContactSearch);

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
//                            RecentContactSearch.setVisibility(View.VISIBLE);
                            ListViewRecentCallHistory.setVisibility(View.VISIBLE);
                            voip_progressBarRecent.setVisibility(View.GONE);
                            progress_bar_message.setVisibility(View.GONE);
                            adapter = new RecentCallHistoryArrayAdapter(getContext(), R.layout.recent_call_history_listview, SkyGoDialer.recentCallHistoryModels);
                            ListViewRecentCallHistory.setAdapter(adapter);
                            ListViewRecentCallHistory.setBackgroundColor(getResources().getColor(R.color.white));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 100);
                }).start();


            } else {
//                RecentContactSearch.setVisibility(View.VISIBLE);
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
                String mobileNumbers = arrayList.getLeg2();

                if (mobileNumbers.length() <= 3) {
                    Toast.makeText(getContext(), getResources().getString(R.string.textEnterValidPhoneNumber), Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.USE_SIP}, PERMISSIONS_REQUEST_MICROPHONE);
                    } else {
                        CallingNumber = mobileNumbers.trim();
                        CallingName = "";
                        SkyGoDialer.service.makeCall(mobileNumbers, 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Fragment Contacts Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        RecentContactSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapter.getFilter().filter(s.toString(), count1 -> {
                        Log.d("FILTER --> ", "filter complete! count: " + count1);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

}
