package com.minergame.minerguide.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by tareq on 05/21/2015.
 */
public class Utility {


    public static boolean isAppInstalled(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

}
