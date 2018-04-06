package com.jacklee.clatclatter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jacklee.clatclatter.swipe.*;

import java.util.Collections;
import java.util.List;

/**
 * 李世杰编写于2018.04.05
 * 用于构建任务展示页面的适配器
 * Created by user on 2018/4/5.
 */

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<TaskItem> mTaskList;

    private Context mContext;

    private OnStartDragListener mDragLisener;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View taskView;

        private CheckBox taskCheckbox;

        private TextView taskName;

        private TextView taskDate;

        private ImageView taskAlarm;

        private ImageView taskFocus;

        private ImageView taskAlert;

        private ImageView taskCycle;

        private ImageView taskPirority;

        private ImageView menu;

        public RelativeLayout taskItem;

        public RelativeLayout backGround;

        public ImageView taskDelete;

        public ViewHolder(View view){
            super(view);
            taskView = view;
            taskCheckbox = (CheckBox) view.findViewById(R.id.cb_completed);
            taskName = (TextView) view.findViewById(R.id.task_name);
            taskDate = (TextView) view.findViewById(R.id.task_date);
            taskAlarm = (ImageView) view.findViewById(R.id.task_alarm);
            taskFocus = (ImageView) view.findViewById(R.id.task_focus);
            taskAlert = (ImageView) view.findViewById(R.id.task_bell);
            taskCycle = (ImageView) view.findViewById(R.id.task_cycle);
            taskPirority = (ImageView) view.findViewById(R.id.task_priority);
            menu = (ImageView) view.findViewById(R.id.task_menu);
            taskItem = (RelativeLayout) view.findViewById(R.id.task_item);
            backGround = view.findViewById(R.id.task_background);
            taskDelete = view.findViewById(R.id.task_delete);
        }
    }

    public TaskItemAdapter(List<TaskItem> taskList,  OnStartDragListener mDragListener){
        this.mTaskList = taskList;
        this.mDragLisener = mDragListener;
    }

    //对滚入屏幕的列表内容进行初始化
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_task_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.taskView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                TaskItem taskItem = mTaskList.get(position);
                Toast.makeText(mContext, "You clicked task" + taskItem.getTaskName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.taskCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                TaskItem task = mTaskList.get(position);
                if (task.isChecked()){
                    task.setChecked(false);
                    onBindViewHolder(holder, position);  //刷新数据
                }

                else{
                    task.setChecked(true);
                    onBindViewHolder(holder, position);
                }

            }
        });
        return holder;
    }

    //进行数据绑定
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TaskItem task = mTaskList.get(position);
        if(task.isChecked()){
            holder.taskCheckbox.setChecked(true);
            holder.taskName.setTextColor(mContext.getResources().getColor(R.color.grey_gone));
            holder.taskDate.setTextColor(mContext.getResources().getColor(R.color.grey_gone));
        }
        else{
            holder.taskCheckbox.setChecked(false);
            holder.taskName.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.taskDate.setTextColor(mContext.getResources().getColor(R.color.teal500));
        }
        holder.taskName.setText(task.getTaskName().toString());
        holder.taskDate.setText(task.getTaskDate() + ", " + task.getTaskStartTime() + "-" + task.getTaskEndTime());
        //设置四个小图标是否显示
        if (task.isTaskAlarm())
            holder.taskAlarm.setVisibility(View.VISIBLE);
        else
            holder.taskAlarm.setVisibility(View.GONE);
        if (task.isTaskFocus())
            holder.taskFocus.setVisibility(View.VISIBLE);
        else
            holder.taskFocus.setVisibility(View.GONE);
        if (task.isTaskAlert())
            holder.taskAlert.setVisibility(View.VISIBLE);
        else
            holder.taskAlert.setVisibility(View.GONE);
        if (task.isTaskCycle())
            holder.taskCycle.setVisibility(View.VISIBLE);
        else
            holder.taskCycle.setVisibility(View.GONE);
        //设置优先级的颜色
        switch (task.getTaskPirority()){
            case 1:
                holder.taskPirority.setImageResource(R.color.red);
                break;
            case 2:
                holder.taskPirority.setImageResource(R.color.blue);
                break;
            case 3:
                holder.taskPirority.setImageResource(R.color.grey_gone);
                break;
            default:
                holder.taskPirority.setImageResource(R.color.grey_gone);
        }
        holder.menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()
                        == MotionEvent.ACTION_DOWN) {
                    mDragLisener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition){
        //交换位置
        Collections.swap(mTaskList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position){
        //移除数据
        mTaskList.remove(position);
        notifyItemRemoved(position);
    }

}
