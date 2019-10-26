package com.mobileapps.mp3player3;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class player extends AppCompatActivity {

    Button playButn;
    SeekBar positionBar;
    TextView timeElapsed;
    TextView timeLeft;
    TextView songName;
    MediaPlayer mp;
    int songLength;



    public void playSong(String path) {
        MediaPlayer mp = new MediaPlayer();
//        try {
//            mp.setDataSource(path);
//            mp.prepare();
//            mp.start();
//            mp.setLooping(true);
//            mp.seekTo(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    public void playBtnClick (View view) {
        if (!mp.isPlaying()) {
            mp.start();
            playButn.setBackgroundResource(R.drawable.stop2);
        } else {
            mp.pause();
            playButn.setBackgroundResource(R.drawable.playbtn);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            int currentTime = message.what;
            //update progress bar
            positionBar.setProgress(currentTime);

            String elapsedTime = createTimeLable (currentTime);
            timeElapsed.setText(elapsedTime);

            String remainingTime = createTimeLable(songLength - currentTime);
            timeLeft.setText("- " + remainingTime);

        }
    };


    public String createTimeLable (int time) {
        String timelabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        timelabel = min + ":";
        if (sec<10) timelabel += "0";
        timelabel += sec;

        return timelabel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_player);

        playButn = (Button) findViewById(R.id.playButton);
        timeElapsed = (TextView) findViewById(R.id.elapsedTime);
        timeLeft = (TextView) findViewById(R.id.leftTime);
        songName = (TextView) findViewById(R.id.songName);
        //get path of the image as a string
        String songPath = getIntent().getExtras().getString("path");
        String nameOfSong = getIntent().getExtras().getString("name");

        songName.setText(nameOfSong);

        mp = MediaPlayer.create(this, Uri.parse(songPath));
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mp.prepareAsync();
        mp.setLooping(true);
        mp.start();




        mp.setVolume(0.5f, 0.5f);
        songLength = mp.getDuration();

        positionBar = (SeekBar) findViewById(R.id.progressBar);
        positionBar.setMax(songLength);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while ( mp != null ) {
                    try {
                        Message message = new Message();
                        message.what = mp.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {}
                }
            }
        }).start();


    }

}

