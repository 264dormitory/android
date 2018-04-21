package com.jacklee.clatclatter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacklee.clatclatter.BaseFragment.BaseFragment;
import com.jacklee.clatclatter.swipe.OnStartDragListener;
import com.jacklee.clatclatter.swipe.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * 李世杰创建
 * 周期性任务页面的Fragement
 */

public class PeriodicTask extends BaseFragment implements OnStartDragListener {

    private final static String TAG = "MainActivity";

    private List<TaskItem> taskList = new ArrayList<>();

    private TaskItemAdapter adapter;

//    private SwipeRefreshLayout swipeRefreshLayout;

    private ItemTouchHelper touchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.periodic_task_content, null);
        //初始化任务展示页面
        if(null ==  taskList || taskList.size() == 0)
            taskDisplayInit(view);
        return view;
    }

    //进行任务展示页面的初始化
    public void taskDisplayInit(View view){
        taskInit();
        adapter = new TaskItemAdapter(taskList, this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.periodic_task_view);
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
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.periodic_swipe_refresh);
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
