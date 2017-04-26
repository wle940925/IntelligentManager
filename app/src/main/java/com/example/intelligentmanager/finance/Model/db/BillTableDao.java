package com.example.intelligentmanager.finance.Model.db;

import android.content.Context;

import com.example.intelligentmanager.finance.Model.bean.Bill;

import java.util.List;
/**
 * Created by ASUS on 2017/3/13.
 * 账单表业务逻辑类
 */

public class BillTableDao {
    public static final String TABLE_NAME = "bill_table";
    public static final String COLUMN_NAME_FLAG="flag";
    public static final String COLUMN_NAME_ID = "id"; //主键
    public static final String COLUMN_NAME_ACCOUNT = "account"; //账户
    public static final String COLUMN_NAME_MONEY = "money"; //记账金额
    public static final String COLUMN_NAME_TYPE = "type";//账单类型
    public static final String COLUMN_NAME_TIME = "time"; //记账时间
    public static final String COLUMN_NAME_REMARK = "remark"; //记账备注
    public static final String COLUMN_NAME_UPLOAD = "upload";//标记是否已经备份,0未备份,1已备份

    public BillTableDao(Context context) {
        DBManager.getInstance().onInit(context);
    }

    //保存账单list
    public void saveBillList(List<Bill> bills) {
        DBManager.getInstance().saveBillList(bills);
    }

    //保存单个记录
    public static boolean saveBill(Bill bill) {
        return DBManager.getInstance().saveBill(bill);
    }

    //根据日期获取账单List
    public List<Bill> getBillList(long time) {
        return DBManager.getInstance().getBill(time);
    }

    public static List<Bill> getrmBillList(){
        return DBManager.getInstance().getrmBill();
    }

    //根据日期获取单个账单
    public Bill getBill(long time) {
        return DBManager.getInstance().getOneBill(time);
    }
    //获取未更新到服务器的账单
    public List<Bill> getUnUploadBillList() {
        return DBManager.getInstance().getUnUploadBillList();
    }

    //删除一条账单
    public void updateBillList(List<Bill> bills) {
        DBManager.getInstance().updateBillList(bills);
    }

    public void rmBill(Bill bill)
    {
        DBManager.getInstance().rmBill(bill);
    }
}
