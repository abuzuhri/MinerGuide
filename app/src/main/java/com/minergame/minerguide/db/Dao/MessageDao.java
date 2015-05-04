package com.minergame.minerguide.db.Dao;

import com.activeandroid.query.Select;
import com.minergame.minerguide.db.Entity.Message;

import java.util.List;

/**
 * Created by Tareq on 04/07/2015.
 */
public class MessageDao extends BaseDao {

    public Message getById(long Id){
        return Message.load(Message.class, Id);
    }

    public List<Message> getAllMessage(){

        return new Select()
                .from(Message.class)
                .execute();
    }

    public List<Message> getAllMessage(long LastId){
        return new Select()
                .from(Message.class)
                .where("_id >  ?",LastId)
                .execute();
    }

}
