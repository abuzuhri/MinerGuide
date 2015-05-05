package com.minergame.minerguide.utils;

/**
 * Created by Tareq on 03/20/2015.
 */
public class AppConstant {


    public enum AppDrawer {
        Home(10),
        All(11),
        Blocks(20),
        Mobs(30),
        Biomes(40),
        Potions(60),
        Redsone(70),
        Achievements(80),
        Commands(90),
        Chat(91),
        Settings(100),
        Favorites(110),;
        public int id;

        private AppDrawer(int id) {
            this.id = id;
        }
    }


    public  static class SharedPreferenceNames{
        public static String SocialUser="SocialUser";
    }



}
