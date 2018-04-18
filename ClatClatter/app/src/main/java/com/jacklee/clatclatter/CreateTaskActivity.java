package com.jacklee.clatclatter;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rey.material.app.BottomSheetDialog;

/**
 * 创建任务是的Activity
 * Created by liming on 18-4-4.
 */

public class CreateTaskActivity extends AppCompatActivity {
    private RowSwitchView specialAlarmSwitch;       //特色闹钟switch
    private RowSwitchView focusModeSwitch;          //专注模式switch
    private RowSwitchView repeatSwitch;             //重复switch
    private RowSwitchView remindSwitch;             //提醒switch

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
    }

    /**
     * 注册控件的监听事件
     */
    private void addListener() {
        Log.i(TAG, "注册特色闹钟的监听事件");
        specialAlarmSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
              //  specialAlarmSwitch.setText("开启");
            }
        });

        Log.i(TAG, "注册专注模式的监听事件");
        focusModeSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                focusModeSwitch.setText("开启");
            }
        });

        Log.i(TAG, "注册重复的监听事件");
        repeatSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                repeatSwitch.setText("开启");
            }
        });

        Log.i(TAG, "注册提醒的监听事件");
        remindSwitch.setOnClickListener(new RowSwitchView.switchClickListener() {
            @Override
            public void switchListener() {
                Log.i(TAG, "调用弹窗");
                CreateTaskActivity.this.showDialog();
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

    //日期选择按钮
    public void date_select(View view) {
        Toast.makeText(CreateTaskActivity.this, "选择了date_select", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateTaskActivity.this, DateTime.class);
        startActivity(intent);
    }
    //特色闹钟
    public void alarm_clock(View view) {
        Toast.makeText(CreateTaskActivity.this, "选择了alarm_clock", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateTaskActivity.this, Alarm_Clock.class);
        startActivityForResult(intent,1);
    }

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
}
