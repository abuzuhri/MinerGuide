package com.minergame.minerguide.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tareq on 07/04/2015.
 */
public class IOUtility {


    public static String ReadFileInAssets(Context context,String fileName){
        try {

                StringBuilder buf = new StringBuilder();
                InputStream json;

                try {
                    String str = "";
                    json = context.getAssets().open(fileName);
                    BufferedReader in = new BufferedReader(new InputStreamReader(json));

                    while ((str = in.readLine()) != null) {
                        buf.append(str);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return buf.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
