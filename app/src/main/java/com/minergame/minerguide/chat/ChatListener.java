package com.minergame.minerguide.chat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Patterns;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.minergame.minerguide.R;
import com.minergame.minerguide.application.myApplication;
import com.minergame.minerguide.db.Entity.Message;
import com.minergame.minerguide.ui.activity.ChatActivity;
import com.minergame.minerguide.utils.AppLog;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Tareq on 04/06/2015.
 */
public class ChatListener  {
    private static   boolean isAppRunning;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private String regid;


    public static final int NOTIFICATION_ID = 1;
    public static  String sessionId;
    private Activity activity;
    private static IChatMessage ichat;
    private GoogleCloudMessaging gcm;
    private boolean isConnectedToChat=false;

    public ChatListener(Activity activity,IChatMessage ichat){
        this.activity=activity;
        this.ichat=ichat;


    }

    public static void  OnReciveMessageFromService(Context context, Intent intent){
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging. MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                AppLog.i("message  "+extras.toString());

                String message = extras.getString("message");
                AppLog.i("message =" + message);
                String sessionId = extras.getString("sessionId");
                AppLog.i("sessionId ="+sessionId);
                String name=extras.getString("name");
                AppLog.i("name ="+name);

                ReceiveIntentMsg(context, intent);

            }
        }
    }

    public void  connect(){
        //LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, new IntentFilter("speedExceeded"));


        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(activity);
            regid = getRegistrationId(activity);

            if (regid.isEmpty()) {
                registerInBackground(new Listener.DoWork() {
                    @Override
                    public void OnFinish() {
                        isConnectedToChat=true;
                        ichat.onConnect();
                    }
                });
            }
            else
            {
                sendRegistrationIdToBackendInBackground(new Listener.DoWork() {
                    @Override
                    public void OnFinish() {
                        isConnectedToChat = true;
                        ichat.onConnect();
                    }
                });
            }
        }
    }
    public void  disConnect(){

    }

    public boolean isConnected(){ return isConnectedToChat;}
    public void send(String msg){
        if(!msg.trim().isEmpty()) {
            sendMessageToServerInBackground(msg);
        }
    }

    public static void ReceiveIntentMsg(Context context, Intent intent){
        String action = intent.getAction();

        Bundle extras = intent.getExtras();

        String message = extras.getString("message");
        AppLog.i("message 2222" + message);
        String pSsessionId = extras.getString("sessionId");
        AppLog.i("sessionId 222"+sessionId);
        String name=extras.getString("name");
        AppLog.i("name 222"+name);

        boolean isSelf=false;



        if(sessionId!=null && sessionId.equals(pSsessionId))
            isSelf=true;

        Message msg=new Message(name,message,isSelf);
        msg.save();

        if(ichat!=null)
            ichat.onMessage(msg);

        if(!msg.isSelf && !isAppRunning) {
            sendNotification(context, name, message);
        }
    }


    public void onResume(){
        isAppRunning=true;
        AppLog.i("isAppRunning == > "+isAppRunning);
    }
    public void onPause(){
        isAppRunning=false;
        AppLog.i("isAppRunning == > "+isAppRunning);
    }

    public static boolean isChatRunning(){
        return  isAppRunning;
    }
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public static void sendNotification(Context context,String name,String msg) {

        NotificationCompat.Builder builder;

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, ChatActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(name)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        // Cancel the notification after its selected
        mBuilder.setAutoCancel(true);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    private void sendRegistrationIdToBackendInBackground(final Listener.DoWork work){
        AsyncTask asyncTask=   new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.

                sendRegistrationIdToBackend(regid);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                //super.onPostExecute(o);
                work.OnFinish();
            }
        };

        asyncTask.execute();
    }
    private void registerInBackground(final Listener.DoWork work) {

        AsyncTask asyncTask=   new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(activity);
                    }
                    String SENDER_ID = activity.getString(R.string.gcm_project_number);
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.

                    storeRegistrationId(activity, regid);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.

                     sendRegistrationIdToBackend(regid);


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object o) {
                //super.onPostExecute(o);
                work.OnFinish();
            }
        };

        asyncTask.execute();


    }

    private void sendMessageToServerInBackground(final String msg){
        AsyncTask asyncTask=   new AsyncTask(){

            @Override
            protected Void doInBackground(Object... params) {
                sendMessageToServer(msg);
                return null;
            }
        };
        asyncTask.execute();
    }


    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) myApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static String post(String endpoint, Map<String, String> params){

        if(isNetworkAvailable()) {
            AppLog.i("endpoint== >" + endpoint);
            OkHttpClient client = new OkHttpClient();

            FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            // constructs the POST body using the parameters
            while (iterator.hasNext()) {
                Map.Entry<String, String> param = iterator.next();
                formBodyBuilder.add(param.getKey(), param.getValue());
            }

            RequestBody formBody = formBodyBuilder.build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(formBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                String str = response.body().string();
                AppLog.i("response.body().string(); == >" + str);

                return str;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }else{

            return "";
        }

    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        AppLog.i("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String registrationId) {
        // Your implementation here.
        String serverUrl = Utils. SERVER_URL_REGISTER;
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", registrationId);
        params.put("name", "Tareq");
        params.put("email", getEmail());
        String postResponse= post(serverUrl, params);
        try {
            JSONObject obj = new JSONObject(postResponse);
            sessionId= obj.getLong("sessionId")+"";
            AppLog.i("sessionId =" + sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private String getEmail(){
        String email="";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(activity).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                return  possibleEmail;
            }
        }
        return "";
    }

    private void sendMessageToServer(String msg) {
        // Your implementation here.
        String serverUrl = Utils. SERVER_URL_SENDMSG;
        Map<String, String> params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        params.put("message", msg);
        params.put("name", "Tareq");
        String postResponse= post(serverUrl, params);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                AppLog.i("This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }


    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            AppLog.i( "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion =getAppVersion(context);
        if (registeredVersion != currentVersion) {
            AppLog.i("App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return activity. getSharedPreferences(activity.getPackageName(),Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
