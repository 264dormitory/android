package com.jacklee.clatclatter;

import android.app.TabActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.Toast;

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

        calendarView.shouldAnimateOnEnter(false)//true和false是选择是否从底下弹出的
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
            // todo        calendarView.setOnMultipleDaySelectedListener(this::onMultipleDaySelected);
        }

        calendarView.update(Calendar.getInstance(Locale.getDefault()));
    }

}
