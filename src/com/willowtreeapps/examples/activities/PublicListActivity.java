package com.willowtreeapps.examples.activities;

import com.actionbarsherlock.view.Menu;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.willowtreeapps.examples.R;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import roboguice.inject.InjectView;

/**
 * User: charlie Date: 1/8/13 Time: 12:51 PM
 */
public class PublicListActivity extends RoboSherlockFragmentActivity {

    @InjectView(R.id.listview) ListView mListView;
    @InjectView(R.id.progress) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.example_list_view);

        //Now what?

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.refresh,menu);
        return true;
    }
}
