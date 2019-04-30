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
    MediaPlayer mediaPlayer, backgroundMedia;
    ImageView countDownImageView;
    int maxVolume = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        countDownImageView = findViewById(R.id.countDownImageView);
        countDownImageView.setBackgroundResource(R.drawable.animation_countdown);
        countDownAnimation = (AnimationDrawable) countDownImageView.getBackground();
        countDownAnimation.setOneShot(true);
        countDownAnimation.start();

        backgroundMedia = MediaPlayer.create(this, R.raw.backgroundmusic);
        backgroundMedia.start();
        float log1=(float)(Math.log(maxVolume-98)/Math.log(maxVolume));
        backgroundMedia.setVolume(log1,log1);

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
