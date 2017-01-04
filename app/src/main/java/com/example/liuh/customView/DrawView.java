package com.example.liuh.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.liuh.R;

/**
 * Created by Administrator on 2016-09-14.
 */
public class DrawView extends View {

    private int radius;
    private int pivotX;
    private int pivotY;

    private double degreeInterval;

    private float size;

    private int degreeIncrement;

    private int drawCountMax = 5;

    private int drawCountMin = 1;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public DrawView(Context context) {
        super(context);
        this.radius = 50;
        this.pivotX = 400;
        this.pivotY = 400;
        this.degreeInterval = 90;
    }

    public void initData(int pivotX,int pivotY,int radius,float size,double degreeInterval,int degreeIncrement){
        this.radius = radius;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.size = size;
        this.degreeIncrement = degreeIncrement;
        this.degreeInterval = degreeInterval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 创建画笔
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.material_textWhite_disable));// 设置红色
        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了

        for(float i=1;i<41;i++){
            if(i>=drawCountMin&&i<=drawCountMax){
                p.setColor(Color.WHITE);
                canvas.drawCircle(pivotX+(int) (radius* Math.sin((degreeInterval*i+degreeIncrement)* Math.PI / 180)), pivotY+(int) (radius* Math.cos((degreeInterval*i+degreeIncrement)* Math.PI / 180)), (int)size, p);// 小圆
                p.setColor(getResources().getColor(R.color.material_textWhite_disable));
            }else{
                canvas.drawCircle(pivotX+(int) (radius* Math.sin((degreeInterval*i+degreeIncrement)* Math.PI / 180)), pivotY+(int) (radius* Math.cos((degreeInterval*i+degreeIncrement)* Math.PI / 180)), (int)size, p);// 小圆
            }
        }
        if(drawCountMax>=40){
            drawCountMax = 5;
            drawCountMin = 1;
        }else{
            drawCountMax++;
            drawCountMin++;
        }
    }

}
