package com.willowtreeapps.examples.intentservice;

import com.google.gson.Gson;
import com.google.inject.Inject;

import com.willowtreeapps.examples.activities.IntentServiceActivity;
import com.willowtreeapps.examples.api.ApiFile;
import com.willowtreeapps.examples.api.ApiUser;
import com.willowtreeapps.examples.api.ExampleApi;
import com.willowtreeapps.examples.contentprovider.AudioContentProvider;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import roboguice.service.RoboIntentService;

/**
 * Created with IntelliJ IDEA.
 * User: ericrichardson
 * Date: 1/8/13
 * Time: 10:22 AM
 */
public class ApiIntentService extends RoboIntentService {
    public static final int UPLOAD = 1;
    public static final int LOGIN = 2;
    public static final int GETFILES = 3;
    private int result = Activity.RESULT_CANCELED;
    public boolean busy = false;

    @Inject
    Gson gson;

    public ApiIntentService() {
        super("ApiIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int type = intent.getIntExtra("switcher", 0);
        switch(type){
            case UPLOAD: doUpload(intent);
                break;
            case LOGIN: doLogin(intent);
                break;
            case GETFILES: getFiles(intent);
                break;
            default:
                break;
        }
    }


    private void doUpload(Intent i){
        busy = true;
        ApiFile file = null;
        Bundle extras = i.getExtras();
        try {
            file = ExampleApi.uploadFile(extras.getInt("userID"), extras.getString("type"), extras.getString("path"), extras.getString("date"));
        } catch (NetworkErrorException e) {
            Log.e("Service", e.getMessage());
        }
        if(file != null){
            ContentValues values = new ContentValues();
            values.put(AudioContentProvider.AudioFiles.TYPE, file.type);
            values.put(AudioContentProvider.AudioFiles.PATH,file.url);
            values.put(AudioContentProvider.AudioFiles.DATE,file.date);
            getContentResolver().insert(AudioContentProvider.AudioFiles.CONTENT_URI,values);
            Intent broadcast = new Intent();
            broadcast.setAction(IntentServiceActivity.RECEIVER);
            broadcast.addCategory(Intent.CATEGORY_DEFAULT);
            broadcast.putExtra("switcher", extras.getInt("switcher"));
            broadcast.putExtra("url", file.url);
            sendBroadcast(broadcast);
        }
        busy = false;
    }

    private void doLogin(Intent i){
        busy = true;
        ApiUser user = null;
        Bundle extras = i.getExtras();
        try {
            user = ExampleApi.LogIn(extras.getString("user"), extras.getString("pass"));
        } catch (NetworkErrorException e) {
            Log.e("Service", e.getMessage());
        }
        if(user != null){
            Intent broadcast = new Intent();
            broadcast.setAction(IntentServiceActivity.RECEIVER);
            broadcast.addCategory(Intent.CATEGORY_DEFAULT);
            broadcast.putExtra("switcher", extras.getInt("switcher"));
            broadcast.putExtra("user", gson.toJson(user));
            sendBroadcast(broadcast);
        }
        busy = false;
    }


    //Added for getting a list of files
    public static final String ERROR_RECEIVER = "com.willowtreeaps.intent.LIST_ERROR";
    private static long lastRequested = 0;
    private void getFiles(Intent i)
    {
        //If the time elapsed time is less than 15 minutes and is not forced then don't do anything
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if(currentTime - lastRequested < 900000 && i.getBooleanExtra("force",false))
        {
            return;
        }

        //Otherwise update the list of files
        busy = true;
        try
        {
            ArrayList<ApiFile> files = ExampleApi.getPublicFiles();
            ContentValues values[] = new ContentValues[files.size()];
            for(int a=0; a<files.size(); a++)
            {
                ContentValues value = new ContentValues();
                ApiFile file = files.get(a);
                value.put(AudioContentProvider.AudioFiles.DATE, file.date);
                value.put(AudioContentProvider.AudioFiles.TYPE, file.type);
                value.put(AudioContentProvider.AudioFiles.PATH, file.url);
                value.put(AudioContentProvider.AudioFiles.PATH, file.url);
                values[a] = value;
            }
            getContentResolver().bulkInsert(AudioContentProvider.CONTENT_URI, values);
        }
        //If there is an exception then broadcast it
        catch(Exception e)
        {
            Intent broadcast = new Intent();
            broadcast.setAction(ApiIntentService.ERROR_RECEIVER);
            broadcast.addCategory(Intent.CATEGORY_DEFAULT);
            broadcast.putExtra("exception", e);
            sendBroadcast(broadcast);
        }
        busy = false;
    }
}
