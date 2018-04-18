package com.jacklee.clatclatter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder>
{
    private List<App> mAppList;

    Context context;
    //List<AppInfo> appInfos;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View appView;
        ImageView appImage;
        TextView appName;
        TextView appStatus;


        android.support.v7.widget.SwitchCompat msc;
        public ViewHolder(View view)
        {
            super(view);
            appView=view;
            appImage = (ImageView) view.findViewById(R.id.app_image);
            appName = (TextView) view.findViewById(R.id.app_name);
            appStatus=(TextView) view.findViewById(R.id.switch_show_status);
            msc=(android.support.v7.widget.SwitchCompat)view.findViewById(R.id.whitelist_switchCompat);
        }
    }
    public AppAdapter (List<App> appList) {
        mAppList = appList;
    }

    public AppAdapter(Context context , List<App> appList ){
        this.context = context;
        this.mAppList = appList;
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setAppInfos(List<App> appList) {
        this.mAppList = appList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent,
                false);
        final ViewHolder holder=new ViewHolder(view);
        holder.appView.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v)
            {
                int position=holder.getAdapterPosition();
                App app=mAppList.get(position);
                Toast.makeText(v.getContext(),"你点击了view"+app.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.appImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                App app=mAppList.get(position);
                Toast.makeText(v.getContext(),"你点击了图片"+app.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.appName.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v)
            {
                int position=holder.getAdapterPosition();
                App app=mAppList.get(position);
                Toast.makeText(v.getContext(),"你点击了text"+app.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.msc.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked)
                        {
                            holder.appStatus.setText("开启");
                        }
                        else
                        {
                            holder.appStatus.setText("关闭");
                        }
                    }
                });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        App app=mAppList.get(position);
        holder.appName.setText(app.getName());
        holder.appImage.setImageDrawable(app.getDrawable());
    }

    @Override
    public int getItemCount()
    {
        return mAppList.size();
    }
}