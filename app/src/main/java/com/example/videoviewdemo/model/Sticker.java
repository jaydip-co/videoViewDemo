package com.example.videoviewdemo.model;

import android.graphics.Bitmap;

public class Sticker {
   public Bitmap image;
   public int viewId;
    public int start, end;
    public int x,y;
    public float angle;
    public int height,width;

    public Sticker(Bitmap image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }
    public Sticker(){}
}

