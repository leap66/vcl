package com.grade.vcl.push;

import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

/**
 * AliPushListener :
 * <p>
 * </> Created by ylwei on 2018/3/26.
 */
public interface AliPushListener {

  void readMessage(String message);

  void logger(Map<String, String> extraMap);

  void parseMessage(CPushMessage message);
}