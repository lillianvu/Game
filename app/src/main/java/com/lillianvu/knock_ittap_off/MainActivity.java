package com.lillianvu.knock_ittap_off;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView commandTextView, scoreTextView, timerTextView;
    ImageView swipeImageView, swipeRevImageView;
    ImageButton tapItImageBtn;
    SparkButton doubleTapSparkBtn;
    MediaPlayer mediaPlayer, backgroundMedia;
    AnimationDrawable swipeAnimation, swipeRevAnimation;
    SoundPool soundPool;
    int swipeItBtnSound, swipeItCommand, doubleBtnSound, doubleTapCommand, tapItBtnSound, tapItCommand, wrongBtnSound;
    int scoreCount = 0;
    int swipeCount = 0;
    String[] commands = {"SPIN IT", "SWIPE IT", "DOUBLE TAP"};
    int maxVolume = 100;

    CountDownTimer countDownTime;
    long timeLeftInMilliseconds = 31000;

    public void startTimer() {
        countDownTime = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(),EndActivity.class);
                intent.putExtra("SCORE", scoreCount);
                startActivity(intent);
            }
        }.start();
    }

    public void updateTimer() {
        int seconds = (int) timeLeftInMilliseconds % 60000/1000;
        String timeLeftText;
        timeLeftText = "Time: " + seconds;
        timerTextView.setText(timeLeftText);
    }

    public void swipeMethodGenerator() {
        if ((swipeCount % 2) == 0) {
            swipeRevImageView.setVisibility(View.VISIBLE);
            swipeImageView.setVisibility(View.GONE);
            swipeRevAnimation.start();
        } else {
            swipeImageView.setVisibility(View.VISIBLE);
            swipeRevImageView.setVisibility(View.GONE);
            swipeAnimation.start();
        }
    }

    public void newNumberGenerator() {
        Random randomNumberGenerator = new Random();
        int randomNumber = randomNumberGenerator.nextInt(3);
        commandTextView.setText(commands[randomNumber]);
        scoreCount++;
        scoreTextView.setText("Score: " + scoreCount);

        //play button sounds delayed to keep from overlapping with command sounds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String commandAudio = commandTextView.getText().toString();
                switch(commandAudio) {
                    case "SPIN IT":
                        soundPool.play(tapItCommand, 1, 1, 0, 0, 1);
                        break;
                    case "SWIPE IT":
                        soundPool.play(swipeItCommand, 1, 1, 0, 0, 1);
                        swipeCount++;
                        swipeMethodGenerator();
                        break;
                    case "DOUBLE TAP":
                        soundPool.play(doubleTapCommand, 1, 1, 0, 0, 1);
                        break;
                }
            }
        }, 320);
    }


    public void startEndActivity() {
        Intent intent = new Intent(getApplicationContext(),EndActivity.class);
        intent.putExtra("SCORE", scoreCount);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC,0);
        tapItBtnSound = soundPool.load(this, R.raw.tapsound,1);
        doubleBtnSound = soundPool.load(this, R.raw.doublesound,1);
        swipeItBtnSound = soundPool.load(this, R.raw.swipesound,1);
        tapItCommand = soundPool.load(this, R.raw.tapitsound, 1);
        swipeItCommand = soundPool.load(this, R.raw.swipeitsound, 1);
        doubleTapCommand = soundPool.load(this, R.raw.doubletapsound, 1);
        wrongBtnSound = soundPool.load(this, R.raw.wrongbtnsound, 1);

        backgroundMedia = MediaPlayer.create(this, R.raw.backgroundmusic);
        backgroundMedia.start();
        float log1=(float)(Math.log(maxVolume-98)/Math.log(maxVolume));
        backgroundMedia.setVolume(log1,log1);

        commandTextView = findViewById(R.id.commandTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        swipeImageView = findViewById(R.id.swipeImageView);
        swipeRevImageView = findViewById(R.id.swipeRevImageView);
        tapItImageBtn = findViewById(R.id.tapItImageBtn);
        doubleTapSparkBtn = findViewById(R.id.doubleTapSparkBtn);

        swipeImageView = findViewById(R.id.swipeImageView);
        swipeImageView.setVisibility(View.VISIBLE);
        swipeImageView.setBackgroundResource(R.drawable.animation_swipe);
        swipeAnimation = (AnimationDrawable) swipeImageView.getBackground();

        swipeRevImageView = findViewById(R.id.swipeRevImageView);
        swipeRevImageView.setVisibility(View.GONE);
        swipeRevImageView.setBackgroundResource(R.drawable.animation_swiperev);
        swipeRevAnimation = (AnimationDrawable) swipeRevImageView.getBackground();

        doubleTapSparkBtn.setSoundEffectsEnabled(false);
        tapItImageBtn.setSoundEffectsEnabled(false);
        swipeImageView.setSoundEffectsEnabled(false);
        swipeRevImageView.setSoundEffectsEnabled(false);


        //initiates the game with push it sound
        mediaPlayer = MediaPlayer.create(this, R.raw.doubletapsound);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        commandTextView.setText("DOUBLE TAP");


        scoreTextView.setText("Score: " + scoreCount);
        startTimer();

        tapItImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapItImageBtn.animate().rotation(tapItImageBtn.getRotation()+360).start();
                soundPool.play(tapItBtnSound, 1, 1, 0, 0, 1);
                final String chosenCommand = commandTextView.getText().toString();

                if (chosenCommand.equals(commands[0])) {
                    newNumberGenerator();
                } else {
                    soundPool.play(wrongBtnSound, 1, 1, 0, 0, 1);
                    startEndActivity();
                    countDownTime.cancel();
                }
            }
        });

        swipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(swipeItBtnSound, 1, 1, 0, 0, 1);
                final String chosenCommand = commandTextView.getText().toString();

                if (chosenCommand.equals(commands[1])) {
                    swipeAnimation.stop();
                    swipeAnimation.selectDrawable(0);
                    newNumberGenerator();
                } else {
                    soundPool.play(wrongBtnSound, 1, 1, 0, 0, 1);
                    startEndActivity();
                    countDownTime.cancel();
                }
            }
        });

        swipeRevImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeAnimation.start();
                soundPool.play(swipeItBtnSound, 1, 1, 0, 0, 1);
                final String chosenCommand = commandTextView.getText().toString();

                if (chosenCommand.equals(commands[1])) {
                    swipeRevAnimation.stop();
                    swipeRevAnimation.selectDrawable(0);
                    newNumberGenerator();
                } else {
                    soundPool.play(wrongBtnSound, 1, 1, 0, 0, 1);
                    startEndActivity();
                    countDownTime.cancel();
                }
            }
        });

        doubleTapSparkBtn.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                soundPool.play(doubleBtnSound, 1, 1, 0, 0, 1);
                final String chosenCommand = commandTextView.getText().toString();

                if (chosenCommand.equals(commands[2])) {
                    newNumberGenerator();
                } else {
                    soundPool.play(wrongBtnSound, 1, 1, 0, 0, 1);
                    startEndActivity();
                    countDownTime.cancel();
                }

                if (buttonState) {

                } else {

                }
            }
            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) { }
            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) { }
        });
    }
}
