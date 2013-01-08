package com.willowtreeapps.examples.activities;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.willowtreeapps.examples.R;

import android.os.Bundle;
import android.widget.ListView;

import roboguice.inject.InjectView;

/**
 * User: charlie Date: 1/8/13 Time: 12:51 PM
 */
public class PrivateListActivity extends RoboSherlockFragmentActivity {

    @InjectView(R.id.listview) ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.example_list_view);

        //Now what?

    }
}
