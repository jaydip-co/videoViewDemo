package com.example.videoviewdemo.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.FileUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GifFile {
    private static GifFile Instance;

    public AnimatedGifEncoder mEncoder;
   public List<Bitmap> frames = new ArrayList<>();
   public float fps = 24;
    public  List<Sticker> stickers = new ArrayList<>();
    public int currentFrame = 1;
    public int currentStriker = -1;

    public static GifFile getInstance(){
        if(Instance == null){
            Instance = new GifFile();
        }
        return Instance;
    }
    private GifFile(){

        mEncoder = new AnimatedGifEncoder();
    }
    public Bitmap getCurrentFrame(){
        return  frames.get(currentFrame - 1);
    }
    public Sticker getCurrentStiker(){
        return stickers.get(currentStriker);
    }

    public void setXandY(int X,int Y,int vId){
        Sticker single = new Sticker();
        for (Sticker s : stickers){
            if(s.viewId == vId){
                single = s;
                break;
            }
        }
        int in = stickers.indexOf(single);
        single.x = X;
        single.y = Y;
        Log.e("jaydipindex",in+"");
        stickers.set(in,single);
    }

    public  void deleteStricker(int vId){
        for (Sticker s : stickers){
            if(s.viewId == vId){
               stickers.remove(s);
                break;
            }
        }
    }
    public void draw(Context context){
        List<Bitmap> finalFrames = new ArrayList<>();
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int ht = context.getResources().getDisplayMetrics().heightPixels;
Log.e("jaydipDraw",ht+"");
        //TODO : change the value 40 witch is fixed according to space abow appbar
        float h = ((float)ht)*2/3 - 40;
        Log.e("jaydipDraw ","heighrt  "+h+" width  "+w);

       int current = 0;
       while(current < frames.size()){
           Bitmap temp = Bitmap.createBitmap(w,(int)h,Bitmap.Config.RGB_565);
           Canvas canvas = new Canvas(temp);
           canvas.drawColor(Color.WHITE);
           Bitmap toDraw = resize(frames.get(current),w,(int)h);
           int BW = toDraw.getWidth();
           int BH = toDraw.getHeight();
           float left=0,top=0;
           if(BW > BH){
               top = (h - BH)/2;
           }
           else {
               left = (w - BW)/2;
           }
           canvas.drawBitmap(toDraw,left,top,null);
           for(Sticker s : stickers){
               if(s.start <= current+1 && s.end >= current+1){
                   canvas.drawBitmap(s.image,s.x,s.y,null);
               }
           }
           finalFrames.add(temp);
           current++;
       }
       frames = finalFrames;
       saveGif("jaydip",context);
    }
    public void saveGif(String FileName,Context context){
        String FinalFileName = FileName +".gif";
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mEncoder.start(bos);
            mEncoder.setFrameRate(fps);
            for(Bitmap b : frames){
                mEncoder.addFrame(b);
            }
            mEncoder.finish();
            File file = new File(context.getExternalFilesDir("jay"),FinalFileName);
            Log.e("jaydipPath",file.getAbsolutePath());

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(bos.toByteArray());
            stream.close();
            Log.e("jaydip : status","saved");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    Bitmap resize(Bitmap b,int width,int height){
        int h = b.getHeight();
        int w = b.getWidth();
        float d = (float) w/(float) h;
        Log.e("bitmap sa",w+"  /  "+h+"   / "+d);
        Log.e("bitmap max,",width+"  / "+height);
        if(w > width){
            Log.e("jaydip res","first 1");
            w = width;
            h = (int) (w / d);
        }
        else if (h > height){
            Log.e("jaydip res","first 2");
            h = height;
            w = (int) (h*d);
        }else if(w<width && h<height){
            Log.e("jaydip res","first 3");
            if(w > h){
                Log.e("jaydip res","first 4");
                w = width;
                h = (int) (w/d);
                Log.e("bitmap height",h+"");

            }
            else {
                Log.e("jaydip res","first 5");
                h = height;
                w = (int) (h * d);
            }
        }
        Log.e("bitmap final",w+"  /"+h);
        return  Bitmap.createScaledBitmap(b,w,h,false);
    }
}
