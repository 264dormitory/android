package com.jacklee.clatclatter;

/**
 * 李世杰创建于2018.04.05
 * 用于初始化任务展示界面的适配器
 * Created by user on 2018/4/5.
 */

public class TaskItem {

    private boolean isChecked;  //单选框是否已选

    private String taskName;  //任务名称

    private String taskDate;  //任务日期

    private String taskStartTime;  //任务时间

    private String taskEndTime;  //任务结束时间

    private boolean taskAlarm;  //闹钟

    private boolean taskFocus;  //专注模式

    private boolean taskAlert;  //任务提醒

    private boolean taskCycle;  //周期性任务

    private int taskPirority;  //任务优先级 1:高 2:中 3:低

    public TaskItem(){}

    public TaskItem(boolean isChecked, String taskName, String taskDate, String taskStartTime, String taskEndTime
                    ,boolean taskAlarm, boolean taskFocus, boolean taskAlert, boolean taskCycle, int taskPirority)
    {
        this.isChecked = isChecked;
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskAlarm = taskAlarm;
        this.taskFocus = taskFocus;
        this.taskAlarm = taskAlarm;
        this.taskAlert = taskAlert;
        this.taskCycle = taskCycle;
        this.taskPirority = taskPirority;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public boolean isTaskAlarm() {
        return taskAlarm;
    }

    public boolean isTaskFocus() {
        return taskFocus;
    }

    public boolean isTaskAlert() {
        return taskAlert;
    }

    public boolean isTaskCycle() {
        return taskCycle;
    }

    public int getTaskPirority() {
        return taskPirority;
    }
}
