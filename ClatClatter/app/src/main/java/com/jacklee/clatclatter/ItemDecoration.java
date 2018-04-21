package com.jacklee.clatclatter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 陈鸿编 on 2018/4/10.
 */

public abstract class ItemDecoration {

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //onDraw(c, parent);
    }


    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //onDrawOver(c, parent);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        getItemOffsets(outRect, ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition(),
                parent);
    }

    @Deprecated
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        outRect.set(0, 0, 0, 0);
    }
}
