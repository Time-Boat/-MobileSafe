package com.example.liuh.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.socks.library.KLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * Created by Administrator on 2017-01-03.
 */
public class DragSwipeMenuRecyclerView extends SwipeMenuRecyclerView {


    public static final String TAG = "DragSwipeMenuRecyclerView";

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
        boolean b;
        b = super.onTouchEvent(e);
        KLog.d(TAG, "onTouchEvent: "+ b);
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        KLog.d(TAG, "DragSwipeMenuRecyclerView: "+ e.getAction());
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
                    //设置这个Swipe是否能够侧滑
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        boolean b;
        b = super.dispatchTouchEvent(event);
        KLog.d(TAG, "dispatchTouchEvent: "+ b);
        return super.dispatchTouchEvent(event);
    }


}
