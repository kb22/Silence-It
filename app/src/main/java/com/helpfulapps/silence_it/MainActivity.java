package com.helpfulapps.silence_it;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.helpfulapps.silence_it.R.id.main;

public class MainActivity extends AppCompatActivity {

    //Variables
    Switch switchAB ;
    public SharedPreferences sharedpreferences;
    private static final int REQUEST_CODE_PERMISSION = 1;
    String[] mPermission = {Manifest.permission.RECEIVE_BOOT_COMPLETED};
    private int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "###### OnCreate in MainActivity ######");
        sharedpreferences = getSharedPreferences("Switch", MODE_PRIVATE);

        try
        {
            Log.i("TAG", "Taking Admin Rights");
            mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, AdminAccess.class);
            if (!mDPM.isAdminActive(mAdminName)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate Button.");
                startActivityForResult(intent,REQUEST_CODE);
            }
            else
            {
                mDPM.lockNow();
            }
        } catch (Exception e)
        {
            Log.i("TAG", "ERROR in Taking Admin Rights: " + e.toString());
            Log.e("TAG", "ERROR in Taking Admin Rights: " + e.toString());
        }

        if (sharedpreferences.getBoolean("First Install", true)) {
            Log.i("TAG", "Runnung for First Time");
            sharedpreferences.edit().putBoolean("First Install", false).commit();
        }else{
            Log.i("TAG", "Must have been Updated");
            if(sharedpreferences.getBoolean("Switch-State", false) == true){
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("Notification", false);
                //editor.putBoolean("Flag", false);
                editor.commit();
                startService(new Intent(MainActivity.this, siService.class));
            }else{
                stopService(new Intent(MainActivity.this, siService.class));
            }
        }
        Log.i("TAG", "After Getting All Permissions and Rights");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("TAG", "###### OnCreateMenu in MainActivity ######");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        switchAB = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchAB);
        sharedpreferences = getSharedPreferences("Switch", MODE_PRIVATE);
        //Checking Initial state of App
        if(sharedpreferences.getBoolean("Switch-State", false) == true){
            switchAB.setChecked(true);
        }else{
            switchAB.setChecked(false);
        }

        //Change listener added to Switch
        switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("TAG", "Switch On");
                    //The switch is turned on
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("Switch-State", true);
                    editor.putBoolean("Flag",true);
                    editor.commit();
                    //Start the Background Service
                    PackageManager pm = MainActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(MainActivity.this, MyReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    startService(new Intent(MainActivity.this, siService.class));
                } else {
                    Log.i("TAG", "Switch Off");
                    //The switch is turned off
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("Switch-State", false);
                    editor.putBoolean("Notification", false);
                    editor.putBoolean("Flag", false);
                    editor.commit();
                    //Stop the Background Service
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
            case R.id.menu_settings:
                //Sett Activity in case about is pressed
                Log.i("TAG", "Settings Activity Opened");
                Intent intent = new Intent(this,Settings.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Code to check if the permission was granted
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission Granted");
            }else{
                //Create Snacker if permission was denied.
                Log.i("TAG", "Permission Denied");
                //Snackbar.make(findViewById(R.id.main),"Permissions were denied.",Snackbar.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "Permissions were denied.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
    mAdminName = new ComponentName(this, AdminAccess.class);
    if(mDPM.isAdminActive(mAdminName)){
        Log.i("TAG", "Admin Access Granted");
        Toast.makeText(MainActivity.this, "Admin Access Granted.", Toast.LENGTH_LONG).show();
    }else{
        Log.i("TAG", "Admin Access Denied");
        Toast.makeText(MainActivity.this, "Admin Access Denied.", Toast.LENGTH_LONG).show();
        switchAB.setChecked(false);
        finish();
    }
}
}
