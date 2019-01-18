package com.grade.vcl.push;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

/**
 * AliMessageReceiver : 用于接收推送的通知和消息
 * <p>
 * </> Created by ylwei on 2018/3/26.
 */
public class AliMessageReceiver extends MessageReceiver {
  private AliPushListener listener;

  public void init(AliPushListener listener) {
    this.listener = listener;
  }

  @Override
  public void onNotification(Context context, String title, String content,
                             Map<String, String> extraMap) {
    parseNotification(extraMap);
  }

  @Override
  protected void onNotificationReceivedInApp(Context context, String title, String summary,
                                             Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
    parseNotification(extraMap);
  }

  @Override
  public void onMessage(Context context, CPushMessage message) {
    try {
      listener.parseMessage(message);
    } catch (Exception e) {
      Log.e(getClass().getSimpleName(), e.toString());
    }
  }

  private void parseNotification(Map<String, String> extraMap) {
    if (extraMap != null) {
      listener.logger(extraMap);
      String read = extraMap.get("leap_read");
      listener.readMessage(read);
    }
  }
}