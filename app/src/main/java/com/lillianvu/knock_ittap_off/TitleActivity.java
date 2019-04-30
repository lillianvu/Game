package com.lillianvu.knock_ittap_off;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class TitleActivity extends AppCompatActivity {

    ImageView startBtnImageView;
    MediaPlayer mediaPlayer;
    AnimationDrawable startAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
        mediaPlayer.start();

        startBtnImageView = findViewById(R.id.startBtnImageView);
        startBtnImageView.setBackgroundResource(R.drawable.animation_startbtn);
        startAnimation = (AnimationDrawable) startBtnImageView.getBackground();
        startAnimation.start();

        startBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                startActivity(new Intent(getApplicationContext(),IntroActivity.class));
            }
        });
    }
}
