package com.jacklee.clatclatter;

import android.app.Activity;
import android.app.TabActivity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
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
        //利用tabhost创建tab标签栏
        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.date_time, tabHost.getTabContentView(),true);
        LayoutInflater.from(this).inflate(R.layout.date_time2, tabHost.getTabContentView(),true);
        tabHost.addTab(tabHost.newTabSpec("TAB1").
                setIndicator("开始").setContent(R.id.date_start));
        tabHost.addTab(tabHost.newTabSpec("TAB2").
                setIndicator("结束").setContent(R.id.date_end));

        //calendarView init
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);

        calendarView.shouldAnimateOnEnter(false)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setOnDateClickListener(new CalendarView.OnDateClickListener(){
                    @Override
                    public void onDateClick(@NonNull Date date) {
                        Toast.makeText(DateTime.this,"你的生日是"+ date,Toast.LENGTH_SHORT).show();

                    }
                });
        /*calendarView.shouldAnimateOnEnter(true)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setOnDateClickListener(new CalendarView.OnDateClickListener())
                .setOnMonthChangeListener(this::onMonthChange)
                .setOnDateLongClickListener(this::onDateLongClick)
                .setOnMonthTitleClickListener(this::onMonthTitleClick);*/

        if (calendarView.isMultiSelectDayEnabled()) {
            //todo        calendarView.setOnMultipleDaySelectedListener(this::onMultipleDaySelected);
        }

        calendarView.update(Calendar.getInstance(Locale.getDefault()));

        TimePicker timePicker=findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                DateTime.this.hour=i;
                DateTime.this.minute=i1;
                showDate(year,month,day,hour,minute);
            }
        });
    }
    private void showDate(int year,int month,int day,int hour,int minute){
        Toast.makeText(DateTime.this,"你的生日是"+ hour+"小时"+minute,Toast.LENGTH_SHORT).show();
    }


}
