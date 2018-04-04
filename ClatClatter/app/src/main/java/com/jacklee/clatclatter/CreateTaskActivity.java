package com.jacklee.clatclatter;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 创建任务是的Activity
 * Created by liming on 18-4-4.
 */

public class CreateTaskActivity extends AppCompatActivity {

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
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel);
            //finish();
        }
    }
    //在只有一个左上角的大返回按钮的时候，如果点击按钮，想处理具体的事件，需要这样写。

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.floatbutton_tick, menu);//引入菜单的xml文件
        return true;
    }
    //为改tick定义点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.tick://点击对勾的事件
                Toast.makeText(CreateTaskActivity.this, "You click the tick", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case android.R.id.home://点击叉号的事件
                this.finish();
                break;
            default:
        }
        return true;
    }
}
