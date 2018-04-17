package com.jacklee.clatclatter;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.TimePickerView;
import com.rey.material.app.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;

import io.blackbox_vision.materialcalendarview.view.DayView;

import java.util.*;
import java.util.Date;

import static com.bigkoo.pickerview.TimePickerView.*;


/**
 * 创建任务是的Activity
 * Created by liming on 18-4-4.
 */

public class CreateTaskActivity extends AppCompatActivity {
    private TimePickerView pvTime,pvtime_time,pvtime_time2;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String time;
    private TextView tvTime,showtime,showtime2;
    private RowSwitchView specialAlarmSwitch;       //特色闹钟switch
    private RowSwitchView focusModeSwitch;          //专注模式switch
    private RowSwitchView repeatSwitch;             //重复switch
    private RowSwitchView remindSwitch;             //提醒switch

    private PackageManager mPackageManager;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;

    private static final String TAG = CreateTaskActivity.class.getSimpleName();

    private EditText editTextMark;
    private EditText editText;
    private int focus;
    private int is_repeat;
    private int sleep_pattern = 1;      //没有获取真正的值
    private String priority;
    private int sleep_pattern_kind = 1; //默认就是第一种防睡模式
    private String start_time;
    private String end_time;
    private String remind_time;
    private String task_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SwitchStyle);
        setContentView(R.layout.create_task);

        //Toolbar Setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(toolbar);//actionbar的位置替换成toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel);
        }
        Log.i(TAG, "初始化页面控件");
        initView();
        Log.i(TAG, "注册监听事件");
        addListener();
        // Check to see if started by LockActivity and disable LockActivity if so
        Intent intent = getIntent();

        if(intent.getIntExtra(LockedActivity.LOCK_ACTIVITY_KEY,0) ==
                LockedActivity.FROM_LOCK_ACTIVITY){
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                    mAdminComponentName,getPackageName());
            mPackageManager.setComponentEnabledSetting(
                    new ComponentName(getApplicationContext(), LockedActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }


        /*从下方弹出日历*/
        Log.i(TAG, "日历开始");
        tvTime = (TextView) findViewById(R.id.tvTime);
        timeInit();//设置时间选择器函数
        tvTime.setText(time);
        pvTime.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                tvTime.setText(getTime(date));
                task_date=getTime(date);
            }
        });
        //弹出时间选择器
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.date);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
        /*日历结束*/
        /*从下方弹出开始时间*/
        Log.i(TAG, "时间开始");
        showtime = (TextView) findViewById(R.id.show_time);
        timeInit_time(R.id.show_time);//设置时间选择器函数
        showtime.setText(time);//初始时间
        pvtime_time.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                showtime.setText(getTime_time(date));
                start_time=getTime_time(date);
            }
        });
        //弹出时间选择器
        LinearLayout linearLayout_time=(LinearLayout) findViewById(R.id.time_start);
        linearLayout_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvtime_time.show();
            }
        });
        /*开始时间结束*/
        /*从下方弹出结束时间*/
        Log.i(TAG, "时间结束");
        showtime2 = (TextView) findViewById(R.id.show_time2);
        timeInit_time2(R.id.show_time2);//设置时间选择器函数
        showtime2.setText(time);//初始时间
        pvtime_time2.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                Log.i(TAG, "时间结束");
                showtime2.setText(getTime_time2(date));
                end_time=getTime_time2(date);
            }
        });
        //弹出时间选择器
        LinearLayout linearLayout_time2=(LinearLayout) findViewById(R.id.time_end);
        linearLayout_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvtime_time2.show();
            }
        });
        /*结束时间结束*/
        /*使得内容框获得聚焦*/
        LinearLayout focus=(LinearLayout) findViewById(R.id.focus);
        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "聚焦内容框");
                EditText note=(EditText) findViewById(R.id.create_task_mark);
                note.setFocusable(true);
                note.setFocusableInTouchMode(true);
                note.requestFocus();
                showKeyboard(note);//弹出软键盘

            }
        });
        /*内容框聚焦结束*/
    }
    /*oncreate结束*/

    /**
     * 注册控件的监听事件
     */
    private void addListener() {
        Log.i(TAG, "注册特色闹钟的监听事件");
        specialAlarmSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                if (specialAlarmSwitch.isChecked())
                    specialAlarmSwitch.setText("开启");
                else
                    specialAlarmSwitch.setText("");
            }
        });

        Log.i(TAG, "注册专注模式的监听事件");
        focusModeSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                if ( mDevicePolicyManager.isDeviceOwnerApp(
                        getApplicationContext().getPackageName())) {
                    Intent lockIntent = new Intent(getApplicationContext(),
                            LockedActivity.class);

                    mPackageManager.setComponentEnabledSetting(
                            new ComponentName(getApplicationContext(),
                                    LockedActivity.class),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    startActivity(lockIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.not_lock_whitelisted,Toast.LENGTH_SHORT)
                            .show();
                }

                if (focusModeSwitch.isChecked()) {
                    Log.i(TAG, "专注模式开启");
                    CreateTaskActivity.this.focus = 1;
                    focusModeSwitch.setText("开启");

                } else {
                    Log.i(TAG, "专注模式关闭");
                    CreateTaskActivity.this.focus = 0;
                    focusModeSwitch.setText("");
                }

            }
        });

        Log.i(TAG, "注册重复的监听事件");
        repeatSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                if (repeatSwitch.isChecked()) {
                    showDialog(getResources().getStringArray(R.array.repeat), repeatSwitch);
                    Log.i(TAG, "是否重复开启");
                    is_repeat = 1;
                }
                else {
                    Log.i(TAG, "是否重复关闭");
                    is_repeat = 0;
                    repeatSwitch.setText("");
                }
            }
        });

        Log.i(TAG, "注册提醒的监听事件");
        remindSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                Log.i(TAG, "调用弹窗");
                if (remindSwitch.isChecked())
                    showDialog(getResources().getStringArray(R.array.remind), remindSwitch);
                else
                    remindSwitch.setText("");
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        specialAlarmSwitch  = (RowSwitchView) findViewById(R.id.special_alarm);
        focusModeSwitch     = (RowSwitchView) findViewById(R.id.focus_mode);
        remindSwitch        = (RowSwitchView) findViewById(R.id.remind);
        repeatSwitch        = (RowSwitchView) findViewById(R.id.repeat);

        Log.i(TAG, "初始化权限内容");
        mDevicePolicyManager    = (DevicePolicyManager)
                getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminComponentName     = DeviceAdminReceiver.getComponentName(this);
        mPackageManager          = this.getPackageManager();

        Log.i(TAG, "初始化控件");
        editTextMark = (EditText) findViewById(R.id.create_task_mark);
        editText = (EditText) findViewById(R.id.create_task_edit_text);
    }

    /**
     * 显示弹窗
     * @author gaoliming
     */
    private void showDialog(String[] arr, final RowSwitchView object) {
        Log.i(TAG, "初始化页面及List控件");
        final BottomSheetDialog dialog = new BottomSheetDialog(CreateTaskActivity.this);
        final String[] array           = arr;
        View dialogView                = LayoutInflater.from(CreateTaskActivity.this)
                                                        .inflate(R.layout.pop_remind, null);
        ListView listView              = dialogView.findViewById(R.id.listview);
        ArrayAdapter adapter           = new ArrayAdapter(CreateTaskActivity.this,
                                                                    android.R.layout.simple_list_item_1,
                                                                    array);
        listView.setAdapter(adapter);

        Log.i(TAG, "为控件注册监听事件");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                object.setText(array[i]);
                Log.i(TAG, "弹窗消失");
                dialog.dismiss();
            }
        });

        Log.i(TAG, "显示弹窗");
        dialog.setContentView(dialogView);
        dialog.show();
    }


    /*完成人——韩瑞峰*/

    //在只有一个左上角的大返回按钮的时候，如果点击按钮，想处理具体的事件，需要这样写。
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.floatbutton_tick, menu);//引入菜单的xml文件
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "为改tick定义点击事件");
        switch (item.getItemId()) {
            case R.id.tick:
                Log.i(TAG, "点击对勾的事件");
                this.saveTask();
                this.finish();
                break;
            case android.R.id.home:
                Log.i(TAG, "点击叉号的事件");
                this.finish();
                break;
            default:
        }
        return true;
    }

    /**
     * 保存创建的任务
     */
    private void saveTask() {
        Log.i(TAG, "初始化数据库对象");
        DBManager db = new DBManager(this, "task.db", null, 3);


        Log.i(TAG, "获取保存数据");
        ContentValues task = new ContentValues();
        task.put("title", editText.getText().toString());
        task.put("mark", editTextMark.getText().toString());
        task.put("focus", focus);
        task.put("is_repeat", is_repeat);
        task.put("sleep_pattern", sleep_pattern);
        task.put("repeat_pattern", repeatSwitch.getText());
        task.put("priority", priority);
        task.put("sleep_pattern_kind", sleep_pattern_kind);
        task.put("start_time", start_time);
        task.put("end_time", end_time);
        task.put("remind_time", getRmindTime());
        task.put("task_date", task_date);

        Log.i(TAG, "保存数据");
        db.getReadableDatabase().insert("task", null, task);

        Log.i(TAG, "查询数据");

        Log.i(TAG, "关闭数据库对象");
        db.close();
    }

    /**
     * 获取提醒时间
     * @return
     */
    private String getRmindTime() {
        switch (remindSwitch.getText()) {
            case "不提醒":
                return "00:00";
            case "正点":
                return start_time;
            case "五分钟前":
                return translateTime(5);
            case "十分钟前":
                return translateTime(10);
                default:
                    return start_time;
        }
    }

    private String translateTime(long time) {
        String strBefore = start_time;
        Log.i(TAG, "将分钟转化为秒");
        time        = time * 60 * 1000;
        String temp = "0000-00-00 " + start_time + ":00";        //拼凑正确的格式
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date   = sdf.parse(temp);
            Date before = new Date(date.getTime() - time);
            strBefore   = before.getHours() + ":" + before.getMinutes();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strBefore;
    }

    //    ImageButton warn_button;
    public void warn_button(View view) {
        Log.i(TAG, "初始化优先级数组");
        final String items[] = getResources().getStringArray(R.array.priority) ;

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //设置对话框标题
                .setTitle("优先级")
                //设置单选列表项
                .setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Toast.makeText(CreateTaskActivity.this, items[i], Toast.LENGTH_SHORT).show();
                        priority = items[i];
                    }
                });
        //添加确定和取消按钮
        setPositiveButton(builder);
        setNegativeButton(builder);
        builder.create();
        builder.show();
    }//ImageButton warn_button;点击事件结束
    //确定按钮点击事件
    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        //调用setPositiveButton方法添加确定按钮
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CreateTaskActivity.this, "确定", Toast.LENGTH_SHORT).show();
            }
        });
    }//确定按钮点击事件结束

    //取消按钮点击事件
    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        //调用该方法，添加取消按钮
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CreateTaskActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });
    }//取消按钮点击事件结束
    /*选择日期*/
    //获得时间函数
    public String getTime(java.util.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    private void timeInit() {
        tvTime = (TextView) findViewById(R.id.tvTime);
        //时间选择器
        pvTime = new TimePickerView(this, Type.YEAR_MONTH_DAY);
        pvTime.setTime(new java.util.Date());
        //  pvTime.setCyclic(false);
        pvTime.setCancelable(false);
        pvTime.setTitle("日期");
        //显示当前时间
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        if (month > 9) {
            if (day < 10) {
                time = year + "-" + (month + 1) + "-" + "0" + day;
            } else {
                time = year + "-" + (month + 1) + "-" + day;
            }
        } else {
            if (day < 10) {
                time = year + "-" + "0" + (month + 1) + "-" + "0" + day;
            } else {
                time = year + "-" + "0" + (month + 1) + "-" + day;
            }
        }
        tvTime.setText(time);
        pvTime.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                tvTime.setText(getTime(date));
                time = getTime(date);
            }
        });
        //弹出时间选择器
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
    }
    /*选择日期结束*/
    /*选择开始时间*/
    //获得时间函数
    public String getTime_time(java.util.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
    private void timeInit_time(int id) {
        final TextView textView;
        Log.i(TAG, "timeInit_time开始");
        textView = (TextView) findViewById(id);
        //时间选择器
        pvtime_time = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvtime_time.setTime(new java.util.Date());
        pvtime_time.setTitle("开始时间");


        //  pvTime.setCyclic(false);
        pvtime_time.setCancelable(false);
        //显示当前时间
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        if(minute<10){
            time=hour+":"+"0"+minute;
        }
        else{
            time=hour+":"+minute;
        }
        Log.i(TAG, "minute="+minute);
        showtime.setText(time);
        pvtime_time.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                textView.setText(getTime(date));
                time = getTime(date);
            }
        });
        //弹出时间选择器
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvtime_time.show();
            }
        });
    }
    /*选择开始时间结束*/
    /*选择结束时间*/
    //获得时间函数
    public String getTime_time2(java.util.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
    private void timeInit_time2(int id) {
        final TextView textView;
        Log.i(TAG, "timeInit_time开始");
        textView = (TextView) findViewById(id);
        //时间选择器
        pvtime_time2 = new TimePickerView(this, Type.HOURS_MINS);
        pvtime_time2.setTime(new java.util.Date());
        pvtime_time2.setCancelable(false);
        pvtime_time2.setTitle("结束时间");
        //显示当前时间
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        if(minute<10){
            time=hour+":"+"0"+minute;
        }
        else{
            time=hour+":"+minute;
        }
        Log.i(TAG, "minute="+minute);
        showtime.setText(time);
        pvtime_time2.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                textView.setText(getTime(date));
                time = getTime(date);
            }
        });
        //弹出时间选择器
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvtime_time2.show();
            }
        });
    }
    /*选择日期结束*/
    /*弹出软键盘函数*/
    public static void showKeyboard(View view){
        InputMethodManager inputMethodManager=(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager!=null){
            view.requestFocus();
            inputMethodManager.showSoftInput(view,0);
        }
    }
    /*弹出软键盘函数结束*/

}
