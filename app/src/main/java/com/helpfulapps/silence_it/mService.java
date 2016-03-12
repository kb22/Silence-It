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

    public mService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("IN", "in");
        /*final int id = 1234;
        Intent intenti = new Intent(this, MainActivity.class);
        intenti.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intenti, 0);

//Build the notification
        Notification.Builder builder = new Notification.Builder(getBaseContext());
        builder.setContentIntent(pendIntent);
        builder.setTicker("message");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(false);
        builder.setContentTitle("Test Service");
        builder.setContentText("message");

        Notification notification = builder.build();

//start foreground service
        startForeground(id, notification);

        Log.i("IN", "in");
        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        Log.i("IN", "in");
                        if (manager.isMusicActive()) {
                            manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        } else {
                            manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }

            }
        }).start();*/

        return flags;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("OUT", "out");
    }
}
