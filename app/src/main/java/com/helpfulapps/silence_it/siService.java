package com.helpfulapps.silence_it;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class siService extends Service {

    //Variables
    boolean notifyenabled, VibrationMode, SilentMode;
    private int ringtype, flag = 1;
    SharedPreferences sharedpreferences;
    boolean lastWasElse = false;
    private AudioManager myAudioManager;
    public static final long NOTIFY_INTERVAL = 2000;
    private Handler handler = new Handler();
    private Timer timer = null;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        Log.i("TAG", "###### OnCreate Service ######");
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ringtype = myAudioManager.getRingerMode();
        sharedpreferences = getSharedPreferences("Switch", MODE_PRIVATE);
        //Checking for Notification
        boolean notifyenabled = sharedpreferences.getBoolean("Notification", false);

        if (notifyenabled) {
            //Notification is present
            Log.i("TAG", "Notification is Present");
        } else {
            //Create notification
            Log.i("TAG", "Notification is Absent");
            Intent main = new Intent(this, MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendIntent = PendingIntent.getActivity(this, 0, main, PendingIntent.FLAG_UPDATE_CURRENT);
            Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.main_logo);
            Notification notification = new NotificationCompat.Builder(getBaseContext())
                    .setTicker("Starting Service")
                    .setSmallIcon(R.drawable.main_logo)
                    .setLargeIcon(bitmap)
                    .setContentTitle("Silence It")
                    .setContentText("Silence It is running.")
                    .setContentIntent(pendIntent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setColor(getResources().getColor(R.color.notificationBackground))
                    .build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int smallIconViewId = getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());

                if (smallIconViewId != 0) {
                    if (notification.contentIntent != null)
                        notification.contentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                    if (notification.headsUpContentView != null)
                        notification.headsUpContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                    if (notification.bigContentView != null)
                        notification.bigContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);
                }
            }
            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
            startForeground(1, notification);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("Notification", true);
            editor.commit();
        }

        //Timer Setup
        Log.i("TAG", "Timer");
        if (timer != null) {
            timer.cancel();
        } else {

            timer = new Timer();
        }
        Log.i("TAG", "Will Schedule Timer Now");
        timer.scheduleAtFixedRate(new TimerClass(), 0, NOTIFY_INTERVAL);
    }


    class TimerClass extends TimerTask {

        @Override
        public void run() {

            handler.post(new Runnable() {

                @Override
                public void run() {
                    sharedpreferences = getSharedPreferences("Switch", MODE_PRIVATE);
                    if (myAudioManager.isMusicActive() == true) {
                        Log.i("TAG", "Music is Playing");
                        if(sharedpreferences.getBoolean("Flag", false)) {
                            ringtype = myAudioManager.getRingerMode();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean("Flag", false);
                            editor.commit();
                        }
                        if(sharedpreferences.getBoolean("Silence", false))
                            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        else myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    } else if(sharedpreferences.getBoolean("Flag", false) == false){
                        Log.i("TAG", "Music stopped");
                        switch (ringtype) {
                            case 0:
                                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                break;
                            case 1:
                                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                break;
                            case 2:
                                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                break;

                        }
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("Flag", true);
                        editor.commit();
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        Log.i("TAG", "###### OnDestroy Service ######");
        timer.cancel();
        Log.i("TAG", "Cancelled Timer");
        if (sharedpreferences.getBoolean("Notification", false)) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.cancel(1);
            Log.i("TAG", "Notification Removed");
        }
        switch (ringtype) {
            case 0:
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case 1:
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case 2:
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("Flag", false);
        editor.commit();
        Log.i("TAG", "Sound Mode Back to Original");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
