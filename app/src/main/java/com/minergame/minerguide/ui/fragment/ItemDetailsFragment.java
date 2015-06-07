package com.minergame.minerguide.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.minergame.minerguide.R;
import com.minergame.minerguide.db.Dao.ObjectDao;
import com.minergame.minerguide.db.Entity.ImageTbl;
import com.minergame.minerguide.db.Entity.Information;
import com.minergame.minerguide.db.Entity.ObjectTbl;
import com.minergame.minerguide.db.Entity.RecipeBrewing;
import com.minergame.minerguide.db.Entity.RecipeCrafting;
import com.minergame.minerguide.db.Entity.RecipeSmelting;
import com.minergame.minerguide.ui.activity.OneFragmentActivity;
import com.minergame.minerguide.utils.AppAction;
import com.minergame.minerguide.utils.AppLog;
import com.minergame.minerguide.utils.IntentUtils;
import com.minergame.minerguide.utils.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Tareq on 03/21/2015.
 */
public class ItemDetailsFragment extends BaseFragment {

    private ObjectTbl objectTbl=null;
    private boolean isViewInited = false;

    private TextView objectName;
    private TextView objectID;
    private ImageView photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Toast.makeText(getActivity(),"ID="+ID,Toast.LENGTH_LONG).show();

        OneFragmentActivity activity=((OneFragmentActivity)getActivity());
        if(activity!=null)
            activity.RemoveToolBarShadow();

        ObjectDao objectDao=new ObjectDao();
        objectTbl=objectDao.getById(ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_details, container, false);
        final FloatingActionButton fb=(FloatingActionButton)rootView.findViewById(R.id.fab);
        DocumentView informationText=(DocumentView) rootView.findViewById(R.id.informationText);
        View header=rootView.findViewById(R.id.header);

        photo=(ImageView)rootView.findViewById(R.id.objectIcon);

        objectName=(TextView)rootView.findViewById(R.id.objectName);
        objectID=(TextView)rootView.findViewById(R.id.objectID);



        if(objectTbl.YouTubeVideo!=null && !objectTbl.YouTubeVideo.equals("") && objectTbl.YouTubeVideo.length() > 3) {
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String youtubelink="https://www.youtube.com/watch?v=" + objectTbl.YouTubeVideo;
                    AppLog.i("objectTbl.YouTubeVideo.length() ==> "+objectTbl.YouTubeVideo.length());
                    AppLog.i("objectTbl.YouTubeVideo ==> "+objectTbl.YouTubeVideo);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubelink));
                    //getActivity().startActivity(intent);
                    PackageManager pm = getActivity().getPackageManager();
                    Intent intent= IntentUtils.newYouTubeIntent(pm, objectTbl.YouTubeVideo);

                    AppAction.OpenActivityIntent(getActivity(), intent);
                }
            });


            ViewTreeObserver vto = fb.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!isViewInited) {
                        int width = fb.getWidth();
                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) fb.getLayoutParams();
                        AppLog.i("ne width ==>" + width);
                        AppLog.i("ne (width/2)*-1 ==>" + (width / 2) * -1);

                        params1.setMargins(0, 0, px(20), (width / 2) * -1);
                        fb.setLayoutParams(params1);
                        isViewInited = true;
                    }
                }
            });
        }else{
            fb.setVisibility(View.GONE);
        }

        String extnsion=".png";
        if(objectTbl.Category.equals("Biome"))
            extnsion=".jpg";

        String uri ="icons/icon_"+objectTbl.getId()+extnsion;
        try {
            // get input stream
            InputStream ims = getActivity().getAssets().open(uri);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            photo.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        objectName.setText(objectTbl.Name);
        String dec=objectTbl.Dec;
        if(dec!=null)
            dec=dec.replace("_",":");

        objectID.setText("ID: "+ dec   +"");


        if(objectTbl.Informations()!=null && objectTbl.Informations().size() >0){
            Information info=objectTbl.Informations().get(0);
            informationText.setText(info.Information);
        }


        ImageView objectImage=(ImageView) rootView.findViewById(R.id.objectImage);
        if(objectTbl.ImageTbls()!=null && objectTbl.ImageTbls().size() >0){
            ImageTbl img=objectTbl.ImageTbls().get(0);
            loadImageFromAsset("icons/"+img.ImageName,objectImage);
        }
        else {
            objectImage.setVisibility(View.GONE);
            rootView.findViewById(R.id.objectImageDivider).setVisibility(View.GONE);

        }


        renderCraftSquares(inflater,rootView);
        renderSmelting(inflater,rootView);
        renderBrewing(inflater,rootView);
        return rootView;
    }


    private void renderBrewing(LayoutInflater inflater,View root)
    {
        List<RecipeBrewing> RecipeBrewingList=objectTbl.RecipeBrewing();

        LinearLayout localLinearLayout= (LinearLayout) root.findViewById(R.id.brewing);
        if(RecipeBrewingList!=null && RecipeBrewingList.size()>0){
            for (RecipeBrewing crf : RecipeBrewingList) {

                View view = inflater.inflate(R.layout.brewing_stand, localLinearLayout, false);
                String[] str=crf.Recipe.split(",");
                for(int i=0;i<str.length;i++){
                    long num=Integer.parseInt(str[i]);
                    if(num!=0){
                        int RsID=view.getResources().getIdentifier("Item"+i, "id", getActivity().getPackageName());
                        ImageButton ImgSqr=(ImageButton) view.findViewById(RsID);
                        String name="icons/icon_"+num+".png";
                        if(ImgSqr!=null)
                            loadImageFromAsset(name,ImgSqr);
                        ImgSqr.setTag(num);
                        ImgSqr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long vID=(long) v.getTag();
                                AppLog.i("vID=>"+vID);
                                AppAction.OpenActivityWithFRAGMENT(getActivity(), ItemDetailsFragment.class.getName(), vID);
                            }
                        });
                    }
                }
                localLinearLayout.addView(view);
            }
        }else{
            localLinearLayout.setVisibility(View.GONE);
            root.findViewById(R.id.brewingDivider).setVisibility(View.GONE);

        }
    }
    private void renderSmelting(LayoutInflater inflater,View root)
    {
        List<RecipeSmelting> RecipeSmeltingList=objectTbl.RecipeSmelting();

        LinearLayout localLinearLayout= (LinearLayout) root.findViewById(R.id.smelting);;
        if(RecipeSmeltingList!=null && RecipeSmeltingList.size()>0){
            for (RecipeSmelting crf : RecipeSmeltingList) {

                View view = inflater.inflate(R.layout.smelt, localLinearLayout, false);
                String[] str=crf.Recipe.split(",");
                for(int i=0;i<str.length;i++){
                    long num=Integer.parseInt(str[i]);
                    if(num!=0){
                        if(num==99)
                            num=ID;
                        int RsID=view.getResources().getIdentifier("Item"+i, "id", getActivity().getPackageName());
                        ImageButton ImgSqr=(ImageButton) view.findViewById(RsID);
                        String name="icons/icon_"+num+".png";
                        if(ImgSqr!=null)
                            loadImageFromAsset(name,ImgSqr);
                        ImgSqr.setTag(num);
                        AppLog.i("num==>"+num);
                        ImgSqr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long vID=(long) v.getTag();
                                AppLog.i("vID=>"+vID);
                                AppAction.OpenActivityWithFRAGMENT(getActivity(), ItemDetailsFragment.class.getName(), vID);
                            }
                        });
                    }
                }
                localLinearLayout.addView(view);
            }
        }else{
            localLinearLayout.setVisibility(View.GONE);
            root.findViewById(R.id.smeltingDivider).setVisibility(View.GONE);
        }
    }

    private void renderCraftSquares(LayoutInflater inflater,View root)
    {
        List<RecipeCrafting> RecipeCraftingList=objectTbl.RecipeCrafting();

        LinearLayout localLinearLayout= (LinearLayout) root.findViewById(R.id.crafting);
        if(RecipeCraftingList!=null && RecipeCraftingList.size()>0){
            for (RecipeCrafting crf : RecipeCraftingList) {

                View view = inflater.inflate(R.layout.square, localLinearLayout, false);
                String[] str=crf.Recipe.split(",");
                for(int i=0;i<str.length;i++){
                    long num=Integer.parseInt(str[i]);
                    if(num!=0){
                        int RsID=view.getResources().getIdentifier("Item"+i, "id", getActivity().getPackageName());
                        ImageButton ImgSqr=(ImageButton) view.findViewById(RsID);
                        String name="icons/icon_"+num+".png";
                        if(ImgSqr!=null)
                            loadImageFromAsset(name,ImgSqr);
                        ImgSqr.setTag(num);
                        ImgSqr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long vID=(long) v.getTag();
                                AppLog.i("vID=>"+vID);
                                AppAction.OpenActivityWithFRAGMENT(getActivity(), ItemDetailsFragment.class.getName(), vID);
                            }
                        });
                    }
                }
                localLinearLayout.addView(view);
            }
        }else {
            localLinearLayout.setVisibility(View.GONE);
            root.findViewById(R.id.craftingDivider).setVisibility(View.GONE);
        }
    }

    public void loadImageFromAsset(String name,ImageView view) {
        // load image
        try {
            // get input stream
            InputStream ims =getActivity().getAssets().open(name);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            view.setImageDrawable(d);
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity(). getMenuInflater().inflate(R.menu.menu_object_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if(objectTbl.Favorite!=null && objectTbl.Favorite.equals(true)){
            menu.removeItem(R.id.favorite_outline);
        }else  menu.removeItem(R.id.favorite);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        ObjectDao objectDao=new ObjectDao();

        //noinspection SimplifiableIfStatement

        if (id == R.id.favorite) {
            objectTbl.Favorite=false;
            objectDao.setFavorite(false,ID);
            getActivity().invalidateOptionsMenu();
            return true;
        }else if (id == R.id.favorite_outline) {
            objectTbl.Favorite=true;
            objectDao.setFavorite(true,ID);
            getActivity().invalidateOptionsMenu();
            return true;
        }else if (id == R.id.question) {
            //AppAction.OpenActivityWithFRAGMENTSearch(MainActivity.this, ItemListFragment.class.getName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
