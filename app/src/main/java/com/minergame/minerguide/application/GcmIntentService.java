package com.minergame.minerguide.application;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.minergame.minerguide.chat.ChatListener;

public class GcmIntentService extends IntentService {

    private String TAG="tg";


    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        ChatListener.OnReciveMessageFromService(this,intent);

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}