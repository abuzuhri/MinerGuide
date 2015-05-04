package com.minergame.minerguide.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.minergame.minerguide.R;
import com.minergame.minerguide.utils.AppAction;
import com.minergame.minerguide.utils.AppLog;

/**
 * Created by Tareq on 03/20/2015.
 */
public class OneFragmentActivity extends BaseActivity {

    //private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Transition
        //overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_right);


        setContentView(R.layout.activity_main);


        SetupToolbarShadow();
        Intent intent = getIntent();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String FragmentName = intent.getStringExtra(AppAction.EXTRA.FRAGMENTEXTRA);
        AppLog.i("FragmentName = > " + FragmentName);



        Fragment fragment= Fragment.instantiate(this,FragmentName);

        //Pass Item  It to Fragment
        long ItemID = intent.getLongExtra(AppAction.EXTRA.IDEXTRA,0);
        boolean IsSearch = intent.getBooleanExtra(AppAction.EXTRA.SEARCHEXTRA,false);
        if(IsSearch) {
            MaterialSearchSetup(true);
        }

        Bundle args = new Bundle();
        args.putLong(AppAction.EXTRA.IDEXTRA, ItemID);
        fragment.setArguments(args);
        selectFragment(fragment);

    }






}
