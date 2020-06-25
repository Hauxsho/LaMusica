package com.example.lamusica;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.lamusica.MainActivity.musicFiles;

public class Player extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    TextView mSongName, mSongArtist, mDurationPlayed, mDurationTotal;
    ImageView mAlbumArt, mPrevious, mNext, mShuffle, mRepeat, mBack, mMenu;
    FloatingActionButton mPlay;
    SeekBar mSeekBar;
    int position;

    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static MediaPlayer mediaPlayer;
    static Uri uri;

    private Handler handler = new Handler();
    private Thread prevThread, playThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        position = getIntent().getIntExtra("position",-1);
        mSongName = findViewById(R.id.songName);
        mSongArtist = findViewById(R.id.songArtist);
        mDurationPlayed = findViewById(R.id.duratioStart);
        mDurationTotal = findViewById(R.id.duratioEnd);

        mAlbumArt = findViewById(R.id.coverArt);
        mPrevious = findViewById(R.id.id_prev);
        mNext = findViewById(R.id.id_next);
        mShuffle = findViewById(R.id.shuffle);
        mRepeat = findViewById(R.id.id_repeat);
        mBack = findViewById(R.id.back_button);
        mMenu = findViewById(R.id.menu_button);

        mPlay = findViewById(R.id.playpause);

        mSeekBar = findViewById(R.id.seek);

        listSongs = musicFiles;
        if(listSongs!=null)
        {
            getIntentMethods();
            mSongName.setText(listSongs.get(position).getTitle());
            mSongArtist.setText(listSongs.get(position).getArtist());
            mediaPlayer.setOnCompletionListener(this);
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b)
                    mediaPlayer.seekTo(i*1000);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setDurationSeekBar();

    }

    @Override
    protected void onResume() {
        prevThreadBtn();
        playThreadBtn();
        nextThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                mPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        position=(position-1)<0?listSongs.size()-1:position-1;
                        startSongAtPosition();
                    }
                });
            }
        };
        prevThread.start();;
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                mPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();;
    }

    private void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()) {
            mPlay.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
        } else {
            mPlay.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        setDurationSeekBar();
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                mNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        position=((position+1)%listSongs.size());
                        startSongAtPosition();
                    }
                });
            }
        };
        nextThread.start();;
    }

    private String formattedTime(int mCurrentPosition) {
        String time="";
        String seconds=String.valueOf(mCurrentPosition%60);
        String minutes=String.valueOf(mCurrentPosition/60);
        if(seconds.length()>1)
            time=minutes+":"+seconds;
        else
            time=minutes+":0"+seconds;
        return time;
    }

    private void getIntentMethods() {
        mPlay.setImageResource(R.drawable.ic_pause);
        uri = Uri.parse(listSongs.get(position).getPath());
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        mSongName.setText(listSongs.get(position).getTitle());
        mSongArtist.setText(listSongs.get(position).getArtist());
        setDurationSeekBar();
        mSeekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void metaData(Uri uri)
    {
        byte[] art = null;
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            int durationTotal = Integer.parseInt(listSongs.get(position).getDuration())/1000;
            mDurationTotal.setText(formattedTime(durationTotal));
            art = retriever.getEmbeddedPicture();
            retriever.release();
            if(art!=null) {
                Glide.with(this).asBitmap().load(art).into(mAlbumArt);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSongAtPosition() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mPlay.setImageResource(R.drawable.ic_pause);
        uri=Uri.parse(listSongs.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        metaData(uri);
        mSongName.setText(listSongs.get(position).getTitle());
        mSongArtist.setText(listSongs.get(position).getArtist());
        setDurationSeekBar();
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
    }

    private void setDurationSeekBar() {
        mSeekBar.setMax(mediaPlayer.getDuration()/1000);
        Player.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null)
                {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    mSeekBar.setProgress(mCurrentPosition);
                    mDurationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mediaPlayer!=null){
            position=((position+1)%listSongs.size());
            getIntentMethods();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}
