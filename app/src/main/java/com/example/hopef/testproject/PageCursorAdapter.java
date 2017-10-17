package com.example.hopef.testproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import me.grantland.widget.AutofitTextView;

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
        AutofitTextView bpvvt = (AutofitTextView) view.findViewById(R.id.book_text);
        //Log.e("Logging", cursor.getString(cursor.getColumnIndexOrThrow("TextData")));
        bpvvt.setText(cursor.getString(cursor.getColumnIndexOrThrow("TextData")));
        //bpvvt.setText("DUMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMTEXT");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.page_preview, parent, false);
    }

    private long getPrimaryKeyAt(int position) {

        Cursor cursor = (Cursor) getItem(position);

        return cursor.getLong(cursor.getColumnIndex("_id"));
    }

    private Bitmap getBitmapAt(int position) {

        Cursor cursor = (Cursor) getItem(position);

        return (Bitmap) deserialize(cursor.getBlob(cursor.getColumnIndex("ImageData")));
    }


    private long getBookIdAt(int position) {

        Cursor cursor = (Cursor) getItem(position);

        return cursor.getLong(cursor.getColumnIndex("BookIds"));
    }

    private String getTextDataAt(int position) {
        Cursor cursor = (Cursor) getItem(position);

        return cursor.getString(cursor.getColumnIndex("TextData"));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View v = super.getView(position, convertView, parent);

        final ImageView bookImage = (ImageView) v.findViewById(R.id.book_image);
        final AutofitTextView bookText = (AutofitTextView) v.findViewById(R.id.book_text);

        View textButton = v.findViewById(R.id.text_button);

        textButton.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              if (TextUtils.isEmpty(bookText.getText().toString())) {
                                                  //TODO
                                                  try {
                                                      callCloudVision(getBitmapAt(position), getPrimaryKeyAt(position), getBookIdAt(position));
                                                      Log.e("Logging", getTextDataAt(position));
                                                      bookText.setText(getTextDataAt(position));
                                                  } catch (IOException e) {
                                                      Log.e("Logging exception", "This shouldn't be possible: " + e.toString());
                                                  }
                                              }
                                              bookImage.setVisibility(View.GONE);
                                              bookText.setVisibility(View.VISIBLE);
                                          }
                                      }
        );
        View imageButton = v.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               bookImage.setVisibility(View.VISIBLE);
                                               bookText.setVisibility(View.GONE);
                                           }
                                       }
        );
        return v;
    }

    private void callCloudVision(final Bitmap bitmap, final long page_id, final long bookKey) throws IOException {
        new AsyncTask<Object, Void, String>() {

            String textData = "";

            @Override
            protected String doInBackground(Object... params) {
                StringBuilder strBuilder = null;
                try {
                    TextRecognizer.Builder trb = new TextRecognizer.Builder(mContext);
                    TextRecognizer textRec = trb.build();
                    Frame.Builder frBuild = new Frame.Builder();
                    frBuild.setBitmap(bitmap);
                    if (textRec.isOperational()) {
                        SparseArray<TextBlock> sATB = textRec.detect(frBuild.build());
                        strBuilder = new StringBuilder();
                        for (int i = 0; i < sATB.size(); i++) {
                            strBuilder.append(sATB.valueAt(i).getValue() + "\n");
                        }
                    }
                    textRec.release();
                } catch (Exception e) {
                }
                textData = strBuilder.toString();
                return textData;
            }

            @Override
            protected void onPostExecute(String s) {

                BookReaderDbHelper mDBHelper = new BookReaderDbHelper(mContext);
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                //Cursor dbCursor = db.query("PageList", null, null, null, null, null, null);
                ContentValues updateValue = new ContentValues();
                updateValue.put("TextData", textData);
                db.update("PageList", updateValue, "rowid=" + page_id, null);
            }
        }.execute();

    }


}

