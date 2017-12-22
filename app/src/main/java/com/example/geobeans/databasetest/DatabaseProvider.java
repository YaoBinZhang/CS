package com.example.geobeans.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR=0;
    public static final int BOOK_ITEM=1;
    public static final int CATEGORY_DIR=2;
    public static final int CATEGORY_ITEM=3;
    public static  final  String AUTHORITY="com.example.geobeans.databasetest.provider";
    private static final String pathDir="vnd.android.cursor.dir/vnd.";
    private static final String pathItem="vnd.android.cursor.item/vnd.";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }


    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper=new MyDatabaseHelper(getContext(),"BookStore.db",null,2);
        return true;
    }


    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                //三部分组成   1. vnd开头 2. Uri 以路径结尾 则加android.cursor.dir  以id结尾 则加android.cursor.item
                return pathDir+AUTHORITY+".book";
            case BOOK_ITEM:
                return pathItem+AUTHORITY+".book";
            case CATEGORY_DIR:
                return pathDir+AUTHORITY+".category";
            case CATEGORY_ITEM:
                return pathItem+AUTHORITY+".category";

        }
        return  null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
       SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri uriReturn=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId=db.insert("Book",null,values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId=db.insert("Category",null,values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
            default:
                break;
        }

        return uriReturn;
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int deletedRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deletedRows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String booId=uri.getPathSegments().get(1);
                deletedRows=db.delete("Book","id=?",new String[]{booId});
                break;
            case CATEGORY_DIR:
                deletedRows = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                deletedRows=db.delete("Category","id=?",new String[]{categoryId});
                break;
            default:
                break;
        }

        return  deletedRows;
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updatedRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updatedRows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String booId=uri.getPathSegments().get(1);
                updatedRows=db.update("Book",values,"id=?",new String[]{booId});
                break;
            case CATEGORY_DIR:
                updatedRows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                updatedRows=db.update("Category",values,"id=?",new String[]{categoryId});
                break;
            default:
                break;
        }

        return  updatedRows;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String booId=uri.getPathSegments().get(1);
                cursor=db.query("Book",projection,"id=?",new String[]{booId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                cursor=db.query("Category",projection,"id=?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return  cursor;
    }

}