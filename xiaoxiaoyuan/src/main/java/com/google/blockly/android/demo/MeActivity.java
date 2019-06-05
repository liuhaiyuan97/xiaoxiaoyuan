package com.google.blockly.android.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadAvatarCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.blockly.android.demo.R.mipmap.close;
import static com.google.blockly.android.demo.R.mipmap.ppp8;

public class MeActivity extends AppCompatActivity{
    private ImageView ivSpin;
    private LinearLayout llMenu;
    private boolean state=false;
    private MenuFlash menuFlash;

    private String userPhoneOrEmail;
    private ImageView ivHomePage;
    private ImageView ivUserImage;
    private ImageView ivUserImageHead;
    private Button btnMeHead;
    private EditText etUserName;
    private EditText etUserSex;
    private EditText etUserPhone;
    private EditText etUserEmail;
    private EditText etUserBirthday;
    private TextView tvUserFloor;
    private TextView tvUserPraise;
    private TextView tvUserName;
    private static int userid=0;
    private static String userName="";
    private static String userSex="";
    private static String userPhone="";
    private static String userEmail="";
    private static String userBirthday="";
    private LinearLayout llMeLeftTop;
    private List<Map<String,Object>> dataList;
    private ImageView ivSetting;
    private ImageView ivMe;
    private ImageView ivCommunity;
    private static boolean phone=false;
    private static boolean email=false;

    private JSONObject object;
    private LinearLayout llMeMyFriend;
    private LinearLayout llMeFriendRequest;

    private LinearLayout llMeRightCenter;
    //private LinearLayout llMeRightMyInformation;
    private LinearLayout llMeRightMyFriend;
    private ListView lvRightMyFriend;
    //添加朋友账号的editText Button 和提示信息
    private EditText etAddFriend;
    private Button btnAddFriend;
    private TextView tvAddFriendWarning;

    private TextView tvMeRightTop;
    private List<JSONObject> friendList;
    //好友请求页面的LinearLayout 和 ListView
    private LinearLayout llMeRightRequest;
    private ListView lvMeRightRequest;
    private List<JSONObject>friendRequestList;

    private TextView tvSetting;
    private ImageView ivCharts;
    private View.OnClickListener delete;
    private View.OnClickListener addorignore;
    private String details="";

    private OkHttpClient okHttpClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        hidTitle();
        init();
        menuFlash=new MenuFlash(ivSpin,ivHomePage,ivCharts,ivCommunity,ivMe);
        menuFlash.play();


        //隐藏状态栏
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

        //运行获取所有信息的异步类

        //修改头像
        btnMeHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow();

            }
        });

        //修改用户名
        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String nowUserName=etUserName.getText().toString();
                if(b==false) {
                    if (!userName.equals(nowUserName)) {
                        ShowAlertDialog("userName", userid, nowUserName, etUserName, userName);
                        tvUserName.setText(nowUserName);
                    }
                }
            }
        });
        etUserSex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String nowUserSex=etUserSex.getText().toString();
                if(b==false) {
                    if (!userSex.equals(nowUserSex)) {
                        ShowAlertDialog("userSex", userid, nowUserSex, etUserSex, userSex);
                    }
                }
            }
        });
        etUserPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String nowUserPhone=etUserPhone.getText().toString();
                if(b==false) {
                    if (!userPhone.equals(nowUserPhone)) {
                        ShowAlertDialog("userPhone", userid, nowUserPhone, etUserPhone, userPhone);
                    }
                    if (userPhoneOrEmail.matches("^1[3|4|5|8][0-9]\\d{8}$")) {
                        userPhoneOrEmail = nowUserPhone;
                    }
                }
            }
        });
        etUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String nowUserEmail=etUserEmail.getText().toString();
                if(b==false) {
                    if (!userEmail.equals(nowUserEmail)) {
                        ShowAlertDialog("userEmail", userid, nowUserEmail, etUserEmail, userEmail);
                    }
                    if (userPhoneOrEmail.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")) {
                        userPhoneOrEmail = nowUserEmail;
                    }
                }
            }
        });
        etUserBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String nowUserBirthday=etUserBirthday.getText().toString();
                if(b==false) {
                    if (!userBirthday.equals(nowUserBirthday)) {
                        ShowAlertDialog("userBirthday", userid, nowUserBirthday, etUserBirthday, userBirthday);
                    }
                }
            }
        });

        ivHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("isAlter","yes");
                intent.putExtra("user",userPhoneOrEmail);
                intent.setClass(MeActivity.this,HomePage.class);
                startActivity(intent);
            }
        });

        llMeLeftTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MeActivity","此时点击了基本信息");
                Intent intent=new Intent();
                intent.setClass(MeActivity.this,MeActivity.class);
                intent.putExtra("user",userPhoneOrEmail);
                intent.putExtra("details",details);
                startActivity(intent);
            }
        });

        ivCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MeActivity.this,RankActivity.class);
                intent.putExtra("user",userPhoneOrEmail);
                startActivity(intent);
            }
        });

        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeActivity.this,"已经在我的页面啦",Toast.LENGTH_SHORT).show();
            }
        });


        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MeActivity.this,HomePage.class);
                intent.putExtra("IsLogin","no");
                details="";
                startActivity(intent);
            }
        });

        delete=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!friendList.isEmpty()){
                    Log.e("MeActivity","此时好友列表不为空，清空好友列表");
                    int length=friendList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendList.remove(i);
                    }
                }
                Log.e("MeActivity","此时点击我的好友列表项");
                tvMeRightTop.setText("我的好友");
                llMeRightCenter.removeAllViews();
                if(!friendRequestList.isEmpty()){
                    Log.e("MeActivity","此时好友请求列表不为空，移除掉所有内容");
                    int length=friendRequestList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendRequestList.remove(i);
                    }
                }

                View view0=LayoutInflater.from(MeActivity.this).inflate(R.layout.layout_me_my_friend,null);
                //获取搜索的朋友账号和按钮
                etAddFriend=view0.findViewById(R.id.et_add_friend);
                btnAddFriend=view0.findViewById(R.id.btn_add_friend);
                tvAddFriendWarning=view0.findViewById(R.id.tv_add_friend_warning);
                //获取ListView控件
                lvRightMyFriend=view0.findViewById(R.id.lv_my_friend);
                //获取数据,创建Adapter对象，为控件设置adapter
                GetMyFriendListTASK getMyFriendListTASK=new GetMyFriendListTASK(userid);
                getMyFriendListTASK.execute();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //创建Adapter对象
                MyFriendAdapter myFriendAdapter=new MyFriendAdapter(MeActivity.this,R.layout.layout_me_my_friend_item,friendList);
                lvRightMyFriend.setAdapter(myFriendAdapter);

                lvRightMyFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Log.e("xinxi",friendList.get(i).getString("user_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Log.e("getFriendListTASK","为控件设置adapter");

                llMeRightMyFriend=view0.findViewById(R.id.ll_me_right_my_friend);
                llMeRightCenter.addView(llMeRightMyFriend);
                llMeRightMyFriend.setVisibility(View.VISIBLE);

//                //为添加好友设置点击事件
//                btnAddFriend.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        final String friend=etAddFriend.getText().toString();
//                        Log.e("MeActivity","点击搜索按钮friend:"+friend);
//                        if(friend==null || friend.equals("")){
//                            Log.e("MeActivity","此时搜索好友账户为空");
//                            tvAddFriendWarning.setTextColor(Color.rgb(255,0,0));
//                            tvAddFriendWarning.setText("账户不得为空");
//
//                        }
//                        else if(friend.equals(userPhoneOrEmail)){
//                            tvAddFriendWarning.setTextColor(Color.rgb(255,0,0));
//                            tvAddFriendWarning.setText("不得添加自己为好友");
//
//                        }else{
//                            Log.e("MeActivity","此时搜索好友账户不为空");
//                            if(friend.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?") || friend.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
//                                Log.e("MeActivity","此时用户搜索好友电话号码或者邮箱格式正确");
//                                SendFriendRequestTASK sendFriendRequestTASK=new SendFriendRequestTASK(userid,friend);
//                                sendFriendRequestTASK.execute();
//                            }else{
//                                tvAddFriendWarning.setTextColor(Color.rgb(255,0,0));
//                                tvAddFriendWarning.setText("好友账号格式不对");
//
//                            }
//                        }
//                    }
//                });

                etAddFriend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b==true)
                            tvAddFriendWarning.setText("");
                    }
                });
            }
        };


        //点击显示我的好友页面
        llMeMyFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!friendList.isEmpty()){
                    Log.e("MeActivity","此时好友列表不为空，清空好友列表");
                    int length=friendList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendList.remove(i);
                    }
                }
                Log.e("MeActivity","此时点击我的好友列表项");
                tvMeRightTop.setText("我的好友");
                llMeRightCenter.removeAllViews();
                if(!friendRequestList.isEmpty()){
                    Log.e("MeActivity","此时好友请求列表不为空，移除掉所有内容");
                    int length=friendRequestList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendRequestList.remove(i);
                    }
                }

                View view0=LayoutInflater.from(MeActivity.this).inflate(R.layout.layout_me_my_friend,null);
                //获取搜索的朋友账号和按钮
                etAddFriend=view0.findViewById(R.id.et_add_friend);
                btnAddFriend=view0.findViewById(R.id.btn_add_friend);
                tvAddFriendWarning=view0.findViewById(R.id.tv_add_friend_warning);
                //获取ListView控件
                lvRightMyFriend=view0.findViewById(R.id.lv_my_friend);
                //获取数据,创建Adapter对象，为控件设置adapter
                GetMyFriendListTASK getMyFriendListTASK=new GetMyFriendListTASK(userid);
                getMyFriendListTASK.execute();

                lvRightMyFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            Log.e("xinxi",friendList.get(i).getString("user_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



                //创建Adapter对象

                Log.e("getFriendListTASK","为控件设置adapter");

                llMeRightMyFriend=view0.findViewById(R.id.ll_me_right_my_friend);
                llMeRightCenter.addView(llMeRightMyFriend);
                llMeRightMyFriend.setVisibility(View.VISIBLE);

                //为添加好友设置点击事件
                btnAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String friend=etAddFriend.getText().toString();
                        Log.e("MeActivity","点击搜索按钮friend:"+friend);
                        if(friend==null || friend.equals("")){
                            Log.e("MeActivity","此时搜索好友账户为空");
                            tvAddFriendWarning.setText("账户不得为空");
                            tvAddFriendWarning.setTextColor(Color.rgb(255,44,0));
                        }
                        else if(friend.equals(userPhoneOrEmail)){
                            tvAddFriendWarning.setText("不得添加自己为好友");
                            tvAddFriendWarning.setTextColor(Color.rgb(255,44,0));
                        }else{
                            Log.e("MeActivity","此时搜索好友账户不为空");
                            if(friend.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?") || friend.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
                                Log.e("MeActivity","此时用户搜索好友电话号码或者邮箱格式正确");
                                SendFriendRequestTASK sendFriendRequestTASK=new SendFriendRequestTASK(userid,friend);
                                sendFriendRequestTASK.execute();
                            }else{
                                tvAddFriendWarning.setTextColor(Color.rgb(255,0,0));
                                tvAddFriendWarning.setText("好友账号格式不对");
                            }
                        }
                    }
                });

                etAddFriend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b==true)
                        tvAddFriendWarning.setText("");
                    }
                });

            }
        });
        addorignore=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MeActivity","此时点击好友请求列表项");
                tvMeRightTop.setText("好友请求");
                llMeRightCenter.removeAllViews();

                if(!friendRequestList.isEmpty()){
                    Log.e("MeActivity","此时好友请求列表不为空，移除掉所有内容");
                    int length=friendRequestList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendRequestList.remove(i);
                    }
                }
                if(!friendList.isEmpty()){
                    Log.e("MeActivity","此时好友列表不为空，清空好友列表");
                    int length=friendList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendList.remove(i);
                    }
                }
                View view1=LayoutInflater.from(MeActivity.this).inflate(R.layout.layout_me_request,null);
                llMeRightRequest=view1.findViewById(R.id.ll_me_right_my_request);
                //获取listview控件
                lvMeRightRequest=view1.findViewById(R.id.lv_my_request);
                //获取数据
                GetFriendRequestTASK getFriendRequestTASK=new GetFriendRequestTASK(userid);
                getFriendRequestTASK.execute();



            }
        };

        //点击显示好友请求页面
        llMeFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MeActivity","此时点击好友请求列表项");
                tvMeRightTop.setText("好友请求");
                llMeRightCenter.removeAllViews();

                if(!friendRequestList.isEmpty()){
                    Log.e("MeActivity","此时好友请求列表不为空，移除掉所有内容");
                    int length=friendRequestList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendRequestList.remove(i);
                    }
                }
                if(!friendList.isEmpty()){
                    Log.e("MeActivity","此时好友列表不为空，清空好友列表");
                    int length=friendList.size()-1;
                    for(int i=length;i>=0;i--){
                        friendList.remove(i);
                    }
                }
                View view1=LayoutInflater.from(MeActivity.this).inflate(R.layout.layout_me_request,null);
                llMeRightRequest=view1.findViewById(R.id.ll_me_right_my_request);
                //获取listview控件
                lvMeRightRequest=view1.findViewById(R.id.lv_my_request);
                //获取数据
                GetFriendRequestTASK getFriendRequestTASK=new GetFriendRequestTASK(userid);
                getFriendRequestTASK.execute();
                //此时让主线程等2秒（异步类执行需要时间）


                //创建adapter对象
                MyRequestAdapter myRequestAdapter=new MyRequestAdapter(MeActivity.this,R.layout.layout_me_request_item,friendRequestList);
                //为控件设置adapter
                lvMeRightRequest.setAdapter(myRequestAdapter);
//                Log.e("FriendRequestListTASK","为控件设置adapter");

//                llMeRightCenter.addView(llMeRightRequest);
//                llMeRightRequest.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * 弹出选择头像的popWindow
     */
    public void showPopWindow(){
        //1.创建popupWindow显示view
        View view=getLayoutInflater().inflate(R.layout.layout_me_head,null);
        //2.创建PopUpWindow
        final PopupWindow popupWindow=new PopupWindow(MeActivity.this);
        //3.设置PopupWindow的长宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //4.设置popupWindow显示的view
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        //5.获取相应控件
        GridView gridView=view.findViewById(R.id.gv_me_head);
        //6.获取数据
        initHeadImageData();
        //7.创建Adapter
        GridViewAdapter adapter=new GridViewAdapter(MeActivity.this,
                R.layout.layout_me_head_image,dataList);
        gridView.setAdapter(adapter);
        //8.设置监听器
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("popupWindow","点击按钮弹出头像");
                Map<String ,Object>map=dataList.get(i);
                int imageId=(int)map.get("images");
                //String strImage=String.valueOf(image);
                Log.e("popUpWindow","imageId:()"+imageId);
                ivUserImage.setImageResource(imageId);
                ivUserImageHead.setImageResource(imageId);
                popupWindow.dismiss();
                AlterUserImageTASK task=new AlterUserImageTASK(userid,imageId);
                task.execute();

            }
        });
        popupWindow.showAsDropDown(btnMeHead,-700,0);


    }
    /**
     * 创建Adapter
     */
    public class GridViewAdapter extends BaseAdapter{
        private Context context;
        private int itemLayoutId;
        private List<Map<String,Object>>datalist;
        public GridViewAdapter(Context context,int itemLayoutId,List<Map<String,Object>>datalist){
            this.context=context;
            this.itemLayoutId=itemLayoutId;
            this.datalist=datalist;
        }
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return datalist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position,
                            View convertView,
                            ViewGroup viewGroup) {
            if(convertView==null){
                LayoutInflater inflater=LayoutInflater.from(context);
                convertView=inflater.inflate(itemLayoutId,null);
                Log.e("GridViewAdapter","convertView");
            }
            ImageView imageView=convertView.findViewById(R.id.iv_me_head_images);
            Map<String,Object>map=dataList.get(position);
            imageView.setImageResource((int)map.get("images"));

            return convertView;
        }
    }

    public class MyFriendAdapter extends BaseAdapter{
        private Context context;
        private int itemLayoutId;
        private List<JSONObject>data;
        public MyFriendAdapter(Context context,int itemLayoutId,List<JSONObject>data){
            this.context=context;
            this.itemLayoutId=itemLayoutId;
            this.data=data;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position,
                            View convertView,
                            ViewGroup viewGroup) {
            if(convertView==null){
                convertView=LayoutInflater.from(context).inflate(itemLayoutId,null);
            }
            ImageView ivFriendHead=convertView.findViewById(R.id.iv_friend_head);
            TextView tvFriendName=convertView.findViewById(R.id.tv_friend_name);
            TextView tvFriendThree=convertView.findViewById(R.id.tv_friend_birthday);
            final Button btnFriendDelete=convertView.findViewById(R.id.btn_friend_delete);
            final JSONObject object=data.get(position);
            try {
                if(object.getInt("user_image")==0){
                    ivFriendHead.setImageResource(R.mipmap.ic_launcher);
                }else {
                    ivFriendHead.setImageResource(object.getInt("user_image"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                tvFriendName.setText(object.getString("user_name"));
                tvFriendThree.setText("停留关卡:"+object.getString("user_current_floor"));
            } catch (JSONException e) {
                e.printStackTrace();
            }



            btnFriendDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MeActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                    Log.e("MeActivity","点击了删除按钮" );
                    DeleteFriendTASK deleteFriendTASK= null;
                    try {
                        deleteFriendTASK = new DeleteFriendTASK(userid,object.getInt("user_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    deleteFriendTASK.execute();
                    Log.e("MeActivity","运行异步类删除好友完毕" );
                    friendList.remove(position);
                    delete.onClick(btnFriendDelete);
                }
            });
            return convertView;
        }
    }

    /**
     * 好友请求列表adapter
     */
    public class MyRequestAdapter extends BaseAdapter{
        private Context context;
        private int itemLayoutId;
        private List<JSONObject> data;
        public MyRequestAdapter(Context context,int itemLayoutId,List<JSONObject> data){
            this.context=context;
            this.itemLayoutId=itemLayoutId;
            this.data=data;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if(convertView==null){
                convertView=LayoutInflater.from(context).inflate(itemLayoutId,null);
            }
            ImageView ivRequestImage=convertView.findViewById(R.id.iv_request_image);
            TextView tvRequestName=convertView.findViewById(R.id.tv_request_name);
            TextView tvRequestThree=convertView.findViewById(R.id.tv_request_three);
            final JSONObject map=data.get(i);
            try {
                if(map.getInt("user_image")!=0){
                    ivRequestImage.setImageResource((int)map.getInt("user_image"));
                    tvRequestName.setText(map.getString("user_name"));
                    tvRequestThree.setText("当前层数:"+map.getString("user_current_floor"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            final Button btnRequestAccept=convertView.findViewById(R.id.btn_request_accept);
            final Button btnRequestIgnore=convertView.findViewById(R.id.btn_request_ignore);
            btnRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MeActivity.this,"已接受",Toast.LENGTH_SHORT).show();
                    DealFriendRequestTASK dealFriendRequestTASK= null;
                    try {
                        Log.e("GetFriendRequestAdapter","点击了接受按钮");
                        Log.e("GetFriendRequestAdapter",map.getInt("user_id")+"");
                        dealFriendRequestTASK = new DealFriendRequestTASK("true",userid,map.getInt("user_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dealFriendRequestTASK.execute();
                    addorignore.onClick(btnRequestAccept);
                }
            });
            btnRequestIgnore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MeActivity.this,"已忽略",Toast.LENGTH_SHORT).show();

                    DealFriendRequestTASK dealFriendRequestTASK= null;
                    try {
                        Log.e("GetFriendRequestAdapter","点击了忽略按钮");
                        Log.e("GetFriendRequestAdapter",(int)map.getInt("user_id")+"");
                        dealFriendRequestTASK = new DealFriendRequestTASK("false",userid,(int)map.getInt("user_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dealFriendRequestTASK.execute();
                    addorignore.onClick(btnRequestIgnore);
                }
            });
            return convertView;
        }
    }

    /**
     * 初始化头像的信息
     */
    public void initHeadImageData(){
        int[] images={R.mipmap.head1,R.mipmap.head2,R.mipmap.head3,R.mipmap.head4,R.mipmap.head5,
                R.mipmap.head6,R.mipmap.head7,R.mipmap.head8,R.mipmap.head9,R.mipmap.head10,R.mipmap.head11,
                R.mipmap.head12,R.mipmap.head13,
                R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,R.mipmap.p6,R.mipmap.p7,R.mipmap.p8,
                R.mipmap.p9,R.mipmap.p10,R.mipmap.p11,R.mipmap.p12,R.mipmap.p13,R.mipmap.p14,
                R.mipmap.p15,R.mipmap.p16,R.mipmap.sy1,R.mipmap.sy3,
                R.mipmap.pp1, R.mipmap.pp2,R.mipmap.pp3,R.mipmap.pp4,R.mipmap.pp5,R.mipmap.pp6,
                R.mipmap.pp7,R.mipmap.pp8,
                R.mipmap.ppp1, R.mipmap.ppp2,R.mipmap.ppp3,R.mipmap.ppp4,R.mipmap.ppp5,R.mipmap.ppp6,
                R.mipmap.ppp7, ppp8,R.mipmap.ppp9, R.mipmap.ppp10,R.mipmap.ppp11,R.mipmap.ppp12,
                R.mipmap.ppp13,R.mipmap.ppp14, R.mipmap.ppp15,R.mipmap.ppp16,R.mipmap.ppp17, R.mipmap.ppp18,
                R.mipmap.ppp19,R.mipmap.ppp20,R.mipmap.ppp21,R.mipmap.ppp22,
                R.mipmap.ppp23,R.mipmap.ppp24,R.mipmap.ppp25,R.mipmap.ppp26};
        dataList=new ArrayList<Map<String,Object>>();
        for(int i=0;i<images.length;++i){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("images",images[i]);
            dataList.add(map);
        }


    }

    /**
     * 弹出修改信息的对话框
     */
    public void ShowAlertDialog(final String ty, final int id,final String in,final EditText et,final String pIn){
        //1.创建构造器
        AlertDialog.Builder builder=new AlertDialog.Builder(MeActivity.this);
        //2.对构造器进行设置
        builder.setTitle("提示信息");
        if(!in.equals("")) {
            if(ty.equals("userName")||ty.equals("userSex")){
                builder.setMessage("是否修改为" + in + "?");
            }else if(ty.equals("userPhone")){
                if(in.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
                    builder.setMessage("是否修改为" + in + "?");
                }else{
                    builder.setMessage("手机号输入错误,请重新输入");
                }
            }else if(ty.equals("userEmail")){
                if(in.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")){
                    builder.setMessage("是否修改为" + in + "?");
                }else{
                    builder.setMessage("邮箱输入错误，请重新输入");
                }
            }else if(ty.equals("userBirthday")){
                builder.setMessage("是否修改为" + in + "?");
            }
            //3.设置对话框按钮，并绑定监听器
            if((ty.equals("userEmail")&&in.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?"))||(ty.equals("userPhone")&&in.matches("^1[3|4|5|7|8][0-9]\\d{8}$"))||ty.equals("userBirthday")||ty.equals("userName")||ty.equals("userSex")) {
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlterUserInformationTASK task = new AlterUserInformationTASK(ty, id, in, et, pIn);
                        task.execute();
                        if (ty.equals("userName")) {
                            userName = in;
                            UserInfo userInfo=new UserInfo() {
                                @Override
                                public String getNotename() {
                                    return null;
                                }

                                @Override
                                public String getNoteText() {
                                    return null;
                                }

                                @Override
                                public long getBirthday() {
                                    return 0;
                                }

                                @Override
                                public File getAvatarFile() {
                                    return null;
                                }

                                @Override
                                public void getAvatarFileAsync(DownloadAvatarCallback downloadAvatarCallback) {

                                }

                                @Override
                                public void getAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

                                }

                                @Override
                                public File getBigAvatarFile() {
                                    return null;
                                }

                                @Override
                                public void getBigAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

                                }

                                @Override
                                public int getBlacklist() {
                                    return 0;
                                }

                                @Override
                                public int getNoDisturb() {
                                    return 0;
                                }

                                @Override
                                public boolean isFriend() {
                                    return false;
                                }

                                @Override
                                public String getAppKey() {
                                    return null;
                                }

                                @Override
                                public void setUserExtras(Map<String, String> map) {

                                }

                                @Override
                                public void setUserExtras(String s, String s1) {

                                }

                                @Override
                                public void setBirthday(long l) {

                                }

                                @Override
                                public void setNoDisturb(int i, BasicCallback basicCallback) {

                                }

                                @Override
                                public void removeFromFriendList(BasicCallback basicCallback) {

                                }

                                @Override
                                public void updateNoteName(String s, BasicCallback basicCallback) {

                                }

                                @Override
                                public void updateNoteText(String s, BasicCallback basicCallback) {

                                }

                                @Override
                                public String getDisplayName() {
                                    return null;
                                }
                            };
                            userInfo.setNickname(in);
                            JMessageClient.updateMyInfo(UserInfo.Field.nickname,userInfo,null);
                        } else if (ty.equals("userSex")) {
                            userSex = in;
                        } else if (ty.equals("userPhone")) {
                            userPhone = in;
                        } else if (ty.equals("userEmail")) {
                            userEmail = in;
                        } else if (ty.equals(userBirthday)) {
                            userBirthday = in;
                        }

                    }
                });
            }
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    et.setText(pIn);
                    if (ty.equals("userName")) {
                        tvUserName.setText(pIn);
                    }
                }
            });
        }else{
            builder.setMessage("是否修改为空?");
            //3.设置对话框按钮，并绑定监听器
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlterUserInformationTASK task = new AlterUserInformationTASK(ty, id, "", et, pIn);
                    task.execute();
                    if (ty.equals("userName")) {
                        userName = in;
                    } else if (ty.equals("userSex")) {
                        userSex = in;
                    } else if (ty.equals("userPhone")&&in.matches("^1[3|4|5|7|8][0-9]\\d{8}$")) {
                        userPhone = in;
                    } else if (ty.equals("userEmail")&&in.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")) {
                        userEmail = in;
                    } else if (ty.equals("userBirthday")) {
                        userBirthday = in;
                    }

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    et.setText(pIn);
                    if (ty.equals("userName")) {
                        tvUserName.setText(pIn);
                    }else if(ty.equals("userPhone")){
                        etUserPhone.setText(pIn);
                    }
                }
            });
        }
        //4.使用构造器builder构造出一个AlertDialog对象
        AlertDialog alertDialog=builder.create();
        //5.设置对话框点击其他地方不消失
        alertDialog.setCancelable(false);
        //6.显示对话框
        alertDialog.show();


    }
    /**
     * 处理好友请求的异步类
     */
    public class DealFriendRequestTASK extends AsyncTask{
        private String type;
        private int id;
        private int friendId;

        public DealFriendRequestTASK(String type,int id,int friendId){
            this.type=type;
            this.id=id;
            this.friendId=friendId;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/DealFriendRequestServlet?type="+type+"&user="+user+"&friendId="+friendId);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//
//                Log.e("DealFriendRequestTASK","与服务器获取连接");
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("DealFriendRequestTASK","输入流中的信息（字符串形式）："+str);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            okHttpClient=new OkHttpClient();
            Request request=new Request.Builder()
                            .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/friends/updateRequest?type="+type+"&id="+id+"&friendId="+friendId)
                            .build();
            Call call=okHttpClient.newCall(request);

            try {
                Response response=call.execute();
                String str=response.body().string();
                String sub=str.substring(0,str.length()-1);
                if(sub.equals("true")){
                    Log.e("sub","接收好友请求");
                }else if(sub.equals("false")){
                    Log.e("sub","忽略好友请求");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    /**
     * 获取好友请求的异步类
     */
    public class GetFriendRequestTASK extends AsyncTask {
        private int id;
        public GetFriendRequestTASK(int id){
            this.id=id;
        }
        @Override
        protected Object doInBackground(Object[] objects) {

//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/getFriendRequestServlet?user="+user);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//                Log.e("GetFriendRequestTask","此时与服务器获得连接");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("GetFriendRequestTask","输入流中的信息（字符串形式）："+str);
//                JSONArray array=new JSONArray(str);
//                for(int i=0;i<array.length();i++ ){
//                    JSONObject object=array.getJSONObject(i);
//                    Map<String,Object>map=new HashMap<String, Object>();
//                    map.put("friendId",object.getInt("friendId"));
//                    map.put("friendImage",object.getInt("friendImage"));
//                    map.put("friendName",object.getString("friendName"));
//                    map.put("friendThree",object.getString("friendThree"));
//                    friendRequestList.add(map);
//                }
//                Log.e("GetFriendRequestTASK","好友请求列表："+friendRequestList);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
                okHttpClient=new OkHttpClient();
                Request request=new Request.Builder()
                                .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/friends/selectRequest?id="+id)
                                .build();
                Call call=okHttpClient.newCall(request);

            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("selectRequet",str);

                JSONArray array=new JSONArray(str);
                friendRequestList=new ArrayList<>();
                for(int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);
                    friendRequestList.add(object);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            //创建adapter对象
            MyRequestAdapter myRequestAdapter=new MyRequestAdapter(MeActivity.this,R.layout.layout_me_request_item,friendRequestList);
            //为控件设置adapter
            lvMeRightRequest.setAdapter(myRequestAdapter);
            Log.e("FriendRequestListTASK","为控件设置adapter");

            llMeRightCenter.addView(llMeRightRequest);
            llMeRightRequest.setVisibility(View.VISIBLE);
            super.onProgressUpdate(values);
        }
    }


    /**
     * 发送好友请求，以及判断好友是否存在的异步类
     */
    public class SendFriendRequestTASK extends AsyncTask{
        int id;
        String friend;
        public SendFriendRequestTASK(int id,String friend){
            this.id=id;
            this.friend=friend;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/sendFriendRequestServlet?user="+user+"&friend="+friend);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//                Log.e("MeActivity","此时与服务器获取连接");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("MeActivity","此时获取到输入流（字符串形式）"+str);
//                object=new JSONObject(str);
//                publishProgress(object);
//                MeActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            tvAddFriendWarning.setText(object.getString("warning"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                //关闭输入流
//                inputStream.close();
//                inputStreamReader.close();
//                reader.close();
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
                            .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/friends/insertRequest?id="+id+"&friend="+friend)
                            .build();
            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                String sub=str.substring(0,str.length());
                Log.e("addfriend",sub);
                if(sub.equals("send")){
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAddFriendWarning.setText("已发送好友请求");
                            tvAddFriendWarning.setTextColor(Color.rgb(0,255,33));
                        }
                    });
                }else if(sub.equals("yourself")){
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAddFriendWarning.setText("不能添加自己为好友");
                            tvAddFriendWarning.setTextColor(Color.rgb(255,66,0));
                        }
                    });
                }else if(sub.equals("notexit")){
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAddFriendWarning.setText("该用户不存在");
                            tvAddFriendWarning.setTextColor(Color.rgb(255,66,0));
                        }
                    });
                }else if(sub.equals("repeatadd")){
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAddFriendWarning.setText("不能重复添加");
                            tvAddFriendWarning.setTextColor(Color.rgb(255,66,0));
                        }
                    });
                }else if(sub.equals("repeatsend")){
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAddFriendWarning.setText("请勿重复发送");
                            tvAddFriendWarning.setTextColor(Color.rgb(255,66,0));
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            tvAddFriendWarning.setTextColor(Color.rgb(0,255,0));
            super.onProgressUpdate(values);
        }
    }
    /**
     * 获取所有好友的异步类
     */
    public class GetMyFriendListTASK extends AsyncTask{
        private int id;
        public GetMyFriendListTASK(int id){
            this.id=id;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/getFriendListServlet?user="+userPhoneOrEmail);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//                Log.e("getFriendListTASK","此时与服务器端产生连接");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("getFriendListTASK","输入流中的信息(字符串形式)："+str);
//                JSONArray array=new JSONArray(str);
//                for(int i=0;i<array.length();i++ ){
//                    JSONObject object=array.getJSONObject(i);
//                    Map<String,Object>map=new HashMap<String, Object>();
//                    map.put("friendId",object.getInt("friendId"));
//                    map.put("friendImage",object.getInt("friendImage"));
//                    map.put("friendName",object.getString("friendName"));
//                    map.put("friendThree",object.getString("friendThree"));
//                    friendList.add(map);
//                }
//                Log.e("getFriendListTASK","friendList: "+friendList.toString());
//                Log.e("getFriendListTASK","friendList.size(): "+friendList.size());
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
                    .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/friends/findAllFriends?id="+id)
                    .build();

            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("findAlFriends",str);
                JSONArray array=new JSONArray(str);
                friendList=new ArrayList<>();
                if(array.length()!=0){
                    for(int i=0;i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        friendList.add(object);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            for(int i=0;i<friendList.size();i++){
                Log.e("friendlist",friendList.get(i).toString());
            }
            MyFriendAdapter myFriendAdapter=new MyFriendAdapter(MeActivity.this,R.layout.layout_me_my_friend_item,friendList);
            lvRightMyFriend.setAdapter(myFriendAdapter);
            super.onProgressUpdate(values);
        }
    }
    /**
     * 修改头像的异步类
     */
    public class AlterUserImageTASK extends AsyncTask{
        int userid;
        int imageId;
        public AlterUserImageTASK(int userid,int imageId){
            this.userid=userid;
            this.imageId=imageId;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/alterUserImageServlet?userPhoneOrEmail="+userPhoneOrEmail+"&imageId="+imageId);
//                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("content-type","utf-8");
//
//                InputStream inputStream=conn.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("MeActivity","获取到的输入流（字符串形式）"+str);
//                JSONObject object=new JSONObject(str);
//                if(object.getString("isAlter").equals("yes")){
//                    Log.e("MeActivity","数据库中改变头像");
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
                            .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/user/updateUserImage?id="+userid+"&imageid="+imageId)
                            .build();

            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("str",str);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }




    /**
     * 确定修改用户信息后执行的异步类
     */
    public class AlterUserInformationTASK extends AsyncTask{
        private String type;
        private int id;
        private String information;
        private EditText et;
        private String pInfor;
        public AlterUserInformationTASK(String type,int id,String information,EditText et,String pInfor){
            this.type=type;
            this.id=id;
            this.information=information;
            this.et=et;
            this.pInfor=pInfor;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/alterUserInformationServlet?type="+type+"&userPhoneOrEmail="+userPhoneOrEmail+"&information="+information);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//                Log.e("alterInformationTask","此时应该连接上");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("alterInformationTask","输入流中的信息（字符串形式）"+str);
//                JSONObject object=new JSONObject(str);
//                if(object.getString("isAlter").equals("yes")){
//                    Log.e("alterInformationTask","修改信息成功");
//                }else{
//                    et.setText(pInfor);
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
                okHttpClient=new OkHttpClient();
                Request request=new Request.Builder()
                        .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/user/updateUserInfo?id="+id+"&type="+type+"&info="+information)
                        .build();
                Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                if(!str.equals("")){
                    publishProgress();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            et.setText(pInfor);
            if (type.equals("userName")){
                etUserName.setText(information);
            }else if(type.equals("userSex")){
                etUserSex.setText(information);
            }else if(type.equals("userPhone")){
                etUserPhone.setText(information);
            }else if(type.equals("userEmail")){
                etUserEmail.setText(information);
            }else if(type.equals("userBirthday")){
                etUserBirthday.setText(information);
            }
            super.onProgressUpdate(values);
        }
    }

    /**
     * 删除好友异步类
     */
    public class DeleteFriendTASK extends AsyncTask{
        private int id;
        private int friendId;
        public DeleteFriendTASK(int id,int friendId){
            this.id=id;
            this.friendId=friendId;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/deleteFriendServlet?user="+user+"&friendId="+friendId);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//                Log.e("MeActivity","获取到服务器端的连接");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("MeActivity","输入流中的信息（字符串形式）："+str);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
                okHttpClient=new OkHttpClient();
                Request request=new Request.Builder()
                                .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/friends/deleteFriend?id="+id+"&friendId="+friendId)
                                .build();
                Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                String sub=str.substring(0,str.length()-1);
                if(sub.equals("true")){
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MeActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    MeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MeActivity.this,"删除失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }



    /**
     * 展示用户所有信息的异步类
     */
    public class ShowAllInformationTASK extends AsyncTask{
        int id;
        public ShowAllInformationTASK(int id){
            this.id=id;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/showAllInformationServlet?userPhoneOrEmail="+phoneOrEmail);
//                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//                connection.setRequestProperty("content-type","utf-8");
//                Log.e("MeActivity","此时应获取到连接");
//
//                InputStream inputStream=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String str=reader.readLine();
//                Log.e("MeActivity","输入流中的信息（字符串形式）"+str);
//                object=new JSONObject(str);
//                publishProgress(object);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            okHttpClient =new OkHttpClient();
            Request request=new Request.Builder()
                            .url("http://10.7.88.20:8080/xiaoxiaoyuanssm/user/showAllInfo?id="+id)
                            .build();
            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("str","获取数据"+str);
                if(!str.equals("")){
                    object=new JSONObject(str);
                    publishProgress(object);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            if (object != null) {
                //用户头像
                Log.e("object", object.toString());
                try {
                    Log.e("image", object.getString("user_image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (Integer.valueOf(object.getString("user_image")) != 0) {
                        int imageId = 0;
                        try {
                            imageId = Integer.valueOf(object.getString("user_image"));
                            Log.e("img",R.mipmap.head9+""+R.mipmap.logo);
                            ivUserImage.setImageResource(imageId);
                            ivUserImageHead.setImageResource(imageId);
                            //用户名
                            if (object.getString("user_name") == null || "".equals(object.getString("user_name"))) {
                                etUserName.setTextSize(15);
                            } else {
                                new Thread() {
                                    @Override
                                    public void run() {
                                            MeActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        etUserName.setText(object.getString("user_name"));
                                                        tvUserName.setText(object.getString("user_name"));
                                                        userid = object.getInt("user_id");
                                                        userName = object.getString("user_name");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            super.run();
                                    }
                                }.start();

                            }
                            //用户性别
                            if (object.getString("user_sex") == null || "".equals(object.getString("user_sex"))) {
                                etUserSex.setTextSize(15);
                            } else {
                                etUserSex.setText(object.getString("user_sex"));
                                userSex = object.getString("user_sex");
                            }
                            //手机号码
                            if (object.getString("user_phone") == null || "".equals(object.getString("user_phone"))) {
                                etUserPhone.setTextSize(15);
                            } else {
                                etUserPhone.setText(object.getString("user_phone"));
                                userPhone = object.getString("user_phone");
                            }
                            //电子邮件
                            if (object.getString("user_email") == null || "".equals(object.getString("user_email"))) {
                                etUserEmail.setTextSize(15);
                            } else {
                                etUserEmail.setText(object.getString("user_email"));
                                userEmail = object.getString("user_email");
                            }
                            //出生日期
                            if (object.getString("user_birthday") == null || "".equals(object.getString("user_birthday"))) {
                                etUserBirthday.setTextSize(15);
                            } else {
                                etUserBirthday.setText(object.getString("user_birthday"));
                                userBirthday = object.getString("user_birthday");
                            }
                            //现在层数
                            tvUserFloor.setText(object.getString("user_current_floor"));
                            //点赞数
                            tvUserPraise.setText(object.getString("user_praise"));
                            //Email

                            Log.e("ShowAllInformationTASK", object.getString("user_current_floor") + "");
                            Log.e("ShowAllInformationTASK", object.getString("user_praise") + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                super.onProgressUpdate(values);
            }
        }
    }




    /**
     * 初始化控件
     */
    public void init(){
        ivSpin=findViewById(R.id.iv_spin);
        llMenu=findViewById(R.id.ll_menu);

        Intent intent=getIntent();
        userPhoneOrEmail=intent.getStringExtra("user");
        try {
            userid=new JSONObject(intent.getStringExtra("details")).getInt("user_id");
            details=intent.getStringExtra("details");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(userPhoneOrEmail.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            phone=true;
            email=false;
        }else if(userPhoneOrEmail.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")){
            email=true;
            phone=false;
        }
        etUserName=findViewById(R.id.et_userName);
        etUserSex=findViewById(R.id.et_userSex);
        etUserPhone=findViewById(R.id.et_userPhone);
        etUserEmail=findViewById(R.id.et_userEmail);
        etUserBirthday=findViewById(R.id.et_userBirthday);
        tvUserFloor=findViewById(R.id.tv_userFloor);
        tvUserPraise=findViewById(R.id.tv_userPraise);
        tvUserName=findViewById(R.id.tv_userName);
        ivHomePage=findViewById(R.id.iv_HomePage);
        llMeLeftTop=findViewById(R.id.ll_me_left_top);
        btnMeHead=findViewById(R.id.btn_exitUserImage);
        ivUserImage=findViewById(R.id.iv_userImage);
        ivUserImageHead=findViewById(R.id.iv_head);
        ivSetting=findViewById(R.id.iv_setting);
        ivSetting.setVisibility(View.INVISIBLE);
        ivMe=findViewById(R.id.iv_me);
        ivCommunity=findViewById(R.id.iv_community);
        tvSetting=findViewById(R.id.tv_setting);
        ivCharts=findViewById(R.id.iv_charts);
        llMeMyFriend=findViewById(R.id.ll_me_my_friend);
        llMeFriendRequest=findViewById(R.id.ll_me_friend_request);
        llMeRightCenter=findViewById(R.id.ll_me_right_center);

        //llMeRightMyInformation=findViewById(R.id.ll_me_right_my_information);
        //llMeRightMyFriend=findViewById(R.id.ll_me_right_my_friend);
        tvMeRightTop=findViewById(R.id.tv_me_right_top);
        friendList=new ArrayList<JSONObject>();
        friendRequestList=new ArrayList<>();
        //llMeWholeRight=findViewById(R.id.ll_me_whole_right);

        if(phone==true){
            etUserPhone.setEnabled(false);
        }else if(email==true){
            etUserEmail.setEnabled(false);
        }
    }

    private void hidTitle(){
        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    @Override
    protected void onStart() {
        Intent intent=getIntent();
        try {
            userid=new JSONObject(intent.getStringExtra("details")).getInt("user_id");
            details=intent.getStringExtra("details");

            UserInfo userInfo=new UserInfo() {
                @Override
                public String getNotename() {
                    return null;
                }

                @Override
                public String getNoteText() {
                    return null;
                }

                @Override
                public long getBirthday() {
                    return 0;
                }

                @Override
                public File getAvatarFile() {
                    return null;
                }

                @Override
                public void getAvatarFileAsync(DownloadAvatarCallback downloadAvatarCallback) {

                }

                @Override
                public void getAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

                }

                @Override
                public File getBigAvatarFile() {
                    return null;
                }

                @Override
                public void getBigAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

                }

                @Override
                public int getBlacklist() {
                    return 0;
                }

                @Override
                public int getNoDisturb() {
                    return 0;
                }

                @Override
                public boolean isFriend() {
                    return false;
                }

                @Override
                public String getAppKey() {
                    return null;
                }

                @Override
                public void setUserExtras(Map<String, String> map) {

                }

                @Override
                public void setUserExtras(String s, String s1) {

                }

                @Override
                public void setBirthday(long l) {

                }

                @Override
                public void setNoDisturb(int i, BasicCallback basicCallback) {

                }

                @Override
                public void removeFromFriendList(BasicCallback basicCallback) {

                }

                @Override
                public void updateNoteName(String s, BasicCallback basicCallback) {

                }

                @Override
                public void updateNoteText(String s, BasicCallback basicCallback) {

                }

                @Override
                public String getDisplayName() {
                    return null;
                }
            };
            userInfo.setNickname(new JSONObject(details).getString("user_name"));
            JMessageClient.updateMyInfo(UserInfo.Field.nickname,userInfo,null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ShowAllInformationTASK showAllInformationTASK=new ShowAllInformationTASK(userid);
        showAllInformationTASK.execute();
        super.onStart();
    }
}
