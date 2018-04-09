package com.jacklee.clatclatter.swipe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jacklee.clatclatter.R;
import com.jacklee.clatclatter.TaskItem;
import com.jacklee.clatclatter.TaskItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 新建事件触发之后的callback函数
 * 继承ItemTouchHelper.Callback
 * 重写相应的函数用于实现相应的动画效果
 * Created by user on 2018/4/6.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "SimpleItemTouchHelperC";

    public static final float ALPHA_FULL = 1.0f;

    private final ItemTouchHelperAdapter mAdapter;

    private TaskItemAdapter taskItemAdapter;

    private List<TaskItem> itemList = new ArrayList<>();

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, List<TaskItem> itemList, TaskItemAdapter taskAdapter){
        Log.d(TAG, "SimpleItemTouchHelperCallback: ");
        mAdapter = adapter;
        this.itemList  =itemList;
        this.taskItemAdapter = taskAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        Log.d(TAG, "getMovementFlags: ");
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;  //允许进行上下滑动

        final int swipeFlags = ItemTouchHelper.LEFT;  //只允许进行向左滑动

        Log.d(TAG, "dragFlags:" + dragFlags);

        return makeMovementFlags(dragFlags, swipeFlags);  //通过makeFlag方法来实现参数传递
    }

    //长按进行列表交换操作
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //允许进行滑动操作
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //列表项上下交换操作
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.d(TAG, "onMoveOut:");
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        else {
            Log.d(TAG, "onMove: ");
            //调用接口的方法,实现列表项的交换
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;  //表示已经成功进行了拖动
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int pos = viewHolder.getAdapterPosition();
        final TaskItem item = itemList.get(pos);
        //调用接口的方法,实现列表左滑删除
        Log.d(TAG, "onSwiped: ");
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        //当滑动进行项目的删除的时候通过Snackbar提示是否进行删除
        Snackbar.make(viewHolder.itemView, "已删除该任务", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList.add(pos, item);
                        taskItemAdapter.notifyItemInserted(pos);
                    }
                }).show();
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 释放View时回调，清除背景颜色，隐藏图标
        // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
        getDefaultUIUtil().clearView(((TaskItemAdapter.ViewHolder) viewHolder).taskItem);
        ((TaskItemAdapter.ViewHolder) viewHolder).backGround.setBackgroundColor(Color.WHITE);
        ((TaskItemAdapter.ViewHolder) viewHolder).taskDelete.setVisibility(View.GONE);
    }

//    @Override
//    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        super.clearView(recyclerView, viewHolder);
//
//        viewHolder.itemView.setAlpha(ALPHA_FULL);
//
//        if (viewHolder instanceof ItemTouchHelperViewHolder) {
//            // Tell the view holder it's time to restore the idle state
//            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
//            itemViewHolder.onItemClear();
//        }
//    }

//    @Override
//    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//        // 当viewHolder的滑动或拖拽状态改变时回调
//        if (viewHolder != null) {
//            // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
//            getDefaultUIUtil().onSelected(((TaskItemAdapter.ViewHolder) viewHolder).taskItem);
//        }
//    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                getDefaultUIUtil().onSelected(((TaskItemAdapter.ViewHolder) viewHolder).taskItem);
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        // ItemTouchHelper的onDraw方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的下方
//        // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
//        getDefaultUIUtil().onDraw(c, recyclerView, ((TaskItemAdapter.ViewHolder) viewHolder).taskItem, dX, dY, actionState, isCurrentlyActive);
//        if (dX < 0) { // 向左滑动时的提示
//            ((TaskItemAdapter.ViewHolder) viewHolder).backGround.setBackgroundResource(R.color.red);
//            ((TaskItemAdapter.ViewHolder) viewHolder).taskDelete.setVisibility(View.VISIBLE);
//        }
//    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            getDefaultUIUtil().onDraw(c, recyclerView, ((TaskItemAdapter.ViewHolder) viewHolder).taskItem, dX, dY, actionState, isCurrentlyActive);
            if (dX < 0) { // 向左滑动时的提示
                ((TaskItemAdapter.ViewHolder) viewHolder).backGround.setBackgroundResource(R.color.red);
                ((TaskItemAdapter.ViewHolder) viewHolder).taskDelete.setVisibility(View.VISIBLE);
            }
            // Fade out the view as it is swiped out of the parent's bounds
//            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
//            viewHolder.itemView.setAlpha(alpha);
//            viewHolder.itemView.setTranslationX(dX);

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // ItemTouchHelper的onDrawOver方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的上方
        // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
        getDefaultUIUtil().onDrawOver(c, recyclerView, ((TaskItemAdapter.ViewHolder) viewHolder).taskItem, dX, dY, actionState, isCurrentlyActive);
    }

}
