package com.example.hopef.testproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.R.attr.data;
import static com.example.hopef.testproject.BookReaderDbHelper.db;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("Item Title");
        TextView name = (TextView) findViewById(R.id.title_text);
        name.setText(bookName);
        Button btn = (Button) findViewById(R.id.button1);
        btn.setText(R.string.alternate_main_button);
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor pageCursor = db.rawQuery("SELECT rowid _id, * FROM PageList WHERE BookName='" + bookName + "'", null);
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new PageCursorAdapter(this, pageCursor));
    }

    public void createNew(View view) {
        Intent i = new Intent(BookActivity.this, NewPageActivity.class);
        TextView name = (TextView) findViewById(R.id.title_text);
        String bNa = (String) name.getText();
        i.putExtra("Item Title", bNa);
        startActivity(i);
    }

}
