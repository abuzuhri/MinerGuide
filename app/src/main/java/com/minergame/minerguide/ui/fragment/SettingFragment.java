package com.minergame.minerguide.ui.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.minergame.minerguide.R;
import com.minergame.minerguide.ui.activity.SplashActivity;
import com.minergame.minerguide.utils.AppAction;
import com.minergame.minerguide.utils.AppLog;
import com.minergame.minerguide.utils.Login.FacebookLogin;
import com.minergame.minerguide.utils.Login.GooglPlusLogin;

import java.util.Set;

/**
 * Created by Tareq on 03/22/2015.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        Set<String> preferenceNames = getPreferenceManager().getSharedPreferences().getAll().keySet();

        Preference  logout = findPreference("logout");
        logout.setOnPreferenceClickListener(this);
        //Preference  mFontSizePreference = findPreference("font_size_preference");

        try{
            throw new Exception("test");
        }catch (Exception e){
            Crashlytics.logException(e);
        }


        for (String prefName : preferenceNames) {
            AppLog.i("prefName"+prefName);
            Preference preference = findPreference(prefName);
            if(preference!=null)
                preference.setOnPreferenceClickListener(this);
        }

/*
        FacebookLogin fbNetwork=new FacebookLogin(getActivity());
        fbNetwork.Logout();

        GooglPlusLogin gpNetwork=new GooglPlusLogin(getActivity());
        gpNetwork.Logout();

        AppAction.OpenActivity(getActivity(), SplashActivity.class);
        getActivity().finish();

  */
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        Toast.makeText(getActivity(),"sss",Toast.LENGTH_LONG).show();

        if(key.equals("logout"))
        {

        }
        return true;
    }
}
