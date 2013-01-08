package com.willowtreeapps.examples.activities;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.willowtreeapps.examples.MainApp;
import com.willowtreeapps.examples.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import roboguice.inject.InjectView;

public class StartupActivity extends RoboSherlockFragmentActivity {

    @InjectView(R.id.public_list_button) Button publicListButton;
    @InjectView(R.id.private_list_button) Button privateListButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainApp.TAG, "onCreate");

        setContentView(R.layout.startup);

        publicListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartupActivity.this,PublicListActivity.class);
                startActivity(i);
            }
        });

        privateListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

