package com.example.liuh.util;

import android.os.Environment;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016-09-07.
 */
public class HttpUtil {

    public static void httpGet(String url,final HttpListener listener){
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        listener.onResponse(response,id);
                    }
                });
    }

    public static void httpPost(String url,final HttpListener listener){
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", "hyman")
                .addParams("password", "123")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
    }

    public static void httpFile(String url,final HttpFileListener listener){
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "update.apk")
                {
                    @Override
                    public void inProgress(float progress, long total , int id)
                    {
                        listener.inProgress(progress,total,id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.onError(call, e, id);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        listener.onResponse(response,id);
                    }
                });
    }

    public interface HttpListener{

        void onResponse(String response, int id);
        void onError(Call call, Exception e, int id);

    }

    public interface HttpFileListener extends HttpListener{

        void inProgress(float progress, long total , int id);
        void onResponse(File response, int id);
    }
}
