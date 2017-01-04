package com.example.liuh.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.liuh.R;
import com.example.liuh.eventbus.AppBus;
import com.example.liuh.eventbus.BusEventData;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("unused")
public class AvatarImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {


    private final static float MIN_AVATAR_PERCENTAGE_SIZE   = 0.3f;
    private final static int EXTRA_FINAL_AVATAR_PADDING     = 80;

    private final static String TAG = "behavior";
    private Context mContext;

    private float mCustomFinalYPosition;
    private float mCustomStartXPosition;
    private float mCustomStartToolbarPosition;
    private float mCustomStartHeight;
    private float mCustomFinalHeight;

    private float mAvatarMaxSize;
    private float mFinalLeftAvatarPadding;
    private float mStartPosition;
    private int mStartXPosition;
    private float mStartToolbarPosition;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalXPosition;
    private float mChangeBehaviorPoint;

    private float childX;
    private float childY;
    private float radius;

    private float drawViewSize = 10.0f;
    private float distance = 30.0f;

    private int status = 1;

    public AvatarImageBehavior(Context context, AttributeSet attrs) {
        mContext = context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior);
            mCustomFinalYPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0);
            mCustomStartXPosition = a.getDimension(R.styleable.AvatarImageBehavior_startXPosition, 0);
            mCustomStartToolbarPosition = a.getDimension(R.styleable.AvatarImageBehavior_startToolbarPosition, 0);
            mCustomStartHeight = a.getDimension(R.styleable.AvatarImageBehavior_startHeight, 0);
            mCustomFinalHeight = a.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0);

            a.recycle();
        }

        init();

        mFinalLeftAvatarPadding = context.getResources().getDimension(
                R.dimen.spacing_normal);
    }

    private void init() {
        bindDimensions();
    }

    private void bindDimensions() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
    }

    //跟随toolbar移动
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
//        Log.e("layoutDependsOn",(dependency instanceof Toolbar) +"");
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {

        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (mStartToolbarPosition);    //toolbar的Y值
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;    //toolbar随着移动改变的Y值

        if (expandedPercentageFactor < mChangeBehaviorPoint) {
//            Log.e("onDependentViewChanged","触发YYYYY");
            float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;

            float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                    * heightFactor) + (child.getHeight()/2);
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight()/2);
            child.setX(mStartXPosition - distanceXToSubtract);
            child.setY(mStartYPosition - distanceYToSubtract);

            float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight - heightToSubtract);
            lp.height = (int) (mStartHeight - heightToSubtract);
            child.setLayoutParams(lp);
        } else {
//            Log.e("onDependentViewChanged","触发XXXXX");
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (mStartHeight/2);
            child.setX(mStartXPosition - child.getWidth()/2);
            child.setY(mStartYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight);
            lp.height = (int) (mStartHeight);
            child.setLayoutParams(lp);
        }

        childX = child.getX();
        childY = child.getY();
        radius = child.getWidth()/2;

//        //drawView距离图片的距离
//        if(distance>=10){
//            distance = distance*((300+childX)/mStartXPosition);
//        }else{
//            distance = 10;
//        }
//
        //圆的大小
        float circleSize = (float) (drawViewSize*(Double.valueOf(child.getWidth()+"")/ Double.valueOf(mStartHeight+"")));

//        Log.e("raba",(Double.valueOf(child.getWidth()+"")/ Double.valueOf(mStartHeight+""))+"");
//        Log.e("rab",mStartHeight+"---"+child.getWidth()+"--"+(circleSize<=3?3:circleSize));

        AppBus.getInstance().post(new BusEventData(childX+radius,childY+radius,radius,circleSize<=3?3:circleSize,distance,status++));
        return true;
    }

    //chuld：这个behavior被哪个控件响应就是指的那个控件   这里是CircleImageView
    //dependency：chuld依赖联动的控件    这里指toolbar
    //初始化
    private void maybeInitProperties(CircleImageView child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (dependency.getY()-350f);     //最开始的移动位置   chuld的初始位置     mStartYPosition = (int) (dependency.getY()+(child.getWidth() / 2)-Y);
        childY = mStartYPosition;

        if (mFinalYPosition == 0)
            mFinalYPosition = (dependency.getHeight() /2);   //toolbar高度的中心点
//        Log.e("mFinalYPosition",dependency.getHeight()+"");

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();               //imageView的中心点的高度
//        Log.e("mStartHeight",child.getHeight()+"");
        radius = mStartHeight/2;


        if (mStartXPosition == 0)
            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));    //imageView的中心点
        childX = mStartXPosition;
//        Log.e("mStartXPosition",mStartXPosition+"");

        //应该是最终imageView的位置
        if (mFinalXPosition == 0)
            mFinalXPosition = mContext.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + ((int) mCustomFinalHeight / 2);
//        Log.e("mFinalXPosition",mFinalXPosition+"");

        if (mStartToolbarPosition == 0) {
            mStartToolbarPosition = dependency.getY();     //toolbar的Y值
//            Log.e("mStartToolbarPosition",dependency.getY()+"");
        }

        if (mChangeBehaviorPoint == 0) {
            //
            mChangeBehaviorPoint = (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition));
//            Log.e("child.getHeight()",child.getHeight()+"");
//            Log.e("mCustomFinalHeight",mCustomFinalHeight+"");
//            Log.e("mStartYPosition",mStartYPosition+"");
//            Log.e("mFinalYPosition",mFinalYPosition+"");
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("AB","result:"+result);
        return result;
    }

}
