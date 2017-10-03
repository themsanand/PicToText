package com.example.hopef.testproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hopef on 6/9/2017.
 */

public class BookPreviewView extends RelativeLayout {
    RelativeLayout layout = null;
    ImageView img = null;
    TextView title = null;//(TextView) layout.findViewById(R.id.book_title);
    Context mContext = null;

    public BookPreviewView(Context context) {
        super(context);
        mContext = context;
        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
        layout = (RelativeLayout) li.inflate(R.layout.book_preview, this, true);
        title = (TextView) layout.findViewById(R.id.book_title);
        title.setText("");
    }

    public BookPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BookPreview);
        String book_title = a.getString(R.styleable.BookPreview_book_title);
        book_title = book_title == null ? "" : book_title;
        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
        layout = (RelativeLayout) li.inflate(R.layout.book_preview, this, true);
        title = (TextView) layout.findViewById(R.id.book_title);
        title.setText(book_title);
        a.recycle();
    }

    public BookPreviewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void setBookTitle(String text) {
        title.setText(text);
    }

    public String getBookTitle() {
        return title.getText().toString();
    }
}
