package com.jacklee.clatclatter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import io.blackbox_vision.materialcalendarview.view.CalendarView;
import com.jacklee.clatclatter.swipe.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements OnStartDragListener {

    private NavigationView navView;  //抽屉栏的View

    private DrawerLayout mDrawerLayout;

    private io.blackbox_vision.materialcalendarview.view.CalendarView calendarView;

    private final static String TAG = "MainActivity";

    private List<TaskItem> taskList = new ArrayList<>();

    private TaskItemAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar setting
        toolbarInit();
        //NavigationView Setting
        navInit();
        //FloatActionButton Setting
        floatButtonInit();
        //calendarView init
        calendarInit();
        //初始化任务展示页面
        taskDisplayInit();
    }

    //进行Toolbar初始化
    public void toolbarInit(){
        //Toolbar Setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navView = (NavigationView) findViewById(R.id.main_nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    //  进行Toolbar的Button事件设置
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    //进行抽屉栏的初始化
    public void navInit(){
        navView.setCheckedItem(R.id.main_nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_calendar:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                        Toast.makeText(MainActivity.this, "You clicked calendar", Toast.LENGTH_SHORT).show();
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

    //进行浮动按钮的初始化
    public void floatButtonInit(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i(TAG, "启动CreateTaskActivity");
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivity(intent);

//                Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
//                        .setAction("undo", new View.OnClickListener(){
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        })
//                        .show();
            }
        });
    }

    //进行日历的初始化
    public void calendarInit(){
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

    //进行任务展示页面的初始化
    public void taskDisplayInit(){
        taskInit();
        adapter = new TaskItemAdapter(taskList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_task_view);
        //解决滑动卡顿的问题
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        //实例化callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter, taskList, adapter);
        //使用callback构造ItemTouchHelper
        touchHelper = new ItemTouchHelper(callback);
        //与对应的RecyclerView进行关联
        touchHelper.attachToRecyclerView(recyclerView);

        //用于实现下拉刷新的操作
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTask();
            }
        });
    }

    //任务内容初始化（测试使用）
    public void taskInit(){
        Random randomTnt = new Random();
        Random randomBoolean = new Random();
        Calendar now = Calendar.getInstance();
        for(int i=0; i<20; i++){
            TaskItem task = new TaskItem(randomBoolean.nextBoolean(), "任务名称"+i, (now.get(Calendar.MONTH)+1)+"月"+now.get(Calendar.DAY_OF_MONTH)+"日"
                    ,"9:00", "10:00", randomBoolean.nextBoolean(), randomBoolean.nextBoolean(), randomBoolean.nextBoolean()
                    , randomBoolean.nextBoolean(), randomTnt.nextInt(4));
            taskList.add(task);
        }

    }

    //  进行任务列表的操作
    public void refreshTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
//              将线程且切换到当前的主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //任务展示列表右面图标的功能实现
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        //通知ItemTouchHelper开始拖拽
        touchHelper.startDrag(viewHolder);
    }

}
