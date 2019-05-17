package com.google.blockly.android.demo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuFlash {
    private ImageView ivSpin;
    private ImageView ivHome;
    private ImageView ivCharts;
    private ImageView ivCommunity;
    private ImageView ivMe;
   public MenuFlash(ImageView ivSpin,ImageView ivHome,ImageView ivCharts,ImageView ivCommunity,ImageView ivMe){
       this.ivSpin=ivSpin;
       this.ivHome=ivHome;
       this.ivCharts=ivCharts;
       this.ivCommunity=ivCommunity;
       this.ivMe=ivMe;

       ObjectAnimator o1 = ObjectAnimator.ofFloat(ivSpin, "rotation", 180, 360);
       o1.setDuration(500);
       o1.start();

       ObjectAnimator o3 = ObjectAnimator.ofFloat(ivHome, "translationX", 0, 200);
       o3.setDuration(500);
       o3.start();
       ObjectAnimator o4 = ObjectAnimator.ofFloat(ivHome, "alpha", 1, 0);
       o4.setDuration(500);
       o4.start();

       ObjectAnimator o5 = ObjectAnimator.ofFloat(ivCharts, "translationX", 0, 200);
       o5.setDuration(500);
       o5.setStartDelay(100);
       o5.start();
       ObjectAnimator o6 = ObjectAnimator.ofFloat(ivCharts, "alpha", 1, 0);
       o6.setDuration(500);
       o6.setStartDelay(100);
       o6.start();

       ObjectAnimator o9 = ObjectAnimator.ofFloat(ivCommunity, "translationX", 0, 200);
       o9.setDuration(500);
       o9.setStartDelay(200);
       o9.start();
       ObjectAnimator o10 = ObjectAnimator.ofFloat(ivCommunity, "alpha", 1, 0);
       o10.setDuration(500);
       o10.setStartDelay(200);
       o10.start();

       ObjectAnimator o11 = ObjectAnimator.ofFloat(ivMe, "translationX", 0, 200);
       o11.setDuration(500);
       o11.setStartDelay(300);
       o11.start();
       ObjectAnimator o12 = ObjectAnimator.ofFloat(ivMe, "alpha", 1, 0);
       o12.setDuration(500);
       o12.setStartDelay(300);
       o12.start();
   }
    public void click(boolean state){
        if(state==false) {
            ObjectAnimator o1 = ObjectAnimator.ofFloat(ivSpin, "rotation", 0, 180);
            o1.setDuration(500);
            o1.start();

            ivHome.setVisibility(View.VISIBLE);
            ObjectAnimator o3 = ObjectAnimator.ofFloat(ivHome, "translationX", 200, 0);
            o3.setDuration(500);
            o3.start();
            ObjectAnimator o4 = ObjectAnimator.ofFloat(ivHome, "alpha", 0, 1);
            o4.setDuration(500);
            o4.start();

            ivCharts.setVisibility(View.VISIBLE);
            ObjectAnimator o5 = ObjectAnimator.ofFloat(ivCharts, "translationX", 200, 0);
            o5.setDuration(500);
            o5.setStartDelay(100);
            o5.start();
            ObjectAnimator o6 = ObjectAnimator.ofFloat(ivCharts, "alpha", 0, 1);
            o6.setDuration(500);
            o6.setStartDelay(100);
            o6.start();


            ivCommunity.setVisibility(View.VISIBLE);
            ObjectAnimator o9 = ObjectAnimator.ofFloat(ivCommunity, "translationX", 200, 0);
            o9.setDuration(500);
            o9.setStartDelay(200);
            o9.start();
            ObjectAnimator o10 = ObjectAnimator.ofFloat(ivCommunity, "alpha", 0, 1);
            o10.setDuration(500);
            o10.setStartDelay(200);
            o10.start();

            ivMe.setVisibility(View.VISIBLE);
            ObjectAnimator o11 = ObjectAnimator.ofFloat(ivMe, "translationX", 200, 0);
            o11.setDuration(500);
            o11.setStartDelay(300);
            o11.start();
            ObjectAnimator o12 = ObjectAnimator.ofFloat(ivMe, "alpha", 0, 1);
            o12.setDuration(500);
            o12.setStartDelay(300);
            o12.start();

        }else{
            ObjectAnimator o1 = ObjectAnimator.ofFloat(ivSpin, "rotation", 180, 360);
            o1.setDuration(500);
            o1.start();

            ObjectAnimator o3 = ObjectAnimator.ofFloat(ivHome, "translationX", 0, 200);
            o3.setDuration(500);
            o3.start();
            ObjectAnimator o4 = ObjectAnimator.ofFloat(ivHome, "alpha", 1, 0);
            o4.setDuration(500);
            o4.start();

            ObjectAnimator o5 = ObjectAnimator.ofFloat(ivCharts, "translationX", 0, 200);
            o5.setDuration(500);
            o5.setStartDelay(100);
            o5.start();
            ObjectAnimator o6 = ObjectAnimator.ofFloat(ivCharts, "alpha", 1, 0);
            o6.setDuration(500);
            o6.setStartDelay(100);
            o6.start();

            ObjectAnimator o9 = ObjectAnimator.ofFloat(ivCommunity, "translationX", 0, 200);
            o9.setDuration(500);
            o9.setStartDelay(200);
            o9.start();
            ObjectAnimator o10 = ObjectAnimator.ofFloat(ivCommunity, "alpha", 1, 0);
            o10.setDuration(500);
            o10.setStartDelay(200);
            o10.start();

            ObjectAnimator o11 = ObjectAnimator.ofFloat(ivMe, "translationX", 0, 200);
            o11.setDuration(500);
            o11.setStartDelay(300);
            o11.start();
            ObjectAnimator o12 = ObjectAnimator.ofFloat(ivMe, "alpha", 1, 0);
            o12.setDuration(500);
            o12.setStartDelay(300);
            o12.start();
        }
    }
    public void play(){
//        ObjectAnimator l5=ObjectAnimator.ofFloat(ivSpin,"translationX",0,-90,0).setDuration(1000);
//        l5.setRepeatCount(ValueAnimator.INFINITE);
//        l5.start();
    }
}
