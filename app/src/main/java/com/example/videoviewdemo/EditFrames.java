package com.example.videoviewdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videoviewdemo.Addapter.StrikerAddapter;
import com.example.videoviewdemo.model.BlurSticker;
import com.example.videoviewdemo.model.GifFile;
import com.example.videoviewdemo.model.RefressIndecator;
import com.example.videoviewdemo.model.Sticker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class EditFrames extends Activity implements  RefressIndecator{
FloatingActionButton addButton,saveButton;

ImageView currentImage,addBlur;
GifFile gifFile;
RecyclerView strikerRecycle;
LinearLayout decorationLayout,speedButton ,decorationButton,speedLayoute,saveB;
ImageView addStriker;
Slider speedSlider,frameSlider;
FrameLayout strikerFrame;
Bitmap secBitmap;
int xMargin,yMargin;
float heightRatio,widthRatio;
int secX,secY;
TextView fps;
    StrikerAddapter addapter;
    ProgressDialog progress;
    int strickerCounter= 1;
boolean isDecorating = false;
boolean isSpesific = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_frames);
        addButton = findViewById(R.id.addButton);
        gifFile = GifFile.getInstance();
        currentImage = findViewById(R.id.currentImage);
        strikerRecycle = findViewById(R.id.StrikerRecycle);
        decorationLayout = findViewById(R.id.decorationLayout);
        speedSlider = findViewById(R.id.speedSlider);
        speedLayoute = findViewById(R.id.speedLayoute);
        speedButton = findViewById(R.id.speedButton);
        decorationButton = findViewById(R.id.decorationButton);
        addStriker = findViewById(R.id.addStriker);
        strikerFrame = findViewById(R.id.strikerContainer);
        frameSlider = findViewById(R.id.frameSlider);
        saveButton = findViewById(R.id.saveButton);
        saveB  =  findViewById(R.id.save);
        fps = findViewById(R.id.fps);
        gifFile.statusHeight = (int) convertDpInPx(24f);
        Log.e("jaydip24dp",convertDpInPx(24f)+"'");
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        widthRatio = getResources().getDisplayMetrics().widthPixels/(float) 1080;
        addBlur = findViewById(R.id.tansparent);
//        heightRatio = getResources().getDisplayMetrics().heightPixels/(float) 2040;
        Log.e("jaydipError",gifFile.maHeight+"");

        Log.e("jaydipDimen",widthRatio+"  ///  "+heightRatio);
//        progress.show();
        saveB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             // disable dismiss by tapping outside of the dialog
//                progress.show();
                progress.show();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        gifFile.draw(getApplicationContext());
                        progress.dismiss();
//                        Intent intent = new Intent(EditFrames.this,ConfirmAvtivity.class);
//                        startActivity(intent);
                        Looper.loop();


//                save();
                    }
                });
                t.start();

            }
        });
        addapter = new StrikerAddapter(getApplicationContext(),this);
        strikerRecycle.setAdapter(addapter);
        strikerRecycle.setLayoutManager(new LinearLayoutManager(this));
       float f =  getResources().getDisplayMetrics().density;
       float h = getResources().getDisplayMetrics().heightPixels;
        float w = getResources().getDisplayMetrics().widthPixels;
       Log.e("jaydip den",f+"");
        Log.e("jaydip width",w+"");
        Log.e("jaydip height",h+"");
        float di = 200f;
        Log.e("jaydip","200 in px "+convertDpInPx(200));
//        int[] scal1 = new int[2];
//        int[] scal2 = new int[2];
//        linearLayout.getLocationOnScreen(scal2);
//        Log.e("jaydip currentImage",scal1[0]+"   //   "+currentImage.getClipBounds().centerY());
//        Log.e("jaydip linear",scal2[0]+"   //   "+scal2[1]);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),205);
            }
        });

        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDecorating = false;
                setlayoute();
            }
        });
        decorationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDecorating = true;
                setlayoute();
            }
        });
        setlayoute();
        addStriker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isSpesific){
                   Intent intent = new Intent();
                   intent.setType("image/*");
                   intent.setAction(Intent.ACTION_GET_CONTENT);
                   intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                   startActivityForResult(Intent.createChooser(intent,"Select Image"),206);
               }else {
                   Intent intent = new Intent();
                   intent.setType("image/*");
                   intent.setAction(Intent.ACTION_GET_CONTENT);
                   startActivityForResult(Intent.createChooser(intent,"Select Image"),206);
               }
            }
        });
        frameSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                gifFile.currentFrame = (int) value;
//                currentImage.setImageBitmap(gifFile.getCurrentFrame());
                refresh();
            }
        });
        Log.e("jaydip pic",currentImage.getHeight()+"");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                Log.e("jaydip hei",getResources().getDisplayMetrics().heightPixels+"");
//                int h  = getResources().getDisplayMetrics().heightPixels;
//                float w = h*2/3;
//                Log.e("jaydip wid",w+"");
//                Bitmap tem = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels,1133,Bitmap.Config.RGB_565);
//                Canvas canvas = new Canvas(tem);
//                canvas.drawColor(Color.WHITE);
//                Bitmap toDraw = gifFile.getCurrentFrame();
//                canvas.drawBitmap(gifFile.getCurrentFrame(),0,0,null);
//                canvas.drawBitmap(secBitmap,secX,secY,null);
//
//                currentImage.setImageBitmap(tem);

                gifFile.draw(getApplicationContext());
            }
        });
//        currentImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("height",v.getHeight()+"");
//                return true;
//            }
//        });
//        currentImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//                startActivityForResult(Intent.createChooser(intent,"Select Image"),205);
//            }
//        });
//        currentImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                    int [] a = new int[2];
//            v.getLocationOnScreen(a);
//                Log.e("jaydip",a[0]+"  // "+a[1]);
//                return true;
//            }
//        });
        speedSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                fps.setText(value+"  fps :  ");
                gifFile.fps = value;
            }
        });
        addBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBlurItem();
            }
        });
        LinearLayout l = findViewById(R.id.MainLayout);
        int a[] = new int[2];
        l.getLocationOnScreen(a);
//        getWindow().getDecorView().getLocationOnScreen(a);
        Log.e("jaydipStat", a[0]+"  //" +a[1]);
        int wt = getResources().getDisplayMetrics().widthPixels;
        int ht = getResources().getDisplayMetrics().heightPixels;

        //TODO : change the value 40 witch is fixed according to space abow appbar
        float hh = ((float)ht-  convertDpInPx(24f))*2/3;
        Log.e("jaydipTest","screent dime"+wt+"  //"+hh);
        setInitial();
        Log.e("jaydipContext",getApplicationContext()+"");

//        Looper.getMainLooper()

    }

    void setInitial(){
        int ht = getResources().getDisplayMetrics().heightPixels;

        //TODO : change the value 40 witch is fixed according to space abow appbar
        float hh = ((float)ht-  convertDpInPx(24f))*2/3;
        int w = getResources().getDisplayMetrics().widthPixels;
        int h = getResources().getDisplayMetrics().heightPixels;
        try {
        if(gifFile.frames.size() > 0){
            currentImage.setImageBitmap(gifFile.getCurrentFrame());
        }
        else {
            isSpesific = true;
            InputStream inputStream = getAssets().open("Picture1.png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Bitmap temp = resize(bitmap,w,h);
            if(temp.getHeight() > gifFile.maHeight){
                gifFile.maHeight = bitmap.getHeight();
            }
            if(temp.getWidth() > gifFile.maxWidth){
                gifFile.maxWidth = temp.getWidth();
            }
            gifFile.frames.add(temp);
            inputStream = getAssets().open("Picture3.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
            temp = resize(bitmap,w,h);
            if(temp.getHeight() > gifFile.maHeight){
                gifFile.maHeight = bitmap.getHeight();
            }
            if(temp.getWidth() > gifFile.maxWidth){
                gifFile.maxWidth = temp.getWidth();
            }
            gifFile.frames.add(temp);
            inputStream = getAssets().open("Picture2.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
            temp = resize(bitmap,w,h);
            if(temp.getHeight() > gifFile.maHeight){
                gifFile.maHeight = bitmap.getHeight();
            }
            if(temp.getWidth() > gifFile.maxWidth){
                gifFile.maxWidth = temp.getWidth();
            }

            gifFile.frames.add(temp);
            heightRatio = (float) gifFile.maHeight/(float)690;
            yMargin = (int) ((hh - gifFile.maHeight)/(float) 2);
            Log.e("jaydipEror",yMargin+"");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }


        frameSlider.setValue(gifFile.currentFrame);
        frameSlider.setValueFrom(1);
        frameSlider.setValueTo(gifFile.frames.size());
        frameSlider.setStepSize(1);
    }
    void save(){
        Bitmap currentFrame = gifFile.getCurrentFrame();
        int w = getResources().getDisplayMetrics().widthPixels;
        int ht = getResources().getDisplayMetrics().heightPixels;
        Log.e("jaydipDraw",ht+"");
        //TODO : change the value 40 witch is fixed according to space abow appbar
        float h = ((float)ht)*2/3 - convertDpInPx(24f);
        Bitmap temp = Bitmap.createBitmap(w,(int)h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(temp);
        canvas.drawColor(Color.WHITE);
        Bitmap toDraw = resize(currentFrame,w,(int)h);
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
        Sticker striker = gifFile.stickers.get(0);
        float angle = striker.angle;
        left = striker.x;
        top = striker.y;
        Bitmap image = striker.image;
        Matrix matrix = new Matrix();
        matrix.preRotate(angle,image.getWidth()/2,image.getHeight()/2);
        Bitmap te = Bitmap.createBitmap(image,0,0,image.getWidth(),image.getHeight(),matrix,false);

        float x = striker.x + (image.getWidth()/2);
        float y = striker.y +(image.getHeight()/2);
        canvas.save();
        canvas.rotate(angle,x,y);
        Log.e("jaydipCanvasCenter",w/2+"   "+h/2);
        Log.e("jaydipCanvasCenter","canvas"+canvas.getWidth()+"   "+canvas.getHeight());

        canvas.drawBitmap(image,left,top,null);
        canvas.restore();
//        canvas.restore();
        currentImage.setImageBitmap(temp);
    }
//    Bitmap resize(Bitmap b,int width,int height){
//        int h = b.getHeight();
//        int w = b.getWidth();
//        float d = (float) w/(float) h;
//        Log.e("bitmap sa",w+"  /  "+h+"   / "+d);
//        Log.e("bitmap max,",width+"  / "+height);
//        if(w > width){
//            Log.e("jaydip res","first 1");
//            w = width;
//            h = (int) (w / d);
//        }
//        else if (h > height){
//            Log.e("jaydip res","first 2");
//            h = height;
//            w = (int) (h*d);
//        }else if(w<width && h<height){
//            Log.e("jaydip res","first 3");
//            if(w > h){
//                Log.e("jaydip res","first 4");
//                w = width;
//                h = (int) (w/d);
//                Log.e("bitmap height",h+"");
//
//            }
//            else {
//                Log.e("jaydip res","first 5");
//                h = height;
//                w = (int) (h * d);
//            }
//        }
//        Log.e("bitmap final",w+"  /"+h);
//        return  Bitmap.createScaledBitmap(b,w,h,false);
//    }
Bitmap resize(Bitmap b,int width,int height){
    int h = b.getHeight();
    int w = b.getWidth();
    float d = (float) w/(float) h;
    Log.e("bitmap sa",w+"  /  "+h+"   / "+d);
    Log.e("bitmap max,",width+"  / "+height);
    float ratiox = width/(float) w;
    float ratioy = height/(float)h;
    if(ratiox < ratioy){
        w = (int) (w*ratiox);
        h = (int) (h*ratiox);
    }
    else {
        w = (int) (w*ratioy);
        h = (int) (h*ratioy);
    }

//        if(w > width){
//            Log.e("jaydip res","first 1");
//            w = width;
//            h = (int) (w / d);
//        }
//        else if (h > height){
//            Log.e("jaydip res","first 2");
//            h = height;
//            w = (int) (h*d);
//        }else if(w<width && h<height){
//            Log.e("jaydip res","first 3");
//            if(w > h){
//                Log.e("jaydip res","first 4");
//                w = width;
//                h = (int) (w/d);
//                Log.e("bitmap height",h+"");
//
//            }
//            else {
//                Log.e("jaydip res","first 5");
//                h = height;
//                w = (int) (h * d);
//            }
//        }
    Log.e("bitmap final",w+"  /"+h);
    return  Bitmap.createScaledBitmap(b,w,h,false);
}

    View.OnTouchListener moveListener = new View.OnTouchListener() {
        private int xDelta, yDelta;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();
            int w = getResources().getDisplayMetrics().widthPixels;
            Log.e("jaydip vie",v.getId()+"");
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN: {
                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();

                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    break;
                }
                case MotionEvent.ACTION_MOVE:{
//                    if (x - xDelta + v.getWidth() <= strikerFrame.getWidth()
//                            && y - yDelta + v.getHeight() <= strikerFrame.getHeight()
//                            && x - xDelta >= 0
//                            && y - yDelta >= 0) {
                        FrameLayout.LayoutParams layoutParams =
                                (FrameLayout.LayoutParams) v.getLayoutParams();
//                            Log.e("jaydip move",layoutParams.width+"");
                        layoutParams.leftMargin = x - xDelta;
                        Log.e("jaydip",(x-xDelta+v.getWidth())+"");
                        secX = x -xDelta + (int) convertDpInPx(25f);
                        secY = y - yDelta + (int) convertDpInPx(25f);;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = w - x - xDelta - v.getWidth();
                        layoutParams.bottomMargin = 0;
                        v.setLayoutParams(layoutParams);
//                        FrameLayout.LayoutParams layoutParams =
//                                (FrameLayout.LayoutParams) view.getLayoutParams();
//                        int oldW = layoutParams.width;
//                        int oldh = layoutParams.height;
//                        layoutParams.width = oldW + x - xDelta;
//                        layoutParams.topMargin = y - yDelta;
//                        layoutParams.rightMargin = 0;
//                        layoutParams.bottomMargin = 0;
//                        view.setLayoutParams(layoutParams);


//                    }

                    break;
                }
                case MotionEvent.ACTION_UP : {
//                    save();
                    FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) v.getLayoutParams();
                    int left = p.leftMargin +(int) convertDpInPx(25f);
                    int top = p.topMargin + (int) convertDpInPx(25f);
                    int finalY = top - yMargin;
                    Log.e("jaydipY",yMargin+"");

                    Log.e("jaydipFixed","left   "+left+"top  "+finalY);

                    gifFile.setXandY(left,finalY,v.getId());
                }
            }
            strikerFrame.invalidate();
            return true;
        }
    };
//
    private View.OnTouchListener getResiseListener() {

        return new View.OnTouchListener() {
            float xCenter =0,yCenter=0;
            float preX=0,preY=0;
            double refdis =0 ;
            View parent;
            int width,height;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:{

                        parent = (View) v.getParent();
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
                        int w = parent.getWidth();
                        int h = parent.getHeight();
                        xCenter = params.leftMargin + (w/2);
                        yCenter = params.topMargin + (h/2);
//                        refdis = Math.sqrt((w*w)+(h*h))/2;
                        preX = event.getRawX();
                        preY = event.getRawY();
                        View imageView = parent.findViewById(R.id.Image);
                        width = imageView.getWidth();
                        height = imageView.getHeight();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE:{
                        float newX = event.getRawX();
                        float newY = event.getRawY();
                        if(newX == xCenter || yCenter == newY){
                            return true;
                        }
                        float dx = Math.abs(newX - xCenter);
                        float dy = Math.abs(newY - yCenter);
//                        double ratiox = Math.sqrt(dx*dx + dy*dy)/refdis;
                        double ratiox = Math.abs((newX - xCenter)/(preX - xCenter));
                        double ratioy = Math.abs((newY - yCenter)/(preY - yCenter));

                            View parent = (View) v.getParent();
                            ImageView imageView = parent.findViewById(R.id.Image);
                            Log.e("jaydipHW",imageView.getHeight()+" // "+imageView.getWidth());
                        float newW=0, newH =0;
//                        if(newX > preX){
//                                newW = imageView.getWidth() + dx;
//                        }
//                        else {
//                            newW = imageView.getWidth() - dx;
//                        }
//                        if(newY > preY){
//                            newH = imageView.getHeight() + dy;
//
//                        }
//                        else {
//                            newH = imageView.getHeight() - dy;
//
//                        }
//                        preX = newX;
//                        preY = newY;
                        newW = (float) (width*ratiox);
                        newH = (float) (height*ratioy);
                        if(newH < 1){newH =1;}
                        if(newW < 1){newW = 1;}

                        Bitmap bitmap = gifFile.getBitmap(parent.getId());
                        Bitmap temp = Bitmap.createScaledBitmap(bitmap,(int)newW,(int)newH,false);
//                        gifFile.setBitmap(temp,parent.getId());
                        imageView.setImageBitmap(temp);

//                         ConstraintLayout.LayoutParams la = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
//                        la.width = (int) newW;
//                        la.height = (int) newH;
//                        Log.e("jaydipTest",newW +"  // "+newH);
                        Log.e("jaydipFixed","width   "+newW+"Height  "+newH);
                        gifFile.setStickerDimension(parent.getId(),(int)newW,(int) newH);
//
//                        imageView.setLayoutParams(la);

                    }
                }
                return true;
            }
        };
    }
//    void save(){
//        Log.e("jaydip hei",getResources().getDisplayMetrics().heightPixels+"");
//        int w  = getResources().getDisplayMetrics().heightPixels;
//        //TODO : change the value 40 witch is fixed according to space abow appbar
//        float h = w*2/3 - 40;
//        Log.e("jaydip wid",w+"");
//        Bitmap tem = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels,(int)h,Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(tem);
//        canvas.drawColor(Color.WHITE);
//        Bitmap toDraw = (resize(gifFile.getCurrentFrame(),(int)h,getResources().getDisplayMetrics().widthPixels));
//        int toW = toDraw.getWidth();
//        int toH = toDraw.getHeight();
//        float top =0,left = 0;
//        if(toW > toH){
//            top = (h - toH)/2;
//        }
//        else {
//            left = (getResources().getDisplayMetrics().widthPixels - toW)/2;
//        }
//        canvas.drawBitmap(toDraw,left,top,null);
//        for(Sticker s:gifFile.stickers){
//          Bitmap oneDraw = s.image;
//          canvas.drawBitmap(oneDraw,s.x,s.y,null);
//        }
//
////        canvas.drawBitmap(secBitmap,secX,secY,null);
//
//        currentImage.setImageBitmap(tem);
//    }
    void setlayoute(){
        decorationLayout.setVisibility(View.VISIBLE);
//        if(isDecorating){
//            decorationLayout.setVisibility(View.VISIBLE);
//        }
//        else {
//            decorationLayout.setVisibility(View.GONE);
////            speedLayoute.setVisibility(View.VISIBLE);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void addBlurItem(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View v  = LayoutInflater.from(getApplicationContext()).inflate(R.layout.blur_v,null);
//        Canvas canvas = new Canvas(temp);

//            canvasResult.drawRect(rectF, blurPaintInner);
            v.setId(View.generateViewId());
        int vID = v.getId();
        Sticker sticker = new BlurSticker();
        sticker.start = 1;
        sticker.viewId = v.getId();
        Log.e("jaydip view id",v.getId()+"");
        sticker.end = gifFile.frames.size();
        sticker.x = 0;
        sticker.y = (int) convertDpInPx(24f);
        sticker.height = (int) (100 * heightRatio);
        sticker.width = (int) (100 * widthRatio);
        sticker.x = (int) (385  * widthRatio);
        sticker.y = (int) ((75 ) * heightRatio) ;
        int w = getResources().getDisplayMetrics().widthPixels;
        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
        params.topMargin = sticker.y - (int)convertDpInPx(25f) +yMargin;
        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
            v.setLayoutParams(params);
            BlurView imageView1 = v.findViewById(R.id.Image);
            View decorView = getWindow().getDecorView();
            ViewGroup rootView = decorView.findViewById(android.R.id.content);
            Drawable drawable = decorView.getBackground();
            imageView1.setupWith(rootView)
                    .setFrameClearDrawable(drawable)
                    .setBlurAlgorithm(new RenderScriptBlur(this))
                    .setBlurRadius(20f)
                    .setBlurAutoUpdate(true)
                    .setHasFixedTransformationMatrix(true);
//            imageView1.setImageBitmap(bitmapResult);
            ImageView cacel = v.findViewById(R.id.cancelButton);
            cacel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View ve) {
                    Log.e("jaydipDelete","reached");
                    gifFile.deleteStricker(vID);
                    strikerFrame.removeView(v);
                    addapter.setStickers(gifFile.stickers);
                }
            });
            ImageView rotate = v.findViewById(R.id.rotate);
            rotate.setOnTouchListener(getRotateListener());
            ImageView resize = v.findViewById(R.id.resize);
            resize.setOnTouchListener(getResiseListener());
            gifFile.stickers.add(sticker);
            addapter.setStickers(gifFile.stickers);

            v.setOnTouchListener(getBlureMove());
            strikerFrame.addView(v);
//            refresh();





    }
    void calculateBlur(){

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int ht = getResources().getDisplayMetrics().heightPixels;
        Log.e("jaydipDraw",ht+"");
        //TODO : change the value 40 witch is fixed according to space abow appbar
        int h = (int) (((float)ht - convertDpInPx(24f))*2/3 );
        int fractor = (h - gifFile.maHeight)/2;
        Log.e("jaydipFractor",fractor+"");
//        if(requestCode == 205 && resultCode == RESULT_OK){
//
//            Log.e("jaydip","jay   /  "+data.getClipData().toString());
//            ClipData clipData = data.getClipData();
//
//            for(int i =0 ;i< clipData.getItemCount();i++){
//                ClipData.Item item = clipData.getItemAt(i);
//                Uri uri = item.getUri();
//                try {
//                    Bitmap tempBit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                    Log.e("jaydipTest","widh "+tempBit.getWidth()+"height"+tempBit.getHeight());
//
//                    gifFile.frames.add(tempBit);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentImage.setImageBitmap(gifFile.getCurrentFrame());
//            frameSlider.setValueTo(gifFile.frames.size());
//            frameSlider.setValueFrom(1);
//            frameSlider.setValue(1);
//            frameSlider.setStepSize(1);
//
////            gifFile.saveGif("jay",getApplicationContext());
//
//        }
        if(requestCode == 206 && resultCode == RESULT_OK && !isSpesific){
            try {
                Bitmap temp = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                Log.e("jaydip",temp.getWidth()+"   /"+temp.getHeight());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


//                imageView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int[] scal = new int[2];
//                        v.getLocationOnScreen(scal);
//                        Log.e("jaydip","x = "+scal[0]+"  y = "+scal[1]);
//                        return true;
//                    }
//                });
                View v  = LayoutInflater.from(getApplicationContext()).inflate(R.layout.one_striker,null);
                v.setId(View.generateViewId());
                int vID = v.getId();
                Sticker sticker = new Sticker(temp);
                sticker.start = 1;
                sticker.viewId = v.getId();
                Log.e("jaydip view id",v.getId()+"");
                sticker.end = gifFile.frames.size();
                sticker.x = 0;
                sticker.y = (int) convertDpInPx(24f);
                int w = getResources().getDisplayMetrics().widthPixels;

                switch (strickerCounter){
//                    case 1:{
//                        sticker.height = (int) (641.0129*heightRatio);
//                        sticker.width = (int) (1094.0 * widthRatio);
//                        sticker.x = (int) (-18  * widthRatio);
//                        sticker.y = (int) ((206 - 369) * heightRatio);
////                        sticker.x = (int) (0  * widthRatio);
////                        sticker.y = (int) ((0) * heightRatio);
//                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
//                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
////                        params.leftMargin = sticker.x ;
////                        params.topMargin = sticker.y ;
//                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
//                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height ,false);
//                        sticker.start = 1;
//                        sticker.end = 1;
//                        Log.e("jaydipTestOn","width = ");
//                        strickerCounter++;
////                        strickerCounter++;
//                        break;
//                    }
                    case 1:{
                        Log.e("jaydipErrorRATion",heightRatio + "   / "+widthRatio);
                        sticker.height = (int)(306.30548 * heightRatio);
                        sticker.width = (int)(408.64758 * widthRatio);
                        sticker.x = (int) (36 * widthRatio);
                        sticker.y = (int) ((258) * heightRatio);
//                        sticker.x = (int) (0  * widthRatio);
//                        sticker.y = (int) ((0) * heightRatio);
                        params.leftMargin = sticker.x - (int)convertDpInPx(25f) ;
                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
//                        params.leftMargin = sticker.x ;
//                        params.topMargin = sticker.y ;
                        Log.e("jaydipError",sticker.width + "   / "+sticker.height);
                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);

                        strickerCounter++;
                        break;

                    }
                    case 2:{
                        sticker.height = (int) (482.63068 * heightRatio);
                        sticker.width = (int) (402.0 * widthRatio);
                        sticker.x = (int) (385  * widthRatio);
                        sticker.y = (int) ((75 ) * heightRatio) ;
                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
                        params.topMargin = sticker.y - (int)convertDpInPx(25f) +yMargin;
                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
                        sticker.start = 1;
                        sticker.end = 1;
                        strickerCounter++;
                        break;
                    }
//                    case 4:{
//                        sticker.height = (int) (581.70544 * heightRatio);
//                        sticker.width = (int) (1139.67 * widthRatio);
//                        sticker.x = (int) (24  * widthRatio);
//                        sticker.y = (int) ((354 -369 )  * heightRatio) ;
//                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
//                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
//                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
//                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
//                        sticker.start = 2;
//                        sticker.end = 2;
//                        strickerCounter++;
//                        break;
//                    }
                    case 3:{
                        sticker.height = (int)( 424.7262  * heightRatio);
                        sticker.width = (int) (128.70093 *  widthRatio);
                        sticker.x = (int) (703  * widthRatio);
                        sticker.y = (int) ( 143 * heightRatio) ;
                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
                        sticker.start = 2;
                        sticker.end = 2;
                        strickerCounter++;
                        break;
                    }
//                    case 6:{
//                        sticker.height = (int) (635.23975  * heightRatio) ;
//                        sticker.width = (int) (1181.0183  * widthRatio);
//                        sticker.x = (int) (-92  * widthRatio);
//                        sticker.y = (int) ((302 - 369)  * heightRatio) ;
//                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
//                        params.topMargin = sticker.y - (int)convertDpInPx(25f)  + yMargin;
//                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
//                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
//                        sticker.start = 3;
//                        sticker.end = 3;
//                        strickerCounter++;
//                        break;
//                    }
                    case 4:{
                        sticker.height = (int) (578.2649  * heightRatio);
                        sticker.width = (int) (325.4461  * widthRatio);
                        sticker.x = (int) (129  * widthRatio);
                        sticker.y = (int) ( -11 * heightRatio) ;
                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
                        sticker.start = 3;
                        sticker.end = 3;
                        strickerCounter++;
                        break;
                    }
                }
                sticker.image = temp;

//                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//                imageView.setLayoutParams(layoutParams);
//                constraintLayout.addView(imageView);
//                ConstraintSet set = new ConstraintSet();

//                imageView.setId(View.generateViewId());
//                set.clone(constraintLayout);
//                set.connect(imageView.getId(), ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.TOP);
//                set.connect(imageView.getId(), ConstraintSet.START,constraintLayout.getId(),ConstraintSet.START);
                v.setLayoutParams(params);
                ImageView imageView1 = v.findViewById(R.id.Image);
                ImageView edit = v.findViewById(R.id.edit);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Sticker s = gifFile.getSticker(vID);
                        Bitmap temp = Bitmap.createScaledBitmap(s.image,s.width*-1,s.height,false);
                        gifFile.setBitmap(temp,vID);
                        imageView1.setImageBitmap(temp);
                    }
                });
                imageView1.setImageBitmap(temp);
                Log.e("jaydipHW",temp.getWidth()+"   //  "+temp.getHeight()+"//   bitmap  ");
                ImageView cacel = v.findViewById(R.id.cancelButton);
                cacel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View ve) {
                        Log.e("jaydipDelete","reached");
                        gifFile.deleteStricker(vID);
                        strikerFrame.removeView(v);
                        addapter.setStickers(gifFile.stickers);
                    }
                });
                ImageView rotate = v.findViewById(R.id.rotate);
                rotate.setOnTouchListener(getRotateListener());
                ImageView resize = v.findViewById(R.id.resize);
                resize.setOnTouchListener(getResiseListener());

//               imageView1.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       Log.e("view id",v.getId()+"");
//                   }
//               });

                gifFile.stickers.add(sticker);
                addapter.setStickers(gifFile.stickers);

                v.setOnTouchListener(moveListener);
                strikerFrame.addView(v);
                refresh();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(requestCode == 206 && resultCode == RESULT_OK && isSpesific){
            try {
                ClipData data1 = data.getClipData();
                if(data1.getItemCount() > 0){
                   for(int i =0 ;i <data1.getItemCount();i++){
                       ClipData.Item item = data1.getItemAt(i);
                       Bitmap temp = BitmapFactory.decodeStream(getContentResolver().openInputStream(item.getUri()));
                       FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                       View v  = LayoutInflater.from(getApplicationContext()).inflate(R.layout.one_striker,null);
                       v.setId(View.generateViewId());
                       int vID = v.getId();
                       Sticker sticker = new Sticker(temp);
                       sticker.start = 1;
                       sticker.viewId = v.getId();
                       Log.e("jaydip view id",v.getId()+"");
                       sticker.end = gifFile.frames.size();
                       sticker.x = 0;
                       sticker.y = (int) convertDpInPx(24f);
                       int w = getResources().getDisplayMetrics().widthPixels;

                       switch (i){
                           case 0 : {
//                               Log.e("jaydipErrorRATion",heightRatio + "   / "+widthRatio);
                               sticker.height = (int)(306.30548 * heightRatio);
                               sticker.width = (int)(408.64758 * widthRatio);
                               sticker.x = (int) (36 * widthRatio);
                               sticker.y = (int) ((258) * heightRatio);
//                        sticker.x = (int) (0  * widthRatio);
//                        sticker.y = (int) ((0) * heightRatio);
                               params.leftMargin = sticker.x - (int)convertDpInPx(25f) ;
                               params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
//                        params.leftMargin = sticker.x ;
//                        params.topMargin = sticker.y ;
                               Log.e("jaydipError",sticker.width + "   / "+sticker.height);
                               params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                               temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
                               break;

                           }
                           case 1:{
                               sticker.height = (int) (482.63068 * heightRatio);
                               sticker.width = (int) (402.0 * widthRatio);
                               sticker.x = (int) (385  * widthRatio);
                               sticker.y = (int) ((75 ) * heightRatio) ;
                               params.leftMargin = sticker.x - (int)convertDpInPx(25f);
                               params.topMargin = sticker.y - (int)convertDpInPx(25f) +yMargin;
                               params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                               temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
                               sticker.start = 1;
                               sticker.end = 1;
                               break;

                           }
                           case 2:{
                               sticker.height = (int)( 424.7262  * heightRatio);
                               sticker.width = (int) (128.70093 *  widthRatio);
                               sticker.x = (int) (703  * widthRatio);
                               sticker.y = (int) ( 143 * heightRatio) ;
                               params.leftMargin = sticker.x - (int)convertDpInPx(25f);
                               params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
                               params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                               temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
                               sticker.start = 2;
                               sticker.end = 2;
                               break;
                           }
                           case 3:{
                               sticker.height = (int) (578.2649  * heightRatio);
                               sticker.width = (int) (325.4461  * widthRatio);
                               sticker.x = (int) (129  * widthRatio);
                               sticker.y = (int) ( -11 * heightRatio) ;
                               params.leftMargin = sticker.x - (int)convertDpInPx(25f);
                               params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
                               params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
                               temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
                               sticker.start = 3;
                               sticker.end = 3;
                               break;
                           }
                       }
                       sticker.image = temp;
                       v.setLayoutParams(params);
                       ImageView imageView1 = v.findViewById(R.id.Image);
                       ImageView edit = v.findViewById(R.id.edit);
                       edit.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Sticker s = gifFile.getSticker(vID);
                               Bitmap temp = Bitmap.createScaledBitmap(s.image,s.width*-1,s.height,false);
                               gifFile.setBitmap(temp,vID);
                               imageView1.setImageBitmap(temp);
                           }
                       });
                       imageView1.setImageBitmap(temp);
                       Log.e("jaydipHW",temp.getWidth()+"   //  "+temp.getHeight()+"//   bitmap  ");
                       ImageView cacel = v.findViewById(R.id.cancelButton);
                       cacel.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View ve) {
                               Log.e("jaydipDelete","reached");
                               gifFile.deleteStricker(vID);
                               strikerFrame.removeView(v);
                               addapter.setStickers(gifFile.stickers);
                           }
                       });
                       ImageView rotate = v.findViewById(R.id.rotate);
                       rotate.setOnTouchListener(getRotateListener());
                       ImageView resize = v.findViewById(R.id.resize);
                       resize.setOnTouchListener(getResiseListener());
                       gifFile.stickers.add(sticker);
                       addapter.setStickers(gifFile.stickers);

                       v.setOnTouchListener(moveListener);
                       strikerFrame.addView(v);
                       isSpesific = false;


                   }
                    refresh();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Select 4 Images",Toast.LENGTH_SHORT).show();

                }

//                Bitmap temp = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
//                Log.e("jaydip",temp.getWidth()+"   /"+temp.getHeight());
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


//                imageView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int[] scal = new int[2];
//                        v.getLocationOnScreen(scal);
//                        Log.e("jaydip","x = "+scal[0]+"  y = "+scal[1]);
//                        return true;
//                    }
//                });
//                View v  = LayoutInflater.from(getApplicationContext()).inflate(R.layout.one_striker,null);
//                v.setId(View.generateViewId());
//                int vID = v.getId();
//                Sticker sticker = new Sticker(temp);
//                sticker.start = 1;
//                sticker.viewId = v.getId();
//                Log.e("jaydip view id",v.getId()+"");
//                sticker.end = gifFile.frames.size();
//                sticker.x = 0;
//                sticker.y = (int) convertDpInPx(24f);
//                int w = getResources().getDisplayMetrics().widthPixels;

//                switch (strickerCounter){
////                    case 1:{
////                        sticker.height = (int) (641.0129*heightRatio);
////                        sticker.width = (int) (1094.0 * widthRatio);
////                        sticker.x = (int) (-18  * widthRatio);
////                        sticker.y = (int) ((206 - 369) * heightRatio);
//////                        sticker.x = (int) (0  * widthRatio);
//////                        sticker.y = (int) ((0) * heightRatio);
////                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
////                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
//////                        params.leftMargin = sticker.x ;
//////                        params.topMargin = sticker.y ;
////                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
////                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height ,false);
////                        sticker.start = 1;
////                        sticker.end = 1;
////                        Log.e("jaydipTestOn","width = ");
////                        strickerCounter++;
//////                        strickerCounter++;
////                        break;
////                    }
//                    case 1:{
////                        Log.e("jaydipErrorRATion",heightRatio + "   / "+widthRatio);
////                        sticker.height = (int)(306.30548 * heightRatio);
////                        sticker.width = (int)(408.64758 * widthRatio);
////                        sticker.x = (int) (36 * widthRatio);
////                        sticker.y = (int) ((258) * heightRatio);
//////                        sticker.x = (int) (0  * widthRatio);
//////                        sticker.y = (int) ((0) * heightRatio);
////                        params.leftMargin = sticker.x - (int)convertDpInPx(25f) ;
////                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
//////                        params.leftMargin = sticker.x ;
//////                        params.topMargin = sticker.y ;
////                        Log.e("jaydipError",sticker.width + "   / "+sticker.height);
////                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
////                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
//
//                        strickerCounter++;
//                        break;
//
//                    }
//                    case 2:{
////                        sticker.height = (int) (482.63068 * heightRatio);
////                        sticker.width = (int) (402.0 * widthRatio);
////                        sticker.x = (int) (385  * widthRatio);
////                        sticker.y = (int) ((75 ) * heightRatio) ;
////                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
////                        params.topMargin = sticker.y - (int)convertDpInPx(25f) +yMargin;
////                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
////                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
////                        sticker.start = 1;
////                        sticker.end = 1;
//                        strickerCounter++;
//                        break;
//                    }
////                    case 4:{
////                        sticker.height = (int) (581.70544 * heightRatio);
////                        sticker.width = (int) (1139.67 * widthRatio);
////                        sticker.x = (int) (24  * widthRatio);
////                        sticker.y = (int) ((354 -369 )  * heightRatio) ;
////                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
////                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
////                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
////                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
////                        sticker.start = 2;
////                        sticker.end = 2;
////                        strickerCounter++;
////                        break;
////                    }
//                    case 3:{
////                        sticker.height = (int)( 424.7262  * heightRatio);
////                        sticker.width = (int) (128.70093 *  widthRatio);
////                        sticker.x = (int) (703  * widthRatio);
////                        sticker.y = (int) ( 143 * heightRatio) ;
////                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
////                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
////                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
////                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
////                        sticker.start = 2;
////                        sticker.end = 2;
//                        strickerCounter++;
//                        break;
//                    }
////                    case 6:{
////                        sticker.height = (int) (635.23975  * heightRatio) ;
////                        sticker.width = (int) (1181.0183  * widthRatio);
////                        sticker.x = (int) (-92  * widthRatio);
////                        sticker.y = (int) ((302 - 369)  * heightRatio) ;
////                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
////                        params.topMargin = sticker.y - (int)convertDpInPx(25f)  + yMargin;
////                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
////                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
////                        sticker.start = 3;
////                        sticker.end = 3;
////                        strickerCounter++;
////                        break;
////                    }
//                    case 4:{
////                        sticker.height = (int) (578.2649  * heightRatio);
////                        sticker.width = (int) (325.4461  * widthRatio);
////                        sticker.x = (int) (129  * widthRatio);
////                        sticker.y = (int) ( -11 * heightRatio) ;
////                        params.leftMargin = sticker.x - (int)convertDpInPx(25f);
////                        params.topMargin = sticker.y - (int)convertDpInPx(25f) + yMargin;
////                        params.rightMargin = w - params.leftMargin - sticker.width - (int)convertDpInPx(50f);
////                        temp = Bitmap.createScaledBitmap(temp,sticker.width,sticker.height,false);
////                        sticker.start = 3;
////                        sticker.end = 3;
//                        strickerCounter++;
//                        break;
//                    }
//                }
//                sticker.image = temp;

//                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                v.setLayoutParams(params);
//                imageView.setLayoutParams(layoutParams);
//                constraintLayout.addView(imageView);
//                ConstraintSet set = new ConstraintSet();

//                imageView.setId(View.generateViewId());
//                set.clone(constraintLayout);
//                set.connect(imageView.getId(), ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.TOP);
//                set.connect(imageView.getId(), ConstraintSet.START,constraintLayout.getId(),ConstraintSet.START);
//                ImageView imageView1 = v.findViewById(R.id.Image);
//                ImageView edit = v.findViewById(R.id.edit);
//                edit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Sticker s = gifFile.getSticker(vID);
//                        Bitmap temp = Bitmap.createScaledBitmap(s.image,s.width*-1,s.height,false);
//                        gifFile.setBitmap(temp,vID);
//                        imageView1.setImageBitmap(temp);
//                    }
//                });
//                imageView1.setImageBitmap(temp);
//                Log.e("jaydipHW",temp.getWidth()+"   //  "+temp.getHeight()+"//   bitmap  ");
//                ImageView cacel = v.findViewById(R.id.cancelButton);
//                cacel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View ve) {
//                        Log.e("jaydipDelete","reached");
//                        gifFile.deleteStricker(vID);
//                        strikerFrame.removeView(v);
//                        addapter.setStickers(gifFile.stickers);
//                    }
//                });
//                ImageView rotate = v.findViewById(R.id.rotate);
//                rotate.setOnTouchListener(getRotateListener());
//                ImageView resize = v.findViewById(R.id.resize);
//                resize.setOnTouchListener(getResiseListener());

//               imageView1.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       Log.e("view id",v.getId()+"");
//                   }
//               });

//                gifFile.stickers.add(sticker);
//                addapter.setStickers(gifFile.stickers);
//
//                v.setOnTouchListener(moveListener);
//                strikerFrame.addView(v);
//                refresh();


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Sleect 4 Images",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private View.OnTouchListener getBlureMove(){
        return new View.OnTouchListener() {
            private int xDelta, yDelta;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();
                    int w = getResources().getDisplayMetrics().widthPixels;
                    Log.e("jaydip vie", v.getId() + "");
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            Sticker sticker = gifFile.getSticker(v.getId());


                            if(checkOverlap(gifFile.stickers.indexOf(sticker),sticker.width,sticker.height,x,y)){
                                Log.e("overLap","overlap thayu");
                                return true;
                            }
//                    if (x - xDelta + v.getWidth() <= strikerFrame.getWidth()
//                            && y - yDelta + v.getHeight() <= strikerFrame.getHeight()
//                            && x - xDelta >= 0
//                            && y - yDelta >= 0) {
                            try {
                            FrameLayout.LayoutParams layoutParams =
                                    (FrameLayout.LayoutParams) v.getLayoutParams();
//                            Log.e("jaydip move",layoutParams.width+"");
                            layoutParams.leftMargin = x - xDelta;
                            Log.e("jaydip", (x - xDelta + v.getWidth()) + "");
                            secX = x - xDelta + (int) convertDpInPx(25f);
                            secY = y - yDelta + (int) convertDpInPx(25f);
                            ;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = w - x - xDelta - v.getWidth();
                            layoutParams.bottomMargin = 0;
                            v.setLayoutParams(layoutParams);
//                        FrameLayout.LayoutParams layoutParams =
//                                (FrameLayout.LayoutParams) view.getLayoutParams();
//                        int oldW = layoutParams.width;
//                        int oldh = layoutParams.height;
//                        layoutParams.width = oldW + x - xDelta;
//                        layoutParams.topMargin = y - yDelta;
//                        layoutParams.rightMargin = 0;
//                        layoutParams.bottomMargin = 0;
//                        view.setLayoutParams(layoutParams);



                                BlurView view = v.findViewById(R.id.Image);
                                View decorView = getWindow().getDecorView();
                                ViewGroup rootView = decorView.findViewById(android.R.id.content);
                                Drawable drawable = decorView.getBackground();
                                view.setupWith(rootView)
                                        .setFrameClearDrawable(drawable)
                                        .setBlurAlgorithm(new RenderScriptBlur(getApplicationContext()))
                                        .setBlurRadius(20f)
                                        .setBlurAutoUpdate(true);
                            }
                            catch (Exception e){
                                Log.e("jaydipException","avu....");
                                return  false;

                            }


//                    }

                            break;
                        }
                        case MotionEvent.ACTION_UP: {
//                    save();
                            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) v.getLayoutParams();
                            int left = p.leftMargin + (int) convertDpInPx(25f);
                            int top = p.topMargin + (int) convertDpInPx(25f);
                            int finalY = top - yMargin;
                            Log.e("jaydipY", yMargin + "");

                            Log.e("jaydipFixed", "left   " + left + "top  " + finalY);

                            gifFile.setXandY(left, finalY, v.getId());
                        }
                    }
                    strikerFrame.invalidate();
                    return true;
                }
        };
    }

    boolean checkOverlap(int index,float W,float H,int x,int y){

        Point p1 = new Point(x,y);
        Point p2 = new Point(x+convertDpInPx(100f),y+convertDpInPx(100f));
        for(int i =0;i<gifFile.stickers.size();i++){
            if(i==index) // || !(gifFile.stickers.get(i) instanceof BlurSticker))
                continue;
            Sticker s = gifFile.stickers.get(i);
            Point px1 = new Point(s.x,s.y);
            Point px2 = new Point(s.x+convertDpInPx(100f),s.y+convertDpInPx(100f));
            if(doOverlap(p1,p2,px1,px2)){
                return true;
            }



        }

        return false;
    }


    private View.OnTouchListener getRotateListener() {
        return new View.OnTouchListener() {
            float xCenter=0,yCenter=0;
            double refAngle = 0;
            View parent;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:{
                        parent = (View) v.getParent();
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
                        xCenter = params.leftMargin + (parent.getWidth()/2);
                        yCenter = params.topMargin + (parent.getHeight()/2);
                      refAngle =  Math.atan(parent.getHeight()/(double)parent.getWidth());
                      Log.e("harshitAngle width",refAngle+"  /// "+parent.getWidth()+ "   //  "+parent.getHeight());
                      break;
                    }
                    case MotionEvent.ACTION_MOVE : {
                        float newx = event.getRawX();
                        float newy = event.getRawY();
                        if(newx == xCenter && newy == yCenter){
                            return true;
                        }
                        double dCR = Math.sqrt((newx-xCenter)*(newx-xCenter) + (newy-yCenter)*(newy-yCenter));
                        double ang = Math.acos((newx -xCenter)/dCR);
                        Log.e("harshitAngle ang",ang+"");
                        if(newy < yCenter){
                            ang =   Math.toDegrees(   refAngle  - ang);
                        }
                        else {
                            ang =   Math.toDegrees(ang + refAngle);
                        }
//                        if(newy < yCenter){
//                            ang =   -Math.toDegrees(   refAngle  +ang);
//                        }
//                        else {
//                            ang =   Math.toDegrees(ang - refAngle);
//                        }
//                        Log.e("harshitAngle final",ang+"\n");
                        Log.e("jaydipFixed","anngle :"+ang);
                        gifFile.setRotation((float)ang,parent.getId());
                        parent.setRotation((float) ang);
                        break;
                    }
                }
                return true;
            }
        };
    }


    void refresh(){
      if(gifFile.frames.size() > 0){
          int currentFrame = gifFile.currentFrame;
          Bitmap currentBitmap = gifFile.getCurrentFrame();
          currentImage.setImageBitmap(currentBitmap);

          for(Sticker s : gifFile.stickers){
              View v = findViewById(s.viewId);
              if(s.start <= currentFrame && s.end >= currentFrame){

                  v.setVisibility(View.VISIBLE);
              }
              else {
                  v.setVisibility(View.GONE);
              }
          }
      }
    }
    float convertDpInPx(float f){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,f,getResources().getDisplayMetrics());
    }

    @Override
    public void onRefresh() {
        Log.e("jaydip","onRefress");
        refresh();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GifFile.reset();
    }




     class Point {
         float x, y;

        Point(float x,float y){
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return  "("+x+","+y+")";
        }

    }

    // Returns true if two rectangles (l1, r1) and (l2, r2) overlap
    static  boolean doOverlap(Point l1, Point r1, Point l2, Point r2) {
        // If one rectangle is on left side of other
        Log.e("Points", "{"+l1.toString()+r1.toString()+"}"+"{"+l2.toString()+r2.toString()+"}");
        if (l1.x > r2.x || l2.x > r1.x) {
            Log.e("OverLap", " x returns flase");
            return false;
        }

        // If one rectangle is above other
        if (l1.y > r2.y || l2.y > r1.y) {
            Log.e("OverLap", " y returns flase");
            return false;
        }
        Log.e("OverLap", "  returns true");
        return true;
    }
}