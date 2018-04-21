package com.jacklee.clatclatter;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.*;

import io.blackbox_vision.materialcalendarview.view.CalendarView;

/**
 * Created by 韩瑞峰 on 2018/4/11.
 */

public class DateTime2 extends TabActivity {
    private int year,month,day,hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /*利用tabhost创建tab标签栏*/
        final TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.date_time2, tabHost.getTabContentView(),true);
        /*tab栏创建完毕*/
        /*calendarView 日历栏函数*/
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.shouldAnimateOnEnter(false)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setOnDateClickListener(new CalendarView.OnDateClickListener(){
                    @Override
                    public void onDateClick(@NonNull java.util.Date date) {
                        Toast.makeText(DateTime2.this,"你的生日是"+ date,Toast.LENGTH_SHORT).show();
                    }
                });
        if (calendarView.isMultiSelectDayEnabled()) {
            //todo        calendarView.setOnMultipleDaySelectedListener(this::onMultipleDaySelected);
        }
        calendarView.update(Calendar.getInstance(Locale.getDefault()));
        /*日历栏结束*/
        /*timepicker函数*/
        TimePicker timePicker=findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                DateTime2.this.hour=i;
                DateTime2.this.minute=i1;
                showDate(year,month,day,hour,minute);
            }
        });
        /*timepicker函数结束*/
        /*sure函数和cancel函数开始*/
        //sure按钮函数
        TextView textView_sure = findViewById(R.id.sure);
        textView_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DateTime2.this,"sure_end",Toast.LENGTH_SHORT).show();
            }
        });
        //cacel按钮函数
        TextView textView_cancel = findViewById(R.id.cancel);
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(DateTime2.this,"cancel_end",Toast.LENGTH_SHORT).show();
            }
        });
        /*sure和cancel函数结束*/
    }
    /*oncreate函数结束*/
    private void showDate(int year,int month,int day,int hour,int minute){
        Toast.makeText(DateTime2.this,"你的生日是"+ hour+"小时"+minute,Toast.LENGTH_SHORT).show();
    }


}
