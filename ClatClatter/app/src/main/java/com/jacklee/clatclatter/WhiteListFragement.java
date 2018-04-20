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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacklee.clatclatter.App;
import com.jacklee.clatclatter.AppAdapter;
import com.jacklee.clatclatter.DividerItemDecoration;
import com.jacklee.clatclatter.R;
import com.jacklee.clatclatter.swipe.OnStartDragListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/4/18.
 */

public class WhiteListFragement extends Fragment {

    RecyclerView mRecyclerView = null;

    List<App> appList = null;

    AppAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_whitelist, null);

        //initApps();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        appList = getApp();
        updateUI(appList);
        return view;
    }

    public void updateUI(List<App> appList)
    {
        if(null != appList)
        {
            adapter = new AppAdapter(getActivity().getApplication(), appList);
            //mRecyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
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
            String appName = packgeInfo.applicationInfo.loadLabel(pm).toString();
            Drawable drawable = packgeInfo.applicationInfo.loadIcon(pm);
            App appInfo = new App(appName,drawable);
            appList.add(appInfo);
        }
        return appList;
    }
}
