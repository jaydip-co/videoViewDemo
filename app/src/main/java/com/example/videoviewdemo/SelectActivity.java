package com.example.videoviewdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.videoviewdemo.model.GifFile;

import java.io.IOException;

public class SelectActivity extends AppCompatActivity {
    Button selectButton;
    int STORAGE_REQUEST_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        selectButton = findViewById(R.id.selectItem);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("images/*");
////                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimetype);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
////                intent.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE,false);
//                startActivityForResult(Intent.createChooser(intent,"Select frames"),205);
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SelectActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_REQUEST_CODE);
                }
                else {
                    String[] mimetype = {"image/jpeg","image/jpg","image/png","image/bmp"};
                    Intent intent = new Intent();
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,mimetype);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//                intent.putExtra(EXTRA)
//                intent.putExtra(intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE,false);
                    startActivityForResult(Intent.createChooser(intent,"Select Image"),205);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
                String[] mimetype = {"image/jpeg","image/jpg","image/png","image/bmp"};
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimetype);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//                intent.putExtra(EXTRA)
//                intent.putExtra(intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE,false);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),205);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 205 && resultCode == RESULT_OK){
            try {
                GifFile gifFile = GifFile.getInstance();
                ClipData clipData = data.getClipData();
                for(int i =0 ;i< clipData.getItemCount();i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try {
                        Bitmap tempBit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        Log.e("jaydipTest","widh "+tempBit.getWidth()+"height"+tempBit.getHeight());

                        gifFile.frames.add(tempBit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(this,EditFrames.class);
                startActivity(intent);
//                Log.e("jaydipOnResult", clipData.getItemCount() + "");
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Choose atleast two image",Toast.LENGTH_SHORT).show();
            }
        }
    }
}