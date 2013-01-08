package com.willowtreeapps.examples.activities;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.willowtreeapps.examples.R;

import android.os.Bundle;
import android.widget.Button;

import roboguice.inject.InjectView;

/**
 * User: charlie Date: 1/8/13 Time: 1:09 PM
 */
public class LoginActivity extends RoboSherlockFragmentActivity {

    @InjectView(R.id.login_button) Button mLoginButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        //TODO Fill me in

    }
}