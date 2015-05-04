package com.minergame.minerguide.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tareq on 03/21/2015.
 */

@Table(name = "images", id = "_id")
public class ImageTbl extends BaseEntity {

    //@Column(name = "object")
    //public long Object;

    @Column(name = "object")
    public ObjectTbl Object;

    @Column(name = "image_name")
    public String ImageName;

}
