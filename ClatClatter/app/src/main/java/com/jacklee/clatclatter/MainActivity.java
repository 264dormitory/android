package com.jacklee.clatclatter;

import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import io.blackbox_vision.materialcalendarview.view.CalendarView;

import com.jacklee.clatclatter.database.app_backlist;
import com.jacklee.clatclatter.swipe.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity{

    private final String TAG = MainActivity.class.getSimpleName();

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private AppBarLayout appBarLayout;

    private int title = R.string.app_name;  //用于设定toolbar的tittle

    private io.blackbox_vision.materialcalendarview.view.CalendarView calendarView;

    private NavigationView navView;  //抽屉栏的View

    private DrawerLayout mDrawerLayout;

    private Toolbar toolbar;  //主页的toobar

    private FloatingActionButton floatingActionButton;  //主页的浮动按钮

    private FrameLayout content;

    private MainFragement mainFragement;  //首页任务展示的Fragement

    private TodayTaskFragement todayTaskFragement;  //今日任务的Fragement

    private PeriodicTask periodicTask;  //周期性任务的Fragment

    private WhiteListFragement whiteListFragement;  //周期性任务的Fragment

    private CircleImageView image;  //个人中心的入口

    private NavigationView navigationView;

    private CoordinatorLayout coordinatorLayout;

    private boolean isToToday;  //用于设置是否显示toolbar的回到今日

    private boolean isDaily;

    private boolean isWeekly;

    private boolean isMonthly;

    private boolean isAnnual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //实现应用的闪屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_main);
        content = (FrameLayout) findViewById(R.id.content);

        //Toolbar setting
        toolbarInit();
        //用于修改appbar的样式
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);
        View view = View.inflate(MainActivity.this, R.layout.main_toolbar, null);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.main_app_bar);
        Log.d("159", appBarLayout + "");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_toolbar_layout);

        navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        image = (CircleImageView) headerView.findViewById(R.id.icon_image);  //获取个人头像对象
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonalPageMain.class);
                startActivity(intent);
            }
        });

        //NavigationView Setting
        navInit();
        //calendarView init
        calendarInit();
        //FloatActionButton Setting
        floatButtonInit();

        switchToMain();
    }

    //进行Toolbar初始化
    public void toolbarInit(){
        //Toolbar Setting
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navView = (NavigationView) findViewById(R.id.main_nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        Log.i(TAG, "对toolbar的标题设置的点击事件");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "刷新数据");
                String title = (String) item.getTitle();
                periodicTask.refreshTask(title);
                toolbar.setTitle(title);
                return false;
            }
        });
    }

    //进行toolbar按钮的动态设置
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.e("456", "onPrepareOptionsMenu: ");
        if(isToToday){
            menu.findItem(R.id.toToday).setVisible(true);
        } else {
            menu.findItem(R.id.toToday).setVisible(false);
        }

        if(isDaily){
            menu.findItem(R.id.daily).setVisible(true);
        }
        else{
            menu.findItem(R.id.daily).setVisible(false);
        }

        if(isWeekly){
            menu.findItem(R.id.weekly).setVisible(true);
        }
        else{
            menu.findItem(R.id.weekly).setVisible(false);
        }

        if(isMonthly){
            menu.findItem(R.id.monthly).setVisible(true);
        }
        else{
            menu.findItem(R.id.monthly).setVisible(false);
        }

        if(isAnnual){
            menu.findItem(R.id.annual).setVisible(true);
        }
        else{
            menu.findItem(R.id.annual).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    //  进行Toolbar的Button事件设置
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    //自定义一个用于动态隐藏已有fragment页面的方法
    public void hideAppointFragment(String tag){
        //传入一个tag用于隐藏除tag之外的所有fragment
        switch (tag){
            case "mainFragment":
                if(todayTaskFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("todayTaskTag")).commit();
                }
                if(periodicTask != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("periodicTaskTag")).commit();
                }
                if(whiteListFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("whiteListFragmentTag")).commit();
                }
                break;
            case "todayTaskTag":
                if(mainFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("mainFragmentTag")).commit();
                }
                if(periodicTask != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("periodicTaskTag")).commit();
                }
                if(whiteListFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("whiteListFragmentTag")).commit();
                }
                break;
            case "periodicTaskTag":
                if(mainFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("mainFragmentTag")).commit();
                }
                if(todayTaskFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("todayTaskTag")).commit();
                }
                if(whiteListFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("whiteListFragmentTag")).commit();
                }
                break;
            case "whiteListFragmentTag":
                if(mainFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("mainFragmentTag")).commit();
                }
                if(todayTaskFragement != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("todayTaskTag")).commit();
                }
                if(periodicTask != null){
                    getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag("periodicTaskTag")).commit();
                }
                break;
            default:
                break;
        }
    }

    //日历页面的跳转
    public void switchToMain(){
        isToToday = true;
        isDaily = false;
        isWeekly = false;
        isMonthly = false;
        isAnnual = false;
        if(mainFragement == null){
            Log.d("123", "switchToMain: 1");
            mainFragement = new MainFragement();
            getFragmentManager().beginTransaction().add(R.id.frame_content, mainFragement, "mainFragmentTag").commit();
            hideAppointFragment("mainFragmentTag");
        }
        else{
            Log.d("123", "switchToMain: 2");;
            getFragmentManager().beginTransaction().show(mainFragement).commit();
            hideAppointFragment("mainFragmentTag");
        }
        invalidateOptionsMenu();  //用于更新menu
        collapsingToolbarLayout.setTitle("ClatClat");
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        floatingActionButton.setVisibility(View.VISIBLE);
        appBarLayout.setFitsSystemWindows(true);
        collapsingToolbarLayout.setFitsSystemWindows(true);
        content.setFitsSystemWindows(true);
        content.setVisibility(View.VISIBLE);
    }

    //今日任务的跳转
    public void switchToTodayTaskFragement(){
        isToToday = false;
        isDaily = false;
        isWeekly = false;
        isMonthly = false;
        isAnnual = false;
        if(todayTaskFragement == null){
            Log.d("123", "switchToTodayTaskFragement: 1");
            todayTaskFragement = new TodayTaskFragement();
            hideAppointFragment("todayTaskTag");
            getFragmentManager().beginTransaction().add(R.id.frame_content, todayTaskFragement, "todayTaskTag").commit();
        }
        else{
            Log.d("123", "switchToTodayTaskFragement: 2");
            hideAppointFragment("todayTaskTag");
            getFragmentManager().beginTransaction().show(todayTaskFragement).commit();
        }
        invalidateOptionsMenu();  //用于更新menu
        floatingActionButton.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        toolbar.setTitle(R.string.today_task);
        collapsingToolbarLayout.setTitleEnabled(false);

    }
    //周期性任务的跳转
    public void switchToPeriodicTaskFragment(){
        isToToday = false;
        isDaily = true;
        isWeekly = true;
        isMonthly = true;
        isAnnual = true;
        if(periodicTask == null){
            Log.d("123", "switchToTodayTaskFragement: 1");
            periodicTask = new PeriodicTask();
            hideAppointFragment("periodicTaskTag");
            getFragmentManager().beginTransaction().add(R.id.frame_content, periodicTask, "periodicTaskTag").commit();
        }
        else{
            Log.d("123", "switchToTodayTaskFragement: 2");
            hideAppointFragment("periodicTaskTag");
            getFragmentManager().beginTransaction().show(periodicTask).commit();
        }
        invalidateOptionsMenu();  //用于更新menu
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle("每天");
        floatingActionButton.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }
    //应用白名单的跳转
    public void switchToWhiteListFragment(){
        isToToday = false;
        isDaily = false;
        isWeekly = false;
        isMonthly = false;
        isAnnual = false;
        if(whiteListFragement == null){
            Log.d("123", "switchToWhiteListFragment: 1");
            whiteListFragement = new WhiteListFragement();
            hideAppointFragment("whiteListFragmentTag");
            getFragmentManager().beginTransaction().add(R.id.frame_content, whiteListFragement, "whiteListFragmentTag").commit();
        }
        else{
            Log.d("123", "switchToWhiteListFragment: 2" + getFragmentManager().findFragmentById(R.id.frame_content));
            hideAppointFragment("whiteListFragmentTag");
            getFragmentManager().beginTransaction().show(whiteListFragement).commit();
        }
        invalidateOptionsMenu();  //用于更新menu
        toolbar.setTitle(R.string.app_whitelist_name);
        collapsingToolbarLayout.setTitleEnabled(false);
        floatingActionButton.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        collapsingToolbarLayout.setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        content.setFitsSystemWindows(false);
    }

    //进行抽屉栏的初始化
    public void navInit(){
        navView.setCheckedItem(R.id.main_nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_calendar:
                        switchToMain();
                        break;
                    case R.id.nav_today:
                        switchToTodayTaskFragement();
                        break;
                    case R.id.nav_cycle:
                        switchToPeriodicTaskFragment();
                        break;
                    case R.id.nav_whiteList:
                        switchToWhiteListFragment();
                        break;
                    default:
                }
                item.setChecked(true);
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

    //进行浮动按钮的初始化
    public void floatButtonInit(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivity(intent);
            }
        });
    }
}
