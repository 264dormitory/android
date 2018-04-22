package com.jacklee.clatclatter;

import android.icu.util.Calendar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.graphics.Paint;

import com.db.chart.renderer.AxisRenderer;
import com.db.chart.util.Tools;

import com.bumptech.glide.Glide;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.HorizontalBarChartView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalPageMain extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    private CircleImageView personalImage;

    private LineChartView lineChartView;

    private BarChartView barChartView;

    private HorizontalBarChartView horizontalBarChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page_main);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.personal_main_collapsing_toolbar);

        toolbar = (Toolbar) findViewById(R.id.personal_main_detail_toolbar);

        personalImage = (CircleImageView) findViewById(R.id.personal_main_toolbar_user_image);

        lineChartView = (LineChartView) findViewById(R.id.personal_achievement_value);

        barChartView = (BarChartView) findViewById(R.id.personal_weekly_achievement_value);

        horizontalBarChartView = (HorizontalBarChartView) findViewById(R.id.weekly_work_performance);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Glide.with(this).load(R.drawable.nav_icon).into(personalImage);
        collapsingToolbarLayout.setTitle("个人中心");
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));

        //初始化折线图
        lineChartInit();
        //初始化横向柱状堆叠图
        horizonalBarChart();
        //初始化柱状图
        barChartInit();
    }

    //设置返回按钮的功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //对页面的最近七天任务完成情况折线图进行设置
    public void lineChartInit(){
        Calendar calendar = Calendar.getInstance();
        //用于初始化页面的横坐标的数组
        String[] labels = new String[]{
                (calendar.get(Calendar.DAY_OF_MONTH) - 6) + "日",
                (calendar.get(Calendar.DAY_OF_MONTH) - 5) + "日",
                (calendar.get(Calendar.DAY_OF_MONTH) - 4) + "日",
                (calendar.get(Calendar.DAY_OF_MONTH) - 3) + "日",
                (calendar.get(Calendar.DAY_OF_MONTH) - 2) + "日",
                (calendar.get(Calendar.DAY_OF_MONTH) - 1) + "日",
                (calendar.get(Calendar.DAY_OF_MONTH) - 0) + "日",
        };
        //用于初始化页面的纵坐标的数组
        float[] values = new float[]{ 10, 11, 10, 12, 12, 20, 30 };
        LineSet dataset = new LineSet(labels, values);
        //对折线图的小圆点进行设置
        dataset.setDotsColor(getResources().getColor(R.color.indigo));  //颜色
        dataset.setDotsRadius(8);  //半径
        //对线进行设置
        dataset.setThickness(3);  //粗细
        dataset.setColor(getResources().getColor(R.color.indigo));
        //填充颜色
        dataset.setFill(getResources().getColor(R.color.dataFill));
        //设置表格的横向线
        Paint gridPaint = new Paint();
        gridPaint.setColor(getResources().getColor(R.color.grey_gone));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.65f));
        //对表格的XY轴进行设置
        lineChartView.setGrid(6, 0, gridPaint);
        lineChartView.setAxisColor(getResources().getColor(R.color.transparent));
        lineChartView.setAxisBorderValues(0, 30, 5);
        lineChartView.setLabelsColor(getResources().getColor(R.color.grey));
        lineChartView.addData(dataset);
        lineChartView.show();
    }

    //对页面的最近七天任务完成情况折线图进行设置
    public void horizonalBarChart(){
        Calendar calendar = Calendar.getInstance();
        //用于初始化页面的横坐标的数组
        String[] labels = new String[]{ "未完成", "已完成" };
        //用于初始化页面的纵坐标的数组
        float[] values = new float[]{ 7, 22 };
        BarSet dataset = new BarSet();
//        dataset.setColor(getResources().getColor(R.color.colorAccent));
        //对每一个柱进行颜色的绘制
        Bar bar;
        for(int i=0; i<labels.length; i++){
            bar = new Bar(labels[i], values[i]);
            switch (i){
                case 0:
                    bar.setColor(getResources().getColor(R.color.grey_transparent));
                    break;
                case 1:
                    bar.setColor(getResources().getColor(R.color.colorAccent));
                    break;
                default:
                    bar.setColor(getResources().getColor(R.color.grey_transparent));
            }
            dataset.addBar(bar);
        }
        //设置表格的纵向线
        Paint gridPaint = new Paint();
        gridPaint.setColor(getResources().getColor(R.color.grey_gone));
        gridPaint.setStyle(Paint.Style.FILL);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.65f));
        horizontalBarChartView.setGrid(0, 6, gridPaint);
        //对表格的XY轴进行设置
        horizontalBarChartView.setAxisColor(getResources().getColor(R.color.transparent));
        horizontalBarChartView.setBarSpacing(200);
        horizontalBarChartView.setRoundCorners(10);
        horizontalBarChartView.setAxisBorderValues(0, 30, 5);
        horizontalBarChartView.setLabelsColor(getResources().getColor(R.color.grey));
        horizontalBarChartView.addData(dataset);
        horizontalBarChartView.show();
    }

    //最佳工作日
    public void barChartInit(){
        int maxLoc = 0;  //最大值的位置
        Calendar calendar = Calendar.getInstance();
        //用于初始化页面的横坐标的数组
        String[] labels = new String[]{ "日", "一", "二", "三", "四", "五", "六" };
        //用于初始化页面的纵坐标的数组
        float[] values = new float[]{ 10, 11, 10, 12, 12, 20, 30 };
        BarSet dataset = new BarSet();
//        dataset.setColor(getResources().getColor(R.color.indigo));
        //对柱状图的每一个柱的颜色进行动态设置
        for(int i=0; i<labels.length; i++)
            if(values[i] > values[maxLoc])
                maxLoc = i;
        Bar bar;
        for(int i=0; i<labels.length; i++){
            bar = new Bar(labels[i], values[i]);
            if(i == maxLoc)
                bar.setColor(getResources().getColor(R.color.pink));
            else
                bar.setColor(getResources().getColor(R.color.grey_transparent));
            dataset.addBar(bar);
        }
        //对表格的XY轴进行设置
        barChartView.setAxisColor(getResources().getColor(R.color.transparent));
        barChartView.setBarSpacing(100);
        barChartView.setRoundCorners(10);
        barChartView.setAxisBorderValues(0, 30, 5);
        barChartView.setLabelsColor(getResources().getColor(R.color.grey));
        barChartView.addData(dataset);
        barChartView.setYLabels(AxisRenderer.LabelPosition.NONE);
        barChartView.show();
    }
}
