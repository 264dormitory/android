package com.jacklee.clatclatter;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;import io.blackbox_vision.materialcalendarview.view.CalendarView;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private io.blackbox_vision.materialcalendarview.view.CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar Setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.main_nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //NavigationView Setting
        navView.setCheckedItem(R.id.main_nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_calendar:
                        Toast.makeText(MainActivity.this, "You clicked calendar", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_today:
                        Toast.makeText(MainActivity.this, "You location today", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_cycle:
                        Toast.makeText(MainActivity.this, "You clicked cycle", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_whiteList:
                        Toast.makeText(MainActivity.this, "You clicked whiteList", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //FloatActionButton Setting
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
                        .setAction("undo", new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .show();
            }
        });

        //calendarView init
        calendarView = (CalendarView) findViewById(R.id.calendar_view);

        calendarView.shouldAnimateOnEnter(true)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setOnDateClickListener(new CalendarView.OnDateClickListener(){
                    @Override
                    public void onDateClick(@NonNull Date date) {
                        Toast.makeText(MainActivity.this, "Clicked " + date.getTime(), Toast.LENGTH_SHORT).show();
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

    //  进行Toolbar的初始化操作
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.toToday:
                Toast.makeText(MainActivity.this, "You click toToday", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

}
