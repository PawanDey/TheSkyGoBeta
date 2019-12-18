package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.global.travel.telecom.app.R;
import com.mizuvoip.jvoip.SipStack;

import org.jetbrains.annotations.NotNull;

public class SkyGoDialer extends AppCompatActivity {
    TextView phoneNumber, timeOnCall;
    GridLayout gridLayoutDailer, gridLayoutOnCall;
    LinearLayout loadspeaker, one, two, three, four, five, six, seven, eight, nine, zero, delete, star, mute, clickToCallButtonLayout;
    ImageView clickToCallButton, hangUp, loadspeakerImage, muteImage, holdImage, contacts;

    public static String LOGTAG = "AJVoIP";
    TextView mStatus = null;
    SipStack mysipclient = null;
    Context ctx = null;
    public static SkyGoDialer instance = null;
    boolean terminateNotifThread = false;
    GetNotificationsThread notifThread = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sky_go_dialer);

        //On click event
        ctx = this;
        instance = this;
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        delete = findViewById(R.id.delete);
        star = findViewById(R.id.star);
        mute = findViewById(R.id.mute);
        hangUp = findViewById(R.id.hangup);
        contacts = findViewById(R.id.contacts);
        loadspeaker = findViewById(R.id.loadspeaker);
        clickToCallButton = findViewById(R.id.clickToCallButton);
        gridLayoutDailer = findViewById(R.id.gridLayoutDailer);
        gridLayoutOnCall = findViewById(R.id.gridLayoutOnCall);
        phoneNumber = findViewById(R.id.phoneNumber);
        timeOnCall = findViewById(R.id.timeOnCall);
        clickToCallButtonLayout = findViewById(R.id.clickToCallButtonLayout);
        loadspeakerImage = findViewById(R.id.loadspeakerImage);
        muteImage = findViewById(R.id.muteImage);
        holdImage = findViewById(R.id.holdImage);
        DisplayLogs("oncreate");

        //contact msges
        //SIP stack parameters separated by CRLF. Will be passed to AJVoIP with the SetParameters API call (you might also use the SetParameter API to pass the parameters separately)
        //Add other settings after your needs. See the documentation for the full list of available parameters.
        String demoparameter = "serveraddress=sip.s.im\r\nusername=447624045000\r\npassword=1122\r\nloglevel=5";
        phoneNumber.setText("");

        DisplayStatus("Ready.");
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


        //on click event
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
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.append("+");
            }

        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteLastChar = phoneNumber.getText().toString().trim();
                phoneNumber.setText(deleteLastChar.substring(0, deleteLastChar.length() - 1));
            }

        });

        clickToCallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisplayLogs("Call on click");
                String number = phoneNumber.getText().toString().trim();
                if (number.length() < 1) {
                    DisplayStatus("ERROR, Invalid destination number");
                    return;
                }
                if (mysipclient == null) {
                    DisplayStatus("ERROR, cannot initiate call because SipStack is not started");
                } else {
                    clickToCallButton.setVisibility(View.GONE);
                    gridLayoutDailer.setVisibility(View.GONE);
                    gridLayoutOnCall.setVisibility(View.VISIBLE);
                    timeOnCall.setVisibility(View.VISIBLE);
                    mysipclient.Call(-1, number);
                    checkTimeOnCall = true;
                }
            }
        });

        hangUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisplayLogs("Hangup on click");

                if (mysipclient == null) {
                    DisplayStatus("ERROR, cannot hangup because SipStack is not started");
                } else {
                    mysipclient.Hangup();
                    checkTimeOnCall = false;
                    TimeUpdateOnCall();
                    clickToCallButtonLayout.setVisibility(View.VISIBLE);
                    clickToCallButton.setVisibility(View.VISIBLE);
                    gridLayoutDailer.setVisibility(View.VISIBLE);
                    gridLayoutOnCall.setVisibility(View.GONE);
                    loadspeakerImage.setImageResource(R.drawable.loadspeaker);
                    muteImage.setImageResource(R.drawable.mute);
                    holdImage.setImageResource(R.drawable.hold);
                    checkMute = true;
                    checkSpeaker = true;
                    checkHold = true;
                    checkTimeOnCall = false;
                }
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
                        loadspeakerImage.setImageResource(R.drawable.loadspeaker_blue);
                    } else {
                        loadspeakerImage.setImageResource(R.drawable.loadspeaker);
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
                        muteImage.setImageResource(R.drawable.mute_blue);
                    } else {
                        mysipclient.Mute(-2, false, 0);
                        checkMute = true;
                        muteImage.setImageResource(R.drawable.mute);
                    }
                }
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentResolver cr = getContentResolver();
                    @SuppressLint("Recycle") Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    assert cur != null;
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            if (Integer.parseInt(cur.getString(
                                    cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                Cursor pCur = cr.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{id}, null);
                                assert pCur != null;
                                while (pCur.moveToNext()) {
                                    String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    //here is add data in popup

                                    Toast.makeText(SkyGoDialer.this, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                                }
                                pCur.close();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        notifThread = null;
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

        if (notarray == null || notarray.length < 1) return;


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
        } else if (line.contains("CallDisconnect")) {
            Log.v(x, "CallDisconnect");
        } else if (line.contains("Call Finished")) {
            Log.v(x, "Call Finished");
            checkTimeOnCall = false;
            oneTimeCall = true;
            TimeUpdateOnCall();
            clickToCallButtonLayout.setVisibility(View.VISIBLE);
            clickToCallButton.setVisibility(View.VISIBLE);
            gridLayoutDailer.setVisibility(View.VISIBLE);
            gridLayoutOnCall.setVisibility(View.GONE);
            loadspeakerImage.setImageResource(R.drawable.loadspeaker);
            muteImage.setImageResource(R.drawable.mute);
            holdImage.setImageResource(R.drawable.hold);
            checkMute = true;
            checkSpeaker = true;
            checkHold = true;
            checkTimeOnCall = false;
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
                handler.removeCallbacks(updateTask);
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
