package com.example.hopef.testproject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import static com.example.hopef.testproject.Serializer.deserialize;

/**
 * Created by hopef on 7/10/2017.
 */

public class PageCursorAdapter extends CursorAdapter {

    final Context mContext;

    public PageCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView bpvvi = (ImageView) view.findViewById(R.id.book_image);
        Bitmap imgbtmp = (Bitmap) deserialize(cursor.getBlob(cursor.getColumnIndexOrThrow("ImageData")));
        bpvvi.setImageBitmap(Bitmap.createScaledBitmap(imgbtmp, 180, 240, false));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.page_preview, parent, false);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        View textButton = v.findViewById(R.id.text_button);
        textButton.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              //TODO
                                          }
                                      }
        );
        View imageButton = v.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               //TODO
                                           }
                                       }
        );
        return v;
    }
}

