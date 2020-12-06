package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chatapp.sip.api.ISipService;
import com.chatapp.sip.api.SipCallSession;
import com.chatapp.sip.api.SipManager;
import com.chatapp.sip.service.SipService;
import com.global.travel.telecom.app.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.CallingName;
import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.CallingNumber;

public class VoipOnCall extends AppCompatActivity {
    TextView setPhoneNumber, timeOnCall, ContactPersonname, firstChar, loadspeakerTextColorChange, holdTextColorChange, muteTextColorChange;
    LinearLayout loadspeaker, mute, hold;
    ImageView hangUp, loadspeakerImageColorChange, holdImageColorChange, muteImageColorChange;
    String firstCharector;
    private ISipService service;
    private PowerManager.WakeLock inCallWakeLock;
    private PowerManager powerManager;
    int flags = 0x00000020;
    private SipCallSession call;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    private TextView lblStatus;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            service = ISipService.Stub.asInterface(arg1);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            service = null;
        }
    };
    Boolean checkLoadspeaker = false;
    Boolean checkHold = false;
    Boolean checkMute = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip_on_call);

        bindService(new Intent(this, SipService.class), connection, Context.BIND_AUTO_CREATE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hold = findViewById(R.id.hold);
        mute = findViewById(R.id.mute);
        hangUp = findViewById(R.id.hangup);
        loadspeaker = findViewById(R.id.loadspeaker);
        setPhoneNumber = findViewById(R.id.setPhoneNumber);
        timeOnCall = findViewById(R.id.timeOnCall);
        firstChar = findViewById(R.id.firstChar);
        ContactPersonname = findViewById(R.id.ContactPersonname);
        loadspeakerImageColorChange = findViewById(R.id.loadspeakerImageColorChange);
        loadspeakerTextColorChange = findViewById(R.id.loadspeakerTextColorChange);
        holdImageColorChange = findViewById(R.id.holdImageColorChange);
        holdTextColorChange = findViewById(R.id.holdTextColorChange);
        muteImageColorChange = findViewById(R.id.muteImageColorChange);
        muteTextColorChange = findViewById(R.id.muteTextColorChange);

        if (CallingName == null || CallingName.length() == 0) {
            CallingName = "Unknown Name";
            ContactPersonname.setText(getResources().getString(R.string.textUnknownName));
        } else {
            ContactPersonname.setText(CallingName);
        }
        try {
            firstCharector = CallingName.substring(0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        firstChar.setText(firstCharector);
        setPhoneNumber.setText(getResources().getString(R.string.textMobile) + " " + CallingNumber);

        bindService(new Intent(this, SipService.class), connection, Context.BIND_AUTO_CREATE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (inCallWakeLock == null) {

            try {
                flags = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
            } catch (Throwable ignored) {
            }


            inCallWakeLock = powerManager.newWakeLock(flags, getLocalClassName());
            inCallWakeLock.setReferenceCounted(false);
        }
        Bundle extras = getIntent().getExtras();
        call = getIntent().getParcelableExtra(SipManager.EXTRA_CALL_INFO);
        lblStatus = (TextView) findViewById(R.id.timeOnCall);
        lblStatus.setText("");

        hangUp.setOnClickListener(v -> {
            DisconnectCall();
            finish();
        });

        loadspeaker.setOnClickListener(v -> {
            try {
                if (!checkLoadspeaker) {
                    service.setSpeakerphoneOn(true);
                    loadspeakerImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.loadspeaker_blue));
                    loadspeakerTextColorChange.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    loadspeakerTextColorChange.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    loadspeakerTextColorChange.setTextSize(12);
                    checkLoadspeaker = true;
                } else {
                    service.setSpeakerphoneOn(false);
                    loadspeakerImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.loadspeaker));
                    loadspeakerTextColorChange.setTextColor(getResources().getColor(R.color.black));
                    loadspeakerTextColorChange.setTypeface(Typeface.DEFAULT);
                    loadspeakerTextColorChange.setTextSize(10);
                    checkLoadspeaker = false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        hold.setOnClickListener(v -> {
            try {
                if (!checkHold) {
                    SipCallSession[] callSessions = service.getCalls();
                    for (SipCallSession callSession : callSessions) {
                        service.hold(callSession.getCallId());
                    }
                    holdImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.hold_blue));
                    holdTextColorChange.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    checkHold = true;
                } else {
                    SipCallSession[] callSessions = service.getCalls();
                    for (SipCallSession callSession : callSessions) {
                        service.reinvite(callSession.getCallId(), true);
                    }
                    holdImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.hold));
                    holdTextColorChange.setTextColor(getResources().getColor(R.color.black));
                    checkHold = false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        mute.setOnClickListener(v -> {
            try {
                if (!checkMute) {
                    service.setMicrophoneMute(true);
                    muteImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.mute_blue));
                    muteTextColorChange.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    muteTextColorChange.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    muteTextColorChange.setTextSize(12);
                    checkMute = true;
                } else {
                    service.setMicrophoneMute(false);
                    muteImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.mute));
                    muteTextColorChange.setTextColor(getResources().getColor(R.color.black));
                    muteTextColorChange.setTypeface(Typeface.DEFAULT);
                    muteTextColorChange.setTextSize(10);
                    checkMute = false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000);
    }

    @Override
    protected void onPause() {
        if (inCallWakeLock != null && inCallWakeLock.isHeld()) {
            inCallWakeLock.release();
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        if (inCallWakeLock != null) {
            inCallWakeLock.acquire();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }


    @Override
    protected void onDestroy() {
        try {
            DisconnectCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void AnswerCall() {
        try {
            service.answer(call.getCallId(), SipCallSession.StatusCode.OK);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void DisconnectCall() {
        try {
            SipCallSession[] callSessions = service.getCalls();
            for (SipCallSession callSession : callSessions) {
                service.hangup(callSession.getCallId(), SipCallSession.StatusCode.OK);
            }

            this.unbindService(connection);
            if (timer != null) {
                timer.cancel();
            }
            timer = null;

        } catch (Exception e) {

        }
        finish();
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(() -> {
                    try {

                        call = service.getCallInfo(call.getCallId());

                        if (call.getCallState() == SipCallSession.InvState.DISCONNECTED || call.getCallState() == SipCallSession.InvState.NULL) {
                            DisconnectCall();
                        }
                        if (call.getCallState() == SipCallSession.InvState.CONFIRMED) {
                            lblStatus.setText("Connected");
                        }
                        if (call.getCallState() == SipCallSession.InvState.INCOMING) {
                            lblStatus.setText("Incoming");
                        }
                        if (call.getCallState() == SipCallSession.InvState.CONNECTING || call.getCallState() == SipCallSession.InvState.EARLY) {
                            lblStatus.setText("Ringing");
                        }
                        if (call.getCallState() == SipCallSession.InvState.CALLING) {
                            lblStatus.setText("Dialing");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };
    }
}