package com.helpfulapps.silence_it;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hp-user on 16-03-2016.
 */
public class siService extends Service{

    boolean notifyenabled, vibenabled;
    boolean lastWasElse = false;
    boolean bootedVibe;
    private AudioManager myAudioManager;
    public static final long NOTIFY_INTERVAL = 2000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private NotificationManager mNotificationManager;


    @Override
    public void onCreate() {
        Log.i("TAG", "ONCreate Service");
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        if (myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)  {
            bootedVibe = true;
        } else {
            bootedVibe = false;
        }

        //Get preferences and check if persistent status bar notification is enabled
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);

        if (notifyenabled) {
            //Don't Show notification
        }
        else{
            //Show notification
            //Notification notification = new Notification(R.drawable.main_icon, "Service Started", System.currentTimeMillis());

            Intent main = new Intent(this, MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendIntent = PendingIntent.getActivity(this, 0, main,  PendingIntent.FLAG_UPDATE_CURRENT);

            //notification.setLatestEventInfo(this, "Silence it", "App is running", pendingIntent);

            /*Intent intenti = new Intent(this, MainActivity.class);
            intenti.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intenti, 0);*/

            Resources r = getResources();
            Notification notification = new NotificationCompat.Builder(getBaseContext())
                    .setTicker("Start")
                    .setSmallIcon(R.drawable.main_icon)
                    .setContentTitle("Silence it")
                    .setContentText("Silence It is running.")
                    .setContentIntent(pendIntent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();

            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
            startForeground(1, notification);
        }

        Log.i("TAG", "mTimer");
        if(mTimer != null) {
            mTimer.cancel();
        } else {

            mTimer = new Timer();
        }
        Log.i("TAG", "Schedule Timer");
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }



    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {

            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    Log.i("TAG", "In new thread");
                    //Check if music is playing
                    if(myAudioManager.isMusicActive() == true) {
                        Log.i("TAG", "Loop");
                        //Music is playing, so set ringer to silent
                        if(myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                        } else {
                            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        }
                        //Manages the boolean so you can decide whether you prefer vibrate or default ringer while the service is running
                        lastWasElse = false;
                    } else if (!lastWasElse) {
                        if(myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {

                        } else {
                            if(bootedVibe) {

                            } else {
                                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            }
                        }
                        lastWasElse = true;
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        Log.i("TAG", "OnDestroy Sevice");
        //Decide what needs to be canceled
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);
        boolean ringdef = mySharedPreferences.getBoolean("ringdef", false);

        mTimer.cancel();

        Log.i("TAG", "mTimer killed");
        if(notifyenabled)
        {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.cancel(1);
        }
        //Return to default ringer, or just release control of the ringer settings?
        if (ringdef) {
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
