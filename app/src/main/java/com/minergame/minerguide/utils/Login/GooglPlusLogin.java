package com.minergame.minerguide.utils.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.minergame.minerguide.utils.AppLog;

import java.util.UUID;

/**
 * Created by Tareq on 04/18/2015.
 */
public class GooglPlusLogin implements SocialNetwork
{

    private Activity activity;
    protected SharedPreferences mSharedPreferences;

    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mConnectRequested;

    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    private OnLoginListener lsnr;
    private static final int REQUEST_AUTH = UUID.randomUUID().hashCode() & 0xFFFF;
    private static final String SAVE_STATE_KEY_IS_CONNECTED = "GooglePlusSocialNetwork.SAVE_STATE_KEY_OAUTH_TOKEN";
    private static final String SHARED_PREFERENCES_NAME = "social_networks";


    public GooglPlusLogin(final Activity activity){
        this.activity=activity;
        mSharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, activity.MODE_PRIVATE);
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks(){

                    @Override
                    public void onConnected(Bundle bundle) {
                        mSignInClicked = false;
                        AppLog.i("onConnected");

                        getProfileInfo();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener(){

                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        AppLog.i("onConnectionFailed   result.hasResolution()"+result.hasResolution());
                        if (!result.hasResolution()) {
                            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), activity, 0).show();
                            return;
                        }

                        AppLog.i("mIntentInProgress "+mIntentInProgress);

                        if (!mIntentInProgress) {
                            // Store the ConnectionResult for later usage
                            mConnectionResult = result;
                            AppLog.i("mSignInClicked "+mSignInClicked);
                            if (mSignInClicked) {
                                resolveSignInError();
                            }
                        }
                    }
                })
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                        //.addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public boolean isLoggedIn()
    {
        return mSharedPreferences.getBoolean(SAVE_STATE_KEY_IS_CONNECTED, false);
    }

    public  void Logout(){
        mConnectRequested = false;

        mSharedPreferences.edit().remove(SAVE_STATE_KEY_IS_CONNECTED).commit();
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                AppLog.i("mIntentInProgress "+mIntentInProgress);
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(activity, REQUEST_AUTH);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    public void Login(final OnLoginListener lsnr)
    {
        this.lsnr=lsnr;
        mConnectRequested = true;
        mSignInClicked=true;



        mGoogleApiClient.connect();

        AppLog.i("mGoogleApiClient.isConnecting() "+mGoogleApiClient.isConnecting());


        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        AppLog.i("onActivityResult ");
        if (requestCode == REQUEST_AUTH) {
            if (resultCode  !=  Activity.RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    private void getProfileInfo(){
        /* This Line is the key */
       // Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            if (lsnr != null) {
                mSharedPreferences.edit().putBoolean(SAVE_STATE_KEY_IS_CONNECTED, true).commit();

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                SocialUser user=new SocialUser();
                user.network=SocialUser.NetworkType.GOOGLEPLUS;
                user.avatarURL=currentPerson.getImage().getUrl();
                user.name=currentPerson.getDisplayName();
                user.email=email;
                user.id=currentPerson.getId();

                lsnr.onSuccess(user);
            }

            return;
        }else if (lsnr != null) {
            AppLog.i("get person == null");
            lsnr.onFail(); //"get person == null"
        }
    }
/*
    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        AppLog.i("onConnected");

        getProfileInfo();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        AppLog.i("onConnectionFailed   result.hasResolution()"+result.hasResolution());
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), activity,0).show();
            return;
        }

        AppLog.i("mIntentInProgress "+mIntentInProgress);

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;
            AppLog.i("mSignInClicked "+mSignInClicked);
            if (mSignInClicked) {
                resolveSignInError();
            }
        }

    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {
        AppLog.i("onResult");
        if (loadPeopleResult.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
            try {
                int count = personBuffer.getCount();

                for (int i = 0; i < count; i++) {
                    Log.d("TEST", "Display name: " + personBuffer.get(i).getDisplayName());

                   // friends.add(personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();

            }
        } else {
            Log.e("TEST", "Error requesting visible circles: " + loadPeopleResult.getStatus());
        }

    }
    */
}
