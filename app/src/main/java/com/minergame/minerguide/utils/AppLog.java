package com.minergame.minerguide.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Tareq on 03/20/2015.
 */
public class AppLog {
    public  static final  String  TAG="MyApp";
    public static void i(String msg){
        Log.i(TAG, msg);
        Crashlytics.log(msg);
    }
}