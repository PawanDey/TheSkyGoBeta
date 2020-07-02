package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.content.Intent;
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
    private EditText ContactSearch;
    ContactsArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        listViewContacts = view.findViewById(R.id.ListViewContacts);
        voip_progressBar = view.findViewById(R.id.voip_progressBar);
        progress_bar_message = view.findViewById(R.id.progress_bar_message);
        ContactSearch = view.findViewById(R.id.ContactSearch);
        listViewContacts.setTextFilterEnabled(true);

        try {
            if (SkyGoDialer.mobileArray == null) {
                listViewContacts.setVisibility(View.GONE);
                voip_progressBar.setVisibility(View.VISIBLE);
                progress_bar_message.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    while (SkyGoDialer.mobileArray == null) {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.postDelayed(() -> {
                        try {
                            ContactSearch.setVisibility(View.VISIBLE);
                            listViewContacts.setVisibility(View.VISIBLE);
                            voip_progressBar.setVisibility(View.GONE);
                            progress_bar_message.setVisibility(View.GONE);
                            adapter = new ContactsArrayAdapter(getContext(), R.layout.contacts_listview, SkyGoDialer.mobileArray);
                            listViewContacts.setAdapter(adapter);
                            listViewContacts.setBackgroundColor(getResources().getColor(R.color.white));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 100);
                }).start();


            } else {
                ContactSearch.setVisibility(View.VISIBLE);
                listViewContacts.setVisibility(View.VISIBLE);
                voip_progressBar.setVisibility(View.GONE);
                progress_bar_message.setVisibility(View.GONE);
                adapter = new ContactsArrayAdapter(getContext(), R.layout.contacts_listview, SkyGoDialer.mobileArray);
                listViewContacts.setAdapter(adapter);
                listViewContacts.setBackgroundColor(getResources().getColor(R.color.white));

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Fragment Contacts Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        listViewContacts.setOnItemClickListener((adapterView, view1, i, l) -> {
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

        });

        ContactSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString(), count1 -> {
                    Log.d("FILTER --> ", "filter complete! count: " + count1);
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

}

