package edu.fje.puzzle;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;



public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    Handler hnd;
    Runnable rn;
    Button pl;
    boolean p = true;
    Button Empezar;
    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button boto = (Button) findViewById(R.id.boto);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int amplada = size.x;
        final int alcada = size.y;
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

        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator animacio1 =
                        ObjectAnimator.ofFloat(boto, "x", 0, amplada-boto.getWidth());
                animacio1.setDuration(1000);
                ObjectAnimator animacio2 =
                        ObjectAnimator.ofFloat(boto, "y", 0, alcada-boto.getHeight());
                animacio1.setDuration(1000);
                ObjectAnimator animacio3 =
                        ObjectAnimator.ofFloat(boto, "x",  amplada-boto.getWidth(),0);
                animacio1.setDuration(1000);
                ObjectAnimator animacio4 =
                        ObjectAnimator.ofFloat(boto, "y", alcada-boto.getHeight(),0);
                animacio1.setDuration(1000);


                final AnimatorSet animacio = new AnimatorSet();
                animacio.playSequentially(animacio1, animacio2, animacio3, animacio4);


                animacio.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        super.onAnimationEnd(a);
                        animacio.start();
                    }
                });
                animacio.start();
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
        public void addFragment(){
        FragmentLogin fragment = new FragmentLogin();
        fragment.setCallbackFragment(this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragmentContainer,fragment);
        ft.commit();
    }

    public void replaceFragment(){
        fragment= new FragmentRegister();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragmentContainer,fragment);
        ft.commit();
    }

    public void startFragment(){
        FragmentMain newFragment = new FragmentMain();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    @Override
    public void changeFragment() {
        replaceFragment();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void Empezar(){
        Intent puzzle = new Intent(this, PuzzleActivity.class);
        startActivity(puzzle);
    }
}
