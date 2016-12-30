package com.example.android.ducklingplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.R.attr.path;
import static com.example.android.ducklingplayer.PlayerActivity.oneTimeOnly;
import static com.example.android.ducklingplayer.R.id.seekbar;
import static com.example.android.ducklingplayer.R.id.songname;
import static com.example.android.ducklingplayer.R.string.playpause;
import static java.security.AccessController.getContext;

/**
 * Created by mikasa on 25/12/16.
 */

public class PlayerActivity extends AppCompatActivity {

    public int currsongindex;
    private SeekBar seekbar;
    private double startTime,finalTime;
    private int forwardTime,backwardTime;
    private TextView st, et, songname;
    public static int oneTimeOnly;
    private Handler myHandler;
    public static MediaPlayer mediaPlayer=new MediaPlayer();
    private SongInfo currsong;
    public String name,fp;
    public ArrayList<SongInfo> songslist=new ArrayList<>();
    public boolean isll=false,isrepeat=false,isshuffle=false,audiofocus=false;

    /*final TextView playpause = (TextView) findViewById(R.id.playpausesong);
    TextView fwd = (TextView) findViewById(R.id.fastfwd);
    TextView rev = (TextView) findViewById(R.id.fastrwd);
    TextView nxt = (TextView) findViewById(R.id.nextsong);
    TextView prev = (TextView) findViewById(R.id.prevsong);
    final TextView repeat=(TextView)findViewById(R.id.rpt);
    final TextView shuffle=(TextView)findViewById(R.id.shuf);
    final LinearLayout ll=(LinearLayout)findViewById(R.id.rpshuf);
    ImageView album=(ImageView)findViewById(R.id.albumart);
    TextView st = (TextView) findViewById(R.id.starttime);
    TextView et = (TextView) findViewById(R.id.endtime);
    TextView songname = (TextView) findViewById(R.id.songtitle);*/
    /*AudioManager am;



    final AudioManager.OnAudioFocusChangeListener af=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                // Pause
                mediaPlayer.pause();
                playpause.setText(">");
                Log.v("Af","Losst");
                //am.abandonAudioFocus(null);
            }
            if(i == AudioManager.AUDIOFOCUS_GAIN)
            {
                // Resume
                init();
                Log.v("Af","Gain");
                mediaPlayer.start();
                //startPlayer();
            }
            else if(i == AudioManager.AUDIOFOCUS_LOSS)
            {
                // Stop or pause depending on your need
                mediaPlayer.pause();
                playpause.setText(">");
                //releaseMediaPlayer();
            }
        }
    };*/

    private MediaPlayer.OnCompletionListener ocl=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            if ((isrepeat && (!isshuffle)) || (isrepeat && (isshuffle))) {
                name = songslist.get(currsongindex).getsongname();
                fp = songslist.get(currsongindex).getsongpath();
                init();
                startPlayer();
            } else if ((!isrepeat) && (!isshuffle)) {
                if (currsongindex < (songslist.size() - 1)) {
                    currsongindex++;
                } else {
                    currsongindex = 0;
                }
                name = songslist.get(currsongindex).getsongname();
                fp = songslist.get(currsongindex).getsongpath();
                init();
                startPlayer();
            } else if (isshuffle && (!isrepeat)) {
                Random rand = new Random();
                currsongindex = rand.nextInt((songslist.size() - 1) - 0 + 1) + 0;
                name = songslist.get(currsongindex).getsongname();
                fp = songslist.get(currsongindex).getsongpath();
                init();
                startPlayer();
            }

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //releaseMediaPlayer();
        setContentView(R.layout.player);
        name=getIntent().getStringExtra("Name");
        fp=getIntent().getStringExtra("Filepath");
        currsongindex=getIntent().getExtras().getInt("SongIndex");
        String msg=""+currsongindex;
        Log.v("SongIndex",msg);
        songslist=(ArrayList<SongInfo>) getIntent().getSerializableExtra("List");
        init();
        startPlayer();
    }
    public void init()
    {
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        startTime=0;
        finalTime=0;
        forwardTime=5000;
        backwardTime=5000;
        oneTimeOnly=0;
        myHandler=new Handler();
    }


    public void startPlayer() {
        mediaPlayer=MediaPlayer.create(PlayerActivity.this,Uri.parse(fp));
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(ocl);

        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if((isrepeat && (!isshuffle)) || (isrepeat && (isshuffle)))
                {
                    name=songslist.get(currsongindex).getsongname();
                    fp=songslist.get(currsongindex).getsongpath();
                    init();
                    startPlayer();
                }
                else if((!isrepeat) && (!isshuffle))
                {
                    if(currsongindex<(songslist.size()-1))
                    {
                        currsongindex++;
                    }
                    else
                    {
                        currsongindex=0;
                    }
                    name=songslist.get(currsongindex).getsongname();
                    fp=songslist.get(currsongindex).getsongpath();
                    init();
                    startPlayer();
                }
                else if(isshuffle && (!isrepeat))
                {
                    Random rand = new Random();
                    currsongindex=rand.nextInt((songslist.size()-1)-0+1)+0;
                    name=songslist.get(currsongindex).getsongname();
                    fp=songslist.get(currsongindex).getsongpath();
                    init();
                    startPlayer();
                }

            }
        });*/

        final TextView playpause = (TextView) findViewById(R.id.playpausesong);
        TextView fwd = (TextView) findViewById(R.id.fastfwd);
        TextView rev = (TextView) findViewById(R.id.fastrwd);
        TextView nxt = (TextView) findViewById(R.id.nextsong);
        TextView prev = (TextView) findViewById(R.id.prevsong);
        final TextView repeat=(TextView)findViewById(R.id.rpt);
        final TextView shuffle=(TextView)findViewById(R.id.shuf);
        final LinearLayout ll=(LinearLayout)findViewById(R.id.rpshuf);
        ImageView album=(ImageView)findViewById(R.id.albumart);
        st = (TextView) findViewById(R.id.starttime);
        et = (TextView) findViewById(R.id.endtime);
        songname = (TextView) findViewById(R.id.songtitle);
        songname.setText(name);

        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
        mmr.setDataSource(songslist.get(currsongindex).getsongpath());
        byte[] art=mmr.getEmbeddedPicture();
        if(art==null)
            album.setImageDrawable(getResources().getDrawable(R.drawable.treble1));
        else
        {
            InputStream is=new ByteArrayInputStream(mmr.getEmbeddedPicture());
            Bitmap bm= BitmapFactory.decodeStream(is);
            album.setImageBitmap(bm);
        }

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isll) {
                    ll.setVisibility(LinearLayout.INVISIBLE);
                    isll = true;
                } else {
                    ll.setVisibility(LinearLayout.VISIBLE);
                    isll = false;
                    ll.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ll.setVisibility(LinearLayout.INVISIBLE);
                            isll = true;
                        }
                    }, 5000);
                }
            }
        });



        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isrepeat) {
                    isrepeat = false;
                    repeat.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else {
                    isrepeat = true;
                    repeat.setTextColor(getResources().getColor(R.color.playlistTextColor));
                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isshuffle) {
                    isshuffle = false;
                    shuffle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else {
                    isshuffle = true;
                    shuffle.setTextColor(getResources().getColor(R.color.playlistTextColor));
                }
            }
        });

        seekbar = (SeekBar) findViewById(R.id.seekbar);

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playpause.setText(">");
                }
                else {
                    playpause.setText("||");
                    mediaPlayer.start();
                }
            }
        });

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        et.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        st.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );
        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isshuffle)
                {
                    Random rand = new Random();
                    currsongindex=rand.nextInt((songslist.size()-1)-0+1)+0;
                    name=songslist.get(currsongindex).getsongname();
                    fp=songslist.get(currsongindex).getsongpath();
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    init();
                    startPlayer();
                }
                else
                {
                    if(currsongindex!=(songslist.size()-1))
                    {
                        currsongindex++;
                    }
                    else
                        currsongindex=0;
                    mediaPlayer.stop();
                    currsong=songslist.get(currsongindex);
                    name=currsong.getsongname();
                    fp=currsong.getsongpath();
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    init();
                    startPlayer();
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isshuffle)
                {
                    Random rand = new Random();
                    currsongindex=rand.nextInt((songslist.size()-1)-0+1)+0;
                    name=songslist.get(currsongindex).getsongname();
                    fp=songslist.get(currsongindex).getsongpath();
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    init();
                    startPlayer();
                }
                else
                {
                    if(currsongindex!=0) {
                        currsongindex--;
                    }
                    else
                        currsongindex=(songslist.size())-1;
                    mediaPlayer.stop();
                    currsong=songslist.get(currsongindex);
                    name=currsong.getsongname();
                    fp=currsong.getsongpath();
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    init();
                    startPlayer();
                }
            }
        });

        fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
            }
        });




        rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myHandler.removeCallbacks(UpdateSongTime);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int CurrentLevel = seekbar.getProgress();
                if(CurrentLevel < 30)
                    CurrentLevel = 30;
                seekbar.setProgress(CurrentLevel);
                mediaPlayer.seekTo(CurrentLevel);
                myHandler.postDelayed(UpdateSongTime,100);
            }
        });
    }



    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            st.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public static MediaPlayer getInstance(){
        return mediaPlayer;
    }

    @Override
    public void onBackPressed(){
        Intent pi=new Intent(PlayerActivity.this,MainActivity.class);
        pi.addFlags(pi.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(pi);
    }

    /*private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            am.abandonAudioFocus(af);
        }
    }*/
}



