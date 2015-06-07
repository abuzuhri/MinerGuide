package com.minergame.minerguide.db.Dao;

import com.activeandroid.query.Update;
import com.minergame.minerguide.db.Entity.AppSetting;
import com.minergame.minerguide.db.Entity.ObjectTbl;

/**
 * Created by tareq on 05/22/2015.
 */
public class AppSettingDao  extends BaseDao {

    public AppSetting getAppSetting(){
        return AppSetting.load(AppSetting.class, 1);
    }

    public void setNotificationChat(boolean isEnable){

        new Update(AppSetting.class)
                .set("NotificationChat = ?",isEnable ? 1 : 0)
                .where("_id = ?", 1)
                .execute();
    }

    public void setNotificationMovie(boolean isEnable){

        new Update(AppSetting.class)
                .set("NotificationMovie = ?",isEnable ? 1 : 0)
                .where("_id = ?", 1)
                .execute();
    }

}
