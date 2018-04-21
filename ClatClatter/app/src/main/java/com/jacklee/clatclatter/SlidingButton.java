package com.jacklee.clatclatter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SlidingButton extends android.support.v7.widget.AppCompatButton {

    public SlidingButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SlidingButton(Context context) {
        super(context);
    }

    private boolean isMe = false;

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public SlidingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isMe = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isMe = false;
        }
        return false;
    }

    public boolean handleActivityEvent(MotionEvent activityEvent) {
        boolean result = false;
        if (isMe()) {
            if (activityEvent.getAction() == MotionEvent.ACTION_UP) {
//              Log.v("yupeng", "frame left" + ((FrameLayout)this.getParent().getParent()).getLeft());
//              Log.v("yupeng", "my left" + this.getLeft());
//              Log.v("yupeng", "touch " + this.getLeft());

                if(this.getLeft() + this.getWidth()/2 > ((FrameLayout)this.getParent().getParent()).getWidth() - this.getWidth()){
                    //用户完成了选择动作
                    Log.v("yupeng", "sliding true");
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)this.getLayoutParams();
                    lp.leftMargin = 0;
                    this.setLayoutParams(lp);
                    this.setMe(false);
                    result = true;
                }else{
                    TranslateAnimation trans =
                            new TranslateAnimation(
                                    Animation.ABSOLUTE, 0,
                                    Animation.ABSOLUTE,-this.getLeft(), Animation.RELATIVE_TO_SELF, 0,
                                    Animation.RELATIVE_TO_SELF, 0);

//                  trans.setStartOffset(0);
                    trans.setDuration(600);
//                  trans.setFillAfter(true);
                    trans.setInterpolator(new AccelerateInterpolator());
                    trans.setInterpolator(new Interpolator() {

                        @Override
                        public float getInterpolation(float input) {
                            // TODO Auto-generated method stub
                            return 0;
                        }
                    });
                    trans.setAnimationListener(new SlidingAnimationListener(this));
                    startAnimation(trans);
                }
            } else {
                // 还在拖动
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
//              Log.v("yupeng", "yu" + lp.leftMargin);
                lp.leftMargin = (int) (activityEvent.getX() - ((FrameLayout)this.getParent().getParent()).getLeft()) - this.getWidth()/2;
                if (lp.leftMargin > 0 && lp.leftMargin < ((FrameLayout)this.getParent().getParent()).getWidth() - this.getWidth()) {
                    setLayoutParams(lp);

                }
            }
        }
        return result;
    }

    private static class SlidingAnimationListener implements AnimationListener {

        private SlidingButton but;

        public SlidingAnimationListener(SlidingButton button) {
            this.but = button;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            rePosition();
            but.setMe(false);
            but.clearAnimation();
        }

        private void rePosition() {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) but
                    .getLayoutParams();
            lp.leftMargin = 0;
            but.setLayoutParams(lp);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }

}