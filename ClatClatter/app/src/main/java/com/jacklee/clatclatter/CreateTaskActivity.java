package com.jacklee.clatclatter;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.rey.material.app.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.blackbox_vision.materialcalendarview.view.DayView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                time = getTime(date);
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
            }
        });

        Log.i(TAG, "注册重复的监听事件");
        repeatSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                if (repeatSwitch.isChecked())
                    repeatSwitch.setText("开启");
                else
                    repeatSwitch.setText("");
            }
        });

        Log.i(TAG, "注册提醒的监听事件");
        remindSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                Log.i(TAG, "调用弹窗");
                if (remindSwitch.isChecked())
                    CreateTaskActivity.this.showDialog();
                else
                    remindSwitch.setText("");
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        specialAlarmSwitch = (RowSwitchView) findViewById(R.id.special_alarm);
        focusModeSwitch    = (RowSwitchView) findViewById(R.id.focus_mode);
        remindSwitch       = (RowSwitchView) findViewById(R.id.remind);
        repeatSwitch       = (RowSwitchView) findViewById(R.id.repeat);

        //初始化权限内容
        mDevicePolicyManager = (DevicePolicyManager)
                getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminComponentName = DeviceAdminReceiver.getComponentName(this);
        mPackageManager = this.getPackageManager();
    }

    /**
     * 显示弹窗
     * @author gaoliming
     */
    public void showDialog() {
        Log.i(TAG, "初始化页面及List控件");
        final BottomSheetDialog dialog = new BottomSheetDialog(CreateTaskActivity.this);
        final String[] array           = new String[]{"不提醒", "正点", "五分钟前", "10分钟前"};
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
                remindSwitch.setText(array[i]);
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

    //为改tick定义点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tick://点击对勾的事件
                Toast.makeText(CreateTaskActivity.this, "You click the tick", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case android.R.id.home://点击叉号的事件
                Toast.makeText(CreateTaskActivity.this, "You click the wrong", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
        }
        return true;
    }//tick定义点击事件结束

    //    ImageButton warn_button;
    public void warn_button(View view) {
        final String items[] = {"高优先级", "中优先级", "低优先级"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //设置对话框标题
                .setTitle("优先级")
                //设置单选列表项
                .setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CreateTaskActivity.this, items[i], Toast.LENGTH_SHORT).show();

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
        pvTime.setCancelable(true);
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
        pvtime_time = new TimePickerView(this, Type.HOURS_MINS);
        pvtime_time.setTime(new java.util.Date());
        //  pvTime.setCyclic(false);
        pvtime_time.setCancelable(true);
        //显示当前时间
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        time=hour+":"+minute;
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
        //  pvTime.setCyclic(false);
        pvtime_time2.setCancelable(true);
        //显示当前时间
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        time=hour+":"+minute;
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

}
