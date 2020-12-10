package com.example.videoviewdemo.model;

import android.graphics.Bitmap;

public class Sticker {
   public Bitmap image;
   public int viewId;
    public int start, end;
    public int x,y;
    public float angle;

    public Sticker(Bitmap image) {
        this.image = image;
    }
    public Sticker(){}
}
