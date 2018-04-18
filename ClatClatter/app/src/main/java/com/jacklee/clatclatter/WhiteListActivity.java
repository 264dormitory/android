package com.jacklee.clatclatter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class WhiteListActivity extends AppCompatActivity {
    RecyclerView mRecyclerView = null;
    List<App> appList = null;
    AppAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_whitelist);
        //Toolbar Setting
        Toolbar toolbar = (Toolbar) findViewById(R.id.whitelist_toolbar);
        toolbar.setTitle("应用白名单");

        //这句话用来实现使用toolbar 代替 actionbar
        setSupportActionBar(toolbar);//actionbar的位置替换成toolbar

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //initApps();
        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);

        appList = getApp();
        updateUI(appList);

    }

    public void updateUI(List<App> appList)
    {
        if(null != appList)
        {
            adapter = new AppAdapter(getApplication(), appList);
            //mRecyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));
            AppAdapter adpter=new AppAdapter(appList);
            mRecyclerView.setAdapter(adpter);
        }
    }
    // 获取包名信息
    public List<App> getApp(){
        PackageManager pm = getApplication().getPackageManager();
        List<PackageInfo>  packgeInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        appList = new ArrayList<App>();
        /* 获取应用程序的名称，不是包名，而是清单文件中的labelname
            String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.setAppName(str_name);
         */
        for(PackageInfo packgeInfo : packgeInfos){
            String appName = packgeInfo.applicationInfo.loadLabel(pm).toString();
            Drawable drawable = packgeInfo.applicationInfo.loadIcon(pm);
            App appInfo = new App(appName,drawable);
            appList.add(appInfo);
        }
        return appList;
    }

}