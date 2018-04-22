package com.jacklee.clatclatter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacklee.clatclatter.database.app_backlist;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private List<App> mAppList;
    private final String TAG = AppAdapter.class.getSimpleName();


    Context context;
    //List<AppInfo> appInfos;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View appView;
        ImageView appImage;
        TextView appName;
        TextView appStatus;
        android.support.v7.widget.SwitchCompat msc;

        public ViewHolder(View view) {
            super(view);
            appView = view;
            appImage = view.findViewById(R.id.app_image);
            appName = view.findViewById(R.id.app_name);
            appStatus = view.findViewById(R.id.switch_show_status);
            msc = view.findViewById(R.id.whitelist_switchCompat);
        }
    }

    public AppAdapter(List<App> appList) {
        mAppList = appList;
    }

    public AppAdapter(Context context, List<App> appList) {
        this.context = context;
        this.mAppList = appList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent,
                false);
        final ViewHolder holder = new ViewHolder(view);


        holder.appView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                App app = mAppList.get(position);
                Toast.makeText(v.getContext(), "你点击了view" + app.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.appImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                App app = mAppList.get(position);
                Toast.makeText(v.getContext(), "你点击了图片" + app.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                App app = mAppList.get(position);
                Toast.makeText(v.getContext(), "你点击了text" + app.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.msc.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        int position = holder.getAdapterPosition();
                        App app = mAppList.get(position);

                        if (isChecked) {
                            app_backlist app_backlist = new app_backlist();
                            app_backlist.setName(app.getPackageName());
                            app_backlist.setEnabled(true);
                            app_backlist.save();
                            Log.i(TAG, "保存成功");
                            holder.appStatus.setText("开启");
                        } else {
                            List<app_backlist> app_backlists = DataSupport.where("name = ?", app.getPackageName()).find(app_backlist.class);
                            int id = app_backlists.get(0).getId();
                            DataSupport.delete(app_backlist.class, id);
                            holder.appStatus.setText("关闭");
                        }
                    }
                });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        App app = mAppList.get(position);
        holder.appName.setText(app.getName());
        holder.appImage.setImageDrawable(app.getDrawable());
        holder.msc.setChecked(app.isEnabled());
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }
}