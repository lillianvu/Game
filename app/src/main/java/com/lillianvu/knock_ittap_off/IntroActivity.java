package com.lillianvu.knock_ittap_off;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    AnimationDrawable countDownAnimation;
    MediaPlayer mediaPlayer;
    ImageView countDownImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        countDownImageView = findViewById(R.id.countDownImageView);
        countDownImageView.setBackgroundResource(R.drawable.animation_countdown);
        countDownAnimation = (AnimationDrawable) countDownImageView.getBackground();
        countDownAnimation.setOneShot(true);
        countDownAnimation.start();

        mediaPlayer = MediaPlayer.create(this, R.raw.countdownsound);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(startActivity);
            }
        }, 2900);
    }
}
