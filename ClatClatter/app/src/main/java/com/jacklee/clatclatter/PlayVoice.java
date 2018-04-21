package com.jacklee.clatclatter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class PlayVoice {
    private static MediaPlayer mediaPlayer;
    private  int song_name;
    public PlayVoice (int song){
        song_name = song;
    }
    public  void playVoice(Context context){
        try {
            mediaPlayer= MediaPlayer.create(context, song_name);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //停止播放声音
    public  static void stopVoice(){
        if(null!=mediaPlayer) {
            mediaPlayer.stop();
        }
    }
    public static void setSource(String filepath,MediaPlayer player) throws IOException {
       player = new MediaPlayer();
       player.setDataSource(filepath); //指定音频文件的路径
       player.prepare();
       player.start();
    }
    public static void onpause(){
        mediaPlayer.pause();
    }
}
