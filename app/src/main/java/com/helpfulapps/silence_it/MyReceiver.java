package com.helpfulapps.silence_it;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public SharedPreferences sharedpreferences;
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "###### Reboot Activity ######");
        sharedpreferences = context.getSharedPreferences("Switch", Context.MODE_PRIVATE);
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            //After Boot Completes activity begins again if it was already on
            if(sharedpreferences.getBoolean("Switch-State", false) == true) {
                //Switch was on
                Log.i("TAG", "Service Started After Boot");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("Notification", false);
                editor.putBoolean("Flag", false);
                editor.commit();
                context.startService(new Intent(context, siService.class));
            }else{
                //Switch was off
                Log.i("TAG", "Service Did Not Start After Boot");
            }
        }
    }
}
