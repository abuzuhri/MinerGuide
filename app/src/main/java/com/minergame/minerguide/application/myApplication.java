package com.minergame.minerguide.application;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

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
        InitImageLoader();
    }

    public static Context getContext(){
        return mContext;
    }

    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        //configurationBuilder.addModelClasses(Test.class);

        ActiveAndroid.initialize(configurationBuilder.create());
    }


    private void InitImageLoader(){
        //SAMPLE using [PICASSO](https://github.com/square/picasso)
        //initialize and create the image loader logic
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
    }
}
