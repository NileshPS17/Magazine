package com.cloudfoyo.magazine.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import static com.cloudfoyo.magazine.sqlite.MagazineDatabaseHelper.ArticlesContract;
import static com.cloudfoyo.magazine.sqlite.MagazineDatabaseHelper.CategoriesContract;

/**
import static com.cloudfoyo.magazine.sqlite.MagazineDatabaseHelper.ArticlesContract;
import static com.cloudfoyo.magazine.sqlite.MagazineDatabaseHelper.CategoriesContract;
 **/


public class MagazineContentProvider extends ContentProvider {

private static final String LOG_TAG = MagazineContentProvider.class.getSimpleName();

    private  MagazineDatabaseHelper helper = null;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);




    public static final String AUTHORITY = "com.cloudfoyo.magazine.provider";

    public MagazineContentProvider() {

        super();

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        int result = uriMatcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret=0;
        switch (result)
        {
            case UriMatcherContract.ARTICLE_BY_ID:
                ret = db.delete(ArticlesContract.TABLE_NAME, selection, selectionArgs);
                break;

            case UriMatcherContract.CAT_BY_ID:
                ret = db.delete(CategoriesContract.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI");
        }

        return ret;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db  = helper.getWritableDatabase();
        int result = uriMatcher.match(uri);
        switch (result)
        {

            case UriMatcherContract.ALL_ARTICLES:
                db.insert(ArticlesContract.TABLE_NAME, null, values);
                break;

            case UriMatcherContract.ALL_CATEGORIES:
                db.insert(CategoriesContract.TABLE_NAME, null, values);

            default:
                throw new IllegalArgumentException("Invalid Uri");

        }

        return null;


    }

    @Override
    public boolean onCreate() {

        helper = new MagazineDatabaseHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor returnCursor = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {

            int result = uriMatcher.match(uri);
            String query;
            switch(result)
            {
                case UriMatcherContract.ALL_ARTICLES:

                     query = "SELECT a."+ ArticlesContract.ID + ", a."+ArticlesContract.TITLE +
                                    ", a."+ArticlesContract.IMAGE+", a."+ArticlesContract.AUTHOR +
                                    ", a."+ ArticlesContract.DATE +", c." +
                                    CategoriesContract.NAME + ", a."+ArticlesContract.CONTENT +
                                    " FROM "+ArticlesContract.TABLE_NAME + " as a, " + CategoriesContract.TABLE_NAME +" as c " +
                                    " WHERE a." + ArticlesContract.CAT_ID + "=c." + CategoriesContract.ID;

                    returnCursor = db.rawQuery(query, null);
                    Log.d(LOG_TAG, "ALL_ARTICLES");
                    break;

                case UriMatcherContract.ALL_ARTICLES_BY_CATEGORY:

                    query = "SELECT * FROM " + ArticlesContract.TABLE_NAME;
                    int cat_id = Integer.parseInt(uri.getLastPathSegment());
                    query = query + " WHERE " + ArticlesContract.CAT_ID + " = " + cat_id;
                    returnCursor = db.rawQuery(query, null);
                    Log.d(LOG_TAG, "ALL_ARTICLES_BY_CATEGORY");
                    break;

                case UriMatcherContract.ALL_CATEGORIES:

                    query = "SELECT * FROM " + CategoriesContract.TABLE_NAME;
                    returnCursor = db.rawQuery(query, null);
                    Log.d(LOG_TAG, "ALL_CATEGORIES");
                    break;

                case UriMatcherContract.ARTICLE_BY_ID:
                    int id = Integer.parseInt(uri.getLastPathSegment());
                    query = "SELECT * FROM "+ArticlesContract.TABLE_NAME + " WHERE " + ArticlesContract.ID + "="+id;
                    returnCursor = db.rawQuery(query, null);
                    Log.d(LOG_TAG, "ARTICLE_BY_ID");
                    break;

                default:
                    throw new IllegalArgumentException("Refer Documentation and choose the correct URI");

            }

        }
        catch (IllegalArgumentException e)
        {
            throw e;
        }
        catch (SQLiteException e)
        {
            returnCursor = null;
        }

        return returnCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int result = uriMatcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (result)
        {
            case UriMatcherContract.ARTICLE_BY_ID:
                db.update(ArticlesContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            case UriMatcherContract.CAT_BY_ID:
                db.update(CategoriesContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI");

        }

        return 1;
    }


    static {


            uriMatcher.addURI(AUTHORITY, "/articles", UriMatcherContract.ALL_ARTICLES);
            uriMatcher.addURI(AUTHORITY, "/articleById/#", UriMatcherContract.ARTICLE_BY_ID);
            uriMatcher.addURI(AUTHORITY, "/categories", UriMatcherContract.ALL_CATEGORIES);
            uriMatcher.addURI(AUTHORITY, "/categories/#", UriMatcherContract.ALL_ARTICLES_BY_CATEGORY);
            uriMatcher.addURI(AUTHORITY, "/categoryById/#", UriMatcherContract.CAT_BY_ID);

    }

    private static class UriMatcherContract {

      /**  public static final int ADD_ARTICLE = 1,
                EDIT_ARTICLE = 2,
                DELETE_ARTICLE = 3,
                ADD_CATEGORY = 4,
                EDIT_CATEGORY = 5,
                DELETE_CATEGORY = 6;  **/

        public static final int ALL_ARTICLES = 1,
                                 ALL_CATEGORIES = 2,
                                 ALL_ARTICLES_BY_CATEGORY = 3 ,
                                 ARTICLE_BY_ID = 4,
                                 CAT_BY_ID = 5;

    }
}
