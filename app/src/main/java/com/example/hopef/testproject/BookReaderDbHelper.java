package com.example.hopef.testproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hopef on 6/16/2017.
 */

public class BookReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "BookReader.db";
    public static final String bTableName = "BookList";
    public static final String bColNames = "BookName";
    public static final String pTableName = "PageList";
    public static final String pColNames = "BookName";
    public static final String pColForeignKeys = "BookIds";
    public static final String pColImage = "ImageData";
    public static final String pColText = "TextData";
    public static final String pColSound = "SoundData";
    public static final String bCreateQuery = "CREATE TABLE " + bTableName + "(" + bColNames + " TEXT);";
    public static final String pCreateQuery = "CREATE TABLE " + pTableName + "(" + pColNames + " TEXT, " + pColForeignKeys + " INTEGER, " + pColImage + " BLOB);";
    public static SQLiteDatabase db;
    //public static final String colBookTexts = "Books";

    public BookReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(bCreateQuery);
        db.execSQL(pCreateQuery);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+bTableName);
        db.execSQL(bCreateQuery);
        db.execSQL("DROP TABLE IF EXISTS "+pTableName);
        db.execSQL(pCreateQuery);
    }
}
