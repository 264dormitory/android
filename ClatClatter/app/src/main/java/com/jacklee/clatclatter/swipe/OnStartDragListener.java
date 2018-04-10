package com.jacklee.clatclatter.swipe;

import android.support.v7.widget.RecyclerView;

/**
 * Created by user on 2018/4/6.
 * 李世杰创建
 * 用于实现任务展示右面按钮
 * 无需等待机壳实现任务的拖拽功能
 */

public interface OnStartDragListener {
    /**
     * 当View需要拖拽时回调
     *
     * @param viewHolder The holder of view to drag
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
