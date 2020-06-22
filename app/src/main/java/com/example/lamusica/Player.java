package com.example.lamusica;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.lamusica.MainActivity.musicFiles;

public class Player extends AppCompatActivity {

    TextView songName,artistName,durationPlay,durationEnd;
    ImageView coverArt , nextButton , prevButton , repeatButton , backButton , shuffleButton;
    FloatingActionButton playPauseButton;
    SeekBar seekbar;
    int position=-1;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    static ArrayList<MusicFiles> listSongs= new ArrayList<>();
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if((mediaPlayer!=null) && b)
                {
                     mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Player.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null)
                {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    seekbar.setProgress(mCurrentPosition);
                    durationPlay.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this , 1000);
            }
        });

    }
    private String formattedTime(int mCurrentPosition)
    {
        String totalOut="",totalNew="",seconds=String.valueOf(mCurrentPosition%60),minutes=String.valueOf(mCurrentPosition/60);
        totalOut=minutes + ":" + seconds;
        totalNew=minutes + ":" + "0" + seconds;
        if(seconds.length()==0)
        {
            return totalNew;
        }
        else {
            return totalOut;
        }
    }



    private void getIntentMethod() {
        position=getIntent().getIntExtra("position",-1);
        listSongs=musicFiles;
        if(listSongs!=null)
        {
            playPauseButton.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekbar.setMax(mediaPlayer.getDuration()/1000);
    }

    private void initView() {
        songName=(TextView)findViewById(R.id.songName);
        artistName=(TextView)findViewById(R.id.songArtist);
        durationPlay=(TextView)findViewById(R.id.duratioStart);
        durationEnd=(TextView)findViewById(R.id.duratioEnd);
        coverArt=(ImageView)findViewById(R.id.coverArt);
        nextButton=(ImageView)findViewById(R.id.id_next);
        prevButton=(ImageView)findViewById(R.id.id_prev);
        repeatButton=(ImageView)findViewById(R.id.id_repeat);
        backButton=(ImageView)findViewById(R.id.back_button);
        shuffleButton=(ImageView)findViewById(R.id.shuffle);
        playPauseButton=(FloatingActionButton)findViewById(R.id.playpause);
        seekbar=(SeekBar)findViewById(R.id.seek);

    }
}
