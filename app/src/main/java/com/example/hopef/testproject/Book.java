package com.example.hopef.testproject;

import android.graphics.Bitmap;

/**
 * Created by hopef on 6/13/2017.
 */

public class Book {

    String title = null;
    Bitmap img = null;

    Book() {}

    Book(String stringTitle) {title = stringTitle;}

    public void setTitle(String titleSet) { title = titleSet; }

    public String getTitle() {
        return title;
    }

    public void setImage(Bitmap bm) { img = bm; }

    public Bitmap getImage() { return img; }

}
