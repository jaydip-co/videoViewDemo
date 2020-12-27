package com.example.videoviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GIFView extends View {
    private Movie movie;
    private long moviestart;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GIFView(Context context) throws IOException {
        super(context);
        InputStream inputStream = new FileInputStream(new File(context.getCacheDir(),"temp.gif"));
        movie=Movie.decodeStream(inputStream);
    }
    public GIFView(Context context, AttributeSet attrs) throws IOException{
        super(context, attrs);
        InputStream inputStream = new FileInputStream(new File(context.getCacheDir(),"temp.gif"));
        movie=Movie.decodeStream(inputStream);
    }
    public GIFView(Context context, AttributeSet attrs, int defStyle) throws IOException {
        super(context, attrs, defStyle);
        InputStream inputStream = new FileInputStream(new File(context.getCacheDir(),"temp.gif"));
        movie=Movie.decodeStream(inputStream);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long now=android.os.SystemClock.uptimeMillis();
        Paint p = new Paint();
        p.setAntiAlias(true);
        if (moviestart == 0)
            moviestart = now;
        int relTime= 0;
        if(movie.duration() > 0) {
            Log.e("jaydip","onConfirmed");
            relTime = (int) ((now - moviestart) % movie.duration());
        }
        movie.setTime(relTime);
        movie.draw(canvas,0,0);
        this.invalidate();
    }
}