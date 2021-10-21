package com.example.alienrev;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;


public class FullscreenActivity extends AppCompatActivity {

    private GifImageView gif;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        gif = findViewById(R.id.imageView);
        gif.setTranslationX(-900);
        ObjectAnimator animation = ObjectAnimator.ofFloat(gif, "translationX", 1100f);
        animation.setRepeatCount(2);
        animation.setDuration(3000);
        animation.start();

        /* Handler to delay what is written inside the run function so that the GIF would have time
        to be visible on the screen*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(FullscreenActivity.this,FullscreenActivity2.class));
                finish();
            }
        },animation.getRepeatCount()*animation.getDuration()+1000);

    }
}
