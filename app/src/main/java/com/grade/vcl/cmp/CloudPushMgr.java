package com.grade.vcl.cmp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.BasicCustomPushNotification;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;

public class CloudPushMgr {
  private static final String TAG = CloudPushMgr.class.getSimpleName();

  public static void initCloudChannel(final Context context, String appKey, String appSecret, final int icon) {
    PushServiceFactory.init(context);
    CloudPushService pushService = PushServiceFactory.getCloudPushService();
    pushService.register(context, appKey, appSecret, new CustomCallback() {
      @Override
      public void onSuccess(String s) {
        setCustomNotification(context, icon);
        super.onSuccess(s);
      }
    });
  }

  public static void binds(String account, String alias) {
    CloudPushMgr.bindAccount(account);
    CloudPushMgr.bindAlias(alias);
  }

  public static void unBinds() {
    CloudPushMgr.unBindAccount();
    CloudPushMgr.unBindAlias();
  }

  public static void bindAccount(String account) {
    if (account != null) {
      PushServiceFactory.getCloudPushService().bindAccount(account, new CustomCallback());
    }
  }

  public static void unBindAccount() {
    PushServiceFactory.getCloudPushService().unbindAccount(new CustomCallback());
  }

  public static void bindAlias(String alias) {
    if (alias != null) {
      PushServiceFactory.getCloudPushService().addAlias(alias, new CustomCallback());
    }
  }

  public static void unBindAlias() {
    PushServiceFactory.getCloudPushService().removeAlias(null, new CustomCallback());
  }

  private static void setCustomNotification(Context context, int icon) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager notificationManager = (NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE);
      if (notificationManager == null)
        return;
      // 通知渠道的id
      String id = "1";
      // 用户可以看到的通知渠道的名字.
      CharSequence name = "notification channel";
      // 用户可以看到的通知渠道的描述
      String description = "notification description";
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel mChannel = new NotificationChannel(id, name, importance);
      // 配置通知渠道的属性
      mChannel.setDescription(description);
      // 设置通知出现时的闪灯（如果 android 设备支持的话）
      mChannel.enableLights(true);
      mChannel.setLightColor(Color.RED);
      // 设置通知出现时的震动（如果 android 设备支持的话）
      mChannel.enableVibration(true);
      mChannel.setVibrationPattern(new long[]{
          100, 200, 300, 400, 500, 400, 300, 200, 400});
      // 最后在notificationmanager中创建该通知渠道
      notificationManager.createNotificationChannel(mChannel);
    } else {
      BasicCustomPushNotification notification = new BasicCustomPushNotification();
      notification.setRemindType(BasicCustomPushNotification.REMIND_TYPE_SOUND);
      notification.setStatusBarDrawable(icon);
      notification.setBuildWhenAppInForeground(false);
      CustomNotificationBuilder.getInstance().setCustomNotification(1, notification);
    }
  }

  private static class CustomCallback implements CommonCallback {
    @Override
    public void onSuccess(String s) {
      Log.e(TAG, "成功---" + s);
    }

    @Override
    public void onFailed(String s, String s1) {
      Log.e(TAG, "init cloudchannel failed -- errorcode:" + s + " -- errorMessage:" + s1);
    }
  }

}
