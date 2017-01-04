package com.example.liuh;

import android.app.Application;

import com.example.liuh.exception.AppExceptionHandler;
import com.socks.library.KLog;



/**
 * Created by Administrator on 2016-10-20.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true,"拌合站管理系统");
        // 程序异常交由AppExceptionHandler来处理
        Thread.setDefaultUncaughtExceptionHandler(AppExceptionHandler.getInstance(this));
    }
}
