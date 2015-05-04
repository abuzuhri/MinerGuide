package com.minergame.minerguide.application;

import android.app.Activity;

import com.minergame.minerguide.R;

/**
 * Created by Tareq on 03/21/2015.
 */
public class OverridePendingUtil {
    public static void in(final Activity activity) {
        new Object() {
            public void overridePendingTransition(Activity c) {
                c.overridePendingTransition(R.anim.fb_slide_in_from_right, R.anim.fb_forward);
            }
        }.overridePendingTransition(activity);
    }

    public static void out(final Activity activity) {
        new Object() {
            public void overridePendingTransition(Activity c) {
                c.overridePendingTransition(R.anim.fb_back, R.anim.fb_slide_out_from_right);
            }
        }.overridePendingTransition(activity);
    }
}