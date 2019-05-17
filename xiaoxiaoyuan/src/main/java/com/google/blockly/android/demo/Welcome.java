package com.google.blockly.android.demo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ant.liao.GifView;

import java.nio.file.Path;

public class Welcome extends Activity{
    private ImageView ivWp;
    private TextView tvWel;
    private TextView tvClick;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            homePage();
            super.handleMessage(msg);
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ivWp=findViewById(R.id.iv_wp);
        tvWel=findViewById(R.id.tv_wel);
        tvClick=findViewById(R.id.tv_click);
        tvClick.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));

        ObjectAnimator o1=ObjectAnimator.ofFloat(ivWp,"rotation",0,360).setDuration(1800);
        o1.start();
        ObjectAnimator ox=ObjectAnimator.ofFloat(ivWp,"scaleY",0,2,1).setDuration(1800);
        ox.start();
        ObjectAnimator oy=ObjectAnimator.ofFloat(ivWp,"scaleX",0,2,1).setDuration(1800);
        oy.start();

        ObjectAnimator o2=ObjectAnimator.ofFloat(tvWel,"translationY",-500,0,-300,0).setDuration(2000);
        o2.start();

        ObjectAnimator o3=ObjectAnimator.ofFloat(tvClick,"translationY",500,-100,300,0).setDuration(2000);
        o3.start();

        ObjectAnimator o4=ObjectAnimator.ofFloat(tvClick,"alpha",0,1,0).setDuration(1500);
        o4.setRepeatCount(ValueAnimator.INFINITE);
        o4.start();


        LinearLayout llGo=findViewById(R.id.ll_go);
        llGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePage();
            }
        });

//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//
//        GifView gifView=findViewById(R.id.gif);
//        gifView.setGifImage(R.drawable.wel);
//        gifView.setX(1);
//        gifView.setY(1);
//        gifView.setShowDimension(width,height);
//
//        LinearLayout llHome=findViewById(R.id.ll_home);
//        llHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                homePage();
//            }
//        });
    }


    public void homePage(){
        Intent intent=new Intent();
        intent.setClass(Welcome.this,HomePage.class);
        startActivity(intent);
        finish();

    }
}
