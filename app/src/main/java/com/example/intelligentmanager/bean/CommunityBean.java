package com.example.intelligentmanager.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by 易水柔 on 2017/3/16.
 */
public class CommunityBean {
    private String name;
    private String time;
    private String content;
    private String userName;
    private int number;
    private Bitmap img;
    private Drawable goodImg;

    public CommunityBean(){

    }

    public CommunityBean(String name, String time, String content, String userName, int number, Bitmap img) {
        this.name = name;
        this.time = time;
        this.content = content;
        this.userName = userName;
        this.number = number;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Drawable getGoodImg() {
        return goodImg;
    }

    public void setGoodImg(Drawable goodImg) {
        this.goodImg = goodImg;
    }
}
