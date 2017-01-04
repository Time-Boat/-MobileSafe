package com.example.liuh.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-09-21.
 */
public class ShowToast {

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
