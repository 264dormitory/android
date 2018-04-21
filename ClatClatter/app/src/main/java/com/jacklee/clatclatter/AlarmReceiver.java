package com.jacklee.clatclatter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

public class AlarmReceiver extends BroadcastReceiver{
    MediaPlayer mediaPlayer = new MediaPlayer();

    public void onReceive(Context context, Intent intent) {
        Log.i("tag", "闹钟响了........");
        int song=intent.getIntExtra("song",-1);//获取歌的位置。默认设为-1
        int indexof=intent.getIntExtra("indexof",-1);
        String songPath=intent.getStringExtra("songPath");
        Log.i("tag", "onReceive: "+songPath);
        boolean isopen = intent.getBooleanExtra("isopen",false);
        Log.i("tag",song+"");
            //都不选
        if(song==-1&&songPath==null)
        {
            final PlayVoice playVoice = new PlayVoice(R.raw.time);
            playVoice.playVoice(context);
        }else if(song!=-1&&songPath==null){  //选系统
            final PlayVoice playVoice = new PlayVoice(song);
            playVoice.playVoice(context);
        }else  {
            try {
                Log.i("tag", "mmmmmmmmmmmmmmmmmmmmmmmmmmm");
                mediaPlayer.setDataSource(songPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000);
        AudioManager audioManager
                = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, 5, 0);
        if(isopen){
            Intent intent1 = new Intent(context,Model_1.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.i("tag",indexof+"");
            switch (indexof){
                case 0:
                    context.startActivity(intent1);
                    break;
                case 1:
                    context.startActivity(intent1);
                    break;
                case 2:
                    context.startActivity(intent1);
                    break;
                case 3:
                    context.startActivity(intent1);
                    break;

                default:
                    break;
            }
        }else{
            Intent intent1=new Intent(context,SlidingButtonActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("提示");
            dialogBuilder.setMessage("闹钟响了，点击确定关闭");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PlayVoice.stopVoice();
                    mediaPlayer.stop();
                }
            });
           AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            alertDialog.show();*/


        }
    }
}
