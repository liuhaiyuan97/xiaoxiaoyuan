package com.google.blockly.android.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity{
    private Button btnRegister;
    private TextView tvPhoneRegister;
    private TextView tvEmailRegister;
    private TextView tvUserNameIsRegistered;
    private EditText etUserPhoneOrEmail;
    private TextView tvIsSameTwoPassword;
    private EditText etUserPassword;
    private EditText etReWriteUserPassword;
    private Boolean state=false;
    private Boolean phoneOrEmailState=true;
    private TextView tvAllIsNotNull;
    private ImageView ivSetting;
    private MenuFlash menuFlash;
    private ImageView ivSpin;
    private LinearLayout llMenu;
    private ImageView ivMe;
    private LinearLayout llAll;
    private ImageView btn_left;
    private ImageView btn_left1;
    private ImageView btn_right;
    private ImageView btn_right1;
    private Button btnReturn;
    private ImageView ivHome;
    private ImageView ivCharts;
    private ImageView ivCommunity;

    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //隐藏标题
        hidTitle();
        //初始化
        init();
        menuFlash=new MenuFlash(ivSpin,ivHome,ivCharts,ivCommunity,ivMe);
        menuFlash.play();

        ivSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuFlash.click(state);
                if(state==false){
                    state=true;
                }else{
                    state=false;
                }
            }
        });

        //按钮移动动画
        play();

        //点击返回按钮
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin();
            }
        });



        //获手机号/邮箱转换的点击事件
        tvEmailRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               etUserPhoneOrEmail.setText("");
                etReWriteUserPassword.setText("");
                etUserPassword.setText("");
                Log.e("RegisterActivity","点击之前"+state);
                phoneOrEmailState=!phoneOrEmailState;
                Log.e("RegisterActivity","点击之后"+state);
                if(phoneOrEmailState==false){
                    tvPhoneRegister.setText("邮箱注册");
                    tvEmailRegister.setText("手机号注册");
                    etUserPhoneOrEmail.setHint("请输入您的邮箱");
                    tvUserNameIsRegistered.setText("");
                }else{
                    tvPhoneRegister.setText("手机号注册");
                    tvEmailRegister.setText("邮箱注册");
                    etUserPhoneOrEmail.setHint("请输入您的手机号码");
                    tvUserNameIsRegistered.setText("");
                }
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,HomePage.class);
                startActivity(intent);
            }
        });

        ivCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });

        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end();
                Intent intent=new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        //输入手机号或者邮箱发生焦点移动事件
        etUserPhoneOrEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //获取手机号码或者email
                String phoneOrEmail=String.valueOf(etUserPhoneOrEmail.getText());
                if(phoneOrEmail!=null && !"".equals(phoneOrEmail)) {
                    if (phoneOrEmailState == true) {
                        Log.e("RegisterActivity", phoneOrEmail + " 长度：" + phoneOrEmail.length());
                        if (!phoneOrEmail.matches("^1[3|4|5|7|8][0-9]\\d{8}$") && phoneOrEmail != null && !"".equals(phoneOrEmail)) {
                            tvUserNameIsRegistered.setText("手机号不正确！");
                        } else {
                            //此时判断该手机号是否注册过
                            Log.e("RegisterActivity","判断手机号是否注册过");
                            tvUserNameIsRegistered.setText("");
                            Log.e("RegisterActivity","此时创建异步类对象并传入手机号码:"+phoneOrEmail);
                            isRepeatedPhoneTask isRepeatedPhoneTask=new isRepeatedPhoneTask(phoneOrEmail);
                            Log.e("RegisterActivity","运行异步类");
                            isRepeatedPhoneTask.execute();
                        }
                    } else {
                        //此时用户使用email注册
                        Log.e("RegisterActivity", phoneOrEmail + " 长度：" + phoneOrEmail.length());
                        boolean isEmailForm=isRightEmailForm(phoneOrEmail);
                        if(!isEmailForm){
                            Log.e("RegisterActivity","请输入正确的email");
                            tvUserNameIsRegistered.setText("邮箱不正确！");
                        }else{
                            Log.e("RegisterActivity","此用户填写的是email");
                            tvUserNameIsRegistered.setText("");
                            isRepeatedEmailTask isRepeatedEmailTask=new isRepeatedEmailTask(phoneOrEmail);
                            isRepeatedEmailTask.execute();

                        }
                    }
                }
            }
        });

        //点击注册按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取手机号码或者email
                String phoneOrEmail=String.valueOf(etUserPhoneOrEmail.getText());
                //获取用户密码
                String userPassword=String.valueOf(etUserPassword.getText());
                //获取用户的再依次输入密码
                String reWriteUserPassword=String.valueOf(etReWriteUserPassword.getText());
                Log.e("RegisterActivity","电话或者Email:"+phoneOrEmail+" 用户密码："+userPassword+" 再次输入用户密码:"+reWriteUserPassword);
                if(!phoneOrEmail.equals("") && !userPassword.equals("") && !"".equals(reWriteUserPassword)){
                    tvAllIsNotNull.setText("");
                }
                if(userPassword.equals(reWriteUserPassword)){
                    tvIsSameTwoPassword.setText("");
                }
                //为userPhone或者userEmail赋值
                if(phoneOrEmail.equals("") || userPassword.equals("") || "".equals(reWriteUserPassword)){
                    Log.e("RegisterActivity","此时是否注册、密码是否相同、三个地方全部填写 存在一个地方不为空");
                    tvAllIsNotNull.setText("以上均不为空");
                }else if(!userPassword.equals(reWriteUserPassword)){
                    Log.e("RegisterActivity","此时用户两次填写的密码不同");
                    tvIsSameTwoPassword.setText("两次密码不同");
                }else if(tvIsSameTwoPassword.getText().equals("")
                        && tvUserNameIsRegistered.getText().equals("")
                        && tvAllIsNotNull.getText().equals("")){
                    Log.e("RegisterActivity","此时：此用户并未注册、两次密码相同、信息填写完整");
                    Log.e("RegisterActivity","此时运行异步类将用户信息填入数据库中");
                    insertUserTask insertUserTask=new insertUserTask(phoneOrEmail,userPassword);
                    insertUserTask.execute();

                }

            }
        });


    }

    private void toLogin() {
        end();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // 去掉系统默认进入的动画效果
                overridePendingTransition(0, 0);
            }
        }, 500);
    }

    private void play() {
        btn_left = findViewById(R.id.btn_left);
        ObjectAnimator o1=ObjectAnimator.ofFloat(btn_left,"translationX",0,-10,0).setDuration(1500);
        o1.setRepeatCount(ValueAnimator.INFINITE);
        o1.start();
        btn_right = findViewById(R.id.btn_right);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(btn_right,"translationX",0,10,0).setDuration(1500);
        o2.setRepeatCount(ValueAnimator.INFINITE);
        o2.start();
        btn_left1 = findViewById(R.id.btn_left1);
        ObjectAnimator o3=ObjectAnimator.ofFloat(btn_left1,"translationX",0,-10,0).setDuration(1500);
        o3.setRepeatCount(ValueAnimator.INFINITE);
        o3.start();
        btn_right1 = findViewById(R.id.btn_right1);
        btn_right = findViewById(R.id.btn_right);
        ObjectAnimator o4 = ObjectAnimator.ofFloat(btn_right1,"translationX",0,10,0).setDuration(1500);
        o4.setRepeatCount(ValueAnimator.INFINITE);
        o4.start();
    }

    //淡入
    public void open(){
        llAll = findViewById(R.id.ll_all);
        llAll.setAlpha(0f);
        llAll.setVisibility(View.VISIBLE);
        llAll.animate().alpha(1f)
                .setDuration(500)
                .setListener(null);
    }

    //淡出
    public void end(){
        llAll = findViewById(R.id.ll_all);
        llAll.animate().alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        llAll.setVisibility(View.GONE);
                    }
                });
    }

    //隐藏标题的函数
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
    //初始化控件的函数
    private void init(){
        btnRegister=findViewById(R.id.btn_register);
        tvPhoneRegister=findViewById(R.id.tv_phoneRegister);
        tvEmailRegister=findViewById(R.id.tv_emailRegister);
        etUserPhoneOrEmail=findViewById(R.id.et_userEmailOrPhone);
        tvUserNameIsRegistered=findViewById(R.id.tv_userNameIsRegistered);
        etUserPassword=findViewById(R.id.et_userPassword);
        etReWriteUserPassword=findViewById(R.id.et_reWriteUserPassword);
        tvIsSameTwoPassword=findViewById(R.id.tv_isSameTwoPassword);
        tvAllIsNotNull=findViewById(R.id.tv_allIsNotNull);
        ivSetting=findViewById(R.id.iv_setting);
        ivSetting.setVisibility(View.INVISIBLE);
        ivSpin=findViewById(R.id.iv_spin);
        llMenu=findViewById(R.id.ll_menu);
        ivMe=findViewById(R.id.iv_me);
        btnReturn = findViewById(R.id.btn_return);
        ivHome=findViewById(R.id.iv_HomePage);
        ivCharts=findViewById(R.id.iv_charts);
        ivCommunity=findViewById(R.id.iv_community);
        open();
    }
    public boolean isRightEmailForm(String email) {
        String regex = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

        //boolean isMatched=Pattern.compile(check).matcher(email).matches();
        boolean isMatched=email.matches(regex);
        if(isMatched) {
            Log.e("RegisterActivi  ty","是邮箱！");
            return true;
        }
        Log.e("RegisterActivity","不是邮箱！");
        return false;
    }
    //判断手机号码是否注册过的异步类
    public class isRepeatedPhoneTask extends AsyncTask {
        private String phone;
        //构造方法
        public isRepeatedPhoneTask(String phone) {
            this.phone = phone;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                Log.e("isRepeatedPhoneTask","电话号码："+phone);
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/isRepeatedPhoneServlet?userPhone="+phone);
//                Log.e("isRepeatedPhoneTask","url,此时连接到服务器");
//                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("contentType","UTF-8");
//                Log.e("isRepeatedPhoneTask","获取Connection");
//
//                InputStream in=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(in);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                Log.e("isRepeatedPhoneTask","获取到输入流");
//                String res=reader.readLine();
//                JSONObject object=new JSONObject(res);
//                Log.e("isRepeatedPhoneTask",object.getString("isRegistered"));
//                if(object.getString("isRegistered").equals("yes")){
//                    tvUserNameIsRegistered.setText("此手机号已注册");
//                }
//                in.close();
//                Log.e("isRepeatedPhoneTask","关闭输入流");
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
                    .url("http://10.88.:8080/xiaoxiaoyuanssm/user/isExitPhone?phone="+phone)
                    .build();
            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                if(!str.equals("")){
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvUserNameIsRegistered.setText("此手机号已注册");
                        }
                    });
                }else{
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvUserNameIsRegistered.setText("手机号可用");
                            tvUserNameIsRegistered.setTextColor(Color.rgb(0,255,0));
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    //判断邮箱是否注册过的异步类
    public class isRepeatedEmailTask extends AsyncTask {
        private String email;
        //构造方法
        public isRepeatedEmailTask(String email) {
            this.email = email;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                Log.e("isRepeatedPhoneTask","邮箱："+email);
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/isRepeatedEmailServlet?userEmail="+email);
//                Log.e("isRepeatedPhoneTask","url,此时连接到服务器");
//                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("contentType","UTF-8");
//                Log.e("isRepeatedEmailTask","获取Connection");
//
//                InputStream in=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(in);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                Log.e("isRepeatedEmailTask","获取到输入流");
//                String res=reader.readLine();
//                Log.e("isRepeatedEmailTask","输入流中的消息（字符串形式）："+res);
//                JSONObject object=new JSONObject(res);
//                Log.e("isRepeatedEmailTask",object.getString("isRegistered"));
//                if(object.getString("isRegistered").equals("yes")){
//                    tvUserNameIsRegistered.setText("此邮箱已注册");
//                }
//                in.close();
//                inputStreamReader.close();
//                reader.close();
//                Log.e("isRepeatedEmailTask","关闭输入流");
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
                    .url("http://47.100.52.142:8080/xiaoxiaoyuanssm/user/isExitEmail?email="+email)
                    .build();
            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("邮箱",str);
                if(!str.equals("")){
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvUserNameIsRegistered.setText("此邮箱已被注册");
                        }
                    });
                }else{
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvUserNameIsRegistered.setText("邮箱可用");
                            tvUserNameIsRegistered.setTextColor(Color.rgb(0,255,0));
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public class insertUserTask extends AsyncTask{

        private String phoneOrEmail;
        private String password;
        private insertUserTask(String phoneOrEmail,String  password){
            this.phoneOrEmail=phoneOrEmail;
            this.password=password;
        }
        @Override
        protected Object doInBackground(Object[] objects) {

//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/insertUserServlet?userPhoneOrEmail="+phoneOrEmail+"&userPassword="+password);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","UTF-8");
//                Log.e("insertUserTask","此时连接eclipse服务器");
//                Log.e("insertUserTask","phoneOrEmail:"+phoneOrEmail+" password:"+password);
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                Log.e("insertUserTask","获取到输入流");
//                String str=reader.readLine();
//                Log.e("insertUserTask","输入流中的信息（字符串形式）："+str);
//                JSONObject object=new JSONObject(str);
//                if(object.getString("isInsert").equals("yes")) {
//                    Intent intent=new Intent();
//                    Log.e("insertUserTask","此时创建一个新的Intent");
//                    intent.setClass(RegisterActivity.this,LoginActivity.class);
//                    Log.e("insertUserTask","执行Intent");
//                    startActivity(intent);
//                }
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
                    .url("http://47.100.52.142:8080/xiaoxiaoyuanssm/user/regist?phoneOremail="+phoneOrEmail+"&password="+password)
                    .build();
            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("注册",str);
                if(!str.equals("")){
                    Intent intent=new Intent();
                    Log.e("insertUserTask","此时创建一个新的Intent");
                    intent.setClass(RegisterActivity.this,LoginActivity.class);
                    Log.e("insertUserTask","执行Intent");
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


    }

}
