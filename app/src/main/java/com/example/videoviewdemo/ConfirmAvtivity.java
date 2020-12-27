package com.example.videoviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import com.example.videoviewdemo.model.GifFile;

public class ConfirmAvtivity extends AppCompatActivity {
    LinearLayout saveButton;
    GifFile file;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_avtivity);
        saveButton = findViewById(R.id.saveButton);
        file = GifFile.getInstance();

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        file.copyGif(getApplicationContext());
                        progress.dismiss();
                        finish();
                        Looper.loop();
                    }
                });
                t.start();
            }
        });
    }
}