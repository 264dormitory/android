package com.jacklee.clatclatter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Random;

public class Model_1 extends Activity {
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private int rightcount=0;//答对次数

    private ProgressBar progressBar;
    private ImageView imageView;
    private int recLen = 0;
    Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_1);

        imageView=(ImageView)findViewById(R.id.problem_image);
        imageView.setImageBitmap(createBitmap());
        cardView1 = (CardView)findViewById(R.id.cardView1);
        cardView2 = (CardView)findViewById(R.id.cardView2);
        cardView3 = (CardView)findViewById(R.id.cardView3);

        cardView1.setRadius(20);//设置图片圆角的半径大小
        cardView1.setCardElevation(8);//设置阴影部分大小
        cardView1.setContentPadding(5,5,5,5);//设置图片距离阴影大小

        cardView2.setRadius(20);//设置图片圆角的半径大小
        cardView2.setCardElevation(8);//设置阴影部分大小
        cardView2.setContentPadding(5,5,5,5);//设置图片距离阴影大小

        cardView3.setRadius(20);//设置图片圆角的半径大小
        cardView3.setCardElevation(8);//设置阴影部分大小
        cardView3.setContentPadding(5,5,5,5);//设置图片距离阴影大小


        progressBar=(ProgressBar)findViewById(R.id.progress1);

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(real_result==display_result){
                    Toast.makeText(Model_1.this,"回答正确", Toast.LENGTH_SHORT).show();
                  rightcount++;
                  if (rightcount>4){

                      message = handler.obtainMessage(2);
                      handler.sendMessage(message);// send message
                  }

                }else {
                    Toast.makeText(Model_1.this,"回答错误", Toast.LENGTH_SHORT).show();

                }
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(real_result==display_result){
                    Toast.makeText(Model_1.this,"回答错误", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(Model_1.this,"回答正确", Toast.LENGTH_SHORT).show();
                    rightcount++;
                    if (rightcount>4){

                        message = handler.obtainMessage(2);
                        handler.sendMessage(message);// send message
                    }
                }
            }
        });
         message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 100);

    }

    private static final int[] CHARS = {0,1,2,3,4,5,6,7,8,9};
    private static final char[] SIGN={'+','-'};

    //default settings
    //验证码默认随机数的个数
    private static final int DEFAULT_CODE_LENGTH = 4;
    //默认字体大小
    private static final int DEFAULT_FONT_SIZE = 100;
    //验证码的默认宽高
    private static final int DEFAULT_WIDTH = 300, DEFAULT_HEIGHT = 200;

    //strings decided by the layout xml
    //canvas width and height
    private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;

    //number of chars, lines; font size
    private int codeLength = DEFAULT_CODE_LENGTH,  font_size = DEFAULT_FONT_SIZE;
    private int  padding_left=20;
    private int Results[]=new int[]{};
    private int left_op;
    private int right_op;
    private int real_result;
    private int display_result;
    private char sign;

    //ariables
    private String code;
    private int  padding_top;
    private Random random = new Random();
    //验证码图片
    public Bitmap createBitmap() {
        padding_left = 0;

        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        code = createCode();

        c.drawColor(Color.parseColor("#9400d3"));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(font_size);
        //画验证码
        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            padding_left=DEFAULT_WIDTH/code.length()*i;
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }

        c.save( Canvas.ALL_SAVE_FLAG );//保存
        c.restore();//
        return bp;
    }


    //生成验证码
    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        left_op=CHARS[random.nextInt(CHARS.length)];
        right_op=CHARS[random.nextInt(CHARS.length)];
        sign=SIGN[random.nextInt(SIGN.length)];
        real_result=getRealResult(left_op,right_op,sign);
        Results=getresults(real_result);
        display_result=Results[random.nextInt(Results.length)];
        buffer.append(left_op);
        buffer.append(sign);
        buffer.append(right_op);
        buffer.append('=');
        buffer.append(display_result);

        return buffer.toString();
    }
    //计算Real_result
    private int getRealResult(int left,int right,char sign){
        int result=0;
        switch (sign){
            case '+':
                result = left +right;
                break;
            case '-':
                result = left - right;
                break;
            default:
                break;

        }
        return result;
    }
    //生成随机颜色
    private int randomColor() {
        return Color.WHITE;
    }
    private int[] getresults(int real){
        int wrong=0 ;
        while (wrong==real){wrong=CHARS[random.nextInt(CHARS.length)];}
        int a[]=new int[]{real,wrong};
        Log.i("tag","real_"+real+"false"+wrong);
        return a;
    }

    /* private int randomColor(int rate) {
         int red = random.nextInt(256) / rate;
         int green = random.nextInt(256) / rate;
         int blue = random.nextInt(256) / rate;
         return Color.rgb(red, green, blue);
     }*/
    //随机生成文字样式，颜色，粗细，倾斜度
    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(true);  //true为粗体，false为非粗体

    }
    //随机生成padding值
    private void randomPadding() {
        padding_top =130 ;
    }
    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    recLen++;
                    progressBar.setProgress((int) (100-recLen/(double)30*100));
                    if(recLen <30){
                         message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 100);// send message

                    }else{
                    imageView.setImageBitmap(createBitmap());
                    progressBar.setProgress(100);
                    recLen=0;
                    message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message, 100);// send message
                }
                    break;
                case 2:
                    Toast.makeText(Model_1.this,"音乐停", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                    playVoice.stopVoice();

                    break;
            }

            super.handleMessage(msg);
        }
    };

    private MediaPlayer mediaPlayer;
    final PlayVoice playVoice = new PlayVoice(0);
}
