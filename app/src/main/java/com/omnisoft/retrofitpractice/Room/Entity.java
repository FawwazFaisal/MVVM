package com.omnisoft.retrofitpractice.Room;

import androidx.room.PrimaryKey;

@androidx.room.Entity(tableName = "heroes")
public class Entity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String bio;
    private String createdby;
    private String firstappearance;
    private String imageurl;
    private String name;
    private String publisher;
    private String realname;
    private String team;

    public Entity(String bio, String createdby, String firstappearance, String imageurl, String name, String publisher, String realname, String team) {
        this.bio = bio;
        this.createdby = createdby;
        this.firstappearance = firstappearance;
        this.imageurl = imageurl;
        this.name = name;
        this.publisher = publisher;
        this.realname = realname;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getFirstappearance() {
        return firstappearance;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getRealname() {
        return realname;
    }

    public String getTeam() {
        return team;
    }
}
