package edu.fje.puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    Handler hnd;
    Runnable rn;
    Button pl;
    boolean p = true;
    Button Emepzar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Emepzar = findViewById(R.id.bEmpezar);
        pl = (Button) findViewById(R.id.bPlpa);
        pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p == true) {
                    p = false;
                    pl.setText("Play");
                    onPause();
                } else {
                    p = true;
                    pl.setText("Pause");
                    onResume();
                }
            }
        });

        hnd = new Handler();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.song);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playCycle();
                mp.start();
            }
        });

        Emepzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Empezar();
            }
        });


    }

    public void playCycle(){
        if(mp.isPlaying()){
            rn = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            hnd.postDelayed(rn, 1000);
        }
    }

    public void onPause(){
        super.onPause();
        mp.pause();
    }

    public void onResume(){
        super.onResume();
        mp.start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.release();
        hnd.removeCallbacks(rn);
    }

    public void Empezar(){
        Intent puzzle = new Intent(this, PuzzleActivity.class);
        startActivity(puzzle);
    }
}