package com.jacklee.clatclatter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class SlidingButtonActivity extends Activity {

    private SlidingButton mSlidingButton;
    private MediaPlayer mediaPlayer;
    final PlayVoice playVoice = new PlayVoice(0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideunlock);

        mSlidingButton = (SlidingButton)this.findViewById(R.id.mainview_answer_1_button);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSlidingButton.handleActivityEvent(event)) {
            Toast.makeText(SlidingButtonActivity.this, "解锁成功", Toast.LENGTH_LONG).show();
            finish();
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
        playVoice.stopVoice();
    }
}