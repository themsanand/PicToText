package com.example.hopef.testproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hopef on 6/16/2017.
 */

public class BookReaderDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 16;
    private static final String DATABASE_NAME = "BookReader.db";
    private static final String bTableName = "BookList";
    private static final String bColNames = "BookName";
    private static final String pTableName = "PageList";
    private static final String pColNames = "BookName";
    private static final String pColForeignKeys = "BookIds";
    private static final String pColImage = "ImageData";
    private static final String pColText = "TextData";
    private static final String pColSound = "SoundData";
    private static final String bCreateQuery = "CREATE TABLE " + bTableName + "(" + bColNames + " TEXT);";
    private static final String pCreateQuery = "CREATE TABLE " + pTableName + "(" + pColNames + " TEXT, " + pColForeignKeys + " INTEGER, " + pColImage + " BLOB, " + pColText + " TEXT, " + pColSound + " BLOB);";
    private static SQLiteDatabase db;
    //public static final String colBookTexts = "Books";

    BookReaderDbHelper(Context context) {
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
