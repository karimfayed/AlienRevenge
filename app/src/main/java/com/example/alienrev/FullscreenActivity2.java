package com.example.alienrev;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class FullscreenActivity2 extends AppCompatActivity {

    MediaPlayer mysong;
    ImageView ship, start;
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen2);
        mysong = MediaPlayer.create(getApplicationContext(),R.raw.song);
        mysong.start();

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        ship = (ImageView)findViewById(R.id.ufo);
        start = (ImageView)findViewById(R.id.start);


        //Set Animation
        ship.setAnimation(topAnim);
        start.setAnimation(bottomAnim);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(FullscreenActivity2.this,MainActivity.class);
                    startActivity(i);
            }
        });


//        Thread thread = new Thread(){
//            public void run(){
//                try {
//                    sleep(5000);
//                }
//                catch (Exception c)
//                {
//
//                }
//                finally {
//                    Intent i = new Intent(FullscreenActivity2.this,MainActivity.class);
//                    startActivity(i);
//                }
//            }
//        };
//        thread.start();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mysong.release();
        finish();
    }
}
