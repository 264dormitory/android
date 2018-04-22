package com.jacklee.clatclatter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacklee.clatclatter.BaseFragment.BaseFragment;
import com.jacklee.clatclatter.database.task;
import com.jacklee.clatclatter.swipe.MainFragement;
import com.jacklee.clatclatter.swipe.OnStartDragListener;
import com.jacklee.clatclatter.swipe.SimpleItemTouchHelperCallback;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * 李世杰创建
 * 今日任务页面的Fragement
 */

public class TodayTaskFragement extends BaseFragment implements OnStartDragListener {

    private final static String TAG = "MainActivity";

    private List<TaskItem> taskList = new ArrayList<>();

    private TaskItemAdapter adapter;

//    private SwipeRefreshLayout swipeRefreshLayout;

    private ItemTouchHelper touchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_task_content, null);
        //初始化任务展示页面
        if(null ==  taskList || taskList.size() == 0)
            taskDisplayInit(view);
        return view;
    }

    //进行任务展示页面的初始化
    public void taskDisplayInit(View view){
        taskInit();
        adapter = new TaskItemAdapter(taskList, this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.today_task_view);
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
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.today_swipe_refresh);
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
                    ,t.getStart_time(), t.getEnd_time(), true, MainFragement.intTOBoolean(t.getFocus()), MainFragement.remindToBoolean(t.getRemind_time())
                    , MainFragement.intTOBoolean(t.getIs_repeat()), randomTnt.nextInt());
            taskList.add(task);
        }

    }

    //  进行任务列表的操作
    public void refreshTask(){
        this.taskInit();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try{
//                    Thread.sleep(2000);
//                }
//                catch(InterruptedException e){
//                    e.printStackTrace();
//                }
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
