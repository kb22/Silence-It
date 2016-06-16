package com.helpfulapps.silence_it;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    boolean flag = false;
    String[] titles = {
            "Sound Mode",
            "Start on Reboot"
    };
    String[] desc = {
            "Select Mode to Vibrate or be Silent",
            "Select what to do on Reboot"
    };
    Integer[] images={
            R.drawable.mode,
            R.drawable.restart,
    };

    String[] titles2 = {
            "About the App",
            "About the Developer"
    };
    String[] desc2 = {
            "Get to know about Key Details.",
            "Reach out to the Developer."
    };
    Integer[] images2 ={
            R.drawable.mode,
            R.drawable.restart,
    };
    AlertDialog levelDialog;
    public SharedPreferences sharedpreferences;
    final CharSequence[] items = {"Silence","Vibration"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedpreferences = getSharedPreferences("Switch", MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Settings");

        ListAdapter adapter = new ListAdapter(this, titles, desc, images);
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        ListAdapter adapter2 = new ListAdapter(this, titles2, desc2, images2);
        ListView list2=(ListView)findViewById(R.id.list2);
        list2.setAdapter(adapter2);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                        builder.setTitle("Sound Mode");
                        //builder.setMessage("Whenever the music plays,\nthe notifications should be\ncompleted muted(Silence)\nor should vibration be enabled\nso that you get notified\nwith a vibration.");
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch(item)
                                {
                                    case 0:
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putBoolean("Silence", true);
                                        editor.commit();
                                        break;
                                    case 1:
                                        SharedPreferences.Editor editor2 = sharedpreferences.edit();
                                        editor2.putBoolean("Silence", false);
                                        editor2.commit();
                                        break;

                                }
                                levelDialog.dismiss();
                            }
                        });
                        levelDialog = builder.create();
                        levelDialog.show();
                        break;
                }



            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch(position){
                    case 0:
                        //About Activity in case About is pressed
                        Log.i("TAG", "About Activity Opened");
                        Intent intent = new Intent(Settings.this,activity_about.class);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

}
