package com.minergame.minerguide.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.minergame.minerguide.R;
import com.minergame.minerguide.db.Dao.AppSettingDao;
import com.minergame.minerguide.db.Entity.AppSetting;
import com.minergame.minerguide.ui.activity.SplashActivity;
import com.minergame.minerguide.utils.AppAction;
import com.minergame.minerguide.utils.AppLog;
import com.minergame.minerguide.utils.DialogUtils;
import com.minergame.minerguide.utils.Login.FacebookLogin;
import com.minergame.minerguide.utils.Login.GooglPlusLogin;

import java.util.Set;

/**
 * Created by Tareq on 03/22/2015.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {


    private SwitchPreference notification_chat;
    private SwitchPreference notification_add_new_movies;
    AppSettingDao settingNotification=new AppSettingDao();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        Set<String> preferenceNames = getPreferenceManager().getSharedPreferences().getAll().keySet();

        findPreference("logout").setOnPreferenceClickListener(this);
        findPreference("rateapp").setOnPreferenceClickListener(this);

        AppSetting appSetting=settingNotification.getAppSetting();

        AppLog.i("appSetting.NotificationChat ==>"+appSetting.NotificationChat);
        AppLog.i("appSetting.NotificationMovie ==>"+appSetting.NotificationMovie);

        notification_chat=(SwitchPreference)findPreference("notification_chat");
        notification_chat.setOnPreferenceClickListener(this);
        notification_chat.setChecked(appSetting.NotificationChat);


        notification_add_new_movies=(SwitchPreference)findPreference("notification_add_new_movies");
        notification_add_new_movies.setOnPreferenceClickListener(this);
        notification_add_new_movies.setChecked(appSetting.NotificationMovie);



        for (String prefName : preferenceNames) {
            AppLog.i("prefName"+prefName);
            Preference preference = findPreference(prefName);
            if(preference!=null)
                preference.setOnPreferenceClickListener(this);
        }

    }

    private  void LogOut(){

        DialogUtils.OkDialog(getActivity(), getString(R.string.user_logout_clear_data), getString(R.string.user_logout_clear_data_summary), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                FacebookLogin fbNetwork = new FacebookLogin(getActivity());
                fbNetwork.Logout();

                GooglPlusLogin gpNetwork = new GooglPlusLogin(getActivity());
                gpNetwork.Logout();

                AppAction.OpenActivity(getActivity(), SplashActivity.class);
                getActivity().finish();


            }
        });
    }
    private  void  RateApp(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=" + getActivity().getPackageName()));
        startActivity(i);
    }

    private void NotificationChat(){
        AppLog.i("notification_chat.isChecked() => "+notification_chat.isChecked());
        settingNotification.setNotificationChat(notification_chat.isChecked());

    }

    private void NotificationMovies(){
        AppLog.i("notification_add_new_movies.isChecked() => "+notification_add_new_movies.isChecked());
        settingNotification.setNotificationMovie(notification_add_new_movies.isChecked());
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        //Toast.makeText(getActivity(),"sss",Toast.LENGTH_LONG).show();

        if(key.equals("logout")){
            LogOut();
        }else if(key.equals("rateapp")){
            RateApp();
        }else if(key.equals("notification_chat")){
            NotificationChat();
        }else if(key.equals("notification_add_new_movies")){
            NotificationMovies();
        }
        return true;
    }
}
