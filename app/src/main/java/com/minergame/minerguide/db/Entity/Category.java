package com.minergame.minerguide.db.Entity;

import com.activeandroid.annotation.*;

/**
 * Created by Tareq on 03/21/2015.
 */

@Table(name = "categories", id = "_id")
public class Category {

    @Column(name = "name")
    public String name;

    @Column(name = "image")
    public String image;

    @Column(name = "object_category")
    public String object_category;


}
