package com.jacklee.clatclatter;

import android.app.Activity;
import android.app.TabActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.blackbox_vision.materialcalendarview.internal.data.Day;
import io.blackbox_vision.materialcalendarview.view.CalendarView;

/**
 * Created by 韩瑞峰 on 2018/4/7.
 */

public class DateTime extends TabActivity {
    private int year,month,day,hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*利用tabhost创建tab标签栏*/
        final TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.date_time, tabHost.getTabContentView(),true);
        LayoutInflater.from(this).inflate(R.layout.date_time2, tabHost.getTabContentView(),true);
        //创建开始tab栏
        tabHost.addTab(tabHost.newTabSpec("TAB1").
                setIndicator("开始").setContent(R.id.date_start));
        //创建结束tab栏
        tabHost.addTab(tabHost.newTabSpec("TAB2").
                setIndicator("结束").setContent(R.id.date_end));
        tabHost.setCurrentTab(0);//置初始的tab为第一个tab栏
        /*tab栏创建完毕*/
        /*calendarView 日历栏函数*/
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.shouldAnimateOnEnter(false)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setOnDateClickListener(new CalendarView.OnDateClickListener(){
                    @Override
                    public void onDateClick(@NonNull Date date) {
                        Toast.makeText(DateTime.this,"你的生日是"+ date,Toast.LENGTH_SHORT).show();
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
                DateTime.this.hour=i;
                DateTime.this.minute=i1;
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
                tabHost.setCurrentTab(1);//跳转到第二个tab栏
                Toast.makeText(DateTime.this,"sure",Toast.LENGTH_SHORT).show();
            }
        });
        //cacel按钮函数
        TextView textView_cancel = findViewById(R.id.cancel);
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(DateTime.this,"cancel",Toast.LENGTH_SHORT).show();
            }
        });
        /*sure和cancel函数结束*/
    }
    /*oncreate函数结束*/
    private void showDate(int year,int month,int day,int hour,int minute){
        Toast.makeText(DateTime.this,"你的生日是"+ hour+"小时"+minute,Toast.LENGTH_SHORT).show();
    }


}
