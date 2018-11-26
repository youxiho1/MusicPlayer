package com.yang.model;

import com.yang.util.DateUtil;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class MusicSheet {
    private int id;
    private String uuid;
    private String name;
    private String creatorid;
    private String creator;
    private String datecreated;
    private String picture;
    // <MD5, Music file name>
    private Map<String, String> musicItems;
    private int flag;

    public MusicSheet() {
        uuid = UUID.randomUUID().toString().replace("-", "");
        datecreated = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss");
        File defaultIcon = new File("resources/defaultSheetIcon.jpg");
        picture = defaultIcon.getAbsolutePath();
    }

    public MusicSheet(String name) {
        this();
        this.name = name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorId) {
        this.creatorid = creatorId;
    }

    public void setDatecreated(String dateCreated) {
        this.datecreated = dateCreated;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Map<String, String> getMusicItems() {
        return musicItems;
    }

    public void setMusicItems(Map<String, String> musicItems) {
        this.musicItems = musicItems;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
