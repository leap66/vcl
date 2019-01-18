package com.grade.vcl.widget.update;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.grade.unit.util.FileUtil;
import com.grade.vcl.R;
import com.grade.vcl.databinding.DialogUpdateBinding;
import com.grade.vcl.model.BUpdateEntity;

import java.io.File;

public class UpdateDialog extends Dialog {
  private DialogUpdateBinding binding;
  private BUpdateEntity update;
  private Context mContext;
  private String filePath;

  public UpdateDialog(Context context, BUpdateEntity update) {
    super(context, R.style.alert_dialog);
    this.mContext = context;
    this.update = update;
    filePath = FileUtil.getFilePath(update.getFileName());
    initView();
  }

  private void initView() {
    binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_update, null,
        true);
    binding.setPresenter(new Presenter());
    binding.setItem(update);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(binding.getRoot());
    setCanceledOnTouchOutside(false);
    setCancelable(!update.isForce());
  }

  public class Presenter {

    // 下次更新
    public void onCancel() {
      dismiss();
    }

    // 立即更新
    public void onUpData() {
      String currentMd5 = "-1";
      try {
        currentMd5 = FileUtil.getFileMD5String(filePath);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (currentMd5.equals(update.getMd5())) {
        FileUtil.installApk(filePath);
        dismiss();
      } else {
        if (new File(filePath).exists())
          update.setPosition(new File(filePath).length());
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.downloadApk(update, filePath);
        progressDialog.show();
        dismiss();
      }
    }
  }
}
