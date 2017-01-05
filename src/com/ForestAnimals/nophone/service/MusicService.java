package com.ForestAnimals.nophone.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by dell on 2016/1/23.
 */
public class MusicService extends Service
{
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public void onCreate() {
//        mediaPlayer= MediaPlayer.create(this, R.raw.chunni);
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        isPlaying = intent.getBooleanExtra("isPlaying", false);
        if (isPlaying){
            mediaPlayer.start();
        }else {
            mediaPlayer.pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}