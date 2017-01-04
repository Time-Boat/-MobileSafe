package com.example.liuh.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016-09-22.
 */
public class PhoneInfoUtil {

    private static final String ERROR = "ERROR";

    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static String getTotalInternalMemorySize(Context context) {

        long avail_rom = Environment.getDataDirectory().getTotalSpace();
        return Formatter.formatFileSize(context,avail_rom);

    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static String getAvailableInternalMemorySize(Context context) {

        long avail_rom = Environment.getDataDirectory().getFreeSpace();
        String str_avail_rom = Formatter.formatFileSize(context, avail_rom);
        return str_avail_rom;
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static String getAvailableExternalMemorySize(Context context) {

        if (externalMemoryAvailable()) {
            long avail_sd = Environment.getExternalStorageDirectory()
                    .getFreeSpace();
            String str_avail_sd = Formatter.formatFileSize(context, avail_sd);
            return str_avail_sd;
        } else {
            return ERROR;
        }

    }

    /**
     * 获取SDCARD总的存储空间
     *
     * @return
     */
    public static String getTotalExternalMemorySize(Context context) {

        if (externalMemoryAvailable()) {
            long avail_sd = Environment.getExternalStorageDirectory().getTotalSpace();
            return Formatter.formatFileSize(context,avail_sd);
        } else {
            return ERROR;
        }
    }

    /**
     * 获取系统总内存   (从系统文件中)
     *
     * @param context 可传入应用程序上下文。
     * @return 总内存大单位为B。
     */
    public static String getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Formatter.formatFileSize(context,Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存单位为B。
     */
    public static String getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return Formatter.formatFileSize(context,memoryInfo.availMem);
    }

//    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
//    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");
//
//    /**
//     * 单位换算
//     *
//     * @param size 单位为B
//     * @param isInteger 是否返回取整的单位
//     * @return 转换后的单位
//     */
//    public static String formatFileSize(long size, boolean isInteger) {
//        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
//        String fileSizeString = "0M";
//        if (size < 1024 && size > 0) {
//            fileSizeString = df.format((double) size) + "B";
//        } else if (size < 1024 * 1024) {
//            fileSizeString = df.format((double) size / 1024) + "K";
//        } else if (size < 1024 * 1024 * 1024) {
//            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
//        } else {
//            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
//        }
//        return fileSizeString;
//    }

}
