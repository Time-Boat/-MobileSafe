package com.example.liuh.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.liuh.R;
import com.example.liuh.activity.base.BaseActivity;
import com.example.liuh.util.SharedPreUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetCenterActivity extends BaseActivity {

    @Bind(R.id.btn_auto_update)
    Switch oc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_center);
        //注册注解
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

        oc.setChecked((Boolean)SharedPreUtil.get(SetCenterActivity.this,"autoUpdate",false));

        oc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    //实例化SharedPreferences对象（第一步）
//                    SharedPreferences mySharedPreferences= getSharedPreferences("test",
//                            Activity.MODE_PRIVATE);
//                    //实例化SharedPreferences.Editor对象（第二步）
//                    SharedPreferences.Editor editor = mySharedPreferences.edit();
//                    //用putString的方法保存数据
//                    editor.putString("name", "Karl");
//                    editor.putString("habit", "sleep");
//                    //提交当前数据
//                    editor.commit();
//                    //使用toast信息提示框提示成功写入数据
//                    Toast.makeText(SetCenterActivity.this, "数据成功写入SharedPreferences！" , Toast.LENGTH_LONG).show();
                Log.e("CenterSetting",isChecked+"");
                SharedPreUtil.put(SetCenterActivity.this,"autoUpdate",isChecked);
            }
        });

    }

}
