package com.example.intelligentmanager.bean;

import android.graphics.Bitmap;

/**
 * Created by 易水柔 on 2017/3/16.
 */
public class SportsBean {
    private String name;
    private int record;
    private Bitmap img;
    private String username;
    private int ranki;

    public SportsBean() {

    }

    public SportsBean(String name, int record, Bitmap img, String username, int ranki) {
        this.img = img;
        this.name = name;
        this.ranki = ranki;
        this.record = record;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRanki() {
        return ranki;
    }

    public void setRanki(int ranki) {
        this.ranki = ranki;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }


}
