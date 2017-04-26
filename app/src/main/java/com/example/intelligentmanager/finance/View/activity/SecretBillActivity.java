package com.example.intelligentmanager.finance.View.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.finance.Model.bean.SecretBill;
import com.example.intelligentmanager.finance.Model.db.BillTableDao;
import com.example.intelligentmanager.finance.Model.db.DbOpenHelper;
import com.example.intelligentmanager.finance.View.adapter.SecretBillAdapter;

/**
 * Created by 易水柔 on 2017/3/27.
 */
public class SecretBillActivity extends Activity{
    private RecyclerView mRecyclerView;
    private SecretBillAdapter secretBillAdapter;
    private DbOpenHelper dbOpenHelper;
    private SecretBill bill[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secret_bill);
        Log.i("bill","创建成果");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_secret);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        dbOpenHelper=new DbOpenHelper(this,"MYDB.db",null,4);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + BillTableDao.TABLE_NAME + " where "+
                BillTableDao.COLUMN_NAME_FLAG+"=?", new String[]{"0"} );
        bill=new SecretBill[cursor.getCount()];
        int position=0;
        if (cursor.moveToFirst()) {
            do {
                bill[position] = new SecretBill();
                bill[position].setMoney(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_MONEY)));
                bill[position].setTypeId(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TYPE)));
                bill[position].setTime(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_TIME)));
                bill[position].setRemark(cursor.getString(cursor.getColumnIndex(BillTableDao.COLUMN_NAME_REMARK)));
                Log.i("bill",bill[position].getMoney());
                position++;

            }while (cursor.moveToNext());
        }
        cursor.close();


        //设置适配器
        secretBillAdapter = new SecretBillAdapter(this,bill);
        mRecyclerView.setAdapter(secretBillAdapter);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        setContentView(R.layout.secret_bill_items);
//        Log.i("bill","创建成果");
//
    }
