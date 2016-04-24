package com.example.myvalentine.myweather.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by myValentine on 16/3/7.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String ALL_CITIES = "create table City("+
            "id integer primary key autoincrement,"+
            "city_name text)";

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ALL_CITIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
