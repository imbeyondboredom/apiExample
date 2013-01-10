package com.willowtreeapps.examples.activities;

import com.actionbarsherlock.view.Menu;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.willowtreeapps.examples.R;
import com.willowtreeapps.examples.contentprovider.AudioContentProvider;
import com.willowtreeapps.examples.intentservice.ApiIntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import roboguice.inject.InjectView;

/**
 * User: charlie Date: 1/8/13 Time: 12:51 PM
 */
public class PublicListActivity extends RoboSherlockFragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.listview) ListView mListView;
    @InjectView(R.id.progress) ProgressBar mProgressBar;

    private static final int LOADER_ID = 0;

    private ApiFileCursorAdapter mAdapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //indicate to the user that there is a problem connecting to the internet
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view
        setContentView(R.layout.example_list_view);

        //Get the loader and see if it's running (we were rotated)
        Loader<Object> loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if(loader == null || loader.isStarted())
        {
            setLoaderRunning(true);
        }
        else
        {
            setLoaderRunning(false);
        }

        getSupportLoaderManager().initLoader(LOADER_ID,null,this);

        //request the service to reload if it needs to
        Intent intent = new Intent(this, ApiIntentService.class);
        intent.putExtra("switcher", ApiIntentService.GETFILES);
        startService(intent);
    }

    private void setLoaderRunning(boolean running)
    {
        if(running)
        {
            mProgressBar.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.refresh,menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i,
            Bundle bundle) {
        Uri uri = AudioContentProvider.CONTENT_URI;
        String selection = null;
        String[] projection = {
                AudioContentProvider.AudioFiles._ID,
                AudioContentProvider.AudioFiles.PATH
        };
        String[] args = null;

        return new CursorLoader(this,uri,projection,selection,args,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> resultOrExceptionLoader, Cursor c) {
        //If there are no results.. keep spinning
        if(c.getCount() ==0)
        {
            setLoaderRunning(true);
        }
        else
        {
            //Init the array adapter
            mAdapter = new ApiFileCursorAdapter(this, c, true);
            mListView.setAdapter(mAdapter);
            setLoaderRunning(false);
        }
    }

    @Override
    public void onLoaderReset(
            Loader<Cursor> resultOrExceptionLoader) {
        //Ignore, not needed
    }

    private class ApiFileCursorAdapter extends CursorAdapter {

        public ApiFileCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

            View v =  getLayoutInflater().inflate(android.R.layout.simple_list_item_1,null);

            ApiFileViewHolder holder = new ApiFileViewHolder();
            holder.textview = (TextView)v.findViewById(android.R.id.text1);
            v.setTag(holder);

            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ApiFileViewHolder holder = (ApiFileViewHolder)view.getTag();
            holder.textview.setText(cursor.getString(cursor.getColumnIndex(AudioContentProvider.AudioFiles.PATH)));
        }
    }

    private static class ApiFileViewHolder
    {
        public TextView textview;
    }
}
