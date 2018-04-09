package com.jacklee.clatclatter.swipe;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by user on 2018/4/6.
 */

public interface ItemTouchHelperViewHolder {

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * 当项目被选中时进行更新操作
     */
    void onItemSelected();


    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * 当项目被删除时进行更新操作
     */
    void onItemClear();
}
