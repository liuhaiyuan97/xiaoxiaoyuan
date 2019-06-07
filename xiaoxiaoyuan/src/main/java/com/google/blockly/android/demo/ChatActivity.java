package com.google.blockly.android.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jmessage.support.google.gson.JsonObject;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private ImageView ivRet;
    private ListView lvChat;
    private EditText edSend;
    private Button btnSend;
    private OkHttpClient okHttpClient;

    private int userid = 0;
    private int toId = 0;
    private int toImg = 0;
    private String details = "";
    private List<Message> messages;
    private int userImg=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        JMessageClient.registerEventReceiver(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);

        ivRet = findViewById(R.id.iv_ret);
        lvChat = findViewById(R.id.lv);
        edSend = findViewById(R.id.et_send);
        btnSend = findViewById(R.id.btn_send);
        ivRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edSend.getText().toString();
                if (!msg.equals("")) {
                    Conversation.createSingleConversation("user" + toId);
                    final Conversation conversation = JMessageClient.getSingleConversation("user" + toId);
                    Message message = conversation.createSendMessage(new TextContent(edSend.getText().toString()));
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                messages = new ArrayList<>();
                                messages = conversation.getAllMessage();
                                ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, R.layout.chat, messages);
                                lvChat.setAdapter(chatAdapter);
                                lvChat.setDivider(null);
                                Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                edSend.setText("");
                            } else {
                                edSend.setText("");
                                Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    MessageSendingOptions options = new MessageSendingOptions();
                    options.setRetainOffline(false);

                    JMessageClient.sendMessage(message);
                } else {
                    Toast.makeText(ChatActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    }

    @Override
    protected void onStart() {
        Intent intent = getIntent();
        String str = intent.getStringExtra("toId");
        toId = Integer.parseInt(str);
        details = intent.getStringExtra("details");
        String toimg = intent.getStringExtra("toImg");
        Log.e("ssss","toId:"+str+"toimg:"+toimg);
        toImg = Integer.parseInt(toimg);

        TextView textView = findViewById(R.id.tv_name);
        textView.setText(intent.getStringExtra("toName"));
        try {
            userid = new JSONObject(details).getInt("user_id");
            ShowAllInformationTASK showAllInformationTASK=new ShowAllInformationTASK(userid);
            showAllInformationTASK.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conversation.createSingleConversation("user" + toId);
        Conversation conversation = JMessageClient.getSingleConversation("user" + toId);

        List<Message> messages = conversation.getAllMessage();
        for (int i = 0; i < messages.size(); i++) {
            Log.e("msg", messages.get(i).toJson());
        }
        ListView listView = findViewById(R.id.lv);
        ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, R.layout.chat, messages);
        listView.setAdapter(chatAdapter);
        listView.setDivider(null);

        super.onStart();
    }

    public class ChatAdapter extends BaseAdapter {
        private Context context;
        private int id;
        private List<Message> data;

        public ChatAdapter(Context context, int id, List<Message> data) {
            this.context = context;
            this.id = id;
            this.data = data;
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
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(id, null);
            }
            TextView textView = convertView.findViewById(R.id.tv_reseive);
            LinearLayout linearLayout = convertView.findViewById(R.id.ll_chat);
            TextView textView1 = convertView.findViewById(R.id.tv_time);
            ImageView imageView = convertView.findViewById(R.id.iv_Img);
            try {
                if (new JSONObject(data.get(i).toJson()).getString("from_id").equals("user" + toId)) {
                    JSONObject jsonObject = new JSONObject(data.get(i).toJson());
                    textView.setText(new JSONObject(jsonObject.getString("content")).getString("text"));
                    textView.setBackgroundResource(R.mipmap.he);
                    textView.setTextColor(Color.rgb(0, 0, 0));
                    imageView.setImageResource(toImg);
                    linearLayout.setGravity(Gravity.LEFT);
                    long l = jsonObject.getLong("createTimeInMillis");
                    Date date = new Date(l);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    textView1.setText(format.format(date));
                } else if (new JSONObject(data.get(i).toJson()).getString("from_id").equals("user" + userid)) {
                    JSONObject jsonObject = new JSONObject(data.get(i).toJson());
                    textView.setText(new JSONObject(jsonObject.getString("content")).getString("text"));
                    textView.setBackgroundResource(R.mipmap.mesay);
                    textView.setTextColor(Color.rgb(255, 255, 255));
                    imageView.setImageResource(userImg);
                    linearLayout.setGravity(Gravity.RIGHT);
                    long l = jsonObject.getLong("createTimeInMillis");
                    Date date = new Date(l);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    textView1.setText(format.format(date));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }

    }

    public void onEvent(MessageEvent event) {
        Message newMessage = event.getMessage();
        Conversation conversation = JMessageClient.getSingleConversation("user" + toId);
        final List<Message> mes = conversation.getAllMessage();
        for (int i = 0; i < mes.size(); i++) {
            Log.e("11", mes.get(i).toJson());
        }
        ChatActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = findViewById(R.id.lv);
                ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this, R.layout.chat, mes);
                listView.setAdapter(chatAdapter);
                listView.setDivider(null);
            }
        });


    }

    public void onEvent(OfflineMessageEvent event) {
        List<Message> newMessageList = event.getOfflineMessageList();
//        tvReceive.setText(newMessageList.toString());
    }

    public class ShowAllInformationTASK extends AsyncTask {
        int id;

        public ShowAllInformationTASK(int id) {
            this.id = id;
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
            okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://47.100.52.142:8080/xiaoxiaoyuanssm/user/showAllInfo?id=" + id)
                    .build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String str = response.body().string();
                Log.e("str", "获取数据" + str);
                if (!str.equals("")) {
                    JSONObject object = new JSONObject(str);
                     userImg= object.getInt("user_image");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
