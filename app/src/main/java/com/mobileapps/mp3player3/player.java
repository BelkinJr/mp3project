package com.mobileapps.mp3player3;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class player extends AppCompatActivity {

    Button playButn;
    SeekBar positionBar;
    TextView timeElapsed;
    TextView timeLeft;
    TextView songName;
    MediaPlayer mp;
    int songLength;


    //change the button image depending on whether the song is playing or not
    //(stop to play and play to stop)
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

    //calculate time of the song
    public String createTimeLable (int time) {
        String timelabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        timelabel = min + ":";
        //if less than 10- the digit is on the right
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
        //get path of the song as a string
        String songPath = getIntent().getExtras().getString("path");
        //get name of the song as a string
        String nameOfSong = getIntent().getExtras().getString("name");

        songName.setText(nameOfSong);

        mp = MediaPlayer.create(this, Uri.parse(songPath));
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
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

        //here the song progress is sent to the handler
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

