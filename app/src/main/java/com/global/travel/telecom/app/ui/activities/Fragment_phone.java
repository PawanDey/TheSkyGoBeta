package com.global.travel.telecom.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;

import java.util.Objects;

public class Fragment_phone extends Fragment {
    LinearLayout one, two, three, four, five, six, seven, eight, nine, zero, delete, plus;
    private TextView phoneNumber;
    private ImageView clickToCallButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);
        six = view.findViewById(R.id.six);
        seven = view.findViewById(R.id.seven);
        eight = view.findViewById(R.id.eight);
        nine = view.findViewById(R.id.nine);
        zero = view.findViewById(R.id.zero);
        delete = view.findViewById(R.id.delete);
        plus = view.findViewById(R.id.plus);
        phoneNumber = view.findViewById(R.id.editphoneNumber);
        clickToCallButton = view.findViewById(R.id.clickToCallButton);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("1");
            }

        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("2");
            }

        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("3");
            }

        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("4");
            }

        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("5");
            }

        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("6");
            }

        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("7");
            }

        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("8");
            }

        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("9");
            }

        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("0");
            }

        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("+");
            }

        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.length() > 0) {
                    String deleteLastChar = phoneNumber.getText().toString().trim();
                    phoneNumber.setText(deleteLastChar.substring(0, deleteLastChar.length() - 1));
                }
            }

        });

        clickToCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(), VoipOnCall.class);
                intent.putExtra("CallingNumber", phoneNumber.getText().toString().trim());
                intent.putExtra("CallingName", "");
                startActivity(intent);
            }
        });
        return view;
    }
}
