package com.example.videoviewdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultipleImage extends AppCompatActivity {
    ImageView currentImage;
    Slider sliderImage;
    FloatingActionButton addButton;
    List<Bitmap> bitmaps;
    RangeSlider slider;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_image);
        DisplayMetrics metrics = new DisplayMetrics();
//Log.e("jaydipWindow",getWindowManager().getCurrentWindowMetrics().getBounds().height()+"");
//       getDisplay().getSize();
        currentImage = findViewById(R.id.currentImage);
        sliderImage = findViewById(R.id.sliderImage);
        addButton = findViewById(R.id.addButton);
        constraintLayout = findViewById(R.id.cons);
        Log.e("jaydip",constraintLayout.getHeight()+"");
        slider = findViewById(R.id.rangeSlider);
        List<Float> values = new ArrayList<>();
        values.add(new Float(0));
        values.add(new Float(2));
//        Log.e("jaydipWidth",constraintLayout.getWidth()+"  /  "+constraintLayout.getHeight());
        slider.setValues(values);
        bitmaps = new ArrayList<>();
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
        sliderImage.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Log.e("jaydipSlider"," /   "+(int)value);
                int val = (int) value;
                if(val == bitmaps.size()){
                    val--;
                }
                currentImage.setImageBitmap(bitmaps.get(val));
            }
        });




        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                Log.e("jaydip",value+"  /  "+slider.getValues());
            }
        });
//        slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
//            @Override
//            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
//                Log.e("jaydipSlide   /",slider.getValues().toString()+"");
//            }
//
//            @Override
//            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
//
//            }
//        });

    }
    void setSlider(){
        sliderImage.setValue(0);
        sliderImage.setValueTo(bitmaps.size());
        sliderImage.setValueFrom(0);
        sliderImage.setStepSize(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 205 && resultCode == RESULT_OK){
            Log.e("jaydip","jay   /  "+data.getClipData().toString());
            ClipData clipData = data.getClipData();
            for(int i =0 ;i< clipData.getItemCount();i++){
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                try {
                    Bitmap tempBit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    bitmaps.add(tempBit);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            currentImage.setImageBitmap(bitmaps.get(0));
            setSlider();

        }
    }
}