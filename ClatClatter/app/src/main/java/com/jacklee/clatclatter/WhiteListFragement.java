package com.jacklee.clatclatter;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jacklee.clatclatter.App;
import com.jacklee.clatclatter.AppAdapter;
import com.jacklee.clatclatter.BaseFragment.BaseFragment;
import com.jacklee.clatclatter.DividerItemDecoration;
import com.jacklee.clatclatter.R;
import com.jacklee.clatclatter.database.app_backlist;
import com.jacklee.clatclatter.swipe.OnStartDragListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 李世杰创建应用白名单的fragment页面
 */

public class WhiteListFragement extends BaseFragment {

    private final String TAG = WhiteListFragement.class.getSimpleName();

    RecyclerView mRecyclerView = null;

    List<App> appList = null;

    AppAdapter adapter = null;

    public String appNames;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_whitelist, null);

        //initApps();
        mRecyclerView = view.findViewById(R.id.list_view);
        appList = getApp();

        updateUI(appList);

        return view;
    }

    public void updateUI(List<App> appList)
    {
        if(null != appList)
        {
            adapter = new AppAdapter(getActivity().getApplication(), appList);
            //解决滑动卡顿的问题
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL_LIST));
            AppAdapter adpter=new AppAdapter(appList);
            mRecyclerView.setAdapter(adpter);
        }

    }
    // 获取包名信息
    public List<App> getApp(){
        PackageManager pm = getActivity().getApplication().getPackageManager();
        List<PackageInfo>  packgeInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        appList = new ArrayList<App>();
        /* 获取应用程序的名称，不是包名，而是清单文件中的labelname
            String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.setAppName(str_name);
         */
        for(PackageInfo packgeInfo : packgeInfos){
            appNames = packgeInfo.applicationInfo.loadLabel(pm).toString();
            Drawable drawable = packgeInfo.applicationInfo.loadIcon(pm);
            App appInfo = new App(appNames,drawable, packgeInfo.applicationInfo.packageName);
            List<app_backlist> app_backlists = DataSupport.where("name = ?", packgeInfo.packageName).find(app_backlist.class);
            if (app_backlists.size() != 0)
                appInfo.setEnabled(true);
            appList.add(appInfo);
        }
        return appList;
    }
}
