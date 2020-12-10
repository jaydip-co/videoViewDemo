package com.example.videoviewdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;

import java.io.IOException;
enum Mode{
    ZOOM,
    MOVE
}
public class MovableImage extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    FrameLayout frameLayout;
    int height,width;
    ImageView imageView;
    Bitmap mbitmap;
    Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movable_image);
        floatingActionButton = findViewById(R.id.addButton);
        frameLayout = findViewById(R.id.Container_fram);
        slider = findViewById(R.id.slider);
//        slider.setONVa
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
//                Log.e("jaydip slider",value+"");
//                Matrix matrix = new Matrix();
//
//                int[] loca = new int[2];
//                imageView.getLocationOnScreen(loca);
//                Rect rect = imageView.getClipBounds();
//                Log.e("harshit", rect+" ");
////                int wid = rect.right-rect.left;
////                int hei = rect.bottom - rect.top;
////                Log.e("harshit", wid+" - "+hei);
////                Log.e("jaydip",loca[0]+"  /  "+loca[1]);
//                Log.e("jaydipbhai",imageView.getWidth()+" /  "+imageView.getHeight());
//                matrix.preRotate(value, imageView.getDrawable().getBounds().width()/2, imageView.getDrawable().getBounds().height()/2);
//                Bitmap tempBit = Bitmap.createScaledBitmap(mbitmap,BitmapWidth,BitmapHeight,false);
//                Log.e("jaydipwidth",tempBit.getWidth()+"  /  "+tempBit.getHeight());
//                Bitmap Rotate = Bitmap.createBitmap(tempBit,0,0,tempBit.getWidth(),tempBit.getHeight(),matrix,false);
//                imageView.setImageBitmap(Rotate);

//                Canvas canvas = new Canvas(tempBit);
//                canvas.save();
//                canvas.restore();

                imageView.setRotation(value);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,205);

            }
        });
        Log.e("jaydip w", Resources.getSystem().getDisplayMetrics().widthPixels+"");
        Log.e("jaydip h", Resources.getSystem().getDisplayMetrics().heightPixels+"");
        width = Resources.getSystem().getDisplayMetrics().widthPixels -10;
        height = Resources.getSystem().getDisplayMetrics().heightPixels - 50;

    }
    private int xDelta, yDelta;
    private Mode mode = Mode.MOVE;
    private int BitmapHeight,BitmapWidth;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        private float initialX1,initialX2,initialY1,initialY2;
        boolean isZooming = true;
        float initialDef = 0;
        @Override public boolean onTouch(View view, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();
            int[] loc = new int[2];
            view.getLocationOnScreen(loc);
            Log.e("jaydip Actions",loc[0]+"  /"+loc[1]);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN : {
                    Log.e("jaydip Multi","touch");

                    mode = Mode.ZOOM;
                    initialX1 =  event.getX(0);
                    initialX2 =event.getX(1);
                    initialY1 = event.getY(0);
                    initialY2 = event.getY(1);
                    initialDef = spacing(event);
                    isZooming = true;
                    break;
                }
                case MotionEvent.ACTION_POINTER_UP :{
                    Log.e("jaydip Multi","touch off");
                    mode = Mode.MOVE;
                }
                case MotionEvent.ACTION_UP: {
                    Toast.makeText(getApplicationContext(), "Объект перемещён", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if(mode == Mode.MOVE) {
                        if (x - xDelta + view.getWidth() <= frameLayout.getWidth()
                                && y - yDelta + view.getHeight() <= frameLayout.getHeight()
                                && x - xDelta >= 0
                                && y - yDelta >= 0) {
                            FrameLayout.LayoutParams layoutParams =
                                    (FrameLayout.LayoutParams) view.getLayoutParams();
//                            Log.e("jaydip move",layoutParams.width+"");
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
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
                    else if(mode == Mode.ZOOM){
                        float tempDef = spacing(event);
                        if(tempDef < initialDef){
                            isZooming = false;
                        }
                        else {
                            isZooming = true;
                        }
                        initialDef = tempDef;
                        Log.e("jaydip","multi touch Zoom");
                        float tempx1 =  event.getX(0);
                        float tempx2 =  event.getX(1);
                        float tempy1 =  event.getX(0);
                        float tempy2 =  event.getX(1);
                        float xdef1 = Math.abs(tempx1 - initialX1);
                        float xdef2 = Math.abs(tempx2 - initialX2);
                        float ydef1 = Math.abs(tempy1 - initialY1);
                        float ydef2 = Math.abs(tempy2 - initialY2);
                        if(isZooming){
                            BitmapWidth = (int) (BitmapWidth + (xdef1+xdef2)/10);
                            BitmapHeight = (int)(BitmapHeight + (ydef1+ydef2)/50);

                        }
                        else {
                            BitmapWidth = (int)(BitmapWidth -(xdef1+xdef2)/10);
                            BitmapHeight = (int)(BitmapHeight - (ydef1+ydef2)/50);
                        }
                        Log.e("width",BitmapWidth+"");
                        Bitmap temp = Bitmap.createScaledBitmap(mbitmap,BitmapWidth,BitmapHeight,false);
                        imageView.setImageBitmap(temp);
//                        int widthDef = tempx1 -
//                        Log.e("jaydip zoom",tempx1+" / "+tempx2+"  / "+tempy1+"  / "+tempy2);
//                        spacing(event);
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();


                    }
                }

            }
            frameLayout.invalidate();
            return true;
        }
        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            float def = (float) Math.sqrt(x*x + y*y);
//            Log.e("scale","x : "+x+"  y  :"+y+ "ddeff : "+def);
            return def;
        }
//public boolean onTouch(View v, MotionEvent event) {
//    // TODO Auto-generated method stub
//
//    ImageView view = (ImageView) v;
//    dumpEvent(event);
//
//    // Handle touch events here...
//    switch (event.getAction() & MotionEvent.ACTION_MASK) {
//        case MotionEvent.ACTION_DOWN:
//            savedMatrix.set(matrix);
//            start.set(event.getX(), event.getY());
//            Log.d(TAG, "mode=DRAG");
//            mode = DRAG;
//            break;
//        case MotionEvent.ACTION_POINTER_DOWN:
//            oldDist = spacing(event);
//            Log.d(TAG, "oldDist=" + oldDist);
//            if (oldDist > 10f) {
//                savedMatrix.set(matrix);
//                midPoint(mid, event);
//                mode = ZOOM;
//                Log.d(TAG, "mode=ZOOM");
//            }
//            break;
//        case MotionEvent.ACTION_UP:
//        case MotionEvent.ACTION_POINTER_UP:
//            mode = NONE;
//            Log.d(TAG, "mode=NONE");
//            break;
//        case MotionEvent.ACTION_MOVE:
//            if (mode == DRAG) {
//                // ...
//                matrix.set(savedMatrix);
//                matrix.postTranslate(event.getX() - start.x, event.getY()
//                        - start.y);
//            } else if (mode == ZOOM) {
//                float newDist = spacing(event);
//                Log.d(TAG, "newDist=" + newDist);
//                if (newDist > 10f) {
//                    matrix.set(savedMatrix);
//                    float scale = newDist / oldDist;
//                    matrix.postScale(scale, scale, mid.x, mid.y);
//                }
//            }
//            break;
//    }
//
//    view.setImageMatrix(matrix);
//    return true;
//}
//
//        private void dumpEvent(MotionEvent event) {
//            String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
//                    "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
//            StringBuilder sb = new StringBuilder();
//            int action = event.getAction();
//            int actionCode = action & MotionEvent.ACTION_MASK;
//            sb.append("event ACTION_").append(names[actionCode]);
//            if (actionCode == MotionEvent.ACTION_POINTER_DOWN
//                    || actionCode == MotionEvent.ACTION_POINTER_UP) {
//                sb.append("(pid ").append(
//                        action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
//                sb.append(")");
//            }
//            sb.append("[");
//            for (int i = 0; i < event.getPointerCount(); i++) {
//                sb.append("#").append(i);
//                sb.append("(pid ").append(event.getPointerId(i));
//                sb.append(")=").append((int) event.getX(i));
//                sb.append(",").append((int) event.getY(i));
//                if (i + 1 < event.getPointerCount())
//                    sb.append(";");
//            }
//            sb.append("]");
//            Log.d("jaydip", sb.toString());
//        }
//
//        /** Determine the space between the first two fingers */
//        private float spacing(MotionEvent event) {
//            float x = event.getX(0) - event.getX(1);
//            float y = event.getY(0) - event.getY(1);
//            return FloatMath.sqrt(x * x + y * y);
//        }
//
//        /** Calculate the mid point of the first two fingers */
//        private void midPoint(PointF point, MotionEvent event) {
//            float x = event.getX(0) + event.getX(1);
//            float y = event.getY(0) + event.getY(1);
//            point.set(x / 2, y / 2);
//        }
    };
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 205){
            try {
                mbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                int h = mbitmap.getHeight();
                int w =mbitmap.getWidth();
                BitmapHeight = h;
                BitmapWidth = w;

                Log.e("jaydip","w :"+w+" h :"+h);
                if(h > height){
                    h = height;
                }
                if(w > width){
                    w = width;
                }
                Log.e("jaydip  2","w :"+w+" h :"+h);
                 imageView= new ImageView(getApplicationContext());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Bitmap bitmap1 = Bitmap.createScaledBitmap(mbitmap,w,h,false);
                Log.e("jaydip  2","w :"+bitmap1.getWidth()+" h :"+bitmap1.getHeight());
                params.leftMargin = 0;
                params.rightMargin = 0;
//                ConstraintLayout constraintLayout = new ConstraintLayout(getApplicationContext());
//                ImageView rotate = new ImageView(getApplicationContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap1);
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                imageView.setOnTouchListener(touchListener);

                frameLayout.addView(imageView);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}