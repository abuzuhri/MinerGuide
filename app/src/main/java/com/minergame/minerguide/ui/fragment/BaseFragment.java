package com.minergame.minerguide.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;

import com.minergame.minerguide.utils.AppAction;

/**
 * Created by Tareq on 03/20/2015.
 */
public class BaseFragment extends Fragment {
    protected Long ID;

    protected int px(float dips)
    {
        float DP = getResources().getDisplayMetrics().density;
        return Math.round(dips * DP);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set Item ID
        if(getArguments()!=null) {
            ID = getArguments().getLong(AppAction.EXTRA.IDEXTRA, 0);
        }
    }

    public void setSubTitle(String txt){
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(txt);
    }
    public void setTitle(String txt) {
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(txt);
    }

    public void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    public void  removeShadowForNewApi21(View rootView){
        //View shadowView=rootView.findViewById(R.id.shadow_main_activity);
        // Solve Android bug in API < 21 by app custom shadow
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //    shadowView.setVisibility(View.GONE);
    }

}
