package com.willowtreeapps.examples.intentservice;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.willowtreeapps.examples.activities.IntentServiceActivity;
import com.willowtreeapps.examples.api.ApiFile;
import com.willowtreeapps.examples.api.ApiUser;
import com.willowtreeapps.examples.api.ExampleApi;
import com.willowtreeapps.examples.contentprovider.AudioContentProvider;
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
}
