package com.example.intelligentmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 易水柔 on 2017/3/7.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper{

    public static final String ranking_sql = "create table ranker ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "record integer)";

    public static final String content_sql = "create table content ("
            + "id integer primary key autoincrement, "
            + "username text,"
            + "time text,"
            + "mcontent text,"
            + "gnumber integer)";
    public static final String systemsend_sql = "create table systemsend ("
            + "id integer primary key autoincrement, "
            + "time text,"
            + "mcontent text)";
    public static final String user_sql = "create table user ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "password text,"
            + "name text,"
            + "gender text,"
            + "motto text,"
            + "head blob)";
    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(user_sql);
        db.execSQL(ranking_sql);
        db.execSQL(content_sql);
        db.execSQL(systemsend_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
