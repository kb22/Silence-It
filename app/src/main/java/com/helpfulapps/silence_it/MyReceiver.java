package com.helpfulapps.silence_it;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.i("TAG", "BootCompleted");
            Intent pushIntent = new Intent(context, mService.class);
            Log.i("TAG", "BootCompleted2");
            pushIntent.putExtra("Flag", true);
            context.startService(pushIntent);
        }
    }
}
