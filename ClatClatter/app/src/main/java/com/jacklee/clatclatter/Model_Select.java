package com.jacklee.clatclatter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model_Select extends AppCompatActivity {

    boolean choose=false; //是否选中
    int lastchoose=0;//上一次选的位置
   private CardView cardView;
    private static final String TAG = CreateTaskActivity.class.getSimpleName();
    Intent intent = new Intent();
    final ArrayList mItemlist = new ArrayList <> ();
    private ImageView imageView=null;//用于定义点击后显示对勾图片
    private  int index;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_selection);

        //Toolbar Setting
        Toolbar yyy_toolbar = (Toolbar) findViewById(R.id.yyy_toolbar);
        yyy_toolbar.setTitle("模式选择");
        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(yyy_toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left);
        }

        final GridView gridview = (GridView) findViewById(R.id.GrilView);

        imageView=(ImageView) findViewById(R.id.mImageView1);

     //   final ArrayList mItemlist = new ArrayList <> ();

        // 往list放HashMap数据,每个HashMap里有一个ImageView,TextView
        initModels();
       // SharedPreferences model_preferences=getSharedPreferences("model_data",MODE_PRIVATE);
        SimpleAdapter mAdaper = new SimpleAdapter(this, mItemlist,
                R.layout.model_item, new String[]{"mImageView", "mTextView"},
                new int[]{R.id.mImageView, R.id.mTextView});

        gridview.setAdapter(mAdaper);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index=i;
                intent.putExtra("model_name",index);
                Log.i(TAG, "点击grideView");
                switch (i){
                    case 0:
                        if(choose==false||i!=lastchoose){
                        cardView=(CardView)gridview.getChildAt(0);
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.clickchange));
                        choose=true;}
                        Log.i(TAG, "点击加减大师");
                        intent.putExtra("model_return","加减大师");
                        setResult(RESULT_OK,intent);
                        SharedPreferences.Editor editor = getSharedPreferences("model_data",MODE_PRIVATE).edit();
                        editor.putInt("indexof",index);
                        editor.apply();
                        break;
                    case 1:
                        intent.putExtra("model_return","摇一摇");
                        setResult(RESULT_OK,intent);
                        SharedPreferences.Editor editor2 = getSharedPreferences("model_data",MODE_PRIVATE).edit();
                        editor2.putInt("indexof",index);
                        editor2.apply();

                        break;
                    case 2:
                        intent.putExtra("model_return","点击50次");
                        setResult(RESULT_OK,intent);
                        SharedPreferences.Editor editor3 = getSharedPreferences("model_data",MODE_PRIVATE).edit();
                        editor3.putInt("indexof",index);
                        editor3.apply();

                        break;
                    case 3:
                        intent.putExtra("model_return","成语猜猜看");
                        setResult(RESULT_OK,intent);
                        SharedPreferences.Editor editor4 = getSharedPreferences("model_data",MODE_PRIVATE).edit();
                        editor4.putInt("indexof",index);
                        editor4.apply();

                        break;
                        default: intent.putExtra("model_return","加减大师");
                            break;
                }
                }

        });
    }


    public void initModels(){
        HashMap map1=new HashMap();
        map1.put("mImageView", R.drawable.ic_task);
        map1.put("mTextView","加减大师");

        HashMap map2=new HashMap();
        map2.put("mImageView",R.drawable.ic_task);
        map2.put("mTextView","摇一摇");
        HashMap map3=new HashMap();
        map3.put("mImageView",R.drawable.ic_task);
        map3.put("mTextView","点击50次");
        HashMap map4=new HashMap();
        map4.put("mImageView",R.drawable.ic_task);
        map4.put("mTextView","成语猜猜看");
        mItemlist.add(map1);
        mItemlist.add(map2);
        mItemlist.add(map3);
        mItemlist.add(map4);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this, "You click the wrong", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK,intent);
                SharedPreferences.Editor editor = getSharedPreferences("model_data",MODE_PRIVATE).edit();
                editor.putInt("indexof",index);
                editor.apply();
                this.finish();
                break;
            default:
        }
        return true;
    }

}
