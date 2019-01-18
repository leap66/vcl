package com.grade.vcl.widget.update;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grade.unit.util.DeviceInfoUtil;
import com.grade.unit.util.FileUtil;
import com.grade.unit.util.ToastUtil;
import com.grade.vcl.R;
import com.grade.vcl.model.BUpdateEntity;
import com.grade.vcl.widget.update.base.UpdateTask;

/**
 * ProgressDialog :
 * <p>
 * </> Created by ylwei on 2019/1/3.
 */
class ProgressDialog extends Dialog {
  private Context mContext;
  private ProgressBar progressBar;
  private TextView tvProgress;

  ProgressDialog(Context context) {
    super(context, R.style.alert_dialog);
    this.mContext = context;
    initViews();
  }

  @SuppressLint("InflateParams")
  private void initViews() {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_progress, null,
        false);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        DeviceInfoUtil.getScreenWidth() * 11 / 15, RelativeLayout.LayoutParams.WRAP_CONTENT);
    setContentView(rootView, params);
    progressBar = rootView.findViewById(R.id.progress_bar);
    tvProgress = rootView.findViewById(R.id.tv_progress);
  }

  void downloadApk(BUpdateEntity model, String filePath) {
    setCancelable(model.isForce());
    UpdateTask updateTask = new UpdateTask(mContext, model.getPosition(), progressBar, tvProgress,
        filePath);
    updateTask.setSuccessListener(new UpdateTask.OnSuccessListener<String>() {
      @Override
      public void onSuccess(String data) {
        FileUtil.installApk(data);
        dismiss();
      }
    });
    updateTask.setErrorListener(new UpdateTask.OnErrorListener<String>() {
      @Override
      public void onError(String data) {
        ToastUtil.showFailure(mContext.getString(R.string.update_failure));
        dismiss();
      }
    });
    updateTask.execute(model.getDownloadUrl());
  }
}
