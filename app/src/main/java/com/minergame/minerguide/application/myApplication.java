package com.minergame.minerguide.application;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * Created by Tareq on 03/21/2015.
 */
public class myApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initializeDB();
    }

    public static Context getContext(){
        return mContext;
    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        //configurationBuilder.addModelClasses(Test.class);

        ActiveAndroid.initialize(configurationBuilder.create());
    }
}
