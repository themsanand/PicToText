package com.example.hopef.testproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by hopef on 6/29/2017.
 */

class BookCursorAdapter extends CursorAdapter {

    BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bpvvt = (TextView) view.findViewById(R.id.book_title);
        //ImageView bpvvi = (ImageView) view.findViewById(R.id.book_image);
        bpvvt.setText(cursor.getString(cursor.getColumnIndexOrThrow("BookName")));
        //Bitmap imgbtmp = (Bitmap) deserialize(cursor.getBlob(cursor.getColumnIndexOrThrow("BookData")));
        //bpvvi.setImageBitmap(Bitmap.createScaledBitmap(imgbtmp, 90, 120, false));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.book_preview, parent, false);
    }
}
