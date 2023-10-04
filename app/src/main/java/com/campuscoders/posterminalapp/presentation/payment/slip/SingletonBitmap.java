package com.campuscoders.posterminalapp.presentation.payment.slip;

import android.graphics.Bitmap;

public class SingletonBitmap {
    private static SingletonBitmap instance;

    private Bitmap bitmap = null;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private SingletonBitmap() {}

    public static SingletonBitmap getInstance() {
        if (instance == null) {
            instance = new SingletonBitmap();
        }
        return instance;
    }
}
