package com.example.liuh.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuh.R;
import com.example.liuh.activity.base.BaseActivity;
import com.example.liuh.adapter.FunctionAdapter;
import com.example.liuh.bean.FunctionAdapterEntity;
import com.example.liuh.customView.DrawView;
import com.example.liuh.decoration.MainDecoration;
import com.example.liuh.eventbus.AppBus;
import com.example.liuh.eventbus.BusEventData;
import com.example.liuh.util.ShowToast;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-09-07.
 */
public class SafeMainActivity extends BaseActivity {

    private String[] functionTitle = {"手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private String[] functionSubhead = {"防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int[] imageRid = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;

    private static final int ALPHA_ANIMATIONS_DURATION = 500;

    private boolean mIsTheTitleVisible = false;

    @Bind(R.id.main_tv_toolbar_title)
    TextView mTvToolbarTitle; // 标题栏Title

    @Bind(R.id.main_abl_app_bar)
    AppBarLayout mAblAppBar; // 整个可以滑动的AppBar

    @Bind(R.id.id_recyclerview)
    RecyclerView mRcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全全屏切换到非全屏的activity之后出现的控件位置偏离    但是设置pandding之后会出现状态栏下呈现白色
//        smoothSwitchScreen();

        setContentView(R.layout.activity_main);

        //注册注解
        ButterKnife.bind(this);

//        initStatusBar(R.color.colorPrimaryDark);

        // AppBar的监听
        mAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleToolbarTitleVisibility(percentage);
            }
        });
        initData();
    }

    private void initData() {

        mRcv.setLayoutManager(new LinearLayoutManager(this));

        List<FunctionAdapterEntity> data = new ArrayList<FunctionAdapterEntity>();

        int i = 0;
        while (i < functionTitle.length) {
            FunctionAdapterEntity f = new FunctionAdapterEntity();
            f.setTitle(functionTitle[i]);
//            f.setSubhead(functionSubhead[i]);
            f.setSubhead("为了部落!!!!");
            if (i % 2 == 0) {
                f.setPic(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.speed));
            } else {
                f.setPic(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.folder));
            }

//            f.setPic(BitmapFactory.decodeResource(this.getApplicationContext().getResources(),imageRid[i]));
            i++;
            data.add(f);
        }

        mRcv.setAdapter(new FunctionAdapter(this, data, new FunctionAdapter.onRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(SafeMainActivity.this, position + "", Toast.LENGTH_SHORT).show();
                goFunction(position);
            }
        }));
        mRcv.addItemDecoration(new MainDecoration(this, MainDecoration.VERTICAL_LIST));
    }

    private void goFunction(int position) {
        //可以用枚举类型
        Intent intent = null;
        switch (position) {
            case 0:
                ShowToast.showToast(this, position + "");
                break;
            case 1:
                ShowToast.showToast(this, position + "");
                break;
            case 2:
                intent = new Intent(SafeMainActivity.this, AppManagerActivity.class);
                startActivity(intent);
                break;
            case 3:
                ShowToast.showToast(this, position + "");
                break;
            case 4:
                ShowToast.showToast(this, position + "");
                break;
            case 5:
                ShowToast.showToast(this, position + "");
                break;
            case 6:
                ShowToast.showToast(this, position + "");
                break;
            case 7:
                ShowToast.showToast(this, position + "");
                break;
            case 8:
                intent = new Intent(SafeMainActivity.this, SetCenterActivity.class);
                break;
            default:
                ShowToast.showToast(SafeMainActivity.this, "SafeMainActivity，default");
                break;
        }
    }

    // 处理ToolBar的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }


    boolean first = true;
    LinearLayout layout;
    DrawView view;

    Thread thread;

    private BusEventData data;

    AnimationThread at = null;

    @Subscribe
    public void setContent(final BusEventData dataa) {

        if (at != null) {
            at.stopthread();
        }

        this.data = dataa;
//        dv.initData((int) data.getChildX(), (int) data.getChildY(), (int) data.getRadius() + 30, 9.0);
        if (first) {
            layout = (LinearLayout) findViewById(R.id.root);
            DrawView view = new DrawView(this);
            view.setTag("drawView");
            layout.invalidate();
            layout.addView(view);
            first = false;
        }

        if (thread != null) {
            thread.interrupt();
//            thread.destroy();
        }

        view = (DrawView) layout.findViewWithTag("drawView");

        at = new AnimationThread();
        at.start();

    }

    class AnimationThread extends Thread {

        boolean runflag = true;

        public synchronized void stopthread() {
            runflag = false;
        }

        public synchronized boolean getrunflag() {
            return runflag;
        }

        public void run() {
            int i = 0;
            while (runflag) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                view.initData((int) data.getChildX(), (int) data.getChildY(), (int) (data.getRadius() + 20), (float) data.getDrawViewSize(), 9.0, i--);//data.getDistance()

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //通知view组件重绘
                        layout.removeAllViews();
                        view.invalidate();
                        layout.addView(view);
                    }
                });
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        AppBus.getInstance().unregister(this);
    }

    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("onKeyDown", "点击按钮:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                finish();
                System.exit(0);
            } else {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                isExit = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isExit = false;
                        Log.e("onKeyDown", "线程执行完毕:" + isExit);
                    }
                }).start();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 状态栏处理：解决全屏切换非全屏页面被压缩问题
     */
//    public void initStatusBar(int barColor) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//            // 获取状态栏高度
//            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//            View rectView = new View(this);
//            // 绘制一个和状态栏一样高的矩形，并添加到视图中
//            LinearLayout.LayoutParams params
//                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
//            rectView.setLayoutParams(params);
//            //设置状态栏颜色
//            rectView.setBackgroundColor(getResources().getColor(barColor));
//
//            // 添加矩形View到布局中
//            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
//            decorView.addView(rectView);
//            ViewGroup rootView = (ViewGroup)((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
//            rootView.setFitsSystemWindows(true);
//            rootView.setClipToPadding(true);
//
//            rootView.setPadding(0, 60, 0, 0);
//        }
//    }

//    private void smoothSwitchScreen() {
//         5.0以上修复了此bug
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//        ViewGroup rootView = ((ViewGroup) this.findViewById(android.R.id.content));
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        Log.e("safeaaaaa",statusBarHeight+"");
//
//        rootView.setPadding(0, 60, 0, 0);
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        WindowManager.LayoutParams attr = getWindow().getAttributes();
//        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setAttributes(attr);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//    }

    //禁止/启用滚动
//        sv.setNestedScrollingEnabled(true);
//    }


}
