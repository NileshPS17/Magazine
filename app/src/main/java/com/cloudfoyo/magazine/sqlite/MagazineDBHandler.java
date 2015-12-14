package com.cloudfoyo.magazine.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nilesh on 14/12/15.
 */
public class MagazineDBHandler extends SQLiteOpenHelper {


    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Magazine";


    private Context context;

    public MagazineDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
