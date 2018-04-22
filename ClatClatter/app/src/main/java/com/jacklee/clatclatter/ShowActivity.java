package com.jacklee.clatclatter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by liming on 18-4-18.
 */

public class ShowActivity extends Activity {
    private final String TAG = ShowActivity.class.getSimpleName();
    private ImageView imageView;

    @Override
    public void onCreate(Bundle bundles) {
        super.onCreate(bundles);
        setContentView(R.layout.activity_show);

        imageView = findViewById(R.id.show_image);
        setImageView(getRandomBetweenZeroAndSix());
    }

    private void setImageView(int ID) {
        switch (ID) {
            case 0:
                imageView.setImageResource(R.drawable.lock_bg_0);
                break;
            case 1:
                imageView.setImageResource(R.drawable.lock_bg_1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.lock_bg_2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.lock_bg_3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.lock_bg_5);
                break;
            case 5:
                imageView.setImageResource(R.drawable.lock_bg_6);
                break;
            case 6:
                imageView.setImageResource(R.drawable.lock_bg_7);
                break;
                default:
                    imageView.setImageResource(R.drawable.lock_bg_0);
        }
    }

    private int getRandomBetweenZeroAndSix() {
        int Min = 0;

        int Max = 6;

        int result = Min + (int)(Math.random() * ((Max - Min) + 1));

        return result;
    }
}
