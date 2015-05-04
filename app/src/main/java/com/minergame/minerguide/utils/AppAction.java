package com.minergame.minerguide.utils;

import android.app.Activity;
import android.content.Intent;

import com.minergame.minerguide.application.OverridePendingUtil;
import com.minergame.minerguide.ui.activity.OneFragmentActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Tareq on 03/20/2015.
 */
public class AppAction {

    public static class EXTRA {
        public static final String IDEXTRA = "IDEXTRA";
        public static final String SEARCHEXTRA = "SEARCHEXTRA";
        public static final String FRAGMENTEXTRA = "FRAGMENT";
        public static final String POSITION = "POSITION";
        public static final String MENUID = "MENUID";


    }

    public  static void OpenActivityIntent(Activity context,Intent intent){
        context.startActivity(intent);
        OverridePendingUtil.in(context);

    }

    public static void OpenActivity(Activity context,Class<?> cls){
        Intent intent = new Intent(context, cls);
        OpenActivityIntent(context,intent);
    }
    public static void OpenActivityWithID(Activity context,Class<?> cls, Long ID){
        Intent intent = new Intent(context, cls);
        if(ID!=null)
            intent.putExtra(EXTRA.IDEXTRA, ID);
        OpenActivityIntent(context,intent);
    }
    public static void OpenActivityWithFRAGMENT(Activity context,Class<?> cls, String name){
        Intent intent = new Intent(context, cls);
        if(name!=null)
            intent.putExtra(EXTRA.FRAGMENTEXTRA, name);
        OpenActivityIntent(context, intent);
    }
    public static void OpenActivityWithFRAGMENTSearch(Activity context, String name){
        Intent intent = new Intent(context, OneFragmentActivity.class);
        if(name!=null)
            intent.putExtra(EXTRA.FRAGMENTEXTRA, name);
            intent.putExtra(EXTRA.SEARCHEXTRA, true);

        OpenActivityIntent(context,intent);
    }
    public static void OpenActivityWithFRAGMENT(Activity context, String name, Long ID){
        Intent intent = new Intent(context, OneFragmentActivity.class);
        if(name!=null)
            intent.putExtra(EXTRA.FRAGMENTEXTRA, name);
        if(ID!=null)
            intent.putExtra(EXTRA.IDEXTRA, ID);

        OpenActivityIntent(context,intent);
    }

    public static void DiaplayError(Activity activity, String msg){
        Crouton.makeText(activity, msg, Style.ALERT).show();
    }
}
