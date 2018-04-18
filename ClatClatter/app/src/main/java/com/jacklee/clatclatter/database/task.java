package com.jacklee.clatclatter.database;

/**
 * 李世杰创建task类，对应相应的数据库中task表的映射
 */

public class task {

    private int id;  //任务id

    private String title;  //任务名称

    private String mark;  //任务备注

    private char focus = 0;  //专注模式是否开启

    private char is_repeat = 0;  //重复是否开启

    private char sleep_pattern = 0;  //防睡模式是否开启

    private String repeat_pattern;  //重复模式

    private String priority;  //优先级

    private int sleep_pattern_kind;  //防睡模式

    private String start_time;  //任务开始时间

    private String end_time;  //任务结束时间

    private String remind_time;  //任务提醒时间

    private String task_date;  //任务日期

    //设置getter和setter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMark() {
        return mark;
    }

    public char getFocus() {
        return focus;
    }

    public char getIs_repeat() {
        return is_repeat;
    }

    public char getSleep_pattern() {
        return sleep_pattern;
    }

    public String getRepeat_pattern() {
        return repeat_pattern;
    }

    public String getPriority() {
        return priority;
    }

    public int getSleep_pattern_kind() {
        return sleep_pattern_kind;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getRemind_time() {
        return remind_time;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setFocus(char focus) {
        this.focus = focus;
    }

    public void setIs_repeat(char is_repeat) {
        this.is_repeat = is_repeat;
    }

    public void setSleep_pattern(char sleep_pattern) {
        this.sleep_pattern = sleep_pattern;
    }

    public void setRepeat_pattern(String repeat_pattern) {
        this.repeat_pattern = repeat_pattern;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setSleep_pattern_kind(int sleep_pattern_kind) {
        this.sleep_pattern_kind = sleep_pattern_kind;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setRemind_time(String remind_time) {
        this.remind_time = remind_time;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }
}
