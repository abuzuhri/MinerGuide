package com.minergame.minerguide.utils.fonts;

import android.content.Context;
import android.graphics.Typeface;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Tareq on 03/20/2015.
 */
public class MinecraftFont implements ITypeface {
    private static final String TTF_FILE = "icomoon.ttf";

    private static Typeface typeface = null;

    private static HashMap<String, Character> mChars;

    @Override
    public IIcon getIcon(String key) {
        return Icon.valueOf(key);
    }

    @Override
    public HashMap<String, Character> getCharacters() {
        if (mChars == null) {
            HashMap<String, Character> aChars = new HashMap<String, Character>();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(),
                        v.character);
            }
            mChars = aChars;
        }

        return mChars;
    }

    @Override
    public String getMappingPrefix() {
        return "gmd";
    }

    @Override
    public String getFontName() {
        return "Google Material Design";
    }

    @Override
    public String getVersion() {
        return "1.1.1";
    }

    @Override
    public int getIconCount() {
        return mChars.size();
    }

    @Override
    public Collection<String> getIcons() {
        Collection<String> icons = new LinkedList<String>();

        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }

        return icons;
    }

    @Override
    public String getAuthor() {
        return "Google";
    }

    @Override
    public String getUrl() {
        return "https://github.com/google/material-design-icons";
    }

    @Override
    public String getDescription() {
        return "Material Design Icons are the official open-source icons featured in the Google Material Design specification.";
    }

    @Override
    public String getLicense() {
        return "Attribution 4.0 International";
    }

    @Override
    public String getLicenseUrl() {
        //http://image.online-convert.com/convert-to-svg
        return "https://icomoon.io/app/#/select/font";
    }

    @Override
    public Typeface getTypeface(Context context) {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(),
                        "fonts/" + TTF_FILE);
            } catch (Exception e) {
                return null;
            }
        }
        return typeface;
    }

    public static enum Icon implements IIcon {
        //Google material design
        ic_achievements('\ue60f'),
        ic_biomes('\ue602'),
        ic_blocks('\ue60c'),
        ic_idlist('\ue604'),
        ic_mobs('\ue610'),
        ic_potions('\ue606'),
        ic_redstone('\ue607'),
        ic_web('\ue609'),
        ic_drop('\ue90b'),
        ic_chat('\ue96c'),
        ic_structures('\ue921'),
        ic_tool('\ue611');


        char character;

        Icon(char character) {
            this.character = character;
        }

        public String getFormattedName() {
            return "{" + name() + "}";
        }

        public char getCharacter() {
            return character;
        }

        public String getName() {
            return name();
        }

        // remember the typeface so we can use it later
        private static ITypeface typeface;

        public ITypeface getTypeface() {
            if (typeface == null) {
                typeface = new MinecraftFont();
            }
            return typeface;
        }
    }
}