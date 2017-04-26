package com.example.intelligentmanager.bean;

/**
 * Created by 易水柔 on 2017/3/22.
 */
public class TransactionBean {
    private String title;
    private String time;
    private long dtstart;
    private long dtend;
    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TransactionBean() {

    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TransactionBean(String title, long dtstart, long dtend, String content) {
        this.title = title;
        this.dtstart = dtstart;
        this.dtend = dtend;
        this.content = content;
    }

    public long getDtstart() {

        return dtstart;
    }

    public void setDtstart(long dtstart) {
        this.dtstart = dtstart;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDtend() {
        return dtend;
    }

    public void setDtend(long dtend) {
        this.dtend = dtend;
    }
}
