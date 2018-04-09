package com.jacklee.clatclatter.swipe;

/**
 * 用于数据交换的接口
 * Created by user on 2018/4/6.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);
    //数据删除
    void onItemDismiss(int position);
}