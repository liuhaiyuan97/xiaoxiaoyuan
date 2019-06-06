package com.google.blockly.android.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity{
    private Button btnLogin;
    private Button btnRegister;
    private ImageView ivSetting;
    private ImageView ivSpin;
    private ImageView ivCharts;
    private LinearLayout llMenu;
    private  LinearLayout llAll;
    private boolean state=false;

    private EditText etUserEmailOrPhone;
    private EditText etUserPassword;
    private TextView tvUserNameAndPwdIsReference;
    private TextView tvForgetPassword;
    private MenuFlash menuFlash;
    private ImageView btn_left;
    private ImageView btn_left1;
    private ImageView btn_right;
    private ImageView btn_right1;
    private ImageView ivMe;
    private ImageView ivHome;
    private ImageView ivCommunity;

    private OkHttpClient okHttpClient;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.setClass(LoginActivity.this,HomePage.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        JMessageClient.registerEventReceiver(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
        init();
        menuFlash=new MenuFlash(ivSpin,ivHome,ivCharts,ivCommunity,ivMe);
        menuFlash.play();
        hidTitle();
        //菜单按钮设置点击事件

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
        //忘记密码点击
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvUserNameAndPwdIsReference.setText("");
                String phoneOrEmail=String.valueOf(etUserEmailOrPhone.getText());
                if("".equals(phoneOrEmail) || phoneOrEmail==null){
                    Log.e("LoginActivity","此时忘记填写用户名，无法找回密码");
                    tvUserNameAndPwdIsReference.setText("找回密码必须填写用户名");
                }else if(phoneOrEmail.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")){
                    Log.e("LoginActivity","此时填写用户名，运行异步类");
//                    ForgetPasswordTASK forgetPasswordTASK=new ForgetPasswordTASK(phoneOrEmail);
//                    forgetPasswordTASK.execute();
                    Log.e("LoginActivity","异步类运行结束");
                }else if(phoneOrEmail.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
                    Toast.makeText(LoginActivity.this,"暂不支持手机号修改密码功能",Toast.LENGTH_SHORT).show();
                }



            }
        });



        //登录按钮点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvUserNameAndPwdIsReference.setText("");
                String userPhoneOrEmail=String.valueOf(etUserEmailOrPhone.getText());
                String userPassword=String.valueOf(etUserPassword.getText());
                LoginTASK loginTASK=new LoginTASK(userPhoneOrEmail,userPassword);
                loginTASK.execute();

            }
        });

        //注册按钮设置点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent();
                        intent.setClass(LoginActivity.this,RegisterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        // 去掉系统默认进入的动画效果
                        overridePendingTransition(0, 0);
                    }
                }, 500);

            }
        });

        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
            }
        });

        ivCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent();
                        intent.setClass(LoginActivity.this,HomePage.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                },500);
            }
        });

    }

    @Override
    protected void onStart() {
        open();
        super.onStart();
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

    private void init(){


        llAll = findViewById(R.id.ll_all);
        btnLogin=findViewById(R.id.btn_login);
        btnRegister=findViewById(R.id.btn_register);
        ivSpin=findViewById(R.id.iv_spin);
        llMenu=findViewById(R.id.ll_menu);
       etUserEmailOrPhone=findViewById(R.id.et_userEmailOrPhone);        etUserPassword=findViewById(R.id.et_userPassword);
        tvUserNameAndPwdIsReference=findViewById(R.id.tv_userNameAndPwdIsReferences);
        tvForgetPassword=findViewById(R.id.tv_forget_password);
        ivSetting=findViewById(R.id.iv_setting);
        ivSetting.setVisibility(View.INVISIBLE);
        ivMe=findViewById(R.id.iv_me);
        ivHome=findViewById(R.id.iv_HomePage);
        ivCharts=findViewById(R.id.iv_charts);
        ivCommunity=findViewById(R.id.iv_community);

    }


    public class LoginTASK extends AsyncTask{
        private String userPhoneOrEmail;
        private String userPassword;
        public LoginTASK(String userPhoneOrEmail,String userPassword){
            this.userPhoneOrEmail=userPhoneOrEmail;
            this.userPassword=userPassword;
        }
        @Override
        protected Object doInBackground(Object[] objects) {

//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/loginServlet?userPhoneOrEmail="+userPhoneOrEmail
//                        +"&userPassword="+userPassword);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","UTF-8");
//                int state=connection.getResponseCode();
//                Log.e("msg",state+"");
//
//                Log.e("LoginActivity","获取连接");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                Log.e("LoginActivity","获取输入流");
//                String str=reader.readLine();
//                Log.e("LoginActivity",str);
//                JSONObject object=new JSONObject(str);
//                if(object.getString("isSelect").equals("null")){
//                    Log.e("msg","1");
//                    tvUserNameAndPwdIsReference.setText("此账户并未注册");
//                }else if(object.getString("isSelect").equals("no")){
//                    Log.e("msg","2");
//                    tvUserNameAndPwdIsReference.setText("账户密码不正确");
//                }else{
//                        Log.e("LoginActivity","此时进入登陆页面");
//                        Intent intent=new Intent();
//                        intent.putExtra("isLogin","yes");
//                        intent.putExtra("user",userPhoneOrEmail);
//                        intent.setClass(LoginActivity.this,HomePage.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
//
//
//                }
//
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
                    .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/user/login?phoneOremail="+userPhoneOrEmail+"&password="+userPassword)
                    .build();

            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("str",str);
                if(str.equals("")){
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvUserNameAndPwdIsReference.setText("用户名或密码不正确!");
                        }
                    });


                }else if(new JSONObject(str).getInt("user_id")!=0){
                        Intent intent=new Intent();
                        intent.putExtra("isLogin","yes");
                        intent.putExtra("user",userPhoneOrEmail);
                        intent.putExtra("details",str);
                        intent.setClass(LoginActivity.this,HomePage.class);
                        int id=new JSONObject(str).getInt("user_id");

                        JMessageClient.register("user"+id,"1234",null);
                        JMessageClient.login("user"+id,"1234",null);

                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    //忘记密码发送到邮箱 异步类
//    public class ForgetPasswordTASK extends AsyncTask{
//        String phoneOrEmail;
//        public ForgetPasswordTASK(String phoneOrEmail){
//            this.phoneOrEmail=phoneOrEmail;
//        }
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/forgetPasswordServlet?userPhoneOrEmail="+phoneOrEmail);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","UTF-8");
//                Log.e("ForgetPasswordTASK","此时应该连接到服务器");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                Log.e("ForgetPasswordTASK","获取到输入流");
//                String str=reader.readLine();
//                Log.e("ForgetPasswordTASK","输入流中的信息(字符串形式)"+str);
//                JSONObject object=new JSONObject(str);
//                if(object.getString("isExist").equals("no")){
//                    tvUserNameAndPwdIsReference.setText("账号信息不存在");
//                }else{
//                    if(object.getString("isExist").equals("yes")){
//                        Log.e("ForgetPasswordTASK","邮箱");
//                        tvUserNameAndPwdIsReference.setText("密码已发送到您的邮箱");
//                    }else{
//                        Log.e("ForgetPasswordTASK","电话号码");
//                        String password=String.valueOf(object.getString("password"));
//                        Log.e("ForgetPasswordTASK","此时发送短信，短信内容为密码");
//                        tvUserNameAndPwdIsReference.setText("密码短信已发送到您的手机");
//                    }
//                }
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }

    public void onEvent(MessageEvent event) {
        Message newMessage = event.getMessage();
//        Conversation conversation = JMessageClient.getSingleConversation("1111");
//        final List<Message> mes = conversation.getAllMessage();
//        for (int i = 0; i < mes.size(); i++) {
//            Log.e("11", mes.get(i).toJson());
//        }
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ListView listView=findViewById(R.id.lv);
//                ChatAdapter chatAdapter=new ChatAdapter(MainActivity.this,R.layout.chat,mes);
//                listView.setAdapter(chatAdapter);
//                listView.setDivider(null);
//            }
//        });
    }

    public void onEvent(OfflineMessageEvent event){
//        List<Message> newMessageList=event.getOfflineMessageList();
//        tvReceive.setText(newMessageList.toString());
    }
}
