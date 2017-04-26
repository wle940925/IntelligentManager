package com.example.intelligentmanager.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.intelligentmanager.finance.Model.db.DbOpenHelper;

/**
 * Created by 易水柔 on 2017/3/21.
 */
public class SmsObserverService extends Service {
    private DbOpenHelper dbHelper;

    private Context context=this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        context.getContentResolver().registerContentObserver(
                Telephony.Sms.CONTENT_URI, true, new SmsObserver(new Handler()));
    }

    private class SmsObserver extends ContentObserver {

        public SmsObserver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            //查询发送箱中的短信（正在发送中的短信放在发送箱中）
            Cursor cursor = getContentResolver().query(Telephony.Sms.CONTENT_URI, new String[]{"_id", "address",
                            "date", "body", "type"},
                    "address=? and read=?", new String[]{"1110", "0"}, "date desc");
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String smsbody = cursor
                            .getString(cursor.getColumnIndex("body"));
                    String money = smsbody.substring(smsbody.indexOf("额") + 1,
                            smsbody.indexOf("元"));
                    String time = String.valueOf(cursor.getLong(cursor.getColumnIndex("date")));
                    //Bill b = new Bill("银行卡", money, "6", time, "银行卡消费", 1);
                    dbHelper=new DbOpenHelper(context,"MYDB.db",null,4);
                    SQLiteDatabase db=dbHelper.getReadableDatabase();
                    ContentValues values=new ContentValues();
                    if (smsbody.contains("消费")){
                        values.put("account","银行卡");
                        values.put("money",money);
                        values.put("type","6");
                        values.put("flag","1");
                        values.put("time", time);
                        values.put("remark","银行卡消费");
                        values.put("upload",1);
                    }else if (smsbody.contains("收入")){
                        values.put("account","银行卡");
                        values.put("money",money);
                        values.put("type","13");
                        values.put("flag","1");
                        values.put("time",time);
                        values.put("remark","银行卡收入");
                        values.put("upload",1);
                    }

                    db.insert("bill_table",null,values);
                    Log.i("----》","银行卡消费");
                    values.clear();
                    db.close();

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.getContentResolver().unregisterContentObserver(new SmsObserver(new Handler()));
    }
}
