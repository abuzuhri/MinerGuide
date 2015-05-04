package com.minergame.minerguide.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tareq on 03/21/2015.
 */

@Table(name = "recipe_brewing", id = "_id")
public class RecipeBrewing extends BaseEntity {

    //@Column(name = "object")
    //public long Object;

    @Column(name = "object")
    public ObjectTbl Object;


    @Column(name = "recipe")
    public String Recipe;
}
