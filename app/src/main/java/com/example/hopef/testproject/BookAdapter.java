package com.example.hopef.testproject;

/**
 * Created by hopef on 6/9/2017.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by hopef on 6/8/2017.
 */

class BookAdapter extends BaseAdapter {
    private Context mContext;

    BookAdapter(Context c) {
        mContext = c;
    }

    //Get number of books
    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BookPreviewView bView;
        if (convertView == null) {
            bView = new BookPreviewView(mContext);
            bView.setPadding(8, 8, 8, 8);
        } else {
            bView = (BookPreviewView) convertView;
        }

        bView.setBookTitle(mThumbIds[position].getTitle());
        //bView.setText(mThumbIds[position]);
        return bView;
    }

    private Book[] mThumbIds = {
            new Book("Aaaaaaaa"), new Book("Bbbbbbbb"),
            new Book("Cccccccc"), new Book("Dddddddd")
    };
}
