package com.example.hopef.testproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static android.R.attr.data;
import static com.example.hopef.testproject.R.id.enterTitle;
import static com.example.hopef.testproject.Serializer.serialize;

/**
 * Created by hopef on 6/15/2017.
 */

public class NewBookActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_REQUEST = 1888;
    Book newBook = new Book();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
    }

    public void createNew(View v) {
        EditText enterTitle = (EditText) findViewById(R.id.enterTitle);
        newBook.setTitle(enterTitle.getText().toString());
        enterTitle.setHint(enterTitle.getText().toString());
        if ((newBook.getImage() != null) &&  (newBook.getTitle() != null)) {
            BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            ContentValues insertValues = new ContentValues();
            insertValues.put("BookName", newBook.getTitle());
            insertValues.put("BookData", serialize(newBook.getImage()));
            db.insert("BookList", null, insertValues);
            Intent i = new Intent(NewBookActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap ph = (Bitmap) data.getExtras().get("data");
        newBook.setImage(ph);
        ImageView imview = (ImageView) findViewById(R.id.cover_photo);
        imview.setImageBitmap(ph);
        if ((newBook.getImage() != null) &&  (newBook.getTitle() != null)) {
            BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            ContentValues insertValues = new ContentValues();
            insertValues.put("BookName", newBook.getTitle());
            insertValues.put("BookData", serialize(newBook.getImage()));
            db.insert("BookList", null, insertValues);
            Intent i = new Intent(NewBookActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

}
