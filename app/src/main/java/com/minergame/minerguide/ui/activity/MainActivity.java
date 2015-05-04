package com.minergame.minerguide.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import com.minergame.minerguide.R;
import com.minergame.minerguide.ui.fragment.ItemListFragment;
import com.minergame.minerguide.utils.AppAction;


public class MainActivity extends BaseActivity {

    Spinner mSpinner;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CreateDrawer(toolbar);
        SetupToolbarShadow();
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


        //MaterialSearchSetup2();
        //displaySearchView(false);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toolbar != null && mSpinner != null) {
            toolbar.removeView(mSpinner);
        }
    }

    @Override
    public void onBackPressed() {
        if(result!=null && result.isDrawerOpen())
            result.closeDrawer();
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            long AppId=R.id.search;
            AppAction.OpenActivityWithFRAGMENTSearch(MainActivity.this, ItemListFragment.class.getName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
