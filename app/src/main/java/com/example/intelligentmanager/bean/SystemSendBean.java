package com.example.intelligentmanager.bean;

import android.graphics.Bitmap;

/**
 * Created by 易水柔 on 2017/3/20.
 */
public class SystemSendBean {
    private String content;
    private String time;
    public SystemSendBean(){

    }
    public SystemSendBean(Bitmap img, String time, String content, String name) {
        this.time = time;
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
