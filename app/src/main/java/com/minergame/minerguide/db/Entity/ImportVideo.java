package com.minergame.minerguide.db.Entity;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minergame.minerguide.utils.IOUtility;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by tareq on 07/04/2015.
 */
public class ImportVideo {
    public static final String  FILENAME="movies.txt";
    public static  Collection<ImportVideo> getList(String json){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<ImportVideo>>(){}.getType();
        Collection<ImportVideo> list = gson.fromJson(json, collectionType);
        return  list;
    }
    public static  Collection<ImportVideo> getListFromFile(Context context){
        String fileText= IOUtility.ReadFileInAssets(context, FILENAME);
        return  getList(fileText);
    }

    public String Vid;
    public String Name;
    public String VdbUrl;
    public String IdbUrl;
    public String VggUrl;
    public String IggUrl;


}
