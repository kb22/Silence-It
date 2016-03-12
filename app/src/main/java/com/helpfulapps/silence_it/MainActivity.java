package com.helpfulapps.silence_it;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Switch switchAB ;
    /*Intent intent = new Intent(MainActivity.this,
            mService.class);*/
    Switch swtService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swtService = (Switch)findViewById(R.id.myswitch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        switchAB = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);
        SharedPreferences sharedPrefs = getSharedPreferences("silence", MODE_PRIVATE);
        switchAB.setChecked(sharedPrefs.getBoolean("Switch-State", false));

        switchAB.setOnCheckedChangeListener(new

        CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton

                                                 buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("silence", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch-State", true);
                    editor.apply();
                    startService(new Intent(MainActivity.this,mService.class));
                    /*Toast.makeText(getApplication(), "ON",

                            Toast.LENGTH_SHORT)
                            .show();*/

                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("silence", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch-State", false);
                    editor.apply();
                    stopService(new Intent(MainActivity.this, mService.class));
                    /*Toast.makeText(getApplication(), "OFF",

                            Toast.LENGTH_SHORT)
                            .show();*/
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
