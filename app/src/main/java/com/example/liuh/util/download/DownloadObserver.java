package com.example.liuh.util.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2016-09-09.
 */
public class DownloadObserver extends ContentObserver {
    private long downid;
    private Handler handler;
    private Context context;

    public DownloadObserver(Handler handler, Context context, long downid) {
        super(handler);
        this.handler = handler;
        this.downid = downid;
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        //每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        Log.w("onChangeID", String.valueOf(downid));
        super.onChange(selfChange);
        //实例化查询类，这里需要一个刚刚的downid
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downid);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //这个就是数据库查询啦
        Cursor cursor = downloadManager.query(query);
        while (cursor.moveToNext()) {
            int mDownload_so_far = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int mDownload_all = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int mProgress = (mDownload_so_far * 99) / mDownload_all;
            Log.e(getClass().getSimpleName(), String.valueOf(mProgress));

            Message msg = new Message();
            msg.what = mProgress;
            handler.sendMessage(msg);
        }
    }
}