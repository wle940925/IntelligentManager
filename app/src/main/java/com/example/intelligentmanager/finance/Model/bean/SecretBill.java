package com.example.intelligentmanager.finance.Model.bean;

import android.graphics.drawable.Drawable;

import com.example.intelligentmanager.finance.Model.config.Global;
import com.example.intelligentmanager.finance.MyApplication;
import com.example.intelligentmanager.finance.Utils.ResourceIdUtils;
import com.example.intelligentmanager.utils.DateUtils;

/**
 * Created by 易水柔 on 2017/3/28.
 */
public class SecretBill {
    private String money;
    private String typeId;
    private String time;
    private String remark;
    private Drawable typedrawable;
    public SecretBill(){
    }
    public SecretBill(String money, String typeId, String remark, String time, Drawable typedrawable) {
        this.money = money;
        this.typeId = typeId;
        this.remark = remark;
        this.time = time;
        this.typedrawable = typedrawable;
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
    public String getTypeName() {
        return Global.types[Integer.valueOf(this.getTypeId())];
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

    public Drawable getTypedrawable() {
        int resId = ResourceIdUtils.getIdOfResource("type_" + this.getTypeId() + "_normal", "drawable");
        return MyApplication.getContext().getResources().getDrawable(resId);
    }
}
