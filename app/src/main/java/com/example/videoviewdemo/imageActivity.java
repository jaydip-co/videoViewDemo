package com.example.videoviewdemo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import com.example.videoviewdemo.model.GifFile;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class imageActivity extends AppCompatActivity implements PickiTCallbacks {

    Button ImageButton,ImageButton2;
    ImageView imageView;
    Bitmap img1,img2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageButton = findViewById(R.id.ImageButton);
        imageView = findViewById(R.id.imageView);
        ImageButton2 = findViewById(R.id.ImageButton2);
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
//                overPrint();
                Bitmap newBItmap = drawTextToBitmap(getApplicationContext(),img1,"jay");
                imageView.setImageBitmap(newBItmap);
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,206);
            }
        });
    }
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index)+"jay";
        } catch (Exception e) {
            Log.e("jaydip", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    String getPath(Uri uri){
        String res = "jay";
        try {
            FileInputStream inputStream = (FileInputStream) getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(),"first.gif");
            FileOutputStream stream = new FileOutputStream(file);
            int len;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf)) > 0){
                stream.write(buf,0,len);
            }
            inputStream.close();
            stream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return  res;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("jaydip",data.getData().toString());
        if(resultCode == RESULT_OK && requestCode == 205){
//            Uri uri = data.getData();
//            PickiT pickiT = new PickiT(getApplicationContext(),this,getParent());
//            pickiT.getPath(data.getData(),Build.VERSION.SDK_INT);
//            ContentResolver resolver = getContentResolver();
//            MimeTypeMap map = MimeTypeMap.getSingleton();
//            String type = map.getExtensionFromMimeType(resolver.getType(data.getData()));
//            String path = getRealPathFromURI(getApplicationContext(),uri);
//            Log.e("jaydip",type);
            getPath(data.getData());



//            GifFile file = new GifFile(uri,getApplicationContext());
        }
        if(requestCode == 205 && resultCode == RESULT_OK){
            try {
                img1 = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                imageView.setImageBitmap(img1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        if(requestCode == 206 && resultCode == RESULT_OK){
//            try {
//                img2 = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
//                imageView.setImageBitmap(img2);
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void overPrint(){
        Canvas canvas = new Canvas();
        Bitmap bitmap = img1.copy(Bitmap.Config.ARGB_8888, true);
        canvas.setBitmap(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20);
        paint.setColor(getColor(R.color.design_default_color_error));
        paint.setStrokeWidth(2);
        int x = canvas.getHeight()/2;
        int y = canvas.getWidth()/2;
        canvas.drawText("jay",x,y,paint);
    }
    public Bitmap drawTextToBitmap(Context gContext,
                                   Bitmap bitmap,
                                   String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(255, 0, 0));
        // text size in pixels
        paint.setTextSize((int) (140 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {
        Log.e("jaydip",progress+"");

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
            Log.e("jaydip",path+"jaydip");
    }
}