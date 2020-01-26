package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class VoipOnCall extends AppCompatActivity {
    TextView phoneNumber, timeOnCall, ContactPersonname, firstChar;
    LinearLayout loadspeaker, mute;
    ImageView hangUp;

    public static String LOGTAG = "AJVoIP";
    TextView mStatus = null;
    SipStack mysipclient = null;
    Context ctx = null;
    GetNotificationsThread notifThread = null;
    public static VoipOnCall instance = null;
    boolean terminateNotifThread = false;
    Boolean checkHold = true;
    Boolean checkTimeOnCall = false;
    int sec = 0;
    int min = 0;
    int hou = 0;
    Handler handler;
    Runnable updateTask;
    String[] notarray = null;
    Boolean checkMute = true;
    Boolean checkSpeaker = true;
    Boolean oneTimeCall = true;
    String CallingNumber, CallingName, firstCharector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip_on_call);
        Intent intent = getIntent();
        UserDetails userDetails = new UserDetails(this);
        CallingNumber = intent.getStringExtra("CallingNumber").replace(" ", "");
        CallingName = intent.getStringExtra("CallingName");

        ctx = this;
        instance = this;
        mute = findViewById(R.id.mute);
        hangUp = findViewById(R.id.hangup);
        loadspeaker = findViewById(R.id.loadspeaker);
        phoneNumber = findViewById(R.id.phoneNumber);
        timeOnCall = findViewById(R.id.timeOnCall);
        firstChar = findViewById(R.id.firstChar);
        ContactPersonname = findViewById(R.id.ContactPersonname);
        if (CallingName == null || CallingName.length() == 0) {
            CallingName = "Unknown Name";
            ContactPersonname.setText("Unknown Name");

        } else {
            ContactPersonname.setText(CallingName);
        }
        try {
            firstCharector = CallingName.substring(0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        firstChar.setText(firstCharector);
        phoneNumber.setText("Mobile " + CallingNumber);
//        String demoparameter = "serveraddress=sip.s.im\r\nusername=SkyGo:246\r\npassword=246\r\nloglevel=5";
        String demoparameter = "serveraddress=sip.s.im\r\nusername=SkyGo:" + userDetails.getUserId() + "\r\npassword=" + userDetails.getUserId() + "\r\nloglevel=5";
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

        hangUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisplayLogs("Hangup on click");
                mysipclient.Hangup();
                finish();
            }
        });

        loadspeaker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //just a loudspeaker test
                DisplayLogs("Toogle loudspeaker");
                if (mysipclient == null) {
                    DisplayStatus("ERROR, SipStack not started");
                } else {
                    if (checkSpeaker) {
                        checkSpeaker = false;
                    } else {
                        checkSpeaker = true;
                    }
                    mysipclient.SetSpeakerMode(!mysipclient.IsLoudspeaker());
                }
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisplayLogs("Toogle loudspeaker");
                if (mysipclient == null) {
                    DisplayStatus("ERROR, SipStack not started");
                } else {
                    if (checkMute) {
                        mysipclient.Mute(-2, true, 0);
                        checkMute = false;
                    } else {
                        mysipclient.Mute(-2, false, 0);
                        checkMute = true;
                    }
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
                }  //we are lowering this thread priority a bit to give more chance for our main GUI thread

                while (!terminateNotifThread) {

                    try {
                        if (mysipclient != null) {
                            //get notifications from the SIP stack
                            sipnotifications = mysipclient.GetNotificationsSync();
                            if (sipnotifications != null && sipnotifications.length() > 0) {
                                // send notifications to Main thread using a Handler

                                Message messageToMainThread = new Message();
                                Bundle messageData = new Bundle();
                                messageToMainThread.what = 0;
                                messageData.putString("notifmessages", sipnotifications);
                                messageToMainThread.setData(messageData);
                                NotifThreadHandler.sendMessage(messageToMainThread);
                            }
                        }

                        if ((sipnotifications == null || sipnotifications.length() < 1) && !terminateNotifThread) {
                            //some error occured. sleep a bit just to be sure to avoid busy loop
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

    //get the notifications from the GetNotificationsThread thread
    @SuppressLint("HandlerLeak")
    public static Handler NotifThreadHandler = new Handler() {
        public void handleMessage(@NotNull android.os.Message msg) {
            try {
                if (msg.getData() == null) return;

                Bundle resBundle = msg.getData();

                String receivedNotif = msg.getData().getString("notifmessages");

                if (receivedNotif != null && receivedNotif.length() > 0)
                    instance.ReceiveNotifications(receivedNotif);

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
        DisplayStatus(line); //we just display them in this simple test application
        //see the Notifications section in the documentation about the possible messages (parse the line string and process them after your needs)
        String x = "____";
        if (line.contains("Calling...")) {
            Log.v(x, "Calling...");
            timeOnCall.setText("Calling...");
        } else if (line.contains("Ringing")) {
            Log.v(x, "Ringing");
            timeOnCall.setText("Ringing");
        } else if (line.contains(",Speaking (0 sec)") && oneTimeCall) {
            Log.v(x, ",Speaking (0 sec)");
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
        } else if (line.contains("CallDisconnect") || line.contains("Call Finished")) {
            Log.v(x, "Call Disconnect");
            Log.v(x, "Call Finished");
            if (mysipclient != null) {
                DisplayLogs("Stop SipStack");
                mysipclient.Stop(true);
            }
            mysipclient = null;
            finish();

        }
    }

    public void DisplayStatus(String stat) {
        if (stat == null) return;
        if (mStatus != null) mStatus.setText(stat);
        DisplayLogs("Status: " + stat);
    }

    public void DisplayLogs(String logmsg) {
        Log.v(LOGTAG, logmsg);
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

    public void Pending() {
        Toast.makeText(this, "ComingSoon", Toast.LENGTH_SHORT).show();
    }


}
