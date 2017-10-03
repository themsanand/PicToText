package com.example.hopef.testproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                        Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor bookCursor = db.rawQuery("SELECT rowid _id, * FROM BookList", null);
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        //gridview.setAdapter(new SimpleCursorAdapter(this, book_preview, bookCursor, new String[] { "BookName" },
        //new int[] { android.R.id.text1 }, 0));
        gridview.setAdapter(new BookCursorAdapter(this, bookCursor));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(MainActivity.this, BookActivity.class);
                //What kind of view is view
                RelativeLayout rlItem = (RelativeLayout) view;
                TextView tvItem = (TextView) rlItem.findViewById(R.id.book_title);
                i.putExtra("Item Id", id);
                Log.e("Logging", Long.toString(id));
                startActivity(i);
            }
        });
    }

    public void createNew(View view) {
        //Intent i = new Intent(MainActivity.this, NewBookActivity.class);
        //startActivity(i);
        EditText enterTitle = (EditText) findViewById(R.id.enterTitle);
        enterTitle.setHint(enterTitle.getText().toString());
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("BookName", enterTitle.getText().toString());
        db.insert("BookList", null, insertValues);
        refresh();
    }

    private void refresh() {
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        Cursor bookCursor = db.rawQuery("SELECT rowid _id, * FROM BookList", null);
        BookCursorAdapter adapter = (BookCursorAdapter) gridview.getAdapter();
        //SimpleCursorAdapter adapter = (SimpleCursorAdapter) gridview.getAdapter();
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
            adapter.swapCursor(bookCursor);
        } else {
            adapter.changeCursor(bookCursor);
        }
        adapter.notifyDataSetChanged();
    }
}