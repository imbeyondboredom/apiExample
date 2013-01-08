package com.willowtreeapps.examples.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.view.Window;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.willowtreeapps.examples.R;
import com.willowtreeapps.examples.api.ApiUser;
import com.willowtreeapps.examples.intentservice.ApiIntentService;
import roboguice.inject.InjectView;

/**
 * Created with IntelliJ IDEA.
 * User: ericrichardson
 * Date: 1/8/13
 * Time: 10:43 AM
 */
public class IntentServiceActivity extends RoboSherlockFragmentActivity {
    public static final String RECEIVER = "com.willowtreeaps.intent.MESSAGE";
    @InjectView(R.id.button1)Button login;
    @InjectView(R.id.button2)Button upload;
    @InjectView(R.id.user)EditText user;
    @InjectView(R.id.password)EditText pass;
    @Inject
    Gson gson;
    @Inject
    ApiIntentService service;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            switch(b.getInt("switcher")){
                case ApiIntentService.LOGIN:
                    ApiUser user = gson.fromJson(b.getString("user"), ApiUser.class);
                    Toast.makeText(IntentServiceActivity.this, "Logged in: "+user.userName+", with ID "+user.userID, Toast.LENGTH_SHORT).show();
                    break;
                case ApiIntentService.UPLOAD:
                    Toast.makeText(IntentServiceActivity.this, "File Uploaded: "+b.getString("url"), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setSupportProgressBarIndeterminate(service.busy);
        setContentView(R.layout.intent_service_buttons);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(user.getText().toString(), pass.getText().toString());
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(12, "test", "www.test.com", "1/8/13");
            }
        });
        IntentFilter filter = new IntentFilter(RECEIVER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void login(String user, String pass){
        setSupportProgressBarIndeterminate(true);
        Intent intent = new Intent(this, ApiIntentService.class);
        intent.putExtra("user", user);
        intent.putExtra("pass", pass);
        intent.putExtra("switcher", ApiIntentService.LOGIN);
        startService(intent);
    }

    private void upload(int userId, String type, String url, String date){
        setSupportProgressBarIndeterminate(true);
        Intent intent = new Intent(this, ApiIntentService.class);
        intent.putExtra("type", type);
        intent.putExtra("path", url);
        intent.putExtra("date", date);
        intent.putExtra("userID", userId);
        intent.putExtra("switcher", ApiIntentService.UPLOAD);
        startService(intent);
    }
}
