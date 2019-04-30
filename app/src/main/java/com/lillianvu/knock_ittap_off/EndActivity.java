package com.lillianvu.knock_ittap_off;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class EndActivity extends AppCompatActivity {

    TextView highScoreTextView, scoreTextView, gameOverWORD, globalScoreTextView;
    MediaPlayer mediaPlayer1, mediaPlayer2;
    AnimationDrawable globalHighScoreAnimation, tryAgainAnimation;
    ImageView globalImageView, tryAgainImageView;
    int userScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        globalImageView = findViewById(R.id.globalImageView);
        globalImageView.setBackgroundResource(R.drawable.animation_globalhighscore);
        globalHighScoreAnimation = (AnimationDrawable) globalImageView.getBackground();
        globalHighScoreAnimation.start();

        tryAgainImageView = findViewById(R.id.tryAgainImageView);
        tryAgainImageView.setBackgroundResource(R.drawable.animation_tryagainbtn);
        tryAgainAnimation = (AnimationDrawable) tryAgainImageView.getBackground();
        tryAgainAnimation.start();


        highScoreTextView = findViewById(R.id.highScoreTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        gameOverWORD = findViewById(R.id.gameOverWORD);
        globalScoreTextView = findViewById(R.id.globalScoreTextView);

        userScore = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText("LAST SCORE: " + userScore);

        sendHighScore();

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGHSCORE", 0);

        if (highScore < userScore) {
            highScoreTextView.setText("NEW HIGH SCORE: " + userScore);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGHSCORE", userScore);
            editor.commit();
            mediaPlayer1 = new MediaPlayer();
            mediaPlayer1 = MediaPlayer.create(this, R.raw.highscoresound);
            mediaPlayer1.setLooping(false);
            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer1.start();

        } else {
            highScoreTextView.setText("PERSONAL HIGH SCORE: " + highScore);
            mediaPlayer2 = new MediaPlayer();
            mediaPlayer2 = MediaPlayer.create(this, R.raw.gameoversound);
            mediaPlayer2.setLooping(false);
            mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer2.start();
        }

        tryAgainImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),IntroActivity.class));
            }
        });
    }

    public void sendHighScore() {

        BufferedReader bufferedReader;

        try {
            String userScoreString = String.valueOf(userScore);
            String sendScoreEncoded = URLEncoder.encode(userScoreString, "UTF-8");
            URL url = new URL("http://10.0.2.2:8080/GlobalHighScores/KIBOHighScore");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(sendScoreEncoded);
            outputStreamWriter.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                globalScoreTextView.setText(line);
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
