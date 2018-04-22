package com.jacklee.clatclatter.swipe;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jacklee.clatclatter.BaseFragment.BaseFragment;
import com.jacklee.clatclatter.CreateTaskActivity;
import com.jacklee.clatclatter.MainActivity;
import com.jacklee.clatclatter.R;
import com.jacklee.clatclatter.TaskItem;
import com.jacklee.clatclatter.TaskItemAdapter;
import com.jacklee.clatclatter.database.task;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.blackbox_vision.materialcalendarview.view.CalendarView;

/**
 * 李世杰创建首页的Fragement
 */

public class MainFragement extends BaseFragment implements OnStartDragListener {


    private final static String TAG = "MainActivity";

    private List<TaskItem> taskList = new ArrayList<>();

    private TaskItemAdapter adapter;

//    private SwipeRefreshLayout swipeRefreshLayout;

    private ItemTouchHelper touchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, null);
        //初始化任务展示页面
        if(null ==  taskList || taskList.size() == 0)
            taskDisplayInit(view);
        return view;
    }

    //进行任务展示页面的初始化
    public void taskDisplayInit(View view){
        taskInit();
        adapter = new TaskItemAdapter(taskList, this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.main_task_view);
        //解决滑动卡顿的问题
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
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
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_refresh);
//        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshTask();
//            }
//        });
    }

    //任务内容初始化（测试使用）
    public void taskInit(){
        Random randomTnt = new Random();
        Log.i(TAG, "默认取出今天的任务");
        //todo 应该取出的任务包括周期性任务
        String d=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        List<task> tasks = DataSupport.where("task_date = ?", d).find(task.class);
//        List<task> tasks = DataSupport.findAll(task.class);

        for (task t: tasks) {
            TaskItem task = new TaskItem(false, t.getTitle(), t.getTask_date()
                    ,t.getStart_time(), t.getEnd_time(), true, intTOBoolean(t.getFocus()), remindToBoolean(t.getRemind_time())
                    , intTOBoolean(t.getIs_repeat()), randomTnt.nextInt());
            taskList.add(task);
        }

    }

    public static boolean intTOBoolean(int i) {
        if (i == 0)
            return false;

        return true;
    }

    public static boolean remindToBoolean(String i) {
        if (i == null)
            return false;

        return true;
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);
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
