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
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

public class AlarmReceiver extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        Log.i("tag", "闹钟响了........");
        int song=intent.getIntExtra("song",0);
        int indexof=intent.getIntExtra("indexof",-1);
        boolean isopen = intent.getBooleanExtra("isopen",false);
        Log.i("tag",song+"");
        final PlayVoice playVoice = new PlayVoice(song);
        playVoice.playVoice(context);
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(10000);
        AudioManager audioManager
                = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, 5, 0);
        if(isopen){
            Intent intent1 = new Intent(context,Model0.class);
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
           // android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("提示");
            dialogBuilder.setMessage("闹钟响了，点击确定关闭");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    playVoice.stopVoice();
                }
            });
           AlertDialog alertDialog = dialogBuilder.create();
           // android.support.v7.app.AlertDialog alertDialog=dialogBuilder.create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            alertDialog.show();

        }
    }
}
