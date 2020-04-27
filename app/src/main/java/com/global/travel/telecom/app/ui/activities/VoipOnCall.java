package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.service.UserDetails;
import com.mizuvoip.jvoip.SipStack;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VoipOnCall extends AppCompatActivity {
    TextView phoneNumber, timeOnCall, ContactPersonname, firstChar, loadspeakerTextColorChange, holdTextColorChange, muteTextColorChange;
    LinearLayout loadspeaker, mute, hold;
    ImageView hangUp, loadspeakerImageColorChange, holdImageColorChange, muteImageColorChange;

    public static String LOGTAG = "AJVoIP_Call";
    public static String mStatus = "Start";
    SipStack mysipclient = null;
    Context ctx = null;
    GetNotificationsThread notifThread = null;
    @SuppressLint("StaticFieldLeak")
    public static VoipOnCall instance = null;
    boolean terminateNotifThread = false;
    Boolean checkHold = true;
    Boolean checkLoadspeaker = true;
    Boolean checkTimeOnCall = false;
    int sec = 0;
    int min = 0;
    int hou = 0;
    Handler handler;
    Runnable updateTask;
    String[] notarray = null;
    Boolean checkMute = true;
    Boolean oneTimeCall = true;
    String CallingNumber, CallingName, firstCharector;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip_on_call);
        Intent intent = getIntent();
        UserDetails userDetails = new UserDetails(this);
        CallingNumber = Objects.requireNonNull(intent.getStringExtra("CallingNumber")).replace(" ", "");
        CallingName = intent.getStringExtra("CallingName");

        ctx = this;
        instance = this;
        hold = findViewById(R.id.hold);
        mute = findViewById(R.id.mute);
        hangUp = findViewById(R.id.hangup);
        loadspeaker = findViewById(R.id.loadspeaker);
        phoneNumber = findViewById(R.id.phoneNumber);
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
        phoneNumber.setText(getResources().getString(R.string.textMobile) + " " + CallingNumber);
//        String demoparameter = "serveraddress=sip.s.im\r\nusername=SkyGo:246\r\npassword=246\r\nloglevel=5";
        String demoparameter = "serveraddress=sip.s.im\r\nusername=" + userDetails.getVoipUserName() + "\r\npassword=" + userDetails.getUserId() + "\r\nloglevel=5";
        mStatus = mStatus + "       " + demoparameter;
        try {
            // start SipStack if it's not already running
            if (mysipclient == null) {
                DisplayLogs("Start SipStack");

                //initialize the SIP engine
                mysipclient = new SipStack();
                mysipclient.Init(ctx);
                mysipclient.SetParameters(demoparameter.trim());

                //start my event listener thread
                notifThread = new GetNotificationsThread();
                notifThread.start();

                //start the SIP engine
                mysipclient.Start();
                mysipclient.Register();

            } else {
                DisplayLogs("SipStack already started");
            }
        } catch (Exception e) {
            DisplayLogs("ERROR, StartSipStack");
        }

        if (CallingNumber.length() < 1) {
            DisplayStatus("ERROR, Invalid destination number");
            return;
        }
        if (mysipclient == null) {
            DisplayStatus("ERROR, cannot initiate call because SipStack is not started");
        } else {
            timeOnCall.setVisibility(View.VISIBLE);
            mysipclient.Call(-1, CallingNumber);
            checkTimeOnCall = true;
        }

        hangUp.setOnClickListener(v -> {
            DisplayLogs("Hangup on click");
            mysipclient.Hangup();
            finish();
        });

        loadspeaker.setOnClickListener(v -> {
            //just a loudspeaker test
            DisplayLogs("Toogle loudspeaker");
            if (mysipclient == null) {
                DisplayStatus("ERROR, SipStack not started");
            } else {
                mysipclient.SetSpeakerMode(!mysipclient.IsLoudspeaker());
                if (checkLoadspeaker) {
                    loadspeakerImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.loadspeaker_blue));
                    loadspeakerTextColorChange.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    loadspeakerTextColorChange.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    loadspeakerTextColorChange.setTextSize(12);
                    checkLoadspeaker = false;
                } else {
                    loadspeakerImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.loadspeaker));
                    loadspeakerTextColorChange.setTextColor(getResources().getColor(R.color.black));
                    loadspeakerTextColorChange.setTypeface(Typeface.DEFAULT);
                    loadspeakerTextColorChange.setTextSize(10);
                    checkLoadspeaker = true;
                }
            }
        });

        hold.setOnClickListener(v -> {
//            int checkpoint = mysipclient.IsOnHold(-2);
            int a = -1;
            if (checkHold) {
                mysipclient.Hold(a, true);
                holdImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.hold_blue));
                holdTextColorChange.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                checkHold = false;
            } else {
                holdImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.hold));
                holdTextColorChange.setTextColor(getResources().getColor(R.color.black));
                mysipclient.Hold(a, false);
                checkHold = true;
            }

        });

        mute.setOnClickListener(v -> {
            DisplayLogs("Toogle loudspeaker");
            if (mysipclient == null) {
                DisplayStatus("ERROR, SipStack not started");
            } else {
                if (!oneTimeCall) {
                    if (checkMute) {
                        mysipclient.Mute(-2, true, 0);
                        muteImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.mute_blue));
                        muteTextColorChange.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        muteTextColorChange.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        muteTextColorChange.setTextSize(12);
                        checkMute = false;
                    } else {
                        mysipclient.Mute(-2, false, 0);
                        muteImageColorChange.setImageDrawable(getResources().getDrawable(R.drawable.mute));
                        muteTextColorChange.setTextColor(getResources().getColor(R.color.black));
                        muteTextColorChange.setTypeface(Typeface.DEFAULT);
                        muteTextColorChange.setTextSize(10);
                        checkMute = true;
                    }
                } else {
                    Toast.makeText(ctx, getResources().getString(R.string.textWaitingForPickTheCall), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisplayLogs("ondestroy");
        terminateNotifThread = true;
        if (mysipclient != null) {
            DisplayLogs("Stop SipStack");
            mysipclient.Stop(true);
        }
        mysipclient = null;
    }

    public void backToDashboardButton(View view) {
        finish();
    }

    public class GetNotificationsThread extends Thread {
        String sipnotifications = "";

        public void run() {
            try {
                try {
                    Thread.currentThread().setPriority(4);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                while (!terminateNotifThread) {

                    try {
                        if (mysipclient != null) {
                            //get notifications from the SIP stack
                            sipnotifications = mysipclient.GetNotificationsSync();
                            if (sipnotifications != null && sipnotifications.length() > 0) {
                                Message messageToMainThread = new Message();
                                Bundle messageData = new Bundle();
                                messageToMainThread.what = 0;
                                messageData.putString("notifmessages", sipnotifications);
                                messageToMainThread.setData(messageData);
                                NotifThreadHandler.sendMessage(messageToMainThread);
                            }
                        }

                        if ((sipnotifications == null || sipnotifications.length() < 1) && !terminateNotifThread) {
                            GetNotificationsThread.sleep(1);
                        }

                        continue;
                    } catch (Throwable e) {
                        Log.e(LOGTAG, "ERROR, WorkerThread on run()intern", e);
                    }
                    if (!terminateNotifThread) {
                        GetNotificationsThread.sleep(10);
                    }
                }
            } catch (Throwable e) {
                Log.e(LOGTAG, "WorkerThread on run()");
            }
        }

    }

    @SuppressLint("HandlerLeak")
    public static Handler NotifThreadHandler = new Handler() {
        public void handleMessage(@NotNull android.os.Message msg) {
            try {
                if (msg.getData() == null) return;
                String lineif = msg.getData().getString("notifmessages");
                assert lineif != null;
                if (lineif != null && lineif.length() > 0)
                    instance.ReceiveNotifications(lineif);

            } catch (Throwable e) {
                Log.e(LOGTAG, "NotifThreadHandler handle Message");
            }
        }
    };


    public void ReceiveNotifications(String notifs) {
        if (notifs == null || notifs.length() < 1) return;
        notarray = notifs.split("\r\n");

        if (notarray.length < 1) return;

        for (String s : notarray) {
            if (s != null && s.length() > 0) {
                ProcessNotifications(s);
            }
        }
    }

    public void ProcessNotifications(String line) {

        if (line == null || line.length() < 1) return;
        // we can receive multiple notifications at once, so we split them by CRLF or with ",NEOL \r\n" and we end up with a
//        String array of notifications
        mysipclient.GetLogs();
        mStatus = mStatus + "         " + line;
        String[] notarray = line.split(",NEOL \n");
        String notifywordcontent;
        for (String s : notarray) {
            notifywordcontent = s;
            if (notifywordcontent == null || notifywordcontent.length() < 1) continue;
            notifywordcontent = notifywordcontent.trim();
            notifywordcontent = notifywordcontent.replace("WPNOTIFICATION,", "");
            // now we have a single notification in the "notifywordcontent" String variable
            Log.v("AJVOIP", "Received Notification: " + notifywordcontent);
            int pos = 0;
            String notifyword1 = ""; // will hold the notification type
            String notifyword2 = ""; // will hold the second most important String in the STATUS notifications, which is the
//            third parameter, right after the "line' parameter
            // First we are checking the first parameter (until the first comma) to determine the event type.
            // Then we will check for the other parameters.
            pos = notifywordcontent.indexOf(",");
            if (pos > 0) {
                notifyword1 = notifywordcontent.substring(0, pos).trim();
                notifywordcontent = notifywordcontent.substring(pos + 1, notifywordcontent.length()).trim();
            } else {
                notifyword1 = "EVENT";
            }
            // Notification type, "notifyword1" can have many values, but the most important ones are the STATUS types.
            // After each call, you will receive a CDR (call detail record). We can parse this to get valuable information
//            about the latest call.
            // CDR,line, peername,caller, called,peeraddress,connecttime,duration,discparty,reasontext
            // Example: CDR,1, 112233, 445566, 112233, voip.mizu-voip.com, 5884, 1429, 2, bye received
            if (notifyword1.equals("CDR")) {
                String[] cdrParams = notifywordcontent.split(",");
                String lin1e1 = cdrParams[0];
                String peername = cdrParams[1];
                String caller = cdrParams[2];
                String called = cdrParams[3];
                String peeraddress = cdrParams[4];
                String connecttime = cdrParams[5];
                String duration = cdrParams[6];
                String discparty = cdrParams[7];
                String reasontext = cdrParams[8];
            }
            // lets parse a few STATUS notifications
            else if (notifyword1.equals("STATUS")) {
                //ignore line number. we are not handling it for now
                pos = notifywordcontent.indexOf(",");
                if (pos > 0)
                    notifywordcontent = notifywordcontent.substring(pos + 1, notifywordcontent.length()).trim();
                pos = notifywordcontent.indexOf(",");
                if (pos > 0) {
                    notifyword2 = notifywordcontent.substring(0, pos).trim();
                    notifywordcontent = notifywordcontent.substring(pos + 1, notifywordcontent.length()).trim();
                } else {
                    notifyword2 = notifywordcontent;
                }
                if (notifyword2.equals("Registered.")) {
                    // means the SDK is successfully registered to the specified VoIP server
                } else if (notifyword2.equals("CallSetup")) {
                    timeOnCall.setText(getResources().getString(R.string.textCalling));
                } else if (notifyword2.equals("Ringing")) {
                    timeOnCall.setText(getResources().getString(R.string.textRinging));
                } else if (notifyword2.equals("CallConnect")) {
                    oneTimeCall = false;
                    handler = new Handler();
                    updateTask = new Runnable() {
                        @Override
                        public void run() {
                            TimeUpdateOnCall();
                            handler.postDelayed(this, 1000);
                        }
                    };
                    handler.postDelayed(updateTask, 1000);
                } else if (notifyword2.equals("CallDisconnect")) {
                    if (mysipclient != null) {
                        DisplayLogs("Stop SipStack");
                        mysipclient.Stop(true);
                    }
                    mysipclient = null;
                    finish();
                } else if (notifyword1.equals("CHAT")) {
                    // we received an incoming chat message (parse the other parameters to get the sender name and the text tobe displayed)}
                } else if (notifyword1.equals("ERROR")) {
                    // we received an error notification; at least log it somewhere
                    Log.e("AJVOIP", "ERROR," + notifywordcontent);
                } else if (notifyword1.equals("WARNING")) {
                    // we received a warning notification; at least log it somewhere
                    Log.w("AJVOIP", "WARNING," + notifywordcontent);
                } else if (notifyword1.equals("EVENT")) {
                    // display important event for the user
                    Log.v("AJVOIP", notifywordcontent);
                }
            }
        }

    }

    public void DisplayStatus(String stat) {
        if (stat == null) return;
        DisplayLogs("Status: " + stat);
        mStatus = mStatus + "       " + "Status:" + stat;

    }

    public void DisplayLogs(String logmsg) {
        Log.v(LOGTAG, logmsg);
        mStatus = mStatus + "       " + logmsg;

    }

    @SuppressLint("SetTextI18n")
    private void TimeUpdateOnCall() {
        try {
            if (checkTimeOnCall) {
                if (sec == 60) {
                    min++;
                    sec = 0;
                    if (min == 60) {
                        hou++;
                        min = 0;
                    }
                }
                if (hou == 0) {
                    if (Integer.toString(sec).length() == 1) {
                        timeOnCall.setText(min + ":0" + sec);
                    } else {
                        timeOnCall.setText(min + ":" + sec);
                    }
                } else {
                    timeOnCall.setText(hou + ":" + min + ":" + sec);
                }
                sec++;
            } else {
                ((TextView) findViewById(R.id.timeOnCall)).setText(" ");
                min = 0;
                sec = 0;
                hou = 0;
                if (updateTask != null) {
                    handler.removeCallbacks(updateTask);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "CallOnTIme Error:" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
