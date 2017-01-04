package com.example.liuh.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.liuh.R;
import com.example.liuh.activity.base.BaseActivity;
import com.example.liuh.adapter.AManagerAdapter;
import com.example.liuh.bean.AppInfo;
import com.example.liuh.customView.DragSwipeMenuRecyclerView;
import com.example.liuh.decoration.MainDecoration;
import com.example.liuh.util.PhoneInfoUtil;
import com.example.liuh.util.ShowToast;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AppManagerActivity extends BaseActivity {

    String memoryAva;
    String memoryTotal;

    List<AppInfo> appinfos;

    private int lastVisibleItemPosition;

    private LinearLayoutManager mLinearLayoutManager;

    @Bind(R.id.tb_am)
    Toolbar tb;
    @Bind(R.id.tv_avail)
    TextView tv_avail;
    @Bind(R.id.tv_mTotal)
    TextView tv_mTotal;
    @Bind(R.id.pb_memory)
    ProgressBar pb_memory;
    @Bind(R.id.rv_appm)
    DragSwipeMenuRecyclerView rv_appm;
    private List<AppInfo> appInfos;
    @Bind(R.id.swipeLayout)
    public SwipeRefreshLayout srl;
    private AppUninstallReceiver receiver;
    private AManagerAdapter adapter;

    //模拟上拉加载
    private List<AppInfo> samples = new ArrayList<>();

    @Bind(R.id.ll_loading)
    public LinearLayout ll_loading;

    private int count = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            for (int i=count;i<appInfos.size();i++){
                samples.add(appInfos.get(i));
                count++;
                if(count%10==0){
                    break;
                }
            }
            if(msg.what == 1){
                ll_loading.removeAllViews();
                adapter = new AManagerAdapter(getBaseContext(),samples);
//                adapter.setOnItemClickListener(new AManagerAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int position) {
//                        ShowToast.showToast(getBaseContext(),""+position);
//                    }
//                });
                rv_appm.setAdapter(adapter);
            }else if(msg.what == 2){
                srl.setRefreshing(false);
            }else if(msg.what == 3){
                adapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int height = getResources().getDimensionPixelSize(R.dimen.text_height);
            int width = getResources().getDimensionPixelSize(R.dimen.am_btn_del);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            SwipeMenuItem addItem = new SwipeMenuItem(getBaseContext())
//                    .setBackgroundColor(Color.GREEN)
//                    .setText("打开")
//                    .setTextColor(Color.WHITE) // 文字颜色。
//                    .setTextSize(16) // 文字大小。
//                    .setWidth(120) // 宽度。
//                    .setHeight(120); // 高度。
//            swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

            SwipeMenuItem openItem = new SwipeMenuItem(getBaseContext())
                    .setBackgroundColor(Color.rgb(0xa2,0xf7,0x8d))
                    .setText("打开") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(width)
                    .setHeight(height);

            SwipeMenuItem setItem = new SwipeMenuItem(getBaseContext())
                    .setBackgroundColor(Color.rgb(0xFF,0xB3,0x00))
                    .setText("设置") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(width+30)
                    .setHeight(height);

            SwipeMenuItem deleteItem = new SwipeMenuItem(getBaseContext())
                    .setBackgroundColor(Color.rgb(0xe5,0x1c,0x23))
                    .setText("卸载") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(width)
                    .setHeight(height);

            swipeRightMenu.addMenuItem(openItem);
            swipeRightMenu.addMenuItem(setItem);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

            // 上面的菜单哪边不要菜单就不要添加。
        }

    };
    private int appPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        //注册注解
        ButterKnife.bind(this);

//        MIUISetStatusBarLightMode(getWindow(),true);

        initRe();
        initView();
        initData();

    }

    private void initRe() {
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //进行网络请求
//                handler.sendEmptyMessageDelayed(2, 2000);
//            }
//        });
//        srl.setColorSchemeColors(R.color.base_color, R.color.red,
//                R.color.green, R.color.black);

        //下拉刷新部分
        //设置卷内的颜色
        srl.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置下拉刷新监听
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
//                        data.add(0, "添加新的item:" + new Random().nextInt());
                        adapter.notifyDataSetChanged();
                        //停止刷新动画
                        srl.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }


    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }

    private void initView() {

        tb.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        tb.setTitle("");
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
//        if(PhoneInfoUtil.externalMemoryAvailable()){
//            //获取sd卡内存剩余空间
//            sdAva = PhoneInfoUtil.getAvailableExternalMemorySize(getBaseContext());
//            //获取sd卡总共的内存大小
//            sdTotal = PhoneInfoUtil.getTotalExternalMemorySize(getBaseContext());
//        }
        //获取手机内存剩余空间
        memoryAva = PhoneInfoUtil.getAvailableInternalMemorySize(getBaseContext());
        //获取手机总共的内存大小
        memoryTotal = PhoneInfoUtil.getTotalInternalMemorySize(getBaseContext());
        tv_avail.setText("可用："+memoryAva);
        tv_mTotal.setText("总共："+memoryTotal);
        pb_memory.setVisibility(View.VISIBLE);
        pb_memory.setMax(100);
        pb_memory.setProgress(100-(int)(Float.parseFloat(memoryAva.substring(0,memoryAva.indexOf(" GB")))/Float.parseFloat(memoryTotal.substring(0,memoryTotal.indexOf(" GB")))*100));
//        pb_memory.setProgress(38);
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfos = getAppInfos();
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();

        mLinearLayoutManager = new LinearLayoutManager(this);

        rv_appm.setLayoutManager(mLinearLayoutManager);
//        rv_appm.setAdapter(new AManagerAdapter(getBaseContext(), appInfos));
        rv_appm.addItemDecoration(new MainDecoration(this,MainDecoration.VERTICAL_LIST));
        // 打开第一个Item的左侧菜单。
        rv_appm.openLeftMenu(0);
        // 打开第一个Item的右侧菜单。
        rv_appm.openRightMenu(0);
        rv_appm.setSwipeMenuCreator(swipeMenuCreator);

        rv_appm.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                Log.e("Swipe adapterPosition",adapterPosition+"");
                Log.e("Swipe menuPosition",menuPosition+"");
                Log.e("Swipe direction",direction+"");
                if(menuPosition==0){
                    ShowToast.showToast(getBaseContext(),"打开");
                    startApp(adapterPosition);
                }else if(menuPosition==1){
                    ShowToast.showToast(getBaseContext(),"设置");
                    viewAppDetail(adapterPosition);
                }else if(menuPosition==2){
                    ShowToast.showToast(getBaseContext(),"删除");
                    uninstallApp(adapterPosition);
                    appPosition = adapterPosition;
                }
            }
        });

        final Activity a = this;

        rv_appm.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                switch(newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                    // 手指触屏拉动准备滚动，只触发一次        顺序: 1
                        Log.e("onScrollStateChanged","SCROLL_STATE_SETTLING："+newState);
                    break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    // 持续滚动开始，只触发一次                顺序: 2
                        Log.e("onScrollStateChanged","SCROLL_STATE_DRAGGING"+newState);
                    break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                    // 整个滚动事件结束，只触发一次            顺序: 4
                        Log.e("onScrollStateChanged","SCROLL_STATE_IDLE"+newState);
                        if(adapter!=null) {
                            Log.e("onScrollStateChanged", "adapter.getItemCount():" + adapter.getItemCount() + "      lastVisibleItemPosition:" + lastVisibleItemPosition);
                            if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SystemClock.sleep(1000);
                                        a.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                manager.removeViewAt(adapter.getItemCount() - 1);
                                                Message msg = new Message();
                                                msg.what = 3;
                                                handler.sendMessage(msg);
                                            }
                                        });
                                    }
                                }).start();
                            }
                        }
                    break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.e("onScrolled","dx:"+dx+"    dy:"+dy);
                lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            }

        });

        receiver = new AppUninstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }

    private void uninstallApp(int position) {
        if (appinfos.get(position).isUserApp()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + appinfos.get(position).getPackname()));
            startActivity(intent);
        }else{
            ShowToast.showToast(getBaseContext(),"系统应用");
        }
    }

    private void startApp(int position) {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appinfos.get(position).getPackname());
        if (intent == null) {
            ShowToast.showToast(getBaseContext(),"该应用没有启动界面");
        }else {
            startActivity(intent);
        }
    }

    private void viewAppDetail(int position) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + appinfos.get(position).getPackname()));
        startActivity(intent);
    }

    /**
     * 获取手机里面的所有的应用程序
     * @return
     */
    public List<AppInfo> getAppInfos(){
        //得到一个java提供的 包管理器。
        PackageManager pm = getBaseContext().getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        appinfos = new ArrayList();
        for(PackageInfo packInfo:packInfos){
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
            appinfo.setPackname(packname);
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo.setIcon(icon);
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.setName(appname);
            //应用程序apk包的路径
            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.setApkpath(apkpath);
            File file = new File(apkpath);
            long appSize = file.length();
            appinfo.setAppSize(appSize);
            appinfo.setVerName(packInfo.versionName);
            //应用程序安装的位置。
            int flags = packInfo.applicationInfo.flags; //二进制映射  大bit-map
            if((ApplicationInfo.FLAG_EXTERNAL_STORAGE&flags)!=0){
                //外部存储
                appinfo.setInRom(false);
            }else{
                //手机内存
                appinfo.setInRom(true);
            }
            if((ApplicationInfo.FLAG_SYSTEM&flags)!=0){
                //系统应用
                appinfo.setUserApp(false);
            }else{
                //用户应用
                appinfo.setUserApp(true);
            }
            appinfos.add(appinfo);
        }
        return appinfos;
    }

    private class AppUninstallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String info = intent.getDataString();
            Log.e("AppManager",info);
            adapter.notifyItemRemoved(appPosition);
        }
    }
}
