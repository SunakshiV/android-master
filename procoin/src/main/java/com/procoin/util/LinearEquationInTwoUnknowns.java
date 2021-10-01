package com.procoin.util;

import android.util.Log;

/**
 *
 * 二元一次方程
 * Created by zhengmj on 18-1-2.
 */

public class LinearEquationInTwoUnknowns {
    float x;
    float y;
    float c;
    // a-2b=0;  x=1,y=-2,c=0
    //a+b=100; x=1,y=1,c=100

    public LinearEquationInTwoUnknowns(float x, float y, float c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }

    public void cheng(float num)// 参数乘以num
    {
        x = x * num;
        y = y * num;
        c = c * num;
    }

    public void jian(LinearEquationInTwoUnknowns er)// 相互减
    {
        this.x -= er.x;
        this.y -= er.y;
        this.c -= er.c;
    }

    public static float[] getXY(LinearEquationInTwoUnknowns l1,LinearEquationInTwoUnknowns l2){
        //这2句打印出来的就是一个二元一次方程
        Log.d("getXY",l1.x + "*" + "x" + "+" + l1.y + "*" + "y" + "=" + l1.c);
        Log.d("getXY",l2.x + "*" + "x" + "+" + l2.y + "*" + "y" + "=" + l2.c);
        float[] floats=new float[2];
        l1.cheng(l2.x);
        l2.cheng(l1.x / l2.x);
        l1.jian(l2);
        floats[1] =  l1.c / l1.y;
        floats[0] = (l2.c - l2.y *floats[1]) / l2.x;
        Log.d("getXY","x=="+floats[0]+"  y=="+floats[1]);
        return floats;
    }
}
