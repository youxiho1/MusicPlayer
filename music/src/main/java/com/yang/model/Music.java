package com.yang.model;

public class Music {
    private int id;
    private String md5value;
    private String name;
    private String singer;
    private String url;
    private int count;
    private int islike;
    private int rowid;          //勿用

    public Music() {
    }


    public Music(int id, String md5value, String name, String singer, String url) {
        this.id = id;
        this.md5value = md5value;
        this.name = name;
        this.singer = singer;
        this.url = url;
        this.count = 0;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMd5value() {
        return md5value;
    }

    public void setMd5value(String md5value) {
        this.md5value = md5value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrCount() {
        count++;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Music) {
            Music music = (Music) obj;
            return id == music.getId() && name.equals(music.getName())
                    && singer.equals(music.getSinger()) && md5value.equals(music.getMd5value());
        }
        return false;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }
}
