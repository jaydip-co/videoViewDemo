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
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.example.videoviewdemo.model.GifFile;
import com.example.videoviewdemo.model.RefressIndecator;
import com.example.videoviewdemo.model.Sticker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditFrames extends Activity implements  RefressIndecator{
FloatingActionButton addButton,saveButton;

ImageView currentImage;
GifFile gifFile;
RecyclerView strikerRecycle;
LinearLayout decorationLayout,speedButton ,decorationButton,speedLayoute,saveB;
ImageView addStriker;
Slider speedSlider,frameSlider;
FrameLayout strikerFrame;
Bitmap secBitmap;
int secX,secY;
TextView fps;
    StrikerAddapter addapter;
boolean isDecorating = false;

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
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifFile.draw(getApplicationContext());
//                save();
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),206);
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
        speedSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                int val = (int) (value * 59) + 1;
               //TODO : add time
                fps.setText(val+"  fps :  ");
                gifFile.fps = val;
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
        float hh = ((float)ht)*2/3 - convertDpInPx(24f);
        Log.e("jaydipTest","screent dime"+wt+"  //"+hh);
        setInitial();
    }
    void setInitial(){
        currentImage.setImageBitmap(gifFile.getCurrentFrame());
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

    View.OnTouchListener moveListener = new View.OnTouchListener() {
        private int xDelta, yDelta;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();
            Log.e("jaydip vie",v.getId()+"");
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN: {
                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();

                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    break;
                }
                case MotionEvent.ACTION_MOVE:{
                    if (x - xDelta + v.getWidth() <= strikerFrame.getWidth()
                            && y - yDelta + v.getHeight() <= strikerFrame.getHeight()
                            && x - xDelta >= 0
                            && y - yDelta >= 0) {
                        FrameLayout.LayoutParams layoutParams =
                                (FrameLayout.LayoutParams) v.getLayoutParams();
//                            Log.e("jaydip move",layoutParams.width+"");
                        layoutParams.leftMargin = x - xDelta;
                        Log.e("jaydip",(x-xDelta+v.getWidth())+"");
                        secX = x -xDelta + (int) convertDpInPx(25f);
                        secY = y - yDelta + (int) convertDpInPx(25f);;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
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


                    }

                    break;
                }
                case MotionEvent.ACTION_UP : {
//                    save();
                    FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) v.getLayoutParams();
                    int left = p.leftMargin +(int) convertDpInPx(25f);
                    int top = p.topMargin + (int) convertDpInPx(25f);
                    View parent = (View) v.getParent();
                    gifFile.setXandY(left,top,v.getId());
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
                        refdis = Math.sqrt((w*w)+(h*h))/2;
                        View imageView = parent.findViewById(R.id.Image);
                        width = imageView.getWidth();
                        height = imageView.getHeight();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE:{
                        float newX = event.getRawX();
                        float newY = event.getRawY();
                        float dx = Math.abs(newX - xCenter);
                        float dy = Math.abs(newY - yCenter);
                        double ratio = Math.sqrt(dx*dx + dy*dy)/refdis;

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
                        newW = (float) (width*ratio);
                        newH = (float) (height*ratio);

//                        Bitmap bitmap = gifFile.getBitmap(parent.getId());
//                        Bitmap temp = Bitmap.createScaledBitmap(bitmap,(int)newH,(int)newW,false);
//                        gifFile.setBitmap(temp,parent.getId());
//                        imageView.setImageBitmap(temp);

                         ConstraintLayout.LayoutParams la = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                        la.width = (int) newW;
                        la.height = (int) newH;
                        Log.e("jaydipTest",newW +"  // "+newH);
                        gifFile.setStickerDimension(parent.getId(),(int)newH,(int) newH);

                        imageView.setLayoutParams(la);

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
        if(isDecorating){
            decorationLayout.setVisibility(View.VISIBLE);
            speedLayoute.setVisibility(View.GONE);
        }
        else {
            decorationLayout.setVisibility(View.GONE);
            speedLayoute.setVisibility(View.VISIBLE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if(requestCode == 206 && resultCode == RESULT_OK){
            try {
                Bitmap temp = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                secBitmap = temp;
                Log.e("jaydip",temp.getWidth()+"   /"+temp.getHeight());
                ImageView imageView = new ImageView(getApplicationContext());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                Log.e("jaydip dynamic image",imageView.getWidth()+"  /  "+imageView.getHeight());
                imageView.setImageBitmap(temp);
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
//                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                v.setLayoutParams(params);
//                imageView.setLayoutParams(layoutParams);
//                constraintLayout.addView(imageView);
//                ConstraintSet set = new ConstraintSet();

//                imageView.setId(View.generateViewId());
//                set.clone(constraintLayout);
//                set.connect(imageView.getId(), ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.TOP);
//                set.connect(imageView.getId(), ConstraintSet.START,constraintLayout.getId(),ConstraintSet.START);
                ImageView imageView1 = v.findViewById(R.id.Image);
                ImageView edit = v.findViewById(R.id.edit);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    edit.setImageDrawable(getDrawable(R.drawable.rorate));
                }
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
                Sticker sticker = new Sticker(temp);
                sticker.start = 1;
                sticker.viewId = v.getId();
                Log.e("jaydip view id",v.getId()+"");
                sticker.end = gifFile.frames.size();
                sticker.x = 0;
                sticker.x = 40;
                gifFile.stickers.add(sticker);
                addapter.setStickers(gifFile.stickers);

                v.setOnTouchListener(moveListener);
                strikerFrame.addView(v);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
                        Log.e("harshitAngle final",ang+"\n");
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
    float convertDpInPx(float f){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,f,getResources().getDisplayMetrics());
    }

    @Override
    public void onRefresh() {
        Log.e("jaydip","onRefress");
        refresh();
    }
}