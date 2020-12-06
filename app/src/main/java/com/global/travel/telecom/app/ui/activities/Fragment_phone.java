package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;

import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.CallingName;
import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.CallingNumber;
import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.PERMISSIONS_REQUEST_MICROPHONE;

public class Fragment_phone extends Fragment {
    TextView phoneNumber;
    public static TextView txtstatus;
    public static ImageView callingIndicator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        LinearLayout one = view.findViewById(R.id.one);
        LinearLayout two = view.findViewById(R.id.two);
        LinearLayout three = view.findViewById(R.id.three);
        LinearLayout four = view.findViewById(R.id.four);
        LinearLayout five = view.findViewById(R.id.five);
        LinearLayout six = view.findViewById(R.id.six);
        LinearLayout seven = view.findViewById(R.id.seven);
        LinearLayout eight = view.findViewById(R.id.eight);
        LinearLayout nine = view.findViewById(R.id.nine);
        LinearLayout zero = view.findViewById(R.id.zero);
        LinearLayout delete = view.findViewById(R.id.delete);
        LinearLayout plus = view.findViewById(R.id.plus);
        phoneNumber = view.findViewById(R.id.editphoneNumber);
        txtstatus = view.findViewById(R.id.txtstatus);
        callingIndicator = view.findViewById(R.id.callingIndicator);
        ImageView clickToCallButton = view.findViewById(R.id.clickToCallButton);

        one.setOnClickListener(v -> phoneNumber.append("1"));
        two.setOnClickListener(v -> phoneNumber.append("2"));
        three.setOnClickListener(v -> phoneNumber.append("3"));
        four.setOnClickListener(v -> phoneNumber.append("4"));
        five.setOnClickListener(v -> phoneNumber.append("5"));
        six.setOnClickListener(v -> phoneNumber.append("6"));
        seven.setOnClickListener(v -> phoneNumber.append("7"));
        eight.setOnClickListener(v -> phoneNumber.append("8"));
        nine.setOnClickListener(v -> phoneNumber.append("9"));
        zero.setOnClickListener(v -> phoneNumber.append("0"));
        plus.setOnClickListener(v -> phoneNumber.append("+"));
        delete.setOnClickListener(v -> {
            if (phoneNumber.length() > 0) {
                String deleteLastChar = phoneNumber.getText().toString().trim();
                phoneNumber.setText(deleteLastChar.substring(0, deleteLastChar.length() - 1));
            }
        });

        clickToCallButton.setOnClickListener(v -> {
            try {
                if (phoneNumber.length() <= 3) {
                    Toast.makeText(getContext(), getResources().getString(R.string.textEnterValidPhoneNumber), Toast.LENGTH_SHORT).show();
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.USE_SIP}, PERMISSIONS_REQUEST_MICROPHONE);
                    } else {
                        CallingNumber = phoneNumber.getText().toString().trim();
                        CallingName = "";
                        SkyGoDialer.service.makeCall(phoneNumber.getText().toString(), 1);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return view;
    }
}
