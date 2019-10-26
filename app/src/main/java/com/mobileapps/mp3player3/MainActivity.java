package com.mobileapps.mp3player3;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "Audio";
    final String[] columns = {  MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
            _ID };
    final String[] filePath = { MediaStore.Audio.Media.DATA };
    final String orderBy = MediaStore.Audio.Media.DATE_ADDED + " DESC";

   // private List<String> songsArray = new ArrayLis>();



    private ListView mTiles;
    Cursor mCursor;
    int mPosition=0;
    SongsAdapter adapter;



    //setting up the cursor and view and adapter and itemclick
    public void init () {

        mCursor=getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (mCursor!= null) mCursor.moveToFirst();




        mTiles=findViewById(R.id.songList);

        adapter=new SongsAdapter();

        mTiles.setAdapter(adapter);

        mTiles.setOnItemClickListener(this);

    }

//    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (getBitmapFromMemCache(key) == null) {
//            memoryCache.put(key, bitmap);
//        }
//    }

//    public Bitmap getBitmapFromMemCache(String key) {
//        return memoryCache.get(key);
//    }
//
//    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
//
//        Matrix matrix = new Matrix();
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_NORMAL:
//                return bitmap;
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                matrix.setScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                matrix.setRotate(180);
//                break;
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                matrix.setRotate(180);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_TRANSPOSE:
//                matrix.setRotate(90);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                matrix.setRotate(90);
//                break;
//            case ExifInterface.ORIENTATION_TRANSVERSE:
//                matrix.setRotate(-90);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                matrix.setRotate(-90);
//                break;
//            default:
//                return bitmap;
//        }
//        try {
//            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            bitmap.recycle();
//            return bmRotated;
//        }
//        catch (OutOfMemoryError e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//

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


//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) >= reqHeight
//                    && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }





    public class SongsAdapter extends BaseAdapter {


        // Holds the photo imageview and it's position
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

            //vh.songName.setImageBitmap(null);

//            //getting bitmap
//            final Bitmap bitmap = getBitmapFromMemCache(String.valueOf(i));
//
//            //setting image bitmap if its there
//            if (bitmap != null) {
//                vh.image.setImageBitmap(bitmap);
//            } else

//                //otherwise make an AsyncTask to load the image and cache bitmaps
//                new AsyncTask<ViewHolder, Void, Bitmap>() {
//                    private ViewHolder vh;
//
//
//                    @Override
//                    protected Bitmap doInBackground(ViewHolder... params) {
//                        vh = params[0];
//                        mCursor.moveToPosition(vh.position);
//                        Bitmap bmp = null;
//
//                        try {
//                            String imagePath = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
//
//                            //THIS BIT IS FOR ORIETNATION
//                            ExifInterface exif = null;
//                            try {
//                                exif = new ExifInterface(imagePath);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                    ExifInterface.ORIENTATION_UNDEFINED);
//
//
//                            // decode the jpeg into a bitmap
//                            bmp = decodeSampledBitmapFromResource(imagePath, 900, 900);
//                            bmp = ThumbnailUtils.extractThumbnail(bmp, 200, 200);
//                            bmp = rotateBitmap(bmp, orientation); //rotate bitmap
//                            addBitmapToMemoryCache(String.valueOf(vh.position), bmp);
//                        } catch (Exception e) {
//                            Log.i(TAG, "Error Loading:" + mCursor.getString(mCursor.getColumnIndex(filePath[0])));
//                            e.printStackTrace();
//                        }
//
//                        return bmp;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Bitmap bmp) {
//                        // only set the imageview if the position hasn't changed.
//                        if (vh.position == i) {
//                            vh.image.setImageBitmap(bmp);
//                        }
//                    }
//                }.execute(vh);
//

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

//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        // Use 1/8th of the available memory for this memory cache.
//        final int cacheSize = maxMemory / 8;

//        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                // The cache size will be measured in kilobytes rather than
//                // number of items.
//                return bitmap.getByteCount() / 1024;
//            }
//        };

        if (Build.VERSION.SDK_INT > 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        else

            init();
    }
}