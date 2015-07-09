package com.minergame.minerguide.application;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.crashlytics.android.Crashlytics;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.minergame.minerguide.db.Entity.ImportVideo;
import com.minergame.minerguide.db.Entity.ObjectTbl;
import com.minergame.minerguide.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Iterator;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Tareq on 03/21/2015.
 */
public class myApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mContext = this;
        initializeDB();
        AddVideo();
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

    private void AddVideo() {
        String spName = "isFirstTime";
        boolean isFirstTime = Utility.getSharedPreferences(mContext, spName, true);
        if (isFirstTime) {
            Utility.setSharedPreferences(mContext, spName, false);
            Collection<ImportVideo> list = ImportVideo.getListFromFile(mContext);
            Iterator itr1 = list.iterator();
            while (itr1.hasNext()) {
                ImportVideo vd = (ImportVideo) itr1.next();

                ObjectTbl obj = new ObjectTbl();
                obj.Name=vd.Name;
                obj.Dec=vd.Vid;

                obj.Visited=false;
                obj.Favorite=false;

                obj.IdbUrl=vd.IdbUrl;
                obj.IggUrl=vd.IggUrl;
                obj.VdbUrl=vd.VdbUrl;
                obj.VggUrl=vd.VggUrl;


                obj.Category="Video";
                obj.SubCategory="Help";
                obj.save();

            }
        }
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
