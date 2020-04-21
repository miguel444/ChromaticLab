package com.example.cuia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;

import android.view.WindowManager;
import android.widget.ProgressBar;

public class splash_activity extends Activity {

    private final int DURACION_SPLASH = 200;
    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_activity);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();

    }

    private void doWork() {
        for (int i = 0; i < 100; i += 10) {

            try {
                Thread.sleep(DURACION_SPLASH);
                mProgress.setProgress(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void startApp() {
        Intent intent = new Intent(splash_activity.this, menu.class);
        startActivity(intent);

    }



        }
