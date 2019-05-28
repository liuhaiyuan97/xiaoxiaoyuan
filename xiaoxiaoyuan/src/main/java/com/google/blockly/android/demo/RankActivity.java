package com.google.blockly.android.demo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RankActivity extends AppCompatActivity {
    private ImageView ivSetting;
    private TextView friends=null;
    private TextView labbers=null;
    private ImageView ivHome;
    private ImageView ivCharts;
    private ImageView ivCommunity;
    private ImageView ivMe;
    private LinearLayout llMenu;
    private ImageView ivSpin;
    private int[] list;
    private boolean state=false;
    private MenuFlash menuFlash;
    private TextView textView1;
    private TextView textView2;
    private TextView rank=null;
    private View view;
    private List<Map<String,Object>> lists;
    private static String user ="";
    private int msg;
    private JSONArray array;

    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labbersrankactivity);
        friends=findViewById(R.id.fra_friends);
        friends.setOnClickListener(listener);
        hide();
        labbersAdapater();
        init();


        menuFlash=new MenuFlash(ivSpin,ivHome,ivCharts,ivCommunity,ivMe);
        menuFlash.play();
        ivSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuFlash.click(state);
                if (state==true){
                    state=false;
                }else{
                    state=true;
                }
            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RankActivity.this,HomePage.class);
                startActivity(intent);
            }
        });

        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(user)){
                    Intent intent=new Intent(RankActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(RankActivity.this,MeActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });
    }

    //界面监听
    public View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId()==R.id.fra_friends){
                gotoFriends();
                state=false;
            }else if (view.getId()==R.id.fra_labber){
                gotolabbers();
                state=false;
            }
        }
    };

    //    天梯榜界面
    private void gotolabbers() {
        setContentView(R.layout.labbersrankactivity);
        hide();
        friends=findViewById(R.id.fra_friends);
        friends.setOnClickListener(listener);
        init();
        //菜单动画
        menuFlash=new MenuFlash(ivSpin,ivHome,ivCharts,ivCommunity,ivMe);
        menuFlash.play();
        ivSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuFlash.click(state);
                if (state==true){
                    state=false;
                }else{
                    state=true;
                }
            }
        });
        //home页
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                intent.setClass(RankActivity.this,HomePage.class);
                startActivity(intent);
            }
        });
        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(user)){
                    Intent intent=new Intent(RankActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(RankActivity.this,MeActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });


//        加好友

//        查看资料

//      设置构造器
        labbersAdapater();

    }
    //设置构造器
    private void labbersAdapater(){
        List<JSONObject>list=indata2();

    }
    //天梯数据
    private List<JSONObject> indata2(){
        RankList rankList=new RankList();
        rankList.execute();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<JSONObject> data=rankList.labdata();
        Log.e("data3",data.toString());
        return data;
    }
    public class RankList extends AsyncTask {
        List<JSONObject>list=new ArrayList<>();
        @Override
        protected Object doInBackground(Object[] objects) {
//            try {
//                URL url=new URL("http://60.205.183.222:8080/xiaoxiaoyuan/FloorList");
//                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//                connection.setRequestProperty("contentType","UTF-8");
//                Log.e("GetFriendRequestTask","此时与服务器获得连接");
//                InputStream is=connection.getInputStream();
//                InputStreamReader inputStreamReader=new InputStreamReader(is);
//                BufferedReader reader=new BufferedReader(inputStreamReader);
//                String res=reader.readLine();
//                array=new JSONArray(res);
//                for (int i=0;i<array.length();i++){
//                    Map<String,Object>map=new HashMap<>();
//                    JSONObject object=array.getJSONObject(i);
//                    map.put("image",object.getString("image"));
//                    map.put("name",object.getString("name"));
//                    map.put("marks",object.getInt("floor"));
//                    map.put("friends","加好友");
//                    map.put("rank",i+1);
//                    list.add(map);
//                }
//                Log.e("data1",list.toString());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            okHttpClient=new OkHttpClient();
            Request request=new Request.Builder()
                                .url("http://192.168.43.179:8080/xiaoxiaoyuanssm/user/FloorList")
                                .build();

            Call call=okHttpClient.newCall(request);
            try {
                Response response=call.execute();
                String str=response.body().string();
                Log.e("strList",str);
                JSONArray jsonArray=new JSONArray(str);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Log.e("jsonObject",jsonObject.toString());
                    list.add(jsonObject);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            publishProgress();
            return null;
        }

        public List<JSONObject> labdata(){
            Log.e("data2",list.toString());
            return list;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            labbersAdapater labbersAdapater=new labbersAdapater(RankActivity.this,R.layout.labbersranklistactivity,list);
            ListView listView=findViewById(R.id.lv_ranklistlabbers);
            listView.setAdapter(labbersAdapater);
            super.onProgressUpdate(values);
        }
    }
    //天梯榜构造器
    private class labbersAdapater extends BaseAdapter {
        private Context context;
        private int Id;
        private List<JSONObject> data;

        public labbersAdapater(Context context,int Id,List<JSONObject> data){
            this.context=context;
            this.Id=Id;
            this.data=data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int postion) {
            return data.get(postion);
        }

        @Override
        public long getItemId(int postion) {
            return postion;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater= LayoutInflater.from(context);
            view=inflater.inflate(Id,null);
            ImageView image=view.findViewById(R.id.lra_image);
            TextView name=view.findViewById(R.id.lra_name);
            TextView labber=view.findViewById(R.id.lra_labber);
            ImageView add=view.findViewById(R.id.lra_addfriends);
            rank=view.findViewById(R.id.lra_marks);
            //获取数据
            JSONObject jsonObject = data.get(position);
            String a= null;
            try {
                image.setImageResource(jsonObject.getInt("user_image"));
                name.setText(jsonObject.getString("user_name"));
                labber.setText("层数:"+jsonObject.getInt("user_current_floor"));
                rank.setText(position+1+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return view;
        }
    }



    //    好友榜界面
    private void gotoFriends() {
        setContentView(R.layout.friendsrankactivity);
        hide();
        labbers=findViewById(R.id.fra_labber);
        labbers.setOnClickListener(listener);
        //设置构造器
        friendsAdapater();
        init();

        menuFlash=new MenuFlash(ivSpin,ivHome,ivCharts,ivCommunity,ivMe);
        menuFlash.play();
        ivSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuFlash.click(state);
                if (state==true){
                    state=false;
                }else{
                    state=true;
                }
            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RankActivity.this,HomePage.class);
                startActivity(intent);
            }
        });


        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(user)){
                    Intent intent=new Intent(RankActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(RankActivity.this,MeActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });


//        查看资料


    }
    //设置构造器
    private void friendsAdapater() {
        List<Map<String,Object>>list=indata1();
        friendsAdapater friendsAdapater=new friendsAdapater(this,R.layout.friendsranklistactivity,list);
        ListView listView=findViewById(R.id.lv_ranklistfriends);
        listView.setAdapter(friendsAdapater);
    }
    //好友数据
    private List<Map<String, Object>> indata1(){
        String name[]={"姑苏","百里","陈生","端木","公羊","黄龙","即墨"};
        String marks[]={"100","99","98","97","96","95","94"};
        List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
        for (int i=0;i<6;i++){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("image",R.mipmap.tou);
            map.put("name",name[i]);
            map.put("marks",marks[i]);
            list.add(map);
        }
        return list;
    }
    //好友构造器
    private class friendsAdapater extends BaseAdapter{
        private Context context;
        private int Id;
        private List<Map<String, Object>> data;

        public friendsAdapater(Context context,int Id,List<Map<String, Object>> data){
            this.context=context;
            this.Id=Id;
            this.data=data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int postion) {
            return data.get(postion);
        }

        @Override
        public long getItemId(int postion) {
            return postion;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater=LayoutInflater.from(context);
            view=inflater.inflate(Id,null);
            ImageView image=view.findViewById(R.id.fra_image);
            TextView name=view.findViewById(R.id.fra_name);
            TextView marks=view.findViewById(R.id.fra_marks);
            //获取数据
            Map<String, Object> map = data.get(position);
            image.setImageResource((int)map.get("image"));
            name.setText((String)map.get("name"));
            marks.setText((String)map.get("marks"));
            return view;
        }
    }



    //隐藏状态栏
    private void hide(){
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
    //边框
    private void init(){
        ivSetting=findViewById(R.id.iv_setting);
        ivSetting.setVisibility(View.INVISIBLE);
        ivHome=findViewById(R.id.iv_HomePage);
        ivCharts=findViewById(R.id.iv_charts);
        ivCommunity=findViewById(R.id.iv_community);
        ivMe=findViewById(R.id.iv_me);
        llMenu=findViewById(R.id.ll_menu);
        ivSpin=findViewById(R.id.iv_spin);
        list= new int[]{R.id.iv_HomePage, R.id.iv_charts, R.id.iv_community, R.id.iv_me};
        Intent intent=getIntent();
        if(intent.getStringExtra("user")!= null) {
            user = intent.getStringExtra("user");
        }
    }
}