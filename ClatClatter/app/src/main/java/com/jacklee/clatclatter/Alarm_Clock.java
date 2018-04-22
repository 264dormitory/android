package com.jacklee.clatclatter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Alarm_Clock extends AppCompatActivity {
    private TimePicker timePicker;
    private Switch mSwitch;
    AlarmManager alarmManager = null;
    Calendar calendar = Calendar.getInstance();
    int song;
    int indexof;
    boolean isopen;
    boolean ismodel;
    TextView songname_tv;
    TextView model_tv;
    private int hour;
    private int minute;
    String songPath;
    boolean local_select=false;//最终选择本地
    boolean system_select=false;//最终选择系统
    //将是否开启防睡模式存起来
    SharedPreferences.Editor isModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_home_page);
        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        isModel=getSharedPreferences("isModel",MODE_PRIVATE).edit();

        timePicker=(TimePicker) findViewById(R.id.timePicker);
        mSwitch=(Switch)findViewById(R.id.model_switch);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour=i;
                minute=i1;
            }
        });
        songname_tv=(TextView)findViewById(R.id.song_name);
        model_tv=(TextView)findViewById(R.id.model_text);
        //preferences存储歌名
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        songname_tv.setText(preferences.getString("song_name",""));
        //model_preferences存储歌名
        SharedPreferences model_preferences = getSharedPreferences("model_data",MODE_PRIVATE);
        model_tv.setText(model_preferences.getString("model_name",""));
        //Toolbar Setting
        Toolbar yyy_toolbar = (Toolbar) findViewById(R.id.yyy_toolbar);
        yyy_toolbar.setTitle("特色闹钟");
        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(yyy_toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel);
        }


        //防睡模式是否开启的开关
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isopen = true;

                }else{
                    isopen = false;

                }
            }
        });
        SharedPreferences pref =getSharedPreferences("isModel",MODE_PRIVATE);
        ismodel = pref.getBoolean("isModel",false);
        if(ismodel){
            mSwitch.setChecked(true);
        }else {
            mSwitch.setChecked(false);
        }
    }

    /**
     * 跳转至模式选择界面，并接收传来的数据
     * @param view
     */
    public void sleep_set(View view){
        Intent intent = new Intent(Alarm_Clock.this,Model_Select.class);
        startActivityForResult(intent,2);
    }

    /**
     * 跳转至铃声选择界面，并接收传来的数据
     * @param view
     */
   public void song_select(View view){
        Intent intent = new Intent(Alarm_Clock.this,Song_Select.class);
        startActivityForResult(intent,1);
    }

    /**
     * 判断传来的数据来自于哪个活动，并采取相应的措施处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           // requestCode是1的情况是铃声选择的数据
           case 1:
               if(resultCode==RESULT_OK){
                   String returnedData=data.getStringExtra("data_return");
                   songPath=data.getStringExtra("songPath");//获取选取的本地音乐的路径
                   song = data.getIntExtra("song",0);//获取系统音乐的位置
                  songname_tv.setText(returnedData);
                  local_select=data.getBooleanExtra("localselect",false);
                  system_select=data.getBooleanExtra("systemselect",false);
                   SharedPreferences.Editor editor= getSharedPreferences("data",MODE_PRIVATE).edit();
                   editor.putString("song_name",returnedData);//将歌名存起来
                   editor.apply();
               }
               break;
           // requestCode是2的情况是模式选择的数据
           case 2:
               if(resultCode==RESULT_OK){
                   String returnedData1=data.getStringExtra("model_return");
                   model_tv.setText(returnedData1);
                   SharedPreferences.Editor editor1=getSharedPreferences("model_data",MODE_PRIVATE).edit();
                   editor1.putString("model_name",returnedData1);
                  editor1.apply();
               }
               break;
               default:
                   break;
       }
    }

    //  进行Toolbar的初始化操作
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.floatbutton_tick, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.tick:
                Toast.makeText(this, "You click the tick", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                if(hour<10){  //显示时间格式设置 小时小于10在前面补0，分钟小于10在前面补0
                    if(minute<10){
                        intent.putExtra("data_return","0"+hour+":0"+minute);//返回给上一层数据
                    }else{
                        intent.putExtra("data_return","0"+hour+":"+minute);//返回给上一层数据
                    }
                }else{
                    if(minute<10){
                        intent.putExtra("data_return",hour+":0"+minute);//返回给上一层数据
                    }else{
                        intent.putExtra("data_return",hour+":"+minute);//返回给上一层数据
                    }
                }

                Calendar c=Calendar.getInstance();//获取日期对象
                c.setTimeInMillis(System.currentTimeMillis());        //设置Calendar对象
                c.set(Calendar.HOUR_OF_DAY, hour);        //设置闹钟小时数
                c.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
                c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                c.set(Calendar.MILLISECOND, 0);//设置闹钟的毫秒数
                Intent intent1 = new Intent("com.jacklee.clatclatter.alarm.clock");
                Log.i("tag-intent1",song+"");

                intent1.putExtra("indexof",indexof);
                intent1.putExtra("isopen",isopen);
                if (system_select&&!local_select){
                    intent1.putExtra("song",song);
                }else if(!system_select&&local_select){
                    intent1.putExtra("songPath",songPath);
                }


                //创建Intent对象
                // intent.setFlags(Integer.parseInt(id));//作为取消时候的标识
                PendingIntent pi = PendingIntent.getBroadcast(Alarm_Clock.this, 0,
                        intent1, PendingIntent.FLAG_UPDATE_CURRENT);    //创建PendingIntent

                //设置一次性闹钟，第一个参数表示闹钟类型，第二个参数表示闹钟执行时间，第三个参数表示闹钟响应动作。
                if(c.getTimeInMillis() < System.currentTimeMillis()){
                    Log.i("clock", "设置时间要推迟24小时,不然立刻会响");
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+24*60*60*1000, pi);
                }else{
                   alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟，当前时间就唤醒
                }
                setResult(RESULT_OK,intent);
                isModel.putBoolean("isModel",isopen);
                isModel.apply();
                this.finish();
                break;
            case android.R.id.home:
                Toast.makeText(this, "You click the wrong", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
        }
        return true;
    }


}
