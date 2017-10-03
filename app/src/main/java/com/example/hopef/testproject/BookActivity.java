package com.example.hopef.testproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.hopef.testproject.Serializer.serialize;

public class BookActivity extends AppCompatActivity {

    final int RESULT_LOAD_IMAGE = 5645;
    long bookKey = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Intent intent = getIntent();
        bookKey = intent.getLongExtra("Item Id", 0);
        TextView name = (TextView) findViewById(R.id.title_text);
        name.setText(getName(bookKey));
        Button btn = (Button) findViewById(R.id.page_button);
        btn.setText(R.string.alternate_main_button);
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor pageCursor = db.rawQuery("SELECT rowid _id, * FROM PageList WHERE BookIds='" + bookKey + "'", null);
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new PageCursorAdapter(this, pageCursor));
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Log.e("Logging", "HELD");
                return true;
            }
        });
    }

    public void createNew(View view) {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK);
        pickPictureIntent.setType("image/*");
        startActivityForResult(pickPictureIntent, RESULT_LOAD_IMAGE);
        startActivity(pickPictureIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap ph = null;
        try {
            Uri uri = data.getData();
            ph = (Bitmap) BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException fnfEx) {
            Log.e("Logging!", "No such file");
        }
        try {
            callCloudVision(ph);
        } catch (IOException ioE) {
        }
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("BookIds", bookKey);
        insertValues.put("ImageData", serialize(ph));
        db.insert("PageList", null, insertValues);
        refresh();
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                StringBuilder strBuilder = null;
                try {
                    TextRecognizer.Builder trb = new TextRecognizer.Builder(BookActivity.this);
                    TextRecognizer textRec = trb.build();
                    Frame.Builder frBuild = new Frame.Builder();
                    frBuild.setBitmap(bitmap);
                    if (textRec.isOperational()) {
                        SparseArray<TextBlock> sATB = textRec.detect(frBuild.build());
                        strBuilder = new StringBuilder();
                        for (int i = 0; i < sATB.size(); i++) {
                            strBuilder.append(sATB.valueAt(i).getValue() + " ");
                        }
                        //for (int i = 0; i < sATB.size(); i++) {
                        //    Log.e("Logging", "ITERATING");
                        //    TextBlock item = sATB.valueAt(i);
                        //    Log.e("Logging!", item.getValue());
                        //}
                    }
                    textRec.release();
                } catch (Exception e) {
                }
                return strBuilder.toString();
            }
        }.execute();
    }

    private String getName(long bookID) {
        Cursor cursor = null;
        String bookName = "";
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT BookName FROM BookList WHERE rowid='" + bookID + "'", null);
        cursor.moveToFirst();
        bookName = cursor.getString(cursor.getColumnIndex("BookName"));
        cursor.close();
        return bookName;
    }

    private void refresh() {
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        Cursor bookCursor = db.rawQuery("SELECT rowid _id, * FROM BookList", null);
        PageCursorAdapter adapter = (PageCursorAdapter) gridview.getAdapter();
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
            adapter.swapCursor(bookCursor);
        } else {
            adapter.changeCursor(bookCursor);
        }
        adapter.notifyDataSetChanged();
    }

}
