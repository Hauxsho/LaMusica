package com.example.lamusica;

import androidx.appcompat.app.AppCompatActivity;
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

    TextView songName,artistName,durationPlay,durationEnd;
    ImageView coverArt , nextButton , prevButton , repeatButton , backButton , shuffleButton;
    FloatingActionButton playPauseButton;
    SeekBar seekbar;
    int position=-1;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    static ArrayList<MusicFiles> listSongs= new ArrayList<>();
    private Handler handler = new Handler();
    private Thread playThread,prevThread,nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
        songName.setText(listSongs.get(position).getTitle());
        artistName.setText(listSongs.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
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


    @Override
    protected void onResume() {
        playThreadbtn();
        nextThreadbtn();
        prevThreadbtn();
        super.onResume();
    }

    private void prevThreadbtn() {
        prevThread=new Thread()
        {
            @Override
            public void run() {
                super.run();
                prevButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevButtonClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevButtonClicked() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=(((position-1)<0) ? (listSongs.size()-1) : (position-1));
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekbar.setMax(mediaPlayer.getDuration()/1000);
            Player.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this , 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseButton.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=(((position-1)<0) ? (listSongs.size()-1) : (position-1));
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekbar.setMax(mediaPlayer.getDuration()/1000);
            Player.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this , 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseButton.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void nextThreadbtn() {
        nextThread=new Thread()
        {
            @Override
            public void run() {
                super.run();
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextButtonClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextButtonClicked() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position+1)%listSongs.size());
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekbar.setMax(mediaPlayer.getDuration()/1000);
            Player.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this , 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseButton.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position=((position+1)%listSongs.size());
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listSongs.get(position).getTitle());
            artistName.setText(listSongs.get(position).getArtist());
            seekbar.setMax(mediaPlayer.getDuration()/1000);
            Player.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this , 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseButton.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void playThreadbtn() {
        playThread=new Thread()
        {
            @Override
            public void run() {
                super.run();
                playPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseButtonClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseButtonClicked() {
        if(mediaPlayer.isPlaying())
        {
            playPauseButton.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekbar.setMax(mediaPlayer.getDuration()/1000);
            Player.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this , 1000);
                }
            });
        }
        else
        {
            playPauseButton.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekbar.setMax(mediaPlayer.getDuration()/1000);
            Player.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this , 1000);
                }
            });
        }
    }


    private String formattedTime(int mCurrentPosition)
    {
        String totalOut="",totalNew="",seconds=String.valueOf(mCurrentPosition%60),minutes=String.valueOf(mCurrentPosition/60);
        totalOut=minutes + ":" + seconds;
        totalNew=minutes + ":0" + seconds;
        if(seconds.length()==1)
        {
            return totalNew;
        }
        else {
            return  totalOut;
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
        metaData(uri);
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

    private void metaData(Uri uri)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration())/1000;
        durationEnd.setText(formattedTime(durationTotal));
        byte[] art=retriever.getEmbeddedPicture();
        if(art!=null)
        {
            Glide.with(this).asBitmap().load(art).into(coverArt);
        }
        else{
            Glide.with(this).asBitmap().load(R.drawable.lamusica).into(coverArt);

        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextButtonClicked();
        if (mediaPlayer != null) {
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);

        }
    }
}
