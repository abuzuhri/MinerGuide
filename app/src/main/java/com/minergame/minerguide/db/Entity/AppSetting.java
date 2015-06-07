package com.minergame.minerguide.db.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by tareq on 05/22/2015.
 */

@Table(name = "app_setting", id = "_id")
public class AppSetting  extends BaseEntity {

    @Column(name = "NotificationChat")
    public Boolean NotificationChat ;

    @Column(name = "NotificationMovie")
    public Boolean NotificationMovie ;


}
