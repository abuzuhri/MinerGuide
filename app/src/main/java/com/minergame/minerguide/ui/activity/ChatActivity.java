package com.minergame.minerguide.ui.activity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.minergame.minerguide.R;
import com.minergame.minerguide.chat.ChatListener;
import com.minergame.minerguide.chat.IChatMessage;
import com.minergame.minerguide.chat.MessagesListAdapter;
import com.minergame.minerguide.chat.Utils;
import com.minergame.minerguide.db.Dao.MessageDao;
import com.minergame.minerguide.db.Entity.Message;
import com.minergame.minerguide.utils.AppLog;

import java.util.List;

/**
 * Created by Tareq on 04/03/2015.
 */
public class ChatActivity extends BaseActivity {

    private Button btnSend;
    private EditText inputMsg;


    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;

    private Utils utils;
    private ChatListener client;
    // Client name
    private String name = null;
    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";



    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        utils = new Utils(getApplicationContext());

        // Getting the person name from previous screen
        Intent i = getIntent();
        name = i.getStringExtra("name");
        name="Tareq";






        client=new ChatListener(this,new IChatMessage() {
            @Override
            public void onMessage(Message message) {

                appendMessage(message);

            }

            @Override
            public void onConnect() {
                AppLog.i("onConnect  onConnect  onConnect  onConnect  ");
            }

            @Override
            public void onDisconnect(int code, String reason) {
                ChatActivity.this.finish();
            }

            @Override
            public void onError(Exception error) {

            }
        });
        client.connect();



        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
                sendMessageToServer(inputMsg.getText().toString());

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });


    }

    private  void fillChatHiestory()
    {
        listMessages = new MessageDao().getAllMessage();

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);
    }



    @Override
    protected void onResume() {
        super.onResume();
        client.onResume();
        fillChatHiestory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        client.onPause();
    }

    private void sendMessageToServer(String message) {
        if (client != null && client.isConnected()) {
            client.send(message);
        }
    }

    /**
     * Appending message to list view
     * */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep(m);
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Plays device's default notification sound
     * */
    public void playBeep(Message msg) {

        try {
            if(!msg.isSelf) {
                Uri notification = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                        notification);
                r.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
