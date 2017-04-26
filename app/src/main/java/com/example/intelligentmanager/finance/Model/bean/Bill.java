package com.example.intelligentmanager.finance.Model.bean;


import android.graphics.drawable.Drawable;

import com.example.intelligentmanager.finance.Model.config.Global;
import com.example.intelligentmanager.finance.MyApplication;
import com.example.intelligentmanager.finance.Utils.ResourceIdUtils;
import com.example.intelligentmanager.utils.DateUtils;

/**
 * Created by 聂敏萍 on 2017/3/13.
 * 账单表实体类
 */

public class Bill {
    private int id;
    private String flag;
    private String accountId;
    private String money;
    private String typeId;
    private String time;
    private String remark;
    private int upload = 0;//0表示未更新，1表示已更新
    private String typeName;//记账种类的名称
    private Drawable typeDrawable;//记账种类对应的图像

    public Bill() {
    }



    public Bill(int id, String accountId, String money, String typeId, String time, String flag, String remark, int upload) {
        this.id=id;
        this.flag=flag;
        this.accountId=accountId;
        this.money = money;
        this.typeId = typeId;
        this.time = time;
        this.remark = remark;
        this.upload = upload;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        String temp= DateUtils.getDateToString(Long.parseLong(time));
        this.time = temp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public String getTypeName() {
        return Global.types[Integer.valueOf(this.getTypeId())];
    }

    public Drawable getTypeDrawable() {
        int resId = ResourceIdUtils.getIdOfResource("type_" + this.getTypeId() + "_normal", "drawable");
        return MyApplication.getContext().getResources().getDrawable(resId);
    }

    @Override
    public String toString() {
        return "typeId:--->" + this.getTypeId() + "\n" +
                "price:--->" + this.getMoney();
    }
}
