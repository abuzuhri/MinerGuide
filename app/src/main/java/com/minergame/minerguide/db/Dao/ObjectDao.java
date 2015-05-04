package com.minergame.minerguide.db.Dao;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.minergame.minerguide.db.Entity.ObjectTbl;
import com.minergame.minerguide.utils.AppConstant;
import com.minergame.minerguide.utils.AppLog;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Tareq on 03/21/2015.
 */
public class ObjectDao extends BaseDao{

    public ObjectTbl getById(long Id){
        return ObjectTbl.load(ObjectTbl.class, Id);
    }


    public List<ObjectTbl> getAllByMenu(int MenuID){

        Calendar calendar = Calendar.getInstance();
        String[] categoryName=new String[] {"Mob"};


        if(MenuID == AppConstant.AppDrawer.All.id){
            return getAll();
        }else if(MenuID == AppConstant.AppDrawer.Blocks.id){
            categoryName=new String[] {"Item"};
        }else if(MenuID== AppConstant.AppDrawer.Mobs.id){
            categoryName=new String[] {"Mob"};
        }else if(MenuID== AppConstant.AppDrawer.Achievements.id){
            categoryName=new String[] {"Achievement"};
        }else if(MenuID== AppConstant.AppDrawer.Biomes.id){
            categoryName=new String[] {"Biome"};
        }else if(MenuID== AppConstant.AppDrawer.Favorites.id){
            return getAllFavorites();
        }else if(MenuID== AppConstant.AppDrawer.Redsone.id){
            categoryName=new String[] {"Redstone"};
        }else if(MenuID== AppConstant.AppDrawer.Potions.id){
            return  getAllByMenu("Item","Potions");
        }

        long currentTimeInMillis= calendar.getTimeInMillis();
        return new Select()
                .from(ObjectTbl.class)
                .where("category =  ?",categoryName)
                .execute();
    }
    public List<ObjectTbl> getAllByMenu(String category,String SubCategory){

        return new Select()
                .from(ObjectTbl.class)
                .where("category =  ?",category).and("sub_category = ?",SubCategory)
                .execute();
    }
    public List<ObjectTbl> getAll(){

        return new Select()
                .from(ObjectTbl.class)
                .execute();
    }

    public void setFavorite(boolean isFav,long ID){

        new Update(ObjectTbl.class)
                .set("favorite = ?",isFav ? 1 : 0)
                .where("_id = ?", ID)
                .execute();
    }
    public List<ObjectTbl> getAllFavorites(){

        return new Select()
                .from(ObjectTbl.class)
                .where("favorite = ?", 1)
                .execute();
    }
    public List<ObjectTbl> getAll(String search){
        search="%"+search+"%";
        AppLog.i("search==> "+search);

        return new Select()
                .from(ObjectTbl.class)
                .where("name like ?", search).or("category like  ?",search).or("sub_category like ?",search)
                .execute();
    }

}
