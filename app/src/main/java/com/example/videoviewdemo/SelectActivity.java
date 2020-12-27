package com.example.videoviewdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.videoviewdemo.model.GifFile;

import java.io.IOException;

public class SelectActivity extends AppCompatActivity {
    Button selectButton,selectButtonTemp;
    int STORAGE_REQUEST_CODE = 112;
    int STORAGE_CODE = 115;
    int w,h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        selectButton = findViewById(R.id.selectItem);
        selectButtonTemp = findViewById(R.id.selectItem2);
        w = getResources().getDisplayMetrics().widthPixels;
        int ht = getResources().getDisplayMetrics().heightPixels;
        Log.e("jaydipDraw",ht+"");
        //TODO : change the value 40 witch is fixed according to space abow appbar
        h = (int) (((float)ht - convertDpInPx(24f))*2/3 );
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
        selectButtonTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SelectActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_CODE);
                }
                else {
                    Intent intent = new Intent(SelectActivity.this,EditFrames.class);
                    startActivity(intent);
                }
            }


        });
//        ProgressDialog progress = new ProgressDialog(this);
//        progress.setTitle("Loading");
//        progress.setMessage("Wait while loading...");
//        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//        progress.show();

    }
    float convertDpInPx(float f){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,f,getResources().getDisplayMetrics());
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
        if(requestCode == STORAGE_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,EditFrames.class);
                startActivity(intent);
            }
        }
    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 205 && resultCode == RESULT_OK){
            try {
                GifFile.reset();
                GifFile gifFile = GifFile.getInstance();
                ClipData clipData = data.getClipData();
                for(int i =0 ;i< clipData.getItemCount();i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try {
                        Bitmap tempBit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        Log.e("jaydipTest","widh "+tempBit.getWidth()+"height"+tempBit.getHeight());
                        Bitmap temp = resize(tempBit,w,h);
                        if(gifFile.maHeight < temp.getHeight()){
                            gifFile.maHeight = temp.getHeight();
                        }
                        if(gifFile.maxWidth < temp.getWidth()){
                            gifFile.maxWidth = temp.getWidth();
                        }
                        gifFile.frames.add(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("jaydipMax",gifFile.maxWidth+"   // "+gifFile.maHeight);
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