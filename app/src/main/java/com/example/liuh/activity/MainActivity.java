package com.example.liuh.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuh.R;
import com.example.liuh.activity.base.BaseActivity;
import com.example.liuh.bean.UpdateInfo;
import com.example.liuh.eventbus.MessageEvent;
import com.example.liuh.util.HttpUtil;
import com.example.liuh.util.NetworkUtils;
import com.example.liuh.util.SharedPreUtil;
import com.example.liuh.util.URL;
import com.example.liuh.util.download.ApkUpdateUtils;
import com.example.liuh.util.download.DownloadObserver;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private static final int CODE_OK = 0;
    private static final int CODE_NO_NETWORK = 1;
    private static final int CODE_SERVERSEXC = 2;
    public static final int CODE_DOWNLOAD_PROGRESS = 999;

    private TextView tv;

    private String mVersionName;
    private int mVersionCode;

    private long startTime = 0;

    private UpdateInfo ui;

    private DownloadObserver dob;

    private boolean isUpdate;

    private Handler hanler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv.setText(""+msg.what+"%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        String[] str = {};
//        String str2 = str[1];

//        goMain();

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }

        //隐藏状态栏
        //定义全屏参数
//        int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
//        Window window=MainActivity.this.getWindow();
        //设置当前窗体为全屏显示
//        window.setFlags(flag, flag);

        setContentView(R.layout.activity_splash);

        EventBus.getDefault().register(this);

        goMain();
//        initView();
//        initData();

    }

    private void initData() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo pi = packageManager.getPackageInfo(getPackageName(),0);
            mVersionCode = pi.versionCode;
            mVersionName = pi.versionName;
            tv.setText("当前版本："+pi.versionName);
            Log.e(TAG,"当前版本："+pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        isUpdate = (Boolean)SharedPreUtil.get(MainActivity.this,"autoUpdate",true);
        Log.e("MainIsUpdate",isUpdate+"");
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkVersion();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv_version);
    }

    private void checkVersion(){
        startTime = System.currentTimeMillis();

        if(!isUpdate){
            goMain();
            return;
        }

        HttpUtil.httpGet(URL.UpdateURL, new HttpUtil.HttpListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG,"onError");
                //提示网络数据异常。1.可能是本机网络机场。2.可能是服务器异常。
                e.printStackTrace();
                int msgCode = -1;
                if (!NetworkUtils.isConnected(MainActivity.this)) {
                    //提示网络异常
                    msgCode = CODE_NO_NETWORK;
                    //点击跳转到设置页面
                } else {
                    //服务器异常
                    msgCode = CODE_SERVERSEXC;
                }
                EventBus.getDefault().post(new MessageEvent(msgCode,null));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG,"onResponse");
                ui = new Gson().fromJson(response,UpdateInfo.class);
                EventBus.getDefault().post(new MessageEvent(CODE_OK,null));
            }
        });

    }

    /**
     * 通知对话框
     *
     * @param
     */
    public void showNotifyDialog() {

        //判断是否是用的手机网络     之后再做
        //NetworkUtils.NetType type = NetworkUtils.getConnectedType(this);

        /*初始化dialog构造器*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新");/*设置dialog的title*/
        builder.setMessage(ui.getDescription()+ui.getVersionCode()+"-"+mVersionCode);/*设置dialog的内容*/
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {/*设置dialog确认按钮的点击事件*/
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                download();
                goMain();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {/*设置dialog取消按钮的点击事件*/
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goMain();
            }
        });

        //builder.setCancelable(false);  设置是否可以通过返回键消失, 默认true  用户体验不好
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                goMain();
            }
        });
        builder.show();/*显示dialog*/
    }

    @Subscribe
    public void onMessageEvent(MessageEvent event){
        switch (event.key){
            case CODE_OK :
                if(ui.getVersionCode()>mVersionCode){
                    showNotifyDialog();
                }else{
                    goMain();
                }
                break;
            case CODE_NO_NETWORK :
                Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                goMain();
                break;
            case CODE_SERVERSEXC :
                Toast.makeText(MainActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                goMain();
                break;
            case CODE_DOWNLOAD_PROGRESS :
                tv.setText(event.val+"");
                Log.e(TAG,event.val+"");
                break;
            default:
                goMain();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if(null!=dob)
            getContentResolver().unregisterContentObserver(dob);
        super.onDestroy();
    }

    public void goMain(){
        Log.e(TAG,"goMain");
        final long curTime = System.currentTimeMillis();

        final Intent intent = new Intent(MainActivity.this,SafeMainActivity.class);

        if(curTime-startTime<2000){
            //延迟执行，尽量看到闪屏页
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //全全屏切换到非全屏的activity之后出现的控件位置偏离    但是设置pandding之后会出现状态栏下呈现白色
                    smoothSwitchScreen();
                    startActivity(intent);
                    finish();
                }
            }, 2000-(curTime-startTime));
        }else{
            smoothSwitchScreen();
            startActivity(intent);
            finish();
        }
    }

    private void smoothSwitchScreen() {
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attr);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void download() {
        if (!canDownloadState()) {
            Toast.makeText(this, "下载服务被禁用,请您启用", Toast.LENGTH_SHORT).show();
            showDownloadSetting();
            return;
        }
        ApkUpdateUtils.download(this, ui.getDownloadUrl(), getResources().getString(R.string.app_name));

        //下载进度监听，在download之后才能拿到id（只有在任务创建之后才能拿到）
        dob = new DownloadObserver(hanler,MainActivity.this,ApkUpdateUtils.getDownloadId(this));
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/"),true,dob);
    }

    //查看下载功能有没有被禁用？
    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //打开应用权限设置
    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    //判断应用程序的activity是否已经被启动了
    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
