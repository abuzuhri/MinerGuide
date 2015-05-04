package com.minergame.minerguide.utils.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.minergame.minerguide.utils.AppLog;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class FacebookLogin implements SocialNetwork {

    private Activity activity;
    public CallbackManager callbackManager;


    public FacebookLogin(Activity activity){
        this.activity=activity;
    }

    public boolean isLoggedIn(){
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        AccessToken accessToken= AccessToken.getCurrentAccessToken();
        if(accessToken == null)
            return  false;
        else return true;
    }

    public  void Logout(){
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        LoginManager.getInstance().logOut();
    }

    public void Login(final OnLoginListener lsnr)
    {
        FacebookLogin.getFacebookHashKey(activity);
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile,email"));
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final AccessToken accessToken = loginResult.getAccessToken();
                        final SocialUser fbUser = new SocialUser();
                        GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                fbUser.email=user.optString("email");
                                fbUser.name=user.optString("name");
                                fbUser.id=user.optString("id");
                                fbUser.network=SocialUser.NetworkType.FACEBOOK;
                                lsnr.onSuccess(fbUser);
                            }
                        }).executeAsync();

                        AppLog.i( "LoginManager FacebookCallback onSuccess");
                        if(loginResult.getAccessToken() != null) {
                            AppLog.i( "Access Token:: " + loginResult.getAccessToken());
                            //facebookSuccess();

                        }
                        AppLog.i("onSuccess");
                    }

                    @Override
                    public void onCancel() {
                        AppLog.i("onCancel");
                        AppLog.i( "LoginManager FacebookCallback onCancel");
                        // App code
                        lsnr.onFail();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        exception.printStackTrace();
                        AppLog.i("onError");
                        AppLog.i("LoginManager FacebookCallback onError");
                        lsnr.onFail();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void getFacebookHashKey(Context context){
        AppLog.i("tg"+"Apppp");
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                AppLog.i("KeyHash:"+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}