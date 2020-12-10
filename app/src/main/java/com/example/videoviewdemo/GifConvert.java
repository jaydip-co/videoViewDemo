package com.example.videoviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.videoviewdemo.model.AnimatedGifEncoder;
import com.example.videoviewdemo.model.GifFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GifConvert extends AppCompatActivity {

    Button ImageButton,ImageButton2;
    ImageView imageView;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    AnimatedGifEncoder mEncoder;
    GifFile gifFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_convert);
        mEncoder = new AnimatedGifEncoder();
        ImageButton = findViewById(R.id.ImageButton);
        imageView = findViewById(R.id.imageView);
        ImageButton2 = findViewById(R.id.ImageButton2);
        gifFile = GifFile.getInstance();
        ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,205);
            }
        });
        ImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gifFile.saveGif("third",getApplicationContext());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 205){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                imageView.setImageBitmap(bitmap);
                bitmaps.add(bitmap);
                Log.e("jaydip : size",bitmaps.size()+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
            gifFile.frames = bitmaps;
        }
    }
}