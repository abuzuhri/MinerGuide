package com.minergame.minerguide.ui.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.minergame.minerguide.R;
import com.minergame.minerguide.application.OverridePendingUtil;
import com.minergame.minerguide.ui.fragment.HomeFragment;
import com.minergame.minerguide.ui.fragment.ItemListFragment;
import com.minergame.minerguide.ui.fragment.MovieFragment;
import com.minergame.minerguide.ui.fragment.SettingFragment;
import com.minergame.minerguide.utils.AppAction;
import com.minergame.minerguide.utils.AppConstant;
import com.minergame.minerguide.utils.AppLog;
import com.minergame.minerguide.utils.Login.SocialUser;
import com.minergame.minerguide.utils.fonts.MinecraftFont;

import java.lang.reflect.Field;

/**
 * Created by Tareq on 03/20/2015.
 */
public class BaseActivity extends ActionBarActivity {

    protected   Toolbar toolbar;
    public View shadowView;
    public View toolbarContainer;
    public boolean toolbarHomeButtonAnimating;
    public Menu menu;

    protected View searchContainer;
    protected EditText toolbarSearchView;
    protected ImageView searchClearButton;
    protected Drawer result;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    public void finish() {
        super.finish();
        OverridePendingUtil.out(this);
    }



    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    protected  void MaterialSearchSetup(boolean isOpen){
        searchContainer = findViewById(R.id.search_container);
        toolbarSearchView = (EditText) findViewById(R.id.search_view);
        searchClearButton = (ImageView) findViewById(R.id.search_clear);

        // Setup search container view
        try {
            // Set cursor colour to white
            // http://stackoverflow.com/a/26544231/1692770
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(toolbarSearchView, R.drawable.edittext_whitecursor);
        } catch (Exception ignored) {
        }

        // Search text changed listener
        toolbarSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Search(s.toString());
                //Fragment mainFragment = getFragmentManager().findFragmentById(R.id.container);
                //if (mainFragment != null && mainFragment instanceof MainListFragment) {
                //    ((MainListFragment) mainFragment).search(s.toString());
                //}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Clear search text when clear button is tapped
        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSearchView.setText("");
            }
        });


        // Hide the search view
        if(isOpen)
            searchContainer.setVisibility(View.VISIBLE);
        else searchContainer.setVisibility(View.GONE);

    }

    public  void  selectFragment(Fragment fragment){
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }

    private void Search(String txt){
        Fragment fragment= Fragment.instantiate(this, ItemListFragment.class.getName());
        Bundle args = new Bundle();
        args.putString(AppAction.EXTRA.SEARCHEXTRA, txt);
        fragment.setArguments(args);
        selectFragment(fragment);
    }

    protected void CreateDrawer(final Toolbar toolbar){

        SharedPreferences mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(AppConstant.SharedPreferenceNames.SocialUser, "");
        SocialUser obj = gson.fromJson(json, SocialUser.class);


        final IProfile profile = new ProfileDrawerItem().withName(obj.name).withEmail(obj.email).withIcon(obj.avatarURL);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.headergreen)
                .addProfiles(
                        profile
                )
                .build();



        //new Drawer().withActivity(this).build();
        //IconicsDrawable(this, getResources().getDrawable(R.drawable.ic_drawer_mobs)
        //        .setIcon(new IconicsDrawable(this, getResources().getDrawable(R.drawable.ic_drawer_mobs))
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_home).withIdentifier(AppConstant.AppDrawer.Home.id).withIcon(FontAwesome.Icon.faw_dashboard),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_all).withIdentifier(AppConstant.AppDrawer.All.id).withIcon(MinecraftFont.Icon.ic_tool),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_movies).withIdentifier(AppConstant.AppDrawer.Movies.id).withIcon(FontAwesome.Icon.faw_video_camera),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_blocks).withIdentifier(AppConstant.AppDrawer.Blocks.id).withIcon(MinecraftFont.Icon.ic_blocks).withBadge("99"),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_mobs).withIdentifier(AppConstant.AppDrawer.Mobs.id).withIcon(MinecraftFont.Icon.ic_mobs).withBadge("99"),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_biomes).withIdentifier(AppConstant.AppDrawer.Biomes.id).withIcon(MinecraftFont.Icon.ic_biomes).withBadge("99"),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_potions).withIdentifier(AppConstant.AppDrawer.Potions.id).withIcon(MinecraftFont.Icon.ic_potions).withBadge("99"),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_redstone).withIdentifier(AppConstant.AppDrawer.Redsone.id).withIcon(MinecraftFont.Icon.ic_redstone).withBadge("99"),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_achievements).withIdentifier(AppConstant.AppDrawer.Achievements.id).withIcon(MinecraftFont.Icon.ic_achievements).withBadge("99"),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_chat).withIdentifier(AppConstant.AppDrawer.Chat.id).withIcon(MinecraftFont.Icon.ic_chat).withBadge("99"),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_favorites).withIdentifier(AppConstant.AppDrawer.Favorites.id).withIcon(GoogleMaterial.Icon.gmd_favorite).withBadge("99"),
                        //new SwitchDrawerItem().withName(R.string.ic_drawer_chat).withIdentifier(AppConstant.AppDrawer.Achievements.id).withIcon(MinecraftFont.Icon.ic_chat).withChecked(true),//.withOnCheckedChangeListener(onCheckedChangeListener),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.ic_drawer_settings).withIdentifier(AppConstant.AppDrawer.Settings.id).withIcon(GoogleMaterial.Icon.gmd_settings)

                )
                .withSelectedItem(0)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {
                                if(drawerItem.getIdentifier()!=AppConstant.AppDrawer.Chat.id)
                                    toolbar.setTitle(((Nameable) drawerItem).getNameRes());
                                    //toolbar.setSubtitle(((Nameable) drawerItem).getNameRes());
                            }

                            selectItem(drawerItem.getIdentifier());
                        }
                        return false;
                    }
                })
                .withActionBarDrawerToggleAnimated(true)
                .build();
    }



    public  void  selectItem(int filter){
        Fragment fragment = null;
        Bundle args = new Bundle();
        AppLog.i("position=> " + filter);


        SetToolbarShadow();
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (filter == AppConstant.AppDrawer.Home.id) {
            fragment = new HomeFragment();
        }else if (filter == AppConstant.AppDrawer.All.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.Movies.id) {
            fragment = new MovieFragment();
        }else if (filter == AppConstant.AppDrawer.Achievements.id) {
            fragment = new ItemListFragment();
        }else  if (filter == AppConstant.AppDrawer.Redsone.id) {
            fragment = new ItemListFragment();
            //args.putString(TabFragment.FRAGMENT, NoteFragment.class.getName());
        }else if (filter == AppConstant.AppDrawer.Potions.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.Favorites.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.Biomes.id) {
            fragment = new ItemListFragment();
            //args.putString(TabFragment.FRAGMENT, CoursesFragment.class.getName());
        }else if (filter == AppConstant.AppDrawer.Mobs.id) {
            fragment = new ItemListFragment();
        }else if (filter == AppConstant.AppDrawer.Blocks.id) {
            fragment = new ItemListFragment();
            //args.putString(TabFragment.FRAGMENT, ExamFragment.class.getName());
        }else if (filter == AppConstant.AppDrawer.Home.id) {
            fragment = new ItemListFragment();
            //args.putString(TabFragment.FRAGMENT, SemesterFragment.class.getName());
        }else if (filter == AppConstant.AppDrawer.Settings.id) {

            //PreferenceFragment setting= new SettingFragment();
            //fragmentManager.beginTransaction().add(android.R.id.content, setting).commit();
            fragment=new SettingFragment();

        }else if (filter == AppConstant.AppDrawer.Chat.id) {
            //fragment = new ChatFragment();
            AppAction.OpenActivity(this,ChatActivity.class);
            return;
            //args.putString(TabFragment.FRAGMENT, YearsFragment.class.getName());
        }

        args.putInt(AppAction.EXTRA.MENUID, filter);
        fragment.setArguments(args);

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();


        }

    }

    public void SetupToolbarShadow(){
        //Shadow View
        shadowView=findViewById(R.id.shadow_main_activity);
        toolbarContainer=findViewById(R.id.toolbar);
        // Solve Android bug in API < 21 by app custom shadow
        SetToolbarShadow();
    }
    public void SetToolbarShadow()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shadowView.setVisibility(View.GONE);
            final float scale = getResources().getDisplayMetrics().density;
            toolbarContainer.setElevation(5f*scale);
        }else{
            shadowView.setVisibility(View.VISIBLE);
        }
    }


    public void RemoveToolBarShadow() {
        if(shadowView!=null)
            shadowView.setVisibility(View.GONE);
        if(toolbarContainer!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                toolbarContainer.setElevation(0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

}
