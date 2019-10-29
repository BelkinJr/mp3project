package com.mobileapps.mp3player3;

//Vitaly Belkin 17385402
//159.336 Assignment 3 2019 S2
//Audio Player

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;


import static android.provider.BaseColumns._ID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "Audio";
    final String[] columns = {  MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
            _ID };
    final String[] filePath = { MediaStore.Audio.Media.DATA };
    final String orderBy = MediaStore.Audio.Media.DATE_ADDED + " DESC";


    private ListView mTiles;
    Cursor mCursor;
    int mPosition=0;
    SongsAdapter adapter;

    //setting up the cursor and view and adapter and itemclick
    public void init () {

        //getting all audiofiles
        mCursor=getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (mCursor!= null) mCursor.moveToFirst();




        mTiles=findViewById(R.id.songList);

        adapter=new SongsAdapter();

        mTiles.setAdapter(adapter);

        mTiles.setOnItemClickListener(this);

    }

    //remembering the tile that was visible when paused, closing sursor
    @Override
    public void onPause() {
        super.onPause();
        // save the list position
        mPosition=mTiles.getFirstVisiblePosition();
        // close the cursor (will be opened again in init() during onResume())
        mCursor.close();

    }

    //clearing up cache and re-initialising
    @Override
    public void onResume() {
        super.onResume();

        // reinit in case things have changed
        init();
        // set the list position
        mTiles.setSelection(mPosition);
    }

    public class SongsAdapter extends BaseAdapter {


        // Holds the song textview and it's position
        class ViewHolder {
            int position;
            TextView songName;
        }


        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        // not used
        @Override
        public Object getItem(int i) {return null;}

        // not used
        @Override
        public long getItemId(int i) {
            return i;
        }
        @SuppressLint("StaticFieldLeak")
        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {


            ViewHolder vh;
            if (convertView == null) {
                // if it's not recycled, inflate it from xml
                convertView = getLayoutInflater().inflate(R.layout.item,  viewGroup, false);
                // convertview will be a LinearLayout
                vh=new ViewHolder();
                vh.songName=convertView.findViewById(R.id.oneSong);
                // and set the tag to it
                convertView.setTag(vh);
            } else
                vh=(ViewHolder)convertView.getTag();
            vh.position = i;
            mCursor.moveToPosition(vh.position);
            String songname = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            vh.songName.setText(songname);


            return convertView;
        }

    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //get song position
        mCursor.moveToPosition(i);

        //get song path to pass to new activity
        String songPath = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        String artist = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));



        Intent openSong = new Intent(this, player.class);

        openSong.putExtra("path", songPath );

        openSong.putExtra("name", artist );

        startActivity(openSong);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT > 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        else

            init();
    }
}