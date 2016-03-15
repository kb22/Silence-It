package com.helpfulapps.silence_it;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Switch switchAB ;
    TextView tx;
    Switch swtService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "OnCreate in Activity");
        tx = (TextView)findViewById(R.id.textView);
        tx.setMovementMethod(new ScrollingMovementMethod());
        swtService = (Switch)findViewById(R.id.myswitch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        switchAB = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);
        SharedPreferences sharedPrefs = getSharedPreferences("silence", MODE_PRIVATE);
        switchAB.setChecked(sharedPrefs.getBoolean("Switch-State", false));
        Log.i("TAG", "OnCreateMenu in Activity");
        switchAB.setOnCheckedChangeListener(new

        CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton

                 buttonView,
         boolean isChecked) {
                if (isChecked) {
                    Log.i("TAG", "Switch On");
                    SharedPreferences.Editor editor = getSharedPreferences("silence", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch-State", true);
                    editor.apply();
                    PackageManager pm = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, MyReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    startService(new Intent(MainActivity.this, siService.class));
                } else {
                    Log.i("TAG", "Switch Off");
                    SharedPreferences.Editor editor = getSharedPreferences("silence", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch-State", false);
                    editor.apply();
                    PackageManager pm = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, MyReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    stopService(new Intent(MainActivity.this, siService.class));
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_about:
                Intent intent = new Intent(this,activity_about.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
