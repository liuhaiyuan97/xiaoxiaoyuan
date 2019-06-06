package com.google.blockly.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.security.auth.login.LoginException;

import cn.jpush.im.android.api.JMessageClient;

public class NotificationClickEvent extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    public void onEvent(NotificationClickEvent event){
        Intent notificationIntent = new Intent(NotificationClickEvent.this, ChatActivity.class);
        Log.e("跳转","222");
        NotificationClickEvent.this.startActivity(notificationIntent);//自定义跳转到指定页面
    }
}
