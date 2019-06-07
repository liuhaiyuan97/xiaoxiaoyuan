package com.google.blockly.android.demo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public  class RunActivity extends AppCompatActivity implements View.OnTouchListener{


    private GestureDetector mGestureDetector;
    protected static final float FLIP_DISTANCE = 50;
    private int i;
    private static int floor=1;
    private static String user="";
    private static int currentFloor=1;
    private Button btnReturn;
    private ImageView[] btns;
    private static String questionName="";
    private static String questionContent="";
    private static String questionResult="";
    private static int userid=0;
    private HorizontalSlowScrollView hv;
    private OkHttpClient okHttpClient;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            changeButtonColor();
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        mGestureDetector = new GestureDetector(new gestureListener());

        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        //1，初始化

        initButton();
        initView();

        //2，绑定触摸监听事件
        hv.setOnTouchListener(this);
        hv.setFocusable(true);
        hv.setClickable(true);
        hv.setLongClickable(true);

        //给每个按钮设置点击事件
        for(int i=0;i<30;i++){
            final int questionID = i+1;
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("RunActivity","关卡："+ questionID +"");
                    currentFloor=questionID;
                    Log.e("RunActivity","运行获取该题内容的异步类，里面包含弹出框");
                    if(currentFloor<=floor) {
                        GetQuestionContentTASK getQuestionContentTASK = new GetQuestionContentTASK(questionID);
                        getQuestionContentTASK.execute();
                        Log.e("RunActivity", "运行弹出题目内容的异步类");
                    }
                    if(currentFloor>floor&&currentFloor<=20){
                        Toast.makeText(RunActivity.this,"请先通过前面关卡",Toast.LENGTH_SHORT).show();
                    }
                    if(currentFloor>20){
                        Toast.makeText(RunActivity.this,"暂未开启",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RunActivity.this,HomePage.class);
                startActivity(intent);
            }
        });
    }

    private void initButton() {
        //获取button
        ImageView btn_0 = findViewById(R.id.btn_0);
        ImageView btn_10 = findViewById(R.id.btn_10);
        ImageView btn_20 = findViewById(R.id.btn_20);
        ImageView btn_1 = findViewById(R.id.btn_1);
        ImageView btn_11 = findViewById(R.id.btn_11);
        ImageView btn_21 = findViewById(R.id.btn_21);
        ImageView btn_2 = findViewById(R.id.btn_2);
        ImageView btn_12 = findViewById(R.id.btn_12);
        ImageView btn_22 = findViewById(R.id.btn_22);
        ImageView btn_3 = findViewById(R.id.btn_3);
        ImageView btn_13 = findViewById(R.id.btn_13);
        ImageView btn_23 = findViewById(R.id.btn_23);
        ImageView btn_4 = findViewById(R.id.btn_4);
        ImageView btn_14 = findViewById(R.id.btn_14);
        ImageView btn_24 = findViewById(R.id.btn_24);
        ImageView btn_5 = findViewById(R.id.btn_5);
        ImageView btn_15 = findViewById(R.id.btn_15);
        ImageView btn_25 = findViewById(R.id.btn_25);
        ImageView btn_6 = findViewById(R.id.btn_6);
        ImageView btn_16 = findViewById(R.id.btn_16);
        ImageView btn_26 = findViewById(R.id.btn_26);
        ImageView btn_7 = findViewById(R.id.btn_7);
        ImageView btn_17 = findViewById(R.id.btn_17);
        ImageView btn_27 = findViewById(R.id.btn_27);
        ImageView btn_8 = findViewById(R.id.btn_8);
        ImageView btn_18 = findViewById(R.id.btn_18);
        ImageView btn_28 = findViewById(R.id.btn_28);
        ImageView btn_9 = findViewById(R.id.btn_9);
        ImageView btn_19 = findViewById(R.id.btn_19);
        ImageView btn_29 = findViewById(R.id.btn_29);
        btns = new ImageView[]{btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9,
                btn_10, btn_11, btn_12, btn_13, btn_14, btn_15, btn_16, btn_17, btn_18, btn_19,
                btn_20, btn_21, btn_22, btn_23, btn_24, btn_25, btn_26, btn_27, btn_28, btn_29};

        animatorUp(btns[2],600);animatorUp(btns[4],1200);animatorUp(btns[7],2100);animatorUp(btns[9],2700);
        animatorDown(btns[0],0);animatorDown(btns[1],300);animatorDown(btns[3],900);animatorDown(btns[5],1500);animatorDown(btns[6],1800);animatorDown(btns[8],2400);

        animatorUp(btns[10],0);animatorUp(btns[12],600);animatorUp(btns[14],1200);animatorUp(btns[16],1800);animatorUp(btns[18],2400);
        animatorDown(btns[11],300);animatorDown(btns[13],900);animatorDown(btns[15],1500);animatorDown(btns[17],2100);animatorDown(btns[19],2700);

        animatorUp(btns[20],0);animatorUp(btns[22],600);animatorUp(btns[24],1200);animatorUp(btns[26],1800);animatorUp(btns[28],2400);
        animatorDown(btns[21],300);animatorDown(btns[23],900);animatorDown(btns[25],1500);animatorDown(btns[27],2100);animatorDown(btns[29],2700);
    }

    private void animatorDown(View view,Integer time) {
        ObjectAnimator o1=ObjectAnimator.ofFloat(view,"translationY",0,30,0).setDuration(5000);
        o1.setRepeatCount(ValueAnimator.INFINITE);
        o1.setInterpolator(new LinearInterpolator());
        o1.setStartDelay(time);
        o1.start();
        ObjectAnimator o2=ObjectAnimator.ofFloat(view,"rotation",0,360).setDuration(25000);
        o2.setRepeatCount(ValueAnimator.INFINITE);
        o2.start();
    }

    private void animatorUp(View view,Integer time) {
        ObjectAnimator o1=ObjectAnimator.ofFloat(view,"translationY",0,-30,0).setDuration(5000);
        o1.setRepeatCount(ValueAnimator.INFINITE);
        o1.setInterpolator(new LinearInterpolator());
        o1.setStartDelay(time);
        o1.start();
        ObjectAnimator o2=ObjectAnimator.ofFloat(view,"rotation",0,360).setDuration(25000);
        o2.setRepeatCount(ValueAnimator.INFINITE);
        o2.start();
    }

    @SuppressLint("ResourceAsColor")
    private void changeButtonColor() {
        RunActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(i = 0;i<30;i++){
                    if(i<(floor-1)){
                        btns[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.t1));
                    }else if(i==(floor-1)){
                        btns[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.t2));
                    }else{
                        btns[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.mipmap.t0));
                    }
                }
            }
        });

    }

    //初始化控件
    private void initView() {
        btnReturn = findViewById(R.id.btn_return);
        hv = findViewById(R.id.hv);
        LinearLayout llAll = findViewById(R.id.ll_all);
        LinearLayout llAll2 = findViewById(R.id.ll_all2);
        LinearLayout llAll3 = findViewById(R.id.ll_all3);

        ViewGroup.LayoutParams lp;
        lp= llAll.getLayoutParams();
        lp.width=getWidth();
        llAll.setLayoutParams(lp);
        llAll2.setLayoutParams(lp);
        llAll3.setLayoutParams(lp);

        //3.获取积分
        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        if(intent.getStringExtra("details")!=null) {
            try {
                userid = new JSONObject(intent.getStringExtra("details")).getInt("user_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("RunActivity", "user:" + user);
        }
    }



    @SuppressLint("ValidFragment")
    public static class QuestionCustomDialog extends DialogFragment {

        private TextView tvQuestionName;
        private TextView tvQuestionContent;
        private Button btnQuestionBegin;
        private Button btnQuestionCancle;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            Log.e("RunActivity","弹出闯关框的onCreateView方法");
            //设置透明度状态
            //getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            View view=inflater.inflate(R.layout.layout_run_to_adjust,null);
            tvQuestionName=view.findViewById(R.id.tv_question_name);
            tvQuestionContent=view.findViewById(R.id.tv_question_content);
            btnQuestionBegin=view.findViewById(R.id.btn_question_begin);
            btnQuestionCancle=view.findViewById(R.id.btn_question_cancle);
            Log.e("弹出框",questionName+":"+questionContent);
            tvQuestionName.setText(questionName);
            tvQuestionContent.setText(questionContent);
            QuestionCustomDialogListener listener=new QuestionCustomDialogListener();
            Log.e("QuestionCustomDialog","创建一个监听器对象");
            btnQuestionBegin.setOnClickListener(listener);
            btnQuestionCancle.setOnClickListener(listener);

            return view;
        }
        @Override
        public void onResume() {
            super.onResume();
            //getDialog().getWindow().setLayout(dip2px(getContext(),425),dip2px(getContext(),310));
            getDialog().getWindow().setLayout(dip2px(getContext(),450),dip2px(getContext(),380) );
        }

        private class QuestionCustomDialogListener implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btn_question_begin:
                        Log.e("弹出框","点击确定按钮");
                        Intent intent=new Intent();
                        intent.setClass(getContext(),AdjustActivity.class);
                        intent.putExtra("user",user);
                        intent.putExtra("floor",floor);
                        intent.putExtra("currentFloor",currentFloor);
                        Log.e("RunActivity","floor:"+floor+" currentFloor:"+currentFloor);
                        intent.putExtra("questionContent",questionContent);
                        intent.putExtra("questionResult",questionResult);
                        intent.putExtra("userid",userid);
                        Log.e("useridformat",userid+"");
                        startActivity(intent);
                        getDialog().dismiss();
                        break;
                    case R.id.btn_question_cancle:
                        Log.e("弹出框","点击取消按钮");
                        getDialog().dismiss();
                        break;
                }
            }
        }
    }

    //获得关数的异步类
    public class GetFloorTASK extends AsyncTask {
        int  id;
        public GetFloorTASK(int id){
            this.id=id;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                String str = "http://60.205.183.222:8080/xiaoxiaoyuan/getFloorByUserServlet?user="+user;
//                URL url = new URL(str);
//                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                //设置请求参数
//                connection.setRequestProperty("contentType","utf-8");
//
//                InputStream is = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(is);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
//                String res = reader.readLine();
//                Log.e("GETTASK","获取到输入流（字符串形式）:"+res);
//                if(res!=null){
//                    JSONObject object=new JSONObject(res);
//                    floor=Integer.valueOf(object.getString("floor"));
//                    Log.e("floor:",floor+"");
//                }
//
//                //4，根据数组改变button颜色
////                changeButtonColor();
//                new Thread(){
//                    @Override
//                    public void run() {
//                        handler.sendEmptyMessageDelayed(0,0);
//                        int i=floor/10;
//                        if(floor%10==0) {
//                            hv.smoothScrollBySlow((i -1)* getWidth(), 0, 1000);
//                        }else{
//                            hv.smoothScrollBySlow((i )* getWidth(), 0, 1000);
//                        }
//                    }
//                }.start();
//
//                reader.close();
//                inputStreamReader.close();
//                is.close();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            okHttpClient=new OkHttpClient();
            Request request=new Request.Builder()
                            .url("http://47.100.52.142:8080/xiaoxiaoyuanssm/user/selectFloorByUserId?id="+userid)
                            .build();

            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                if(!str.equals("")) {
                    JSONObject object = new JSONObject(str);
                    floor=object.getInt("user_current_floor");
                    changeButtonColor();
                    new Thread(){
                    @Override
                    public void run() {
                        handler.sendEmptyMessageDelayed(0,0);
                        int i=floor/10;
                        if(floor%10==0) {
                            hv.smoothScrollToSlow((i -1)* getWidth(), 0, 800);
                            Log.e("log1",i-1+"");
                        }else{
                            hv.smoothScrollToSlow((i )* getWidth(), 0, 800);
                            Log.e("log2",i+"");
                        }
                    }
                     }.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class GetQuestionContentTASK extends AsyncTask{
        int questionId;
        public GetQuestionContentTASK(int questionId){
            this.questionId=questionId;
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/getQuestionContentServlet?questionId="+questionId);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("RunActivity","获取到输入流中的信息（字符串形式）："+str);

                okHttpClient=new OkHttpClient();
                Request request=new Request.Builder()
                                    .url("http://47.100.52.142:8080/xiaoxiaoyuanssm/question/selectQuestionByQid?id="+questionId)
                                    .build();
                Call call=okHttpClient.newCall(request);
                Response response=call.execute();
                String str=response.body().string();

                JSONObject object=new JSONObject(str);
//                questionName=object.getString("questionName");
//                questionContent=object.getString("questionContent");
//                questionResult=object.getString("questionResult");
//                Log.e("RunActivity","questionName:"+questionName);
//                Log.e("RunActivity","questionContent:"+questionContent);
//                Log.e("RunActivity","questionResult:"+questionResult);
                questionName=object.getString("question_name");
                questionContent=object.getString("question_content");
                questionResult=object.getString("question_result");

                QuestionCustomDialog dialog=new QuestionCustomDialog();
                Log.e("RunActivity","创建弹出框对象");
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(),"");
                Log.e("RunActivity","将Dialog对象显示出来");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    //获取页面宽度
    public int getWidth(){
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

    //将像素转换为px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //将px转换为dp
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //上一页点击事件
    public void topre(View view) {

        hv.post(new Runnable(){
            @Override
            public void run(){
                hv.smoothScrollBySlow(-getWidth(),0,1000);
            }
        });
    }

    //下一页点击事件
    public void tonext(View view) {
        hv.post(new Runnable(){
            @Override
            public void run(){
                hv.smoothScrollBySlow(getWidth(),0,1000);
            }
        });

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private class gestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            //按下
            Log.e("text","onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.e("text","onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //抬起
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("text","onScroll");
            //滑动
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e("text","onLongPress");

        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 0) {
                if(hv.getScrollX()<getWidth()){
                    hv.post(new Runnable(){
                        @Override
                        public void run(){
                            hv.smoothScrollToSlow(getWidth(),0,1000);
                        }
                    });
                }else if(hv.getScrollX()<2*getWidth()){
                    hv.post(new Runnable(){
                        @Override
                        public void run(){
                            hv.smoothScrollToSlow(2*getWidth(),0,1000);
                        }
                    });
                }
                return true;
            }
            if (e2.getX() - e1.getX() > 0) {
                if(hv.getScrollX()<getWidth()){
                    hv.post(new Runnable(){
                        @Override
                        public void run(){
                            hv.smoothScrollToSlow(0,0,1000);
                        }
                    });
                }else if(hv.getScrollX()<2*getWidth()){
                    hv.post(new Runnable(){
                        @Override
                        public void run(){
                            hv.smoothScrollToSlow(getWidth(),0,1000);
                        }
                    });
                }else if(hv.getScrollX()<3*getWidth()){
                    hv.post(new Runnable(){
                        @Override
                        public void run(){
                            hv.smoothScrollToSlow(2*getWidth(),0,1000);
                        }
                    });
                }
                return true;
            }
            return false;
        }
    }

    @Override
    protected void onStart() {
        GetFloorTASK getFloorTASK=new GetFloorTASK(userid);
        getFloorTASK.execute();
        super.onStart();
    }
}