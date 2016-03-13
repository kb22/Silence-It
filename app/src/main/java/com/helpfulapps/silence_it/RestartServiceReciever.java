package com.helpfulapps.silence_it;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartServiceReciever extends BroadcastReceiver {
    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "onReceive");
        intent = new Intent(context,mService.class);
        intent.putExtra("Flag", true);
        context.startService(intent);
    }
}
