package com.example.videoviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class BitmapCreate extends AppCompatActivity {
    Button b1,b2,b3;
    ImageView imageView;
    Bitmap bit1,bit2;
    boolean isSecond = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_create);
        b1 = findViewById(R.id.ImageButton);
        b2 = findViewById(R.id.ImageButton2);
        b3 = findViewById(R.id.ImageButton3);
        imageView = findViewById(R.id.imageView);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,205);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,205);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 205){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());

                Log.e("jaydip","widh = "+bitmap.getWidth()+" height ="+bitmap.getHeight());
//                Log.e("jaydip : size",bitmaps.size()+"");
                if(isSecond){
                    bit2 = bitmap;
                    Bitmap temp = Bitmap.createBitmap(bit1.getWidth(),bit1.getHeight(),bit1.getConfig());
                    Canvas canvas = new Canvas(temp);
                    canvas.drawBitmap(bit1,new Matrix(),null);
                    canvas.drawBitmap(bit2,-50f,-100f,null);
                    imageView.setImageBitmap(temp);

                }
                else {
                    bit1 = bitmap;
                    isSecond = true;
                    imageView.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            gifFile.frames = bitmaps;
        }
    }
}