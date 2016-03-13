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
    final Intent intent2 = new Intent(MainActivity.this, mService.class);
    /*Intent intent = new Intent(MainActivity.this,
            mService.class);*/
    Switch swtService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.main_logo);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("  Silence It");*/
        //ActionBar mActionBar = getSupportActionBar();
        //mActionBar.setIcon(R.drawable.main_icon);
        //mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);

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
        Log.i("TAG", "OnCreateMain");
        switchAB.setOnCheckedChangeListener(new

        CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton

                 buttonView,
         boolean isChecked) {
                if (isChecked) {
                    Log.i("TAG", "IsChecked");
                    SharedPreferences.Editor editor = getSharedPreferences("silence", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch-State", true);
                    editor.apply();
                    PackageManager pm = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, MyReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Intent intent2 = new Intent(MainActivity.this, mService.class);
                    intent2.putExtra("Flag", true);
                    startService(intent2);

                } else {
                    Log.i("TAG", "IsNotChecked");
                    SharedPreferences.Editor editor = getSharedPreferences("silence", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch-State", false);
                    editor.apply();
                    PackageManager pm = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, MyReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Intent intent2 = new Intent(MainActivity.this, mService.class);
                    intent2.putExtra("Flag", false);
                    startService(intent2);
                }
            }
        });
        if (switchAB.isChecked() && !isMyServiceRunning(mService.class)) {
            Intent intent2 = new Intent(MainActivity.this, mService.class);
            Log.i("TAG", "RunAgain");
            intent2.putExtra("Flag", true);
            Log.i("TAG", "RunAgain");
            startService(intent2);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
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

    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context,mService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        stopService(intent2);
        Log.i("TAG", "onDestroyMain");
        super.onDestroy();

    }
}
