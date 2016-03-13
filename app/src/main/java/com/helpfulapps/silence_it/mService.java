package com.helpfulapps.silence_it;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

public class mService extends Service {

    boolean flag = true;
    boolean keeprunning = true;
    public mService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show();
        flag = intent.getExtras().getBoolean("Flag");
        Log.i("TAG","FlagSet: " + flag);
        if(flag) {
            keeprunning = true;
            Log.i("TAG", "MiddleTop");
            final int id = 1234;
            Intent intenti = new Intent(this, MainActivity.class);
            intenti.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intenti, 0);

//Build the notification
            /*Notification.Builder builder = new Notification.Builder(getBaseContext());
            builder.setContentIntent(pendIntent);
            builder.setTicker("Service Started");
            builder.setWhen(System.currentTimeMillis());
            builder.setAutoCancel(false);
            builder.setContentTitle("Silence It");
            builder.setContentText("Touch to disable in Options");
            //builder.setAutoCancel(true);

            Notification notification = builder.build();

//start foreground service
            startForeground(id, notification);*/

            Log.i("TAG", "Start");
            new Thread(new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub
                    while (keeprunning) {
                        try {
                            AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            Log.i("TAG", "Middle");
                            if (manager.isMusicActive()) {
                                manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            } else {
                                manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            }
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }
        else
        {
            onDestroy();
        }
        return flags;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keeprunning = false;
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        //Toast.makeText(this,"Bye",Toast.LENGTH_LONG).show();
        if(flag)
        sendBroadcast(new Intent("YouWillNeverKillMe"));
        Log.i("TAG", "Exit");
    }

}
