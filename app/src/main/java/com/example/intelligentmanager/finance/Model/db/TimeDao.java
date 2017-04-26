package com.example.intelligentmanager.finance.Model.db;

import android.content.Context;

import com.example.intelligentmanager.finance.Model.bean.BillType;

import java.util.List;
/**
 * Created by 聂敏萍 on 2017/3/13.
 * 次数表
 */

public class TimeDao {
    public static final String TABLE_NAME = "s_time";

    public TimeDao(Context context) {
        DBManager.getInstance().onInit(context);
    }

    //根据当前的时间段更新次数表
    public void updateTime(BillType billType) {
        DBManager.getInstance().updateTime(billType);
    }

    //根据当前的时间段获取排好序的次数表
    public List<BillType> getBillTypeList() {
        return DBManager.getInstance().getBillTypeList();
    }
}
