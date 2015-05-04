package com.minergame.minerguide.db.Entity;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "messages", id = "_id")
public class Message extends BaseEntity{

    public Message() {
    }

    public Message(String fromName, String message, boolean isSelf) {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
    }

    @Column(name = "fromName")
    public String fromName;

    @Column(name = "message")
    public String message;

    @Column(name = "isSelf")
    public boolean isSelf;
}
