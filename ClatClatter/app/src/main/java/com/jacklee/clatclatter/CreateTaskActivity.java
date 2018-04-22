package com.jacklee.clatclatter;

import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.TimePickerView;
import com.jacklee.clatclatter.database.task;
import com.jacklee.clatclatter.service.CreateTaskService;
import com.jacklee.clatclatter.service.NotificationService;
import com.jacklee.clatclatter.swipe.MainFragement;
import com.rey.material.app.BottomSheetDialog;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.util.Date;
import java.util.List;

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
    private String title_p,mark_p,start_time_p,end_time_p,task_date_p;//用于定义页面初始的状态
    private PackageManager mPackageManager;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;

    private static final String TAG = CreateTaskActivity.class.getSimpleName();

    private ServiceConnection serviceConnection;

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

        /*获取传过来的task任务名*/
        //从Intent当中根据key取得value
        Intent intent_task = getIntent();//获得从task数据

        String name = intent_task.getStringExtra("taskname");
        if (name != null) {
            //从数据库根据名字取出数据
            Log.i(TAG, "获取到task");
            List<task> tasks = DataSupport.where("title = ?", name).find(task.class);
            task task = tasks.get(0);
            title_p=task.getTitle();mark_p=task.getMark();start_time_p=task.getStart_time();end_time_p=task.getEnd_time();task_date_p=task.getTask_date();
            EditText editText_title=(EditText)findViewById(R.id.create_task_edit_text);
            editText_title.setText(title_p);
            EditText editText_mark=(EditText)findViewById(R.id.create_task_mark);
            editText_mark.setText(mark_p);
            Log.i(TAG, title_p);

            Log.i(TAG, "初始化自定义控件功能");
            remindSwitch.setChecked(MainFragement.remindToBoolean(task.getRemind_time()));
            remindSwitch.setText(timeToRemindStr(task.getRemind_time(), task.getStart_time()));

            repeatSwitch.setChecked(MainFragement.intTOBoolean(task.getIs_repeat()));
            repeatSwitch.setText(task.getRepeat_pattern());

            focusModeSwitch.setChecked(MainFragement.intTOBoolean(task.getFocus()));
            if (task.getFocus() == 1)
                focusModeSwitch.setText("开启");

        }else
            Log.i(TAG, "未获取到task");

        /*获取传过来的task任务名结束*/
        /*从下方弹出日历*/
        Log.i(TAG, "日历开始");
        tvTime = (TextView) findViewById(R.id.tvTime);
        timeInit();//设置时间选择器函数
        if(task_date_p!=null){
            Log.i(TAG, "task_date_p不为空");
            tvTime.setText(task_date_p);
        }
        else {
            tvTime.setText(time);
            Log.i(TAG, "task_date_p为空");
        }

        pvTime.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                tvTime.setText(getTime(date));
                task_date=getTime(date);
            }
        });
        //弹出时间选择器
        final LinearLayout linearLayout=(LinearLayout) findViewById(R.id.date);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
                pvTime.show();
            }
        });
        /*日历结束*/
        /*从下方弹出开始时间*/
        Log.i(TAG, "时间开始");
        showtime = (TextView) findViewById(R.id.show_time);
        EditText note=(EditText) findViewById(R.id.create_task_mark);
//        note.setFocusable(false);
//        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
        timeInit_time(R.id.show_time);//设置时间选择器函数
        if(start_time_p!=null){
            Log.i(TAG, "start_time_p不为空");
            showtime.setText(start_time_p);
        }
        else {
            showtime.setText(time);
            Log.i(TAG, "start_time_p为空");
        }

        pvtime_time.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                Log.i(TAG, "timepicker弹出选择后");
                showtime.setText(getTime_time(date));
                start_time=getTime_time(date);
            }
        });
        //弹出时间选择器
        final LinearLayout linearLayout_time=(LinearLayout) findViewById(R.id.time_start);
        linearLayout_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linearLayout_time.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
                pvtime_time.show();
            }
        });
        /*开始时间结束*/
        /*从下方弹出结束时间*/
        Log.i(TAG, "时间结束");
        showtime2 = (TextView) findViewById(R.id.show_time2);
        timeInit_time2(R.id.show_time2);//设置时间选择器函数
        if(end_time_p!=null){
            Log.i(TAG, "end_time_p不为空");
            showtime2.setText(end_time_p);
        }
        else {
            showtime2.setText(time);
            Log.i(TAG, "end_time_p为空");
        }
        pvtime_time2.setOnTimeSelectListener(new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                Log.i(TAG, "时间结束");
                showtime2.setText(getTime_time2(date));
                end_time=getTime_time2(date);
            }
        });
        //弹出时间选择器
        final LinearLayout linearLayout_time2=(LinearLayout) findViewById(R.id.time_end);
        linearLayout_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linearLayout_time2.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
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

    private String timeToRemindStr(String startTime, String endTime) {
        Log.i(TAG, "拼接出正确的时间格式");
        startTime = "0000-00-00 " + startTime + ":00";
        endTime   = "0000-00-00 " +endTime + ":00";

        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        int minutes = 0;
        try {
            Log.i(TAG, "java Date 算出时间差");
            long from   = sdf.parse(startTime).getTime();
            long to     = sdf.parse(endTime).getTime();
            minutes     = (int) ((to - from)/(1000 * 60));
        } catch (Exception e ) {
            e.printStackTrace();
        }

        Log.i(TAG, "返回正确的提醒时间字符串");
        switch (minutes) {
            case 0:
                return "正点";
            case 5:
                return "5分钟前";
            case 10:
                return "10分钟前";
            case 30:
                return "30分钟前";
        }

        return "";
    }

    /**
     * 注册控件的监听事件
     */
    private void addListener() {
        Log.i(TAG, "注册特色闹钟的监听事件");
        specialAlarmSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                if (specialAlarmSwitch.isChecked()) {
                    Toast.makeText(CreateTaskActivity.this, "选择了alarm_clock", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateTaskActivity.this, Alarm_Clock.class);
                    startActivityForResult(intent, 1);
                }
                else
                    specialAlarmSwitch.setText("");
            }
        });

        Log.i(TAG, "注册专注模式的监听事件");
        focusModeSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
//                if ( mDevicePolicyManager.isDeviceOwnerApp(
//                        getApplicationContext().getPackageName())) {
//                    Intent lockIntent = new Intent(getApplicationContext(),
//                            LockedActivity.class);
//
//                    mPackageManager.setComponentEnabledSetting(
//                            new ComponentName(getApplicationContext(),
//                                    LockedActivity.class),
//                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                            PackageManager.DONT_KILL_APP);
//                    startActivity(lockIntent);
//                    finish();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            R.string.not_lock_whitelisted,Toast.LENGTH_SHORT)
//                            .show();
//
//                }

                Toast.makeText(getApplicationContext(),
                            R.string.remind_auxiliary_function,Toast.LENGTH_SHORT)
                            .show();

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
                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(repeatSwitch.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
            }
        });

        Log.i(TAG, "注册提醒的监听事件");
        remindSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                Log.i(TAG, "调用弹窗");
                if (remindSwitch.isChecked()) {
                    if(!isNotificationEnabled(CreateTaskActivity.this)) {
                        Log.i(TAG, "引导用户开启提醒权限");
                        Toast.makeText(getApplicationContext(),
                                R.string.remind_message,Toast.LENGTH_SHORT)
                                .show();
                        toSettingNotification();
                    }

                    showDialog(getResources().getStringArray(R.array.remind), remindSwitch);
                }
                else
                    remindSwitch.setText("");

                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(remindSwitch.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
            }
        });
    }

    /**
     * 判断用户是否给此应用开启的提醒权限
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 引导用户开启提醒权限
     */
    private void toSettingNotification() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
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
        editTextMark = (EditText) findViewById(R.id.create_task_mark);//内容
        editText = (EditText) findViewById(R.id.create_task_edit_text);//标题
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
                setResult(RESULT_OK);
                this.finish();
                break;
            case android.R.id.home:
                Log.i(TAG, "点击叉号的事件");
                this.finish();
                setResult(RESULT_CANCELED);
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
        task task = new task();

        Log.i(TAG, "获取保存数据");
        task.setTitle(editText.getText().toString());
        task.setMark(editTextMark.getText().toString());
        task.setFocus((char) focus);
        task.setIs_repeat((char) is_repeat);
        task.setSleep_pattern((char) sleep_pattern);
        task.setRepeat_pattern(repeatSwitch.getText());
        task.setPriority(priority);
        task.setSleep_pattern_kind(sleep_pattern_kind);
        task.setStart_time(start_time);
        task.setEnd_time(end_time);
        task.setRemind_time(getRmindTime());
        task.setTask_date(task_date);

        Log.i(TAG, "保存数据");
        task.save();

        if (focus == 1) {
            Log.i(TAG, "开启专注模式并设置定时任务");
            this.setFocusTask();
        }

        if (!getRmindTime().equals("00:00")) {
            Log.i(TAG, "设置定时提醒服务");
            this.setRmindTask();
        }
    }

    private void setRmindTask() {
        Log.i(TAG, "正确拼接出第一次提醒的时间");
        String taskRemindTime = task_date + " " + getRmindTime() + ":00";

        Log.i(TAG, "设置定时任务");
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra("remindTime", taskRemindTime);
        intent.putExtra("secondRemindTime", getRmindTime());
        startService(intent);
    }

    private void setFocusTask() {
        Log.i(TAG, "正确拼接出第一次的时间");
        String taskTime = task_date + " " + start_time + ":00";
        String endTaskTime = task_date + " " + end_time + ":00";

        Log.i(TAG, "在开始时刻启动服务");
        Intent intent = new Intent(this, CreateTaskService.class);
        intent.putExtra("startTime", taskTime);
        intent.putExtra("endTime", endTaskTime);
        intent.putExtra("strCycle", repeatSwitch.getText());
        startService(intent);

    }



    /**
     * 获取提醒时间
     * @return
     */
    private String getRmindTime() {
        switch (remindSwitch.getText()) {
            case "":
                return "00:00";
            case "正点":
                return start_time;
            case "5分钟前":
                return translateTime(5);
            case "10分钟前":
                return translateTime(10);
            case "30分钟前":
                return translateTime(30);
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
                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tvTime.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
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
                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
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
    //特色闹钟

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String returnedData=data.getStringExtra("data_return");
                    Log.i("tag",returnedData);
                    specialAlarmSwitch.setText(returnedData);

                }
        }
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
                /*取消软键盘*/
                Log.i(TAG, "取消软键盘");
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                try {
                    Thread.sleep(200); // 休眠1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*取消软键盘结束*/
                pvtime_time2.show();
            }
        });
    }
    /*选择结束时间结束*/
    /*弹出软键盘函数*/
    public static void showKeyboard(View view){
        InputMethodManager inputMethodManager=(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager!=null){
            view.requestFocus();
            inputMethodManager.showSoftInput(view,0);
        }
    }
    /*弹出软键盘函数结束*/
    /**
     * 关闭软键盘
     *
     * @param
     * @param
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        Log.i(TAG, "成功调用关闭函数");
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
