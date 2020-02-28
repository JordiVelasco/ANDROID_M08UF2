package edu.fje.puzzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements CallbackFragment {

    MediaPlayer mp;
    Handler hnd;
    Runnable rn;
    Button pl, p2,p3;
    boolean p = true;
    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;
    FirebaseAuth fAuth;
    Button Emepzar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pl = (Button) findViewById(R.id.bPlpa);
        p2 = (Button) findViewById(R.id.button);
        p3 = (Button) findViewById(R.id.button2);

        fAuth = FirebaseAuth.getInstance();


        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment();
            }
        });

        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment();
            }
        });

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
}
