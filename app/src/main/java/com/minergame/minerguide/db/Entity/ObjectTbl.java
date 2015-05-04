package com.minergame.minerguide.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by Tareq on 03/21/2015.
 */

@Table(name = "objects", id = "_id")
public class ObjectTbl extends BaseEntity {

    @Column(name = "name")
    public String Name;

    @Column(name = "decription")
    public String Decription;

    @Column(name = "category")
    public String Category;

    @Column(name = "sub_category")
    public String SubCategory;

    @Column(name = "ut_video")
    public String YouTubeVideo;

    @Column(name = "favorite")
    public Boolean Favorite;

    @Column(name = "visited")
    public Boolean Visited ;

    public List<ImageTbl> ImageTbls() {
        return getMany(ImageTbl.class, "object");
    }

    public List<Information> Informations() {
        return getMany(Information.class, "object");
    }

    public List<RecipeBrewing> RecipeBrewing() {
        return getMany(RecipeBrewing.class, "object");
    }

    public List<RecipeCrafting> RecipeCrafting() {
        return getMany(RecipeCrafting.class, "object");
    }

    public List<RecipeSmelting> RecipeSmelting() {
        return getMany(RecipeSmelting.class, "object");
    }
}
