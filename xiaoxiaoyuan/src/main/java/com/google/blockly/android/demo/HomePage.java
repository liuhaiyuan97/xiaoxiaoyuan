package com.google.blockly.android.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;


public class HomePage extends AppCompatActivity {
    private static AudioManager audioManager=null;
    private static MediaPlayer player=null;

    private ImageView ivPao1;
    private ImageView ivCreat;
    private ImageView ivArts;
    private ImageView ivTv;
    private ImageView ivSetting;
    private ImageView ivSpin;
    private LinearLayout llMenu;
    private ImageView ivHome;
    private ImageView ivCharts;
    private ImageView ivCommunity;
    private ImageView ivMe;
    private int[] list;
    private boolean state=false;
    private static boolean volume=true;
    private MenuFlash menuFlash;
    private static boolean isLogin=false;
    private static String user="";
    private  static boolean exist=false;
    private static String details="";

    private void init(){
        ivCreat=findViewById(R.id.iv_creat);
        ivArts=findViewById(R.id.iv_arts);
        ivTv=findViewById(R.id.iv_tv);
        ivSetting=findViewById(R.id.iv_setting);
        if(volume==true){
            ivSetting.setImageResource(R.mipmap.open);
        }else{
            ivSetting.setImageResource(R.mipmap.close);
        }
        ivSpin=findViewById(R.id.iv_spin);
        ivHome=findViewById(R.id.iv_HomePage);
        ivCharts=findViewById(R.id.iv_charts);
        ivCommunity=findViewById(R.id.iv_community);
        ivMe=findViewById(R.id.iv_me);
        llMenu=findViewById(R.id.ll_menu);
        list= new int[]{R.id.iv_HomePage, R.id.iv_charts, R.id.iv_community, R.id.iv_me};

    }

    private void show(){
        ObjectAnimator l1=ObjectAnimator.ofFloat(ivCreat,"translationY",-500,0).setDuration(2000);
        l1.start();
        ObjectAnimator o1=ObjectAnimator.ofFloat(ivCreat,"alpha",0,1).setDuration(1000);
        o1.start();


        ObjectAnimator l2=ObjectAnimator.ofFloat(ivArts,"translationY",500,0).setDuration(2000);
        l2.start();
        ObjectAnimator o2=ObjectAnimator.ofFloat(ivArts,"alpha",0,1).setDuration(1000);
        o2.start();


        ObjectAnimator l3=ObjectAnimator.ofFloat(ivTv,"translationY",500,0).setDuration(2000);
        l3.start();
        ObjectAnimator o3=ObjectAnimator.ofFloat(ivTv,"alpha",0,1).setDuration(1000);
        o3.start();


        ObjectAnimator l4=ObjectAnimator.ofFloat(ivPao1,"translationY",0,20,0).setDuration(4000);
        l4.setRepeatCount(ValueAnimator.INFINITE);
        l4.setInterpolator(new AccelerateDecelerateInterpolator());
        l4.start();


        menuFlash=new MenuFlash(ivSpin,ivHome,ivCharts,ivCommunity,ivMe);
        menuFlash.play();

    }

    private void menu(){
        if(state==false) {
            ObjectAnimator o1 = ObjectAnimator.ofFloat(ivSpin, "rotation", 0, 180);
            o1.setDuration(500);
            o1.start();

            llMenu.setVisibility(View.VISIBLE);
            ObjectAnimator o3 = ObjectAnimator.ofFloat(llMenu, "translationX", 200, 0);
            o3.setDuration(500);
            o3.start();
            ObjectAnimator o4 = ObjectAnimator.ofFloat(llMenu, "alpha", 0, 1);
            o4.setDuration(500);
            o4.start();
            state=true;

        }else{
            ObjectAnimator o1 = ObjectAnimator.ofFloat(ivSpin, "rotation", 180, 360);
            o1.setDuration(500);
            o1.start();

            ObjectAnimator o3 = ObjectAnimator.ofFloat(llMenu, "translationX", 0, 200);
            o3.setDuration(500);
            o3.start();
            ObjectAnimator o4 = ObjectAnimator.ofFloat(llMenu, "alpha", 1, 0);
            o4.setDuration(500);
            o4.start();
            state=false;
        }
    }

    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            creat();
            super.handleMessage(msg);
        }
    };

    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            arts();
            super.handleMessage(msg);
        }
    };

    private Handler handler3=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv();
            super.handleMessage(msg);
        }
    };
    private Handler loop=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ObjectAnimator o1=ObjectAnimator.ofFloat(ivCreat,"translationY",0,-100,0).setDuration(6000);
            o1.setRepeatCount(ValueAnimator.INFINITE);
            o1.setInterpolator(new AccelerateDecelerateInterpolator());
            o1.start();

            ObjectAnimator o2=ObjectAnimator.ofFloat(ivArts,"translationY",0,60,0).setDuration(6000);
            o2.setRepeatCount(ValueAnimator.INFINITE);
            o2.setInterpolator(new AccelerateDecelerateInterpolator());
            o2.start();

            ObjectAnimator o3=ObjectAnimator.ofFloat(ivTv,"translationY",0,80,0).setDuration(6000);
            o3.setRepeatCount(ValueAnimator.INFINITE);
            o3.setInterpolator(new AccelerateDecelerateInterpolator());
            o3.start();


            super.handleMessage(msg);
        }
    };
    private void creat(){
        Intent intent=new Intent();
        intent.setClass(HomePage.this,LuaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_top,R.anim.activity_stay);
    }

    private void arts(){
        if(user.equals("")){
            Toast.makeText(HomePage.this,"请先登录",Toast.LENGTH_SHORT).show();
        }else{
        Intent intent=new Intent();
        intent.setClass(HomePage.this,RunActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("details",details);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_top,R.anim.activity_stay);
        }
    }

    private void tv(){
        Intent intent=new Intent(HomePage.this,CourseActivity.class);
        startActivity(intent);
    }
    private void hidTitle(){
        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }
    private void playMusic(){
        if (exist==false) {
            audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
            player=MediaPlayer.create(HomePage.this,R.raw.bgmusic);
            player.setLooping(true);
            player.start();
            exist=true;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //播放音乐
        playMusic();
        //隐藏状态栏方法
        hidTitle();
        //初始化菜单列表
        init();
        //弹出首页控件操作
        //弹出菜单操作

        //
        final Intent intent=getIntent();
        if(intent.getStringExtra("isLogin")!=null && intent.getStringExtra("isLogin").equals("yes")){
            user=intent.getStringExtra("user");
            details=intent.getStringExtra("details");
            Log.e("details",details);
            Toast.makeText(HomePage.this,"登录成功",Toast.LENGTH_SHORT).show();
            isLogin=true;
            Log.e("HomePage","获取Intent(login界面返回的),以及其中的user"+user);
        }else if(intent.getStringExtra("isAlter")!=null && intent.getStringExtra("isAlter").equals("yes")){
            Log.e("HomePage","此时可能修改用户账号，在个人信息界面返回主页面");
            user=intent.getStringExtra("user");
            isLogin=true;

        }else if(intent.getStringExtra("IsLogin")!=null && intent.getStringExtra("IsLogin").equals("no")){
            Toast.makeText(HomePage.this,"注销成功",Toast.LENGTH_SHORT).show();
            isLogin=false;
            user="";
            details="";
        }
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(volume==true){
                    ivSetting.setImageResource(R.mipmap.close);
                    volume=false;
                    player.pause();
                }else{
                    ivSetting.setImageResource(R.mipmap.open);
                    volume=true;
                    player.start();
                }
            }
        });
        ivSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuFlash=new MenuFlash(ivSpin,ivHome,ivCharts,ivCommunity,ivMe);
                menuFlash.click(state);
                if(state==false){
                    state=true;
                }else{
                    state=false;
                }
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
                    if(runningActivity.contains("HomePage")){
                        Toast.makeText(HomePage.this,"已经在主页啦",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(HomePage.this, RegisterActivity.class);
                        startActivity(intent);
                    }

            }
        });


        ivCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator o1=ObjectAnimator.ofFloat(ivCreat,"scaleY",1,2,1).setDuration(800);
                o1.start();
                ObjectAnimator o2=ObjectAnimator.ofFloat(ivCreat,"scaleX",1,2,1).setDuration(800);
                o2.start();
                handler1.sendEmptyMessageDelayed(0,800);
            }
        });

        ivArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator o1=ObjectAnimator.ofFloat(ivArts,"scaleY",1,0,1).setDuration(800);
                o1.start();
                ObjectAnimator o2=ObjectAnimator.ofFloat(ivArts,"scaleX",1,0,1).setDuration(800);
                o2.start();
                ObjectAnimator o3=ObjectAnimator.ofFloat(ivArts,"rotation",0,360).setDuration(800);
                o3.start();
                handler2.sendEmptyMessageDelayed(0,800);
            }
        });


        ivCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end();
                Intent intent=new Intent(HomePage.this,RankActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        ivTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end();
                ObjectAnimator o1=ObjectAnimator.ofFloat(ivTv,"scaleY",1,0,1).setDuration(800);
                o1.start();
                ObjectAnimator o2=ObjectAnimator.ofFloat(ivTv,"scaleX",1,0,1).setDuration(800);
                o2.start();
                ObjectAnimator o3=ObjectAnimator.ofFloat(ivTv,"rotation",0,360).setDuration(800);
                o3.start();
                handler3.sendEmptyMessageDelayed(0,800);
                overridePendingTransition(0, 0);
            }
        });
        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ObjectAnimator o1=ObjectAnimator.ofFloat(ivArts,"alpha",1,0).setDuration(1000);
                o1.start();

                ObjectAnimator o2=ObjectAnimator.ofFloat(ivCreat,"alpha",1,0).setDuration(1000);
                o2.start();

                ObjectAnimator o3=ObjectAnimator.ofFloat(ivTv,"alpha",1,0).setDuration(1000);
                o3.start();

                if(!isLogin ) {
                    end();
                    Log.e("HomePage","此时并未登陆过");
                    Intent intent = new Intent();
                    intent.setClass(HomePage.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }else{
                    end();
                    Log.e("HomePage","已经登录过");
                    Intent intent = new Intent();
                    intent.setClass(HomePage.this, MeActivity.class);
                    intent.putExtra("user",user);
                    intent.putExtra("details",details);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });

    }

    public void open(){
        ivCreat.setAlpha(0f);
        ivCreat.setVisibility(View.VISIBLE);
        ivCreat.animate().alpha(1f)
                .setDuration(500)
                .setListener(null);

        ivTv.setAlpha(0f);
        ivTv.setVisibility(View.VISIBLE);
        ivTv.animate().alpha(1f)
                .setDuration(500)
                .setListener(null);

        ivArts.setAlpha(0f);
        ivArts.setVisibility(View.VISIBLE);
        ivArts.animate().alpha(1f)
                .setDuration(500)
                .setListener(null);
    }

    public void end(){
        ivCreat.animate().alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ivCreat.setVisibility(View.GONE);
                    }
                });

        ivArts.animate().alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ivArts.setVisibility(View.GONE);
                    }
                });

        ivTv.animate().alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ivTv.setVisibility(View.GONE);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        state=false;
        player.start();
        show();
        open();

        try {
           Field field=R.mipmap.class.getField("logo");
           int i=field.getInt(new R.mipmap());
           Log.e("icon",i+"");





        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        loop.sendEmptyMessageDelayed(0,2000);
    }

    @Override
    protected void onStop() {
        state=true;
        player.pause();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        state=false;
        player.start();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        state=true;
        player.pause();
        super.onDestroy();
    }


}
