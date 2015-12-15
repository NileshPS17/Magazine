package com.cloudfoyo.magazine.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nilesh on 15/12/15.
 */
public class MagazineDatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 3;
    public static final String DB_NAME = "Magazine";





    public static class ArticlesContract {


        public static final String TABLE_NAME   = "Articles";
        public static final String ID           = "_id",
                                   CAT_ID       = "cat_id",
                                   TITLE        = "article_title",
                                   IMAGE        = "article_image",
                                   AUTHOR       = "article_author" ,
                                   DATE         = "date_published",
                                   CONTENT      = "content";

        public static final String CREATE_TABLE_ARTICLES = "CREATE TABLE "+TABLE_NAME + " (" +
                                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    CAT_ID + " INTEGER NOT NULL, " +
                                    TITLE + " VARCHAR(500), " +
                                    IMAGE + " VARCHAR(250), " +
                                    AUTHOR + " VARCHAR(250), " +
                                    DATE   + " VARCHAR(100), " +
                                    CONTENT + " TEXT " +  ")";


    }


    public MagazineDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static class CategoriesContract {


        public static final String TABLE_NAME   = "Categories";
        public static final String ID           = "_id",
                                   NAME         = "cat_name",
                                   IMAGE        = "cat_image",
                                   DESCRIPTION  = "cat_description",
                                   CREATED_AT   = "cat_created_at";


        public static final String CREATE_TABLE_CATEGORIES  = "CREATE TABLE " + TABLE_NAME + " ( " +
                                                            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                            NAME + " VARCHAR(250) NOT NULL ," +
                                                            IMAGE + " VARCHAR(250) NOT NULL , " +
                                                            DESCRIPTION + " TEXT , " +
                                                            CREATED_AT + " VARCHAR(1000) " + ")";


    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CategoriesContract.CREATE_TABLE_CATEGORIES);
        db.execSQL(ArticlesContract.CREATE_TABLE_ARTICLES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + CategoriesContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ArticlesContract.TABLE_NAME);
        onCreate(db);


    }
}
