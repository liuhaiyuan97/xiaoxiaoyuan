package com.google.blockly.android.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Message;

public class MessageEventReceiver extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    //用户在线期间收到的消息都会以MessageEvent的方式上抛
    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
                break;
            case image:
                //处理图片消息
//                ImageContent imageContent = (ImageContent) msg.getContent();
//                imageContent.getLocalPath();//图片本地地址
//                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
//                VoiceContent voiceContent = (VoiceContent) msg.getContent();
//                voiceContent.getLocalPath();//语音文件本地地址
//                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
//                //处理自定义消息
//                CustomContent customContent = (CustomContent) msg.getContent();
//                customContent.getNumberValue("custom_num"); //获取自定义的值
//                customContent.getBooleanValue("custom_boolean");
//                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
//                //处理事件提醒消息
//                EventNotificationContent eventNotificationContent = (EventNotificationContent)msg.getContent();
//                switch (eventNotificationContent.getEventNotificationType()){
//                    case group_member_added:
//                        //群成员加群事件
//                        break;
//                    case group_member_removed:
//                        //群成员被踢事件
//                        break;
//                    case group_member_exit:
//                        //群成员退群事件
//                        break;
//                    case group_info_updated://since 2.2.1
//                        //群信息变更事件
//                        break;
//                }
                break;
            case unknown:
//                // 处理未知消息，未知消息的Content为PromptContent 默认提示文本为“当前版本不支持此类型消息，请更新sdk版本”，上层可选择不处理
//                PromptContent promptContent = (PromptContent) msg.getContent();
//                promptContent.getPromptType();//未知消息的type是unknown_msg_type
//                promptContent.getPromptText();//提示文本，“当前版本不支持此类型消息，请更新sdk版本”
                break;
        }
    }

    public void onEvent(OfflineMessageEvent event) {
        List<Message> msgs = event.getOfflineMessageList();
        for (Message msg:msgs) {
            switch (msg.getContentType()) {
                case text:
                    //处理文字消息
                    TextContent textContent = (TextContent) msg.getContent();
                    textContent.getText();
                    break;
                case image:
                    //处理图片消息
//                ImageContent imageContent = (ImageContent) msg.getContent();
//                imageContent.getLocalPath();//图片本地地址
//                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                    break;
                case voice:
                    //处理语音消息
//                VoiceContent voiceContent = (VoiceContent) msg.getContent();
//                voiceContent.getLocalPath();//语音文件本地地址
//                voiceContent.getDuration();//语音文件时长
                    break;
                case custom:
//                //处理自定义消息
//                CustomContent customContent = (CustomContent) msg.getContent();
//                customContent.getNumberValue("custom_num"); //获取自定义的值
//                customContent.getBooleanValue("custom_boolean");
//                customContent.getStringValue("custom_string");
                    break;
                case eventNotification:
//                //处理事件提醒消息
//                EventNotificationContent eventNotificationContent = (EventNotificationContent)msg.getContent();
//                switch (eventNotificationContent.getEventNotificationType()){
//                    case group_member_added:
//                        //群成员加群事件
//                        break;
//                    case group_member_removed:
//                        //群成员被踢事件
//                        break;
//                    case group_member_exit:
//                        //群成员退群事件
//                        break;
//                    case group_info_updated://since 2.2.1
//                        //群信息变更事件
//                        break;
//                }
                    break;
                case unknown:
//                // 处理未知消息，未知消息的Content为PromptContent 默认提示文本为“当前版本不支持此类型消息，请更新sdk版本”，上层可选择不处理
//                PromptContent promptContent = (PromptContent) msg.getContent();
//                promptContent.getPromptType();//未知消息的type是unknown_msg_type
//                promptContent.getPromptText();//提示文本，“当前版本不支持此类型消息，请更新sdk版本”
                    break;
            }
        }
    }

}
