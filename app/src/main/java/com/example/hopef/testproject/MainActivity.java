package com.example.hopef.testproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.io.IOException;

import static com.example.hopef.testproject.Serializer.serialize;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor bookCursor = db.rawQuery("SELECT rowid _id, * FROM BookList", null);
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new BookCursorAdapter(this, bookCursor));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(MainActivity.this, BookActivity.class);
                //What kind of view is view
                RelativeLayout rlItem = (RelativeLayout) view;
                TextView tvItem = (TextView) rlItem.findViewById(R.id.book_title);
                i.putExtra("Item Title", tvItem.getText());
                startActivity(i);
            }
        });
    }

    public void createNew(View view) {
        Intent i = new Intent(MainActivity.this, NewBookActivity.class);
        startActivity(i);
    }
}
