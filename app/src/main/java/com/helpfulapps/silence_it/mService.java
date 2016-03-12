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
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

public class mService extends Service {

    boolean keeprunning = true;
    public mService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show();
        final int id = 1234;
        Intent intenti = new Intent(this, MainActivity.class);
        intenti.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intenti, 0);

//Build the notification
        Notification.Builder builder = new Notification.Builder(getBaseContext());
        builder.setContentIntent(pendIntent);
        builder.setTicker("Service Started");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(false);
        builder.setContentTitle("Silence It");
        builder.setContentText("Touch to disable in Options");

        Notification notification = builder.build();

//start foreground service
        startForeground(id, notification);

        Log.i("TAG", "Start");
        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true && keeprunning)
                {
                    try {
                        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        Log.i("TAG", "Middle");
                        if (manager.isMusicActive()) {
                            manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        } else {
                            manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
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
        Log.i("TAG", "Exit");
    }

}
