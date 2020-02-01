package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.ContactsModel;

import java.io.Serializable;
import java.util.Objects;

public class Fragment_contacts extends Fragment implements Serializable {

    private ListView listViewContacts;
    Context context;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ProgressBar voip_progressBar;
    private TextView progress_bar_message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        listViewContacts = view.findViewById(R.id.ListViewContacts);
        voip_progressBar = view.findViewById(R.id.voip_progressBar);
        progress_bar_message = view.findViewById(R.id.progress_bar_message);

        try {
            if (SkyGoDialer.mobileArray == null) {
                listViewContacts.setVisibility(View.GONE);
                voip_progressBar.setVisibility(View.VISIBLE);
                progress_bar_message.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (SkyGoDialer.mobileArray == null) {
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    listViewContacts.setVisibility(View.VISIBLE);
                                    voip_progressBar.setVisibility(View.GONE);
                                    progress_bar_message.setVisibility(View.GONE);
                                    ContactsArrayAdapter adapter = new ContactsArrayAdapter(getContext(), R.layout.contacts_listview, SkyGoDialer.mobileArray);
                                    listViewContacts.setAdapter(adapter);
                                    listViewContacts.setBackgroundColor(getResources().getColor(R.color.white));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 100);
                    }
                }).start();


            } else {
                listViewContacts.setVisibility(View.VISIBLE);
                voip_progressBar.setVisibility(View.GONE);
                progress_bar_message.setVisibility(View.GONE);
                ContactsArrayAdapter adapter = new ContactsArrayAdapter(getContext(), R.layout.contacts_listview, SkyGoDialer.mobileArray);
                listViewContacts.setAdapter(adapter);
                listViewContacts.setBackgroundColor(getResources().getColor(R.color.white));

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Fragment Contacts Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ContactsModel arrayList = (ContactsModel) listViewContacts.getItemAtPosition(i);
                    String name = arrayList.name;
                    String mobileNumbers = arrayList.mobileNumber;
                    Intent intent = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(), VoipOnCall.class);
                    intent.putExtra("CallingNumber", mobileNumbers.trim());
                    intent.putExtra("CallingName", name.trim());
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Fragment Contacts Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
        return view;
    }
}

