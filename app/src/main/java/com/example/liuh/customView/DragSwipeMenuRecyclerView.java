package com.example.liuh.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.liuh.manager.NormalLinearLayoutManager;
import com.socks.library.KLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * Created by Administrator on 2017-01-03.
 */
public class DragSwipeMenuRecyclerView extends SwipeMenuRecyclerView {

    public DragSwipeMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragSwipeMenuRecyclerView(Context context) {
        super(context);
    }

    int mDownX;
    int mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        KLog.e("DragSwipeMenuRecyclerView", "e: "+ e.getAction());
        switch(e.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mDownX =(int)e.getRawX();
                mDownY =(int)e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int nowX = (int) e.getRawX();
                int nowY = (int) e.getRawY();

//                KLog.e("DragSwipeMenuRecyclerView", "ACTION_MOVE: nowX "+ nowX);
//                KLog.e("DragSwipeMenuRecyclerView", "ACTION_MOVE: nowY "+ nowY);

                // 判断是否为偏向左右的滑动
                if (Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
                    setItemViewSwipeEnabled(false);
                    KLog.e("DragSwipeMenuRecyclerView", "setItemViewSwipeEnabled(false))");
                    //当返回true时，事件将不会再向下传递，这个onTouch也不会再此次事件中再执行
//                    return true;
                }else{
                    setItemViewSwipeEnabled(true);
                    KLog.e("DragSwipeMenuRecyclerView", "setItemViewSwipeEnabled(true)");
//                    setLongPressDragEnabled(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                setItemViewSwipeEnabled(false);
                KLog.e("DragSwipeMenuRecyclerView", "ACTION_UP");
                break;
        }

        boolean b;
        KLog.e("onInterceptTouchEvent",b = super.onInterceptTouchEvent(e));
        return b;
    }

}
