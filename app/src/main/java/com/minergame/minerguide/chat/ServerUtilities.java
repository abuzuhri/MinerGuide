package com.minergame.minerguide.chat;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.minergame.minerguide.R;
import com.minergame.minerguide.utils.AppLog;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public final class ServerUtilities {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    private static String registrationId;
    private static GoogleCloudMessaging gcm;
    /**
     * Register this account/device pair within the server.
     *
     */
    static void register(final Context context, String name, String email) throws IOException {

        String SENDER_ID = context.getString(R.string.gcm_project_key);
        gcm = GoogleCloudMessaging.getInstance(context);

            registrationId = gcm.register(SENDER_ID);

        Log.i(Utils.TAG, "registering device (regId = " + registrationId + ")");
        String serverUrl = Utils. SERVER_URL_REGISTER;
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", registrationId);
        params.put("name", name);
        params.put("email", email);

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(Utils.TAG, "Attempt #" + i + " to register");
            try {
                Utils.displayMessage(context, context.getString(R.string.server_registering, i, MAX_ATTEMPTS));
                post2(serverUrl, params);
                //GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);
                Utils.displayMessage(context, message);
                return;
            } catch (Exception e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(Utils.TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(Utils.TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(Utils.TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        Utils. displayMessage(context, message);
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context) {
        Log.i(Utils.TAG, "unregistering device (regId = " + registrationId + ")");
        String serverUrl = Utils.SERVER_URL_SENDMSG + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", registrationId);
        try {
            post2(serverUrl, params);
            gcm.unregister();
            //GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            Utils.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,e.getMessage());
            Utils.displayMessage(context, message);
        }
    }


    private static void post2(String endpoint, Map<String, String> params){
        AppLog.i("endpoint== >"+endpoint);
        OkHttpClient client = new OkHttpClient();

        FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            formBodyBuilder.add(param.getKey(),param.getValue());
        }

        RequestBody formBody=formBodyBuilder.build();

        Request request = new Request.Builder()
                .url(endpoint)
                .post(formBody)
                .build();


        try {
            Response response = client.newCall(request).execute();
            String str=response.body().string();
            //return str;
            AppLog.i("response.body().string(); == >"+str );
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {

        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(Utils.TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}