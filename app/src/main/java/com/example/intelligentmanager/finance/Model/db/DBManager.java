package com.example.intelligentmanager.finance.Model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.intelligentmanager.finance.Model.bean.Bill;
import com.example.intelligentmanager.finance.Model.bean.BillType;
import com.example.intelligentmanager.finance.Model.config.Global;
import com.example.intelligentmanager.finance.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 聂敏萍 on 2017/3/13.
 * SQLite数据库操作类
 */

public class DBManager {
    static private DBManager dbMgr = new DBManager();
    private DbOpenHelper dbHelper;

    public void onInit(Context context) {
        dbHelper = DbOpenHelper.getInstance(context);
    }

    public static synchronized DBManager getInstance() {
        return dbMgr;
    }


    synchronized public void closeDB() {
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
    }

    //保存账单list
    synchronized void saveBillList(List<Bill> bills) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(BillTableDao.TABLE_NAME, null, null);
            for (Bill bill : bills) {
                ContentValues cv = new ContentValues();
                cv.put(BillTableDao.COLUMN_NAME_ACCOUNT, bill.getAccountId());
                cv.put(BillTableDao.COLUMN_NAME_MONEY, bill.getMoney());
                cv.put(BillTableDao.COLUMN_NAME_TYPE, bill.getTypeId());
                cv.put(BillTableDao.COLUMN_NAME_TIME, bill.getTime());
                cv.put(BillTableDao.COLUMN_NAME_REMARK, bill.getRemark());
                cv.put(BillTableDao.COLUMN_NAME_UPLOAD, bill.getUpload());
                db.replace(BillTableDao.TABLE_NAME, null, cv);
            }
        }
    }

    //更新账单list
    synchronized void updateBillList(List<Bill> bills) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            for (Bill bill : bills) {
                ContentValues cv = new ContentValues();
                cv.put(BillTableDao.COLUMN_NAME_UPLOAD, 1);
                db.update(BillTableDao.TABLE_NAME, cv, "time = ?", new String[]{bill.getTime() + ""});
            }
        }
    }

    //保存单个账单
    synchronized boolean saveBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean b = false;
        if (db.isOpen()) {
            ContentValues cv = new ContentValues();
            cv.put(BillTableDao.COLUMN_NAME_FLAG,bill.getFlag());
            cv.put(BillTableDao.COLUMN_NAME_ACCOUNT, bill.getAccountId());
            cv.put(BillTableDao.COLUMN_NAME_MONEY, bill.getMoney());
            cv.put(BillTableDao.COLUMN_NAME_TYPE, bill.getTypeId());
            cv.put(BillTableDao.COLUMN_NAME_TIME, bill.getTime());
            cv.put(BillTableDao.COLUMN_NAME_REMARK, bill.getRemark());
            cv.put(BillTableDao.COLUMN_NAME_UPLOAD, bill.getUpload());
            b=db.replace(BillTableDao.TABLE_NAME, null, cv)==-1?false:true;
        }
        return b;
    }

    //根据日期获取账单
    synchronized List<Bill> getBill(long time) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Bill> bills = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + BillTableDao.TABLE_NAME + " where " + BillTableDao.COLUMN_NAME_TIME + " > ? and "
                    +BillTableDao.COLUMN_NAME_FLAG+"=? order by time", new String[]{time + "","1"});
            Bill bill;
            while (cursor.moveToNext()) {
                bill = new Bill();
                bill.setFlag(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_FLAG)));
                bill.setId(cursor.getInt(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_ID)));
                bill.setAccountId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_ACCOUNT)));
                bill.setMoney(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_MONEY)));
                bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TYPE)));
                bill.setTime(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TIME)));
                bill.setRemark(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_REMARK)));
                bill.setUpload(cursor.getInt(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_UPLOAD)));
                bills.add(bill);
            }
            cursor.close();
        }
        return bills;
    }    //根据日期获取账单
    synchronized List<Bill> getrmBill() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Bill> bills = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + BillTableDao.TABLE_NAME + " where "+BillTableDao.COLUMN_NAME_FLAG+"=?", new String[]{"0"} );
            Bill bill;
            while (cursor.moveToNext()) {
                bill = new Bill();
                bill.setFlag(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_FLAG)));
                bill.setId(cursor.getInt(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_ID)));
                bill.setAccountId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_ACCOUNT)));
                bill.setMoney(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_MONEY)));
                bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TYPE)));
                bill.setTime(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TIME)));
                bill.setRemark(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_REMARK)));
                bill.setUpload(cursor.getInt(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_UPLOAD)));
                bills.add(bill);
            }
            cursor.close();
        }
        return bills;
    }

    //根据日期获取单个账单（时间不具有唯一性）
    synchronized Bill getOneBill(long time) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Bill bill= new Bill();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + BillTableDao.TABLE_NAME + " where " + BillTableDao.COLUMN_NAME_TIME + " > ?", new String[]{time + ""});
            bill.setAccountId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_ACCOUNT)));
            bill.setMoney(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_MONEY)));
            bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TYPE)));
            bill.setTime(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TIME)));
            bill.setRemark(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_REMARK)));
            bill.setUpload(cursor.getInt(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_UPLOAD)));

            cursor.close();
        }
        return bill;

    }

    //获取未更新到服务器的账单
    synchronized List<Bill> getUnUploadBillList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Bill> bills = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + BillTableDao.TABLE_NAME + " where " + BillTableDao.COLUMN_NAME_UPLOAD + " = ?", new String[]{"0"});
            Bill bill;
            while (cursor.moveToNext()) {
                bill = new Bill();
                bill.setAccountId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_ACCOUNT)));
                bill.setMoney(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_MONEY)));
                bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TYPE)));
                bill.setTime(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TIME)));
                bill.setRemark(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_REMARK)));
                bill.setUpload(cursor.getInt(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_UPLOAD)));
                bills.add(bill);
            }
            cursor.close();
        }
        return bills;
    }

    //更新次数表
    synchronized void updateTime(BillType billType) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues cv = new ContentValues();
            cv.put("type_" + billType.getTypeId(), billType.getTime());
            db.update(TimeDao.TABLE_NAME, cv, "time = ?", new String[]{TimeUtils.getCurrentTime() + ""});
        }
    }

    //获取所有记账类型
    synchronized List<BillType> getBillTypeList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<BillType> billTypes = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TimeDao.TABLE_NAME + " where time = ?", new String[]{TimeUtils.getCurrentTime() + ""});
            BillType billType;
            while (cursor.moveToNext()) {
                for (int i = 0; i < Global.types.length; i++) {
                    billType = new BillType();
                    billType.setTime(cursor.getInt(cursor.getColumnIndex("type_" + i)));
                    billType.setTypeId(i);
                    billType.setTypeName(Global.types[i]);
                    billTypes.add(billType);
                }
            }
            cursor.close();
        }
        return billTypes;
    }

    //删除一条账单
    synchronized void rmBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values=new ContentValues();
            values.put("flag","0");
            db.update(BillTableDao.TABLE_NAME,values,"id = ?", new String[]{bill.getId() + ""});
//            db.delete(BillTableDao.TABLE_NAME, "id = ?", new String[]{bill.getId() + ""});
            Log.d("log", "rmBill: succeed");
        }
    }
}
