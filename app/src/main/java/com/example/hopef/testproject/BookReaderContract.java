package com.example.hopef.testproject;

import android.provider.BaseColumns;

/**
 * Created by hopef on 6/16/2017.
 */

public class BookReaderContract {
    private BookReaderContract() {}

    public static class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_BOOK_TITLE = "title";
        public static final String COLUMN_NAME_BOOK_IMAGE = "image";
    }
}
