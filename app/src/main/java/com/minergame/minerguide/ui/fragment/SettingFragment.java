package com.minergame.minerguide.ui.fragment;

import android.os.Bundle;

import com.minergame.minerguide.ui.activity.SplashActivity;
import com.minergame.minerguide.utils.AppAction;
import com.minergame.minerguide.utils.Login.FacebookLogin;
import com.minergame.minerguide.utils.Login.GooglPlusLogin;

/**
 * Created by Tareq on 03/22/2015.
 */
public class SettingFragment extends BaseFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


        FacebookLogin fbNetwork=new FacebookLogin(getActivity());
        fbNetwork.Logout();

        GooglPlusLogin gpNetwork=new GooglPlusLogin(getActivity());
        gpNetwork.Logout();

        AppAction.OpenActivity(getActivity(), SplashActivity.class);
        getActivity().finish();
    }
}
