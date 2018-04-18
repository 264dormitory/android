package com.jacklee.clatclatter;

import android.content.Context;
import android.media.MediaPlayer;

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
}
