package com.minergame.minerguide.chat;

import com.minergame.minerguide.db.Entity.Message;

/**
 * Created by Tareq on 04/03/2015.
 */
public interface IChatMessage  {
    public void onMessage(Message message);
    public void onConnect();
    public void onDisconnect(int code, String reason);
    public void onError(Exception error);

}
